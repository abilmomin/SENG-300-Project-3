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

import java.math.BigDecimal;

import java.util.Currency;

import org.junit.Before;
import org.junit.Test;
import org.junit.Test.None;

import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;

import com.thelocalmarketplace.hardware.external.CardIssuer;

import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

import com.thelocalmarketplace.software.funds.CardHandler;
import com.thelocalmarketplace.software.funds.Funds;

import powerutility.PowerGrid;

public class CardHandlerTest {
	private SelfCheckoutStationSoftware station;
	private SelfCheckoutStationBronze checkoutStationB;
	private SelfCheckoutStationSilver checkoutStationS;
	private SelfCheckoutStationGold checkoutStationG;
	private Funds funds;
	private CardHandler cardHandler;
	private CardIssuer cardIssuer;
	
	@Before
	public void setUp() {
		BigDecimal[] coinDenominations = { new BigDecimal("0.25"), new BigDecimal("0.10"), new BigDecimal("0.50"),
				new BigDecimal("1.0") };
		
		// Set up Bronze selfCheckoutStation
		SelfCheckoutStationBronze.resetConfigurationToDefaults();
		SelfCheckoutStationBronze.configureCoinDenominations(coinDenominations);
		SelfCheckoutStationBronze.configureCurrency(Currency.getInstance("CAD"));
		this.checkoutStationB = new SelfCheckoutStationBronze();
		this.checkoutStationB.plugIn(PowerGrid.instance());
		this.checkoutStationB.turnOn();

		// Set up Silver selfCheckoutStation
		SelfCheckoutStationSilver.resetConfigurationToDefaults();
		SelfCheckoutStationSilver.configureCoinDenominations(coinDenominations);
		SelfCheckoutStationSilver.configureCurrency(Currency.getInstance("CAD"));
		this.checkoutStationS = new SelfCheckoutStationSilver();
		this.checkoutStationS.plugIn(PowerGrid.instance());
		this.checkoutStationS.turnOn();
		
		// Set up Gold selfCheckoutStation
		SelfCheckoutStationGold.resetConfigurationToDefaults();
		SelfCheckoutStationGold.configureCoinDenominations(coinDenominations);
		SelfCheckoutStationGold.configureCurrency(Currency.getInstance("CAD"));
		this.checkoutStationG = new SelfCheckoutStationGold();
		this.checkoutStationG.plugIn(PowerGrid.instance());
		this.checkoutStationG.turnOn();
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullFundControllerInConstructor() {
		cardHandler = new CardHandler(null);
	}

	@Test(expected = None.class)
	public void testCardHandlerConstructor() {
		// Test with SelfCheckoutStationBronze
		station = new SelfCheckoutStationSoftware(checkoutStationB);
		funds = new Funds(station);
		cardHandler = new CardHandler(funds);

		// Test with SelfCheckoutStationSilver
		station = new SelfCheckoutStationSoftware(checkoutStationS);
		funds = new Funds(station);
		cardHandler = new CardHandler(funds);

		// Test with SelfCheckoutStationGold
		station = new SelfCheckoutStationSoftware(checkoutStationG);
		funds = new Funds(station);
		cardHandler = new CardHandler(funds);
	}
	
	@Test(expected = None.class)
	public void approveNullPurchase() {
		// Test with SelfCheckoutStationBronze
		station = new SelfCheckoutStationSoftware(checkoutStationB);
		funds = new Funds(station);
		cardHandler = new CardHandler(funds);
		cardHandler.approvePurchase(null, 0);

		// Test with SelfCheckoutStationSilver
		station = new SelfCheckoutStationSoftware(checkoutStationS);
		funds = new Funds(station);
		cardHandler = new CardHandler(funds);
		cardHandler.approvePurchase(null, 0);

		// Test with SelfCheckoutStationGold
		station = new SelfCheckoutStationSoftware(checkoutStationG);
		funds = new Funds(station);
		cardHandler = new CardHandler(funds);
		cardHandler.approvePurchase(null, 0);
	}
	
}