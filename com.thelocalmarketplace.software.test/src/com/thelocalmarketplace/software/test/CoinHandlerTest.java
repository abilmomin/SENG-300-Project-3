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

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Map;

import org.junit.*;

import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.NoCashAvailableException;
import com.tdc.coin.Coin;
import com.tdc.coin.CoinSlot;
import com.tdc.coin.CoinStorageUnit;
import com.tdc.coin.CoinStorageUnitObserver;
import com.tdc.coin.CoinValidator;
import com.tdc.coin.ICoinDispenser;
import com.thelocalmarketplace.hardware.ISelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.funds.*;

import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import ca.ucalgary.seng300.simulation.SimulationException;
import powerutility.PowerGrid;

public class CoinHandlerTest {
	private SelfCheckoutStationSoftware stationG;
	private SelfCheckoutStationSoftware stationB;
	private SelfCheckoutStationSoftware stationS;
	private SelfCheckoutStationGold checkoutStationG;
	private SelfCheckoutStationSilver checkoutStationS;
	private SelfCheckoutStationBronze checkoutStationB;
	private Coin coin1, coin2;
	
	@Before
	public void setUp() {
		BigDecimal[] coinDenominations = { new BigDecimal("0.25"), new BigDecimal("0.10"), new BigDecimal("0.50"),
				new BigDecimal("1.0") };

		// Set up Gold selfCheckoutStation
		SelfCheckoutStationGold.resetConfigurationToDefaults();
		SelfCheckoutStationGold.configureCoinDenominations(coinDenominations);
		SelfCheckoutStationGold.configureCurrency(Currency.getInstance("CAD"));
		this.checkoutStationG = new SelfCheckoutStationGold();
		this.checkoutStationG.plugIn(PowerGrid.instance());
		this.checkoutStationG.turnOn();
		this.stationG = new SelfCheckoutStationSoftware(checkoutStationG);

		// Set up Silver selfCheckoutStation
		SelfCheckoutStationSilver.resetConfigurationToDefaults();
		SelfCheckoutStationSilver.configureCoinDenominations(coinDenominations);
		SelfCheckoutStationSilver.configureCurrency(Currency.getInstance("CAD"));
		this.checkoutStationS = new SelfCheckoutStationSilver();
		this.checkoutStationS.plugIn(PowerGrid.instance());
		this.checkoutStationS.turnOn();
		this.stationS = new SelfCheckoutStationSoftware(checkoutStationS);

		// Set up Bronze selfCheckoutStation
		SelfCheckoutStationBronze.resetConfigurationToDefaults();
		SelfCheckoutStationBronze.configureCoinDenominations(coinDenominations);
		SelfCheckoutStationBronze.configureCurrency(Currency.getInstance("CAD"));
		this.checkoutStationB = new SelfCheckoutStationBronze();
		this.checkoutStationB.plugIn(PowerGrid.instance());
		this.checkoutStationB.turnOn();
		this.stationB = new SelfCheckoutStationSoftware(checkoutStationB);

	}
	
	@After
	public void tearDown() {
		stationG = null;
		stationS = null;
		stationB = null;
		checkoutStationB = null;
		checkoutStationS = null;
		checkoutStationG = null;
	}

	@Test
	public void coinAddedTestG() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some coins
		coin1 = new Coin(currency, new BigDecimal("0.10"));

