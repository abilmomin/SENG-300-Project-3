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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import org.junit.*;

import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.coin.Coin;
import com.tdc.coin.CoinSlot;
import com.tdc.coin.CoinValidator;

import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.funds.*;

import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import ca.ucalgary.seng300.simulation.SimulationException;
import powerutility.PowerGrid;

public class CoinHandlerTest {
	private SelfCheckoutStationSoftware station;
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
		this.station = new SelfCheckoutStationSoftware(checkoutStationG);

		// Set up Silver selfCheckoutStation
		SelfCheckoutStationSilver.resetConfigurationToDefaults();
		SelfCheckoutStationSilver.configureCoinDenominations(coinDenominations);
		SelfCheckoutStationSilver.configureCurrency(Currency.getInstance("CAD"));
		this.checkoutStationS = new SelfCheckoutStationSilver();
		this.checkoutStationS.plugIn(PowerGrid.instance());
		this.checkoutStationS.turnOn();
		this.station = new SelfCheckoutStationSoftware(checkoutStationS);

		// Set up Bronze selfCheckoutStation
		SelfCheckoutStationBronze.resetConfigurationToDefaults();
		SelfCheckoutStationBronze.configureCoinDenominations(coinDenominations);
		SelfCheckoutStationBronze.configureCurrency(Currency.getInstance("CAD"));
		this.checkoutStationB = new SelfCheckoutStationBronze();
		this.checkoutStationB.plugIn(PowerGrid.instance());
		this.checkoutStationB.turnOn();
		this.station = new SelfCheckoutStationSoftware(checkoutStationB);

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

	////////////////////////////////////////////////////////////////////////////////////
	// Everything below this line needs the tests are passing but coverage isn't

	@Test
	public void coinsLoadedG() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some coins
		coin1 = new Coin(currency, BigDecimal.valueOf(0.25));
		coin2 = new Coin(currency, new BigDecimal("0.10"));

