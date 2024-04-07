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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import java.math.BigDecimal;

import java.util.Calendar;
import java.util.Currency;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Test.None;

import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.card.Card.CardData;
import com.jjjwelectronics.card.CardReaderGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;

import com.thelocalmarketplace.hardware.external.CardIssuer;

import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

import com.thelocalmarketplace.software.funds.CardHandler;
import com.thelocalmarketplace.software.funds.Funds;

import powerutility.PowerGrid;

public class CardHandlerTest {
	private SelfCheckoutStationSoftware stationB;
	private SelfCheckoutStationSoftware stationS;
	private SelfCheckoutStationSoftware stationG;
	private SelfCheckoutStationBronze checkoutStationB;
	private SelfCheckoutStationSilver checkoutStationS;
	private SelfCheckoutStationGold checkoutStationG;
	private Funds fundsB;
	private Funds fundsS;
	private Funds fundsG;
	private CardHandler cardHandlerB;
	private CardHandler cardHandlerS;
	private CardHandler cardHandlerG;
	private CardIssuer cardIssuer;
	private Card creditCard;
	private CardReaderGold cardReader;
	private Calendar expiry;
	
	@Before
	public void setUp() {
		BigDecimal[] coinDenominations = { new BigDecimal("0.25"), new BigDecimal("0.10"), new BigDecimal("0.50"),
				new BigDecimal("1.0") };
		PowerGrid.engageUninterruptiblePowerSource();
		
		int holdCount = 10;
		cardIssuer = new CardIssuer("Seng300 Bank", holdCount);
		creditCard = new Card("Credit", "21", "Holder1", "211", null, false, false);
		expiry = Calendar.getInstance();
		expiry.add(Calendar.YEAR, 5);
		cardIssuer.addCardData(creditCard.number, creditCard.cardholder, expiry, creditCard.cvv, 2000);
		cardReader = new CardReaderGold();
		cardReader.plugIn(PowerGrid.instance());
		cardReader.turnOn();
		
		// Set up Bronze selfCheckoutStation
		SelfCheckoutStationBronze.resetConfigurationToDefaults();
		SelfCheckoutStationBronze.configureCoinDenominations(coinDenominations);
		SelfCheckoutStationBronze.configureCurrency(Currency.getInstance("CAD"));
		checkoutStationB = new SelfCheckoutStationBronze();
		checkoutStationB.plugIn(PowerGrid.instance());
		checkoutStationB.turnOn();
		stationB = new SelfCheckoutStationSoftware(checkoutStationB);
		fundsB = new Funds(stationB);
		
		// Set up Silver selfCheckoutStation
		SelfCheckoutStationSilver.resetConfigurationToDefaults();
		SelfCheckoutStationSilver.configureCoinDenominations(coinDenominations);
		SelfCheckoutStationSilver.configureCurrency(Currency.getInstance("CAD"));
		checkoutStationS = new SelfCheckoutStationSilver();
		checkoutStationS.plugIn(PowerGrid.instance());
		checkoutStationS.turnOn();
		stationS = new SelfCheckoutStationSoftware(checkoutStationB);
		fundsS = new Funds(stationS);
		
		// Set up Gold selfCheckoutStation
		SelfCheckoutStationGold.resetConfigurationToDefaults();
		SelfCheckoutStationGold.configureCoinDenominations(coinDenominations);
		SelfCheckoutStationGold.configureCurrency(Currency.getInstance("CAD"));
		checkoutStationG = new SelfCheckoutStationGold();
		checkoutStationG.plugIn(PowerGrid.instance());
		checkoutStationG.turnOn();
		stationG = new SelfCheckoutStationSoftware(checkoutStationB);
        fundsG = new Funds(stationG);
	}
	
