/**

 SENG 300 - ITERATION 3
 GROUP GOLD {8}

 Name                      UCID

 Yotam Rojnov             30173949
 Duncan McKay             30177857
 Mahfuz Alam              30142265
 Luis Trigueros Granillo  30167989
 Lilia Skumatova          30187339
 Abdelrahman Abbas        30110374
 Talaal Irtija            30169780
 Alejandro Cardona        30178941
 Alexandre Duteau         30192082
 Grace Johnson            30149693
 Abil Momin               30154771
 Tara Ghasemi M. Rad      30171212
 Izabella Mawani          30179738
 Binish Khalid            30061367
 Fatima Khalid            30140757
 Lucas Kasdorf            30173922
 Emily Garcia-Volk        30140791
 Yuinikoru Futamata       30173228
 Joseph Tandyo            30182561
 Syed Haider              30143096
 Nami Marwah              30178528

 */

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

import com.tdc.CashOverloadException;

import com.tdc.banknote.Banknote;
import com.tdc.banknote.BanknoteStorageUnit;
import com.tdc.banknote.IBanknoteDispenser;

import com.tdc.coin.Coin;
import com.tdc.coin.CoinStorageUnit;
import com.tdc.coin.ICoinDispenser;

import com.thelocalmarketplace.hardware.ISelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;

import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantLogic;

import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantPageGUI;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.CustomerStation;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.StartSession;
import com.thelocalmarketplace.software.funds.ReceiptObserver;

import ca.ucalgary.seng300.simulation.SimulationException;

import powerutility.PowerGrid;


public class AttendantLogicTest {
	private AttendantLogic aLogic;
	public ReceiptObserver rO;

	private SelfCheckoutStationSoftware station;
	
	private CustomerStation[] customerStations;
	private SelfCheckoutStationSoftware[] stationSoftwareInstances;
	private StartSession[] startSessions;

	@Before
	public void setUp() {
		BigDecimal[] coinDenominations = {new BigDecimal("0.25"), new BigDecimal("0.10"), new BigDecimal("0.50"),
				new BigDecimal("1.0") };
		BigDecimal[] banknoteDenominations = {new BigDecimal("5.0"), new BigDecimal("10.0"), new BigDecimal("20.0"),
				new BigDecimal("50.0"), new BigDecimal("100.0")};

		// Set up Gold selfCheckoutStation
		SelfCheckoutStationGold.resetConfigurationToDefaults();
		SelfCheckoutStationGold.configureBanknoteDenominations(banknoteDenominations);
		SelfCheckoutStationGold.configureCoinDenominations(coinDenominations);
		SelfCheckoutStationGold.configureCurrency(Currency.getInstance("CAD"));
		SelfCheckoutStationGold checkoutStationG = new SelfCheckoutStationGold();
		PowerGrid.engageUninterruptiblePowerSource();
		checkoutStationG.plugIn(PowerGrid.instance());
		checkoutStationG.turnOn();
		this.station = new SelfCheckoutStationSoftware(checkoutStationG);
		this.aLogic = new AttendantLogic();
		
		customerStations = new CustomerStation[4]; // Example size
        stationSoftwareInstances = new SelfCheckoutStationSoftware[4];
	}
	
	@After
	public void tearDown() {
		station = null;
		aLogic = null;
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
		aLogic.refillPrinterInk(station);
	}

	@Test
	public void testRefillPaper() throws OverloadedDevice {
		aLogic.refillPrinterPaper(station);
	}

	@Test
	public void testRefillPrinterPaperWhenLow() throws OverloadedDevice {
		aLogic.refillPrinterPaperWhenLow(station);
	}

	@Test
	public void testRefillInkWhenLow() throws OverloadedDevice {
		aLogic.refillPrinterInkWhenLow(station);
	}

	@Test
	public void testEnableStation_StationNotSelected() {
		AttendantLogic logic = new AttendantLogic();
		int selectedStation = 1;
		SelfCheckoutStationBronze bronzeS = new SelfCheckoutStationBronze();
		startSessions = new StartSession[5];
		startSessions[1] = new StartSession(selectedStation, station, null);
		PowerGrid.engageUninterruptiblePowerSource();
        bronzeS.plugIn(PowerGrid.instance());
        bronzeS.turnOn();
        stationSoftwareInstances = new SelfCheckoutStationSoftware[5];
		stationSoftwareInstances[selectedStation] = new SelfCheckoutStationSoftware(bronzeS);
		stationSoftwareInstances[selectedStation].setStationBlock();
		assertTrue(stationSoftwareInstances[1].getStationBlock());
		logic.EnableStation(selectedStation, customerStations, stationSoftwareInstances, bronzeS, startSessions);
		assertFalse(stationSoftwareInstances[1].getStationBlock());
	}
	
	@Test
	public void testDisableStation_StationNotSelected() {
		AttendantLogic logic = new AttendantLogic();
		int selectedStation = 1; 
		SelfCheckoutStationBronze bronzeS = new SelfCheckoutStationBronze(); 
		startSessions = new StartSession[5];	
		startSessions[1] = new StartSession(selectedStation, station, null);	
		PowerGrid.engageUninterruptiblePowerSource();
		bronzeS.plugIn(PowerGrid.instance());
		bronzeS.turnOn();
		stationSoftwareInstances = new SelfCheckoutStationSoftware[5];   
		stationSoftwareInstances[selectedStation] = new SelfCheckoutStationSoftware(bronzeS);	
		stationSoftwareInstances[selectedStation].setStationUnblock();	
		assertFalse(stationSoftwareInstances[1].getStationBlock());	
		logic.DisableStation(selectedStation, customerStations, stationSoftwareInstances, bronzeS, startSessions);
		assertTrue(stationSoftwareInstances[1].getStationBlock());	
		boolean result = logic.DisableStation(selectedStation, customerStations, stationSoftwareInstances, bronzeS, startSessions);  
		assertFalse(result);
	}
}
	  
	