		checkoutStationG.getCoinStorage().load(coin1, coin2);

	}

	@Test
	public void coinsLoadedS() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some coins
		coin1 = new Coin(currency, BigDecimal.valueOf(0.25));
		coin2 = new Coin(currency, new BigDecimal("0.10"));

		checkoutStationS.getCoinStorage().load(coin1, coin2);

	}

	@Test
	public void coinsLoadedB() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some coins
		coin1 = new Coin(currency, BigDecimal.valueOf(0.25));
		coin2 = new Coin(currency, new BigDecimal("0.10"));

		checkoutStationB.getCoinStorage().load(coin1, coin2);

	}

	// this test isn't throwing the overloadException
	@Test(expected = CashOverloadException.class)
	public void testCoinsLoadedOverloadG() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some coins
		Coin c1 = new Coin(currency, new BigDecimal("0.05"));
		Coin c2 = new Coin(currency, new BigDecimal("0.10"));

		SelfCheckoutStationGold.configureCoinStorageUnitCapacity(2);
		this.checkoutStationG = new SelfCheckoutStationGold();
		this.checkoutStationG.plugIn(PowerGrid.instance());
		this.checkoutStationG.turnOn();
		checkoutStationG.getCoinStorage().load(c1, c2);
		// should cause overload now
		Coin c3 = new Coin(currency, new BigDecimal("0.25"));
		checkoutStationG.getCoinStorage().load(c3);
	}

	// this test isn't throwing the overloadException
	@Test(expected = CashOverloadException.class)
	public void testCoinsLoadedOverloadS() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some coins
		Coin c1 = new Coin(currency, new BigDecimal("0.05"));
		Coin c2 = new Coin(currency, new BigDecimal("0.10"));

		SelfCheckoutStationSilver.configureCoinStorageUnitCapacity(2);
		this.checkoutStationS = new SelfCheckoutStationSilver();
		this.checkoutStationS.plugIn(PowerGrid.instance());
		this.checkoutStationS.turnOn();
		checkoutStationS.getCoinStorage().load(c1, c2);
		// should cause overload now
		Coin c3 = new Coin(currency, new BigDecimal("0.25"));
		checkoutStationS.getCoinStorage().load(c3);
	}

	// this test isn't throwing the overloadException
	@Test(expected = CashOverloadException.class)
	public void testCoinsLoadedOverloadB() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some coins
		Coin c1 = new Coin(currency, new BigDecimal("0.05"));
		Coin c2 = new Coin(currency, new BigDecimal("0.10"));

		SelfCheckoutStationBronze.configureCoinStorageUnitCapacity(2);
		this.checkoutStationB = new SelfCheckoutStationBronze();
		this.checkoutStationB.plugIn(PowerGrid.instance());
		this.checkoutStationB.turnOn();
		checkoutStationB.getCoinStorage().load(c1, c2);
		// should cause overload now
		Coin c3 = new Coin(currency, new BigDecimal("0.25"));
		checkoutStationB.getCoinStorage().load(c3);
	}

	@Test(expected = SimulationException.class)
	public void loadCoinDoesntExistTestG() throws SimulationException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare a coin
		Coin c1 = new Coin(currency, new BigDecimal("0.27"));
		checkoutStationG.getCoinStorage().load(c1);
	}

	@Test(expected = SimulationException.class)
	public void loadCoinDoesntExistTestS() throws SimulationException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare a coin
		Coin c1 = new Coin(currency, new BigDecimal("0.27"));
		checkoutStationS.getCoinStorage().load(c1);
	}

	@Test(expected = SimulationException.class)
	public void loadCoinDoesntExistTestB() throws SimulationException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare a coin
		Coin c1 = new Coin(currency, new BigDecimal("0.27"));
		checkoutStationB.getCoinStorage().load(c1);
	}

	@Test(expected = SimulationException.class)
	public void loadNullCoinTestG() throws SimulationException, CashOverloadException {
		// Prepare a coin
		Coin nullCoin = new Coin(null, null);
		checkoutStationG.getCoinStorage().load(nullCoin);
	}

	@Test(expected = SimulationException.class)
	public void loadNullCoinTestS() throws SimulationException, CashOverloadException {
		// Prepare a coin
		Coin nullCoin = new Coin(null, null);
		checkoutStationS.getCoinStorage().load(nullCoin);
	}

	@Test(expected = SimulationException.class)
	public void loadNullCoinTestB() throws SimulationException, CashOverloadException {
		// Prepare a coin
		Coin nullCoin = new Coin(null, null);
		checkoutStationB.getCoinStorage().load(nullCoin);
	}

	@Test
	public void coinsUnloadedG() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some coins
		coin1 = new Coin(currency, BigDecimal.valueOf(0.25));
		coin2 = new Coin(currency, new BigDecimal("0.10"));
		checkoutStationG.getCoinStorage().load(coin1, coin2);
		// now unloaded
		checkoutStationG.getCoinStorage().unload();
		assertTrue(checkoutStationG.getCoinStorage().getCoinCount() == 0);

	}

	@Test
	public void coinsUnloadedS() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some coins
		coin1 = new Coin(currency, BigDecimal.valueOf(0.25));
		coin2 = new Coin(currency, new BigDecimal("0.10"));
		checkoutStationS.getCoinStorage().load(coin1, coin2);
		// now unloaded
		checkoutStationS.getCoinStorage().unload();
		assertTrue(checkoutStationS.getCoinStorage().getCoinCount() == 0);

	}

	@Test
	public void coinsUnloadedB() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some coins
		coin1 = new Coin(currency, BigDecimal.valueOf(0.25));
		coin2 = new Coin(currency, new BigDecimal("0.10"));
		checkoutStationB.getCoinStorage().load(coin1, coin2);
		// now unloaded
		checkoutStationB.getCoinStorage().unload();
		assertTrue(checkoutStationB.getCoinStorage().getCoinCount() == 0);

	}

	@Test
	public void coinRemovedTestG() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		coin1 = new Coin(currency, BigDecimal.valueOf(5.56));
		CoinSlot cs = this.checkoutStationG.getCoinSlot();
		cs.receive(coin1);
		checkoutStationG.getCoinDispensers().remove(coin1);
	}

	@Test
	public void coinRemovedTestS() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		coin1 = new Coin(currency, BigDecimal.valueOf(5.56));
		CoinSlot cs = this.checkoutStationS.getCoinSlot();
		cs.receive(coin1);
		checkoutStationS.getCoinDispensers().remove(coin1);

	}

	@Test
	public void coinRemovedTestB() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		coin1 = new Coin(currency, BigDecimal.valueOf(5.56));
		CoinSlot cs = this.checkoutStationB.getCoinSlot();
		cs.receive(coin1);
		checkoutStationB.getCoinDispensers().remove(coin1);
	}

	// Also does not give nullPointException
	@Test(expected = NullPointerException.class)
	public void removeNonExistentCoinTestG() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		coin1 = new Coin(currency, BigDecimal.valueOf(0.10));
		coin2 = new Coin(currency, BigDecimal.valueOf(0.25));
		CoinSlot cs = this.checkoutStationG.getCoinSlot();
		cs.receive(coin1);
		checkoutStationG.getCoinDispensers().remove(coin2);
	}

	// Also does not give nullPointException
	@Test(expected = NullPointerException.class)
	public void removeNonExistentCoinTestS() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		coin1 = new Coin(currency, BigDecimal.valueOf(0.10));
		coin2 = new Coin(currency, BigDecimal.valueOf(0.25));
		CoinSlot cs = this.checkoutStationS.getCoinSlot();
		cs.receive(coin1);
		checkoutStationS.getCoinDispensers().remove(coin2);
	}

	// Also does not give nullPointException
	@Test(expected = NullPointerException.class)
	public void removeNonExistentCoinTestB() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		coin1 = new Coin(currency, BigDecimal.valueOf(0.10));
		coin2 = new Coin(currency, BigDecimal.valueOf(0.25));
		CoinSlot cs = this.checkoutStationB.getCoinSlot();
		cs.receive(coin1);
		checkoutStationB.getCoinDispensers().remove(coin2);
	}

}