	@After
	public void tearDown() {
		checkoutStationB = null;
		checkoutStationS = null;
		checkoutStationG = null;
		stationB = null;
		stationS = null;
		stationG = null;
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullFundControllerInConstructorB() {
		cardHandlerB = new CardHandler(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullFundControllerInConstructorS() {
		cardHandlerS = new CardHandler(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullFundControllerInConstructorG() {
		cardHandlerG = new CardHandler(null);
	}

	@Test(expected = None.class)
	public void testCardHandlerConstructorB() {
		cardHandlerB = new CardHandler(fundsB);
	}

	@Test(expected = None.class)
	public void testCardHandlerConstructorS() {
		cardHandlerS = new CardHandler(fundsS);
	}

	@Test(expected = None.class)
	public void testCardHandlerConstructorG() {
		cardHandlerG = new CardHandler(fundsG);
	}

	@Test
	public void approvePurchaseWithoutBankB() {
		cardHandlerB = new CardHandler(fundsB);
		assertFalse(cardHandlerB.approvePurchase(null, 0));
        assertFalse(cardHandlerB.approvePurchase(creditCard.number, 10));
	}

	@Test
	public void approvePurchaseWithoutBankS() {
		cardHandlerS = new CardHandler(fundsS);
		assertFalse(cardHandlerS.approvePurchase(null, 0));
        assertFalse(cardHandlerS.approvePurchase(creditCard.number, 10));
	}

	@Test
	public void approvePurchaseWithoutBankG() {
		cardHandlerG = new CardHandler(fundsG);
		assertFalse(cardHandlerG.approvePurchase(null, 0));
        assertFalse(cardHandlerG.approvePurchase(creditCard.number, 10));
	}
	
	@Test
	public void approveValidPurchaseB() {
		stationB.addBank(cardIssuer);
        cardHandlerB = new CardHandler(fundsB);
        assertTrue(cardHandlerB.approvePurchase(creditCard.number, 10));
	}
	
	@Test
	public void approveValidPurchaseS() {
		stationS.addBank(cardIssuer);
        cardHandlerS = new CardHandler(fundsS);
        assertTrue(cardHandlerS.approvePurchase(creditCard.number, 10));
	}
	
	@Test
	public void approveValidPurchaseG() {
        stationG.addBank(cardIssuer);
        cardHandlerG = new CardHandler(fundsG);
        assertTrue(cardHandlerG.approvePurchase(creditCard.number, 10));
	}
	
	@Test
	public void approveInvalidPurchaseB() {
        stationB.addBank(cardIssuer);
        cardHandlerB = new CardHandler(fundsB);
        assertFalse(cardHandlerB.approvePurchase(null, 10));
        assertFalse(cardHandlerB.approvePurchase(creditCard.number, -1));
	}
	
	@Test
	public void approveInvalidPurchaseS() {
        stationS.addBank(cardIssuer);
        cardHandlerS = new CardHandler(fundsS);
        assertFalse(cardHandlerS.approvePurchase(null, 10));
        assertFalse(cardHandlerS.approvePurchase(creditCard.number, -1));
	}
	
	@Test
	public void approveInvalidPurchaseG() {
        stationG.addBank(cardIssuer);
        cardHandlerG = new CardHandler(fundsG);
        assertFalse(cardHandlerG.approvePurchase(null, 10));
        assertFalse(cardHandlerG.approvePurchase(creditCard.number, -1));
	}
	
	@Test(expected = None.class)
	public void theDataFromACardHasBeenReadWhileBlockedB() throws IOException {
		cardHandlerB = new CardHandler(fundsB);
		CardData data = cardReader.swipe(creditCard);
		stationB.setStationBlock();
		cardHandlerB.theDataFromACardHasBeenRead(data);
	}
	
	@Test(expected = None.class)
	public void theDataFromACardHasBeenReadWhileBlockedS() throws IOException {
		cardHandlerS = new CardHandler(fundsS);
		CardData data = cardReader.swipe(creditCard);
		stationS.setStationBlock();
		cardHandlerS.theDataFromACardHasBeenRead(data);
	}
	
	@Test(expected = None.class)
	public void theDataFromACardHasBeenReadWhileBlockedG() throws IOException {
		cardHandlerG = new CardHandler(fundsG);
		CardData data = cardReader.swipe(creditCard);
		stationG.setStationBlock();
		cardHandlerG.theDataFromACardHasBeenRead(data);
	}
	
	@Test(expected = None.class)
	public void theDataFromACardHasBeenReadInvalidB() throws IOException {
		stationB.addBank(cardIssuer);
		cardHandlerB = new CardHandler(fundsB);
		CardData data = cardReader.swipe(creditCard);
		cardHandlerB.theDataFromACardHasBeenRead(data);
	}
	
	@Test(expected = None.class)
	public void theDataFromACardHasBeenReadInvalidS() throws IOException {
		stationS.addBank(cardIssuer);
		cardHandlerS = new CardHandler(fundsS);
		CardData data = cardReader.swipe(creditCard);
		cardHandlerS.theDataFromACardHasBeenRead(data);
	}
	
	@Test(expected = None.class)
	public void theDataFromACardHasBeenReadInvalidG() throws IOException {
		stationG.addBank(cardIssuer);
		cardHandlerG = new CardHandler(fundsG);
		CardData data = cardReader.swipe(creditCard);
		cardHandlerG.theDataFromACardHasBeenRead(data);
	}
	
	@Test(expected = None.class)
	public void theDataFromACardHasBeenReadValidB() throws IOException {
		stationB.addBank(cardIssuer);
		stationB.setOrderTotalPrice(1);
		cardHandlerB = new CardHandler(fundsB);
		CardData data = cardReader.swipe(creditCard);
		cardHandlerB.theDataFromACardHasBeenRead(data);
	}
	
	@Test(expected = None.class)
	public void theDataFromACardHasBeenReadValidS() throws IOException {
		stationS.addBank(cardIssuer);
		stationS.setOrderTotalPrice(1);
		cardHandlerS = new CardHandler(fundsS);
		CardData data = cardReader.swipe(creditCard);
		cardHandlerS.theDataFromACardHasBeenRead(data);
	}
	
	@Test(expected = None.class)
	public void theDataFromACardHasBeenReadValidG() throws IOException {
		stationG.addBank(cardIssuer);
		stationG.setOrderTotalPrice(1);
		cardHandlerG = new CardHandler(fundsG);
		CardData data = cardReader.swipe(creditCard);
		cardHandlerG.theDataFromACardHasBeenRead(data);
	}
}