		CoinSlot cs = this.checkoutStationG.getCoinSlot();
		cs.receive(coin1);

	}

	@Test
	public void coinAddedTestS() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some coins
		coin1 = new Coin(currency, new BigDecimal("0.10"));

		CoinSlot cs = this.checkoutStationS.getCoinSlot();
		cs.receive(coin1);
	}

	@Test
	public void coinAddedTestB() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some coins
		coin1 = new Coin(currency, new BigDecimal("0.10"));

		CoinSlot cs = this.checkoutStationB.getCoinSlot();
		cs.receive(coin1);
	}
	
	@Test
	public void coinAddedTestDIspenserChangeCoverage() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some coins
		coin1 = new Coin(currency, new BigDecimal("0.25"));
		coin2 = new Coin(currency, new BigDecimal("0.10"));

		CoinSlot cs = this.checkoutStationG.getCoinSlot();
		checkoutStationG.getCoinDispensers().get(new BigDecimal("0.10")).receive(coin2);
		stationG.setOrderTotalPrice(0.15);
		cs.receive(coin1);
		assertTrue(checkoutStationG.getCoinDispensers().get(new BigDecimal("0.25")).size() == 1);
		assertTrue(stationG.getTotalOrderPrice() == 0);
		checkoutStationG.getCoinDispensers().get(new BigDecimal("0.10")).receive(coin2);
		stationG.setOrderTotalPrice(0.05);
		cs.receive(new Coin(currency, new BigDecimal("0.10")));
	}
	

	@Test
	public void validCoinDetectedGoldTest() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some coins
		coin1 = new Coin(currency, BigDecimal.valueOf(0.25));
		CoinSlot cs = this.checkoutStationG.getCoinSlot();
		cs.receive(coin1);
	}

	@Test
	public void validCoinDetectedSilverTest() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some coins
		coin1 = new Coin(currency, BigDecimal.valueOf(0.25));
		CoinSlot cs = this.checkoutStationS.getCoinSlot();
		cs.receive(coin1);
	}

	@Test
	public void validCoinDetectedBronzeTest() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some coins
		coin1 = new Coin(currency, BigDecimal.valueOf(0.25));
		CoinSlot cs = this.checkoutStationB.getCoinSlot();
		cs.receive(coin1);
	}

	@Test(expected = NullPointerSimulationException.class)
	public void invalidNullCoinDetectedGoldTest() throws DisabledException, CashOverloadException {

		CoinSlot cs = this.checkoutStationG.getCoinSlot();
		cs.receive(null);
	}

	@Test(expected = NullPointerSimulationException.class)
	public void invalidNullCoinDetectedSilverTest() throws DisabledException, CashOverloadException {
		CoinSlot cs = this.checkoutStationS.getCoinSlot();
		cs.receive(null);
	}

	@Test(expected = NullPointerSimulationException.class)
	public void invalidNullCoinDetectedBronzeTest() throws DisabledException, CashOverloadException {
		CoinSlot cs = this.checkoutStationB.getCoinSlot();
		cs.receive(null);
	}

	@Test
	public void invalidCoinDetectedGoldTest() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		coin1 = new Coin(currency, BigDecimal.valueOf(5.56));
		CoinSlot cs = this.checkoutStationG.getCoinSlot();
		cs.receive(coin1);
	}

	@Test
	public void invalidCoinDetectedSilverTest() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		coin1 = new Coin(currency, BigDecimal.valueOf(5.56));
		CoinSlot cs = this.checkoutStationS.getCoinSlot();
		cs.receive(coin1);
	}

	@Test
	public void invalidCoinDetectedBronzeTest() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		coin1 = new Coin(currency, BigDecimal.valueOf(5.56));
		CoinSlot cs = this.checkoutStationB.getCoinSlot();
		cs.receive(coin1);
	}


	@Test
	public void coinsLoadedG() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some coins
		coin1 = new Coin(currency, BigDecimal.valueOf(0.25));
		coin2 = new Coin(currency, new BigDecimal("0.10"));

		checkoutStationG.getCoinDispensers().get(new BigDecimal("0.25")).load(coin1);
		;

	}

	@Test
	public void coinsLoadedS() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some coins
		coin1 = new Coin(currency, BigDecimal.valueOf(0.25));
		coin2 = new Coin(currency, new BigDecimal("0.10"));

		checkoutStationS.getCoinDispensers().get(new BigDecimal("0.25")).load(coin1);
		;

	}

	@Test
	public void coinsLoadedB() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some coins
		coin1 = new Coin(currency, BigDecimal.valueOf(0.25));
		coin2 = new Coin(currency, new BigDecimal("0.10"));

		checkoutStationB.getCoinDispensers().get(new BigDecimal("0.25")).load(coin1);
		;

	}

	@Test
	public void coinsUnloadedG() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some coins
		coin1 = new Coin(currency, BigDecimal.valueOf(0.25));
		coin2 = new Coin(currency, BigDecimal.valueOf(0.25));
		checkoutStationG.getCoinDispensers().get(new BigDecimal("0.25")).load(coin1, coin2);
		// now unloaded
		checkoutStationG.getCoinDispensers().get(new BigDecimal("0.25")).unload();
	}

	@Test
	public void coinsUnloadedS() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some coins
		coin1 = new Coin(currency, BigDecimal.valueOf(0.25));
		coin2 = new Coin(currency, BigDecimal.valueOf(0.25));
		checkoutStationS.getCoinDispensers().get(new BigDecimal("0.25")).load(coin1, coin2);
		// now unloaded
		checkoutStationS.getCoinDispensers().get(new BigDecimal("0.25")).unload();

	}

	@Test
	public void coinsUnloadedB() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some coins
		coin1 = new Coin(currency, BigDecimal.valueOf(0.25));
		coin2 = new Coin(currency, BigDecimal.valueOf(0.25));
		checkoutStationB.getCoinDispensers().get(new BigDecimal("0.25")).load(coin1, coin2);
		// now unloaded
		checkoutStationB.getCoinDispensers().get(new BigDecimal("0.25")).unload();

	}

	@Test
	public void coinRemovedTestG() throws DisabledException, CashOverloadException, NoCashAvailableException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some coins
		coin1 = new Coin(currency, BigDecimal.valueOf(0.25));
		coin2 = new Coin(currency, BigDecimal.valueOf(0.25));
		checkoutStationG.getCoinDispensers().get(new BigDecimal("0.25")).load(coin1, coin2);
		// remove coin
		checkoutStationG.getCoinDispensers().get(new BigDecimal("0.25")).emit();

	}

	@Test
	public void coinRemovedTestS() throws DisabledException, CashOverloadException, NoCashAvailableException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some coins
		coin1 = new Coin(currency, BigDecimal.valueOf(0.25));
		coin2 = new Coin(currency, BigDecimal.valueOf(0.25));
		checkoutStationS.getCoinDispensers().get(new BigDecimal("0.25")).load(coin1, coin2);
		// remove coin
		checkoutStationS.getCoinDispensers().get(new BigDecimal("0.25")).emit();
		;
	}

	@Test
	public void coinRemovedTestB() throws DisabledException, CashOverloadException, NoCashAvailableException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some coins
		coin1 = new Coin(currency, BigDecimal.valueOf(0.25));
		coin2 = new Coin(currency, BigDecimal.valueOf(0.25));
		checkoutStationB.getCoinDispensers().get(new BigDecimal("0.25")).load(coin1, coin2);
		// remove coin
		checkoutStationB.getCoinDispensers().get(new BigDecimal("0.25")).emit();
		;
	}
	
	// Coin Dispenser Tests
	@Test
	public void testCoinsEmptyDS() throws SimulationException, CashOverloadException, NoCashAvailableException, DisabledException {
		Currency currency = Currency.getInstance("CAD");
		List<BigDecimal> denominations = checkoutStationS.getCoinDenominations();
		Map<BigDecimal, ICoinDispenser> dispensers = checkoutStationS.getCoinDispensers();
		for (BigDecimal denomination : denominations) {
			ICoinDispenser dispenser = dispensers.get(denomination);
			Coin coin = new Coin(currency, denomination);
			dispenser.load(coin);
			dispenser.emit();
		}
	}
	
	@Test
	public void testCoinsEmptyDB() throws SimulationException, CashOverloadException, NoCashAvailableException, DisabledException {
		Currency currency = Currency.getInstance("CAD");
		List<BigDecimal> denominations = checkoutStationB.getCoinDenominations();
		Map<BigDecimal, ICoinDispenser> dispensers = checkoutStationB.getCoinDispensers();
		for (BigDecimal denomination : denominations) {
			ICoinDispenser dispenser = dispensers.get(denomination);
			Coin coin = new Coin(currency, denomination);
			dispenser.load(coin);
			dispenser.emit();
		}
	}
	
	@Test
	public void testCoinsEmptyDG() throws SimulationException, CashOverloadException, NoCashAvailableException, DisabledException {
		Currency currency = Currency.getInstance("CAD");
		List<BigDecimal> denominations = checkoutStationG.getCoinDenominations();
		Map<BigDecimal, ICoinDispenser> dispensers = checkoutStationG.getCoinDispensers();
		for (BigDecimal denomination : denominations) {
			ICoinDispenser dispenser = dispensers.get(denomination);
			Coin coin = new Coin(currency, denomination);
			dispenser.load(coin);
			dispenser.emit();
		}
	}
	
	// Coin Storage Unit Tests
	@Test
	public void testCoinsFullSS() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		CoinStorageUnit storage = checkoutStationS.getCoinStorage();
		Funds funds = new Funds(stationS);
		CoinHandler handler = new CoinHandler(funds);
		storage.attach(handler);
		while (storage.hasSpace()) {
			storage.receive(new Coin(currency, BigDecimal.valueOf(0.25)));
		}
	}
	
	@Test
	public void testCoinsFullSB() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		CoinStorageUnit storage = checkoutStationB.getCoinStorage();
		Funds funds = new Funds(stationB);
		CoinHandler handler = new CoinHandler(funds);
		storage.attach(handler);
		while (storage.hasSpace()) {
			storage.receive(new Coin(currency, BigDecimal.valueOf(0.25)));
		}
	}
	
	@Test
	public void testCoinsFullSG() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		CoinStorageUnit storage = checkoutStationG.getCoinStorage();
		Funds funds = new Funds(stationG);
		CoinHandler handler = new CoinHandler(funds);
		storage.attach(handler);
		while (storage.hasSpace()) {
			storage.receive(new Coin(currency, BigDecimal.valueOf(0.25)));
		}
	}
	
	@Test
	public void testCoinsLoadSS() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		CoinStorageUnit storage = checkoutStationS.getCoinStorage();
		Funds funds = new Funds(stationS);
		CoinHandler handler = new CoinHandler(funds);
		storage.attach(handler);
		while (storage.hasSpace()) {
			storage.load(new Coin(currency, BigDecimal.valueOf(0.25)));
		}
	}
	
	@Test
	public void testCoinsLoadSB() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		CoinStorageUnit storage = checkoutStationB.getCoinStorage();
		Funds funds = new Funds(stationB);
		CoinHandler handler = new CoinHandler(funds);
		storage.attach(handler);
		while (storage.hasSpace()) {
			storage.load(new Coin(currency, BigDecimal.valueOf(0.25)));
		}
	}
	
	@Test
	public void testCoinsLoadSG() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		CoinStorageUnit storage = checkoutStationG.getCoinStorage();
		Funds funds = new Funds(stationG);
		CoinHandler handler = new CoinHandler(funds);
		storage.attach(handler);
		while (storage.hasSpace()) {
			storage.load(new Coin(currency, BigDecimal.valueOf(0.25)));
		}
	}

}
