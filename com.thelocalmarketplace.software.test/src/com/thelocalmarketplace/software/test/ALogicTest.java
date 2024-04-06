package com.thelocalmarketplace.software.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.printer.IReceiptPrinter;

import com.tdc.CashOverloadException;
import com.tdc.banknote.Banknote;
import com.tdc.banknote.BanknoteStorageUnit;
import com.tdc.banknote.IBanknoteDispenser;
import com.tdc.coin.Coin;
import com.tdc.coin.CoinStorageUnit;
import com.tdc.coin.ICoinDispenser;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.ISelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.ALogic;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.CustomerStation;
import com.thelocalmarketplace.software.funds.Funds;
import com.thelocalmarketplace.software.funds.Receipt;
import com.thelocalmarketplace.software.funds.ReceiptObserver;

import ca.ucalgary.seng300.simulation.SimulationException;
import powerutility.PowerGrid;

public class ALogicTest {

	private ALogic aLogic;
	public ReceiptObserver rO;

	private SelfCheckoutStationSoftware station;
	
	 private CustomerStation[] customerStations;
	 private SelfCheckoutStationSoftware[] stationSoftwareInstances;
	 private AbstractSelfCheckoutStation checkoutStation;
	 private SelfCheckoutStationBronze teststation;


	@Before
	public void setUp() {
		BigDecimal[] coinDenominations = { new BigDecimal("0.25"), new BigDecimal("0.10"), new BigDecimal("0.50"),
				new BigDecimal("1.0") };
		BigDecimal[] banknoteDenominations = { new BigDecimal("5.0"), new BigDecimal("10.0"), new BigDecimal("20.0"),
				new BigDecimal("50.0"), new BigDecimal("100.0")};

		// Set up Gold selfCheckoutStation
		SelfCheckoutStationGold.resetConfigurationToDefaults();
		SelfCheckoutStationGold.configureBanknoteDenominations(banknoteDenominations);
		SelfCheckoutStationGold.configureCoinDenominations(coinDenominations);
		SelfCheckoutStationGold.configureCurrency(Currency.getInstance("CAD"));
		SelfCheckoutStationGold checkoutStationG = new SelfCheckoutStationGold();
		checkoutStationG.plugIn(PowerGrid.instance());
		checkoutStationG.turnOn();
		this.station = new SelfCheckoutStationSoftware(checkoutStationG);
		this.aLogic = new ALogic();
		
		
		customerStations = new CustomerStation[4]; // Example size
        stationSoftwareInstances = new SelfCheckoutStationSoftware[4];
        teststation = new SelfCheckoutStationBronze();
        teststation.resetConfigurationToDefaults();
		teststation.plugIn(PowerGrid.instance());
		teststation.turnOn();
	}

	@Test
	public void testEmptyCoins() throws SimulationException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		Coin coin1 = new Coin(currency, BigDecimal.valueOf(0.25));
		Coin coin2 = new Coin(currency, BigDecimal.valueOf(0.10));
		CoinStorageUnit storage = station.getStationHardware().getCoinStorage();
		storage.load(coin1, coin2);
		assertTrue(storage.getCoinCount() == 2);
		aLogic.emptyCoinStorage(station);
		assertTrue(storage.getCoinCount() == 0);
	}

	@Test
	public void testEmptyBanknotes() throws SimulationException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		Banknote banknote1 = new Banknote(currency, BigDecimal.valueOf(5.00));
		Banknote banknote2 = new Banknote(currency, BigDecimal.valueOf(10.00));
		BanknoteStorageUnit storage = station.getStationHardware().getBanknoteStorage();
		storage.load(banknote1, banknote2);
		assertTrue(storage.getBanknoteCount() == 2);
		aLogic.emptyBanknoteStorage(station);
		assertTrue(storage.getBanknoteCount() == 0);
	}

    @Test
    public void testFillBanknoteDispensers() throws SimulationException, CashOverloadException {
    	ISelfCheckoutStation cS = station.getStationHardware();
		BigDecimal[] denominations = cS.getBanknoteDenominations();
		Map<BigDecimal, IBanknoteDispenser> dispensers = cS.getBanknoteDispensers();
		for (BigDecimal denomination : denominations) {
			IBanknoteDispenser dispenser = dispensers.get(denomination);
			dispenser.unload();
			assertTrue(dispenser.size() == 0);
		}
		cS.plugIn(PowerGrid.instance());
		cS.turnOn();
		aLogic.refillBanknoteDispensers(station);
		for (BigDecimal denomination : denominations) {
			IBanknoteDispenser dispenser = dispensers.get(denomination);
			assertTrue(dispenser.size() == dispenser.getCapacity());
		}
    }

	@Test
	public void testFillCoinDispensers() throws SimulationException, CashOverloadException {
		ISelfCheckoutStation cS = station.getStationHardware();
		List<BigDecimal> denominations = cS.getCoinDenominations();
		Map<BigDecimal, ICoinDispenser> dispensers = cS.getCoinDispensers();
		for (BigDecimal denomination : denominations) {
			ICoinDispenser dispenser = dispensers.get(denomination);
			dispenser.unload();
			assertTrue(dispenser.size() == 0);
		}

		aLogic.refillCoinDispensers(station);
		for (BigDecimal denomination : denominations) {
			ICoinDispenser dispenser = dispensers.get(denomination);
			assertFalse(dispenser.hasSpace());
		}
	}

	@Test
	public void testRefillInk() throws OverloadedDevice {
		ISelfCheckoutStation cS = station.getStationHardware();
		IReceiptPrinter printer = cS.getPrinter();
		Receipt receipt = new Receipt(printer, new Funds(station));
		aLogic.refillPrinterInk(station, receipt);
	}

	@Test
	public void testRefillPaper() throws OverloadedDevice {
		ISelfCheckoutStation cS = station.getStationHardware();
		IReceiptPrinter printer = cS.getPrinter();
		Receipt receipt = new Receipt(printer, new Funds(station));
		aLogic.refillPrinterPaper(station, receipt);
	}


	@Test
	  public void testEnableStation_StationNotSelected() {
	      //assertEquals(aLogic.EnableStation(0, customerStations, stationSoftwareInstances, teststation));
	  }

	  
	  @Test
	  public void testDisableStation_StationNotSelected() {
	      assertFalse(aLogic.DisableStation(0, customerStations, stationSoftwareInstances, teststation));
	  }
	  

	@After
	public void tearDown() {
		station = null;
		aLogic = null;
	}

}
