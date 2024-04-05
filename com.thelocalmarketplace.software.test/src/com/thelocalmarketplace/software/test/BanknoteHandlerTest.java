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

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.*;

import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.NoCashAvailableException;
import com.tdc.banknote.Banknote;
import com.tdc.banknote.BanknoteInsertionSlot;
import com.tdc.coin.Coin;
import com.tdc.coin.CoinSlot;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

import ca.ucalgary.seng300.simulation.SimulationException;
import powerutility.PowerGrid;

public class BanknoteHandlerTest {
	private SelfCheckoutStationSoftware station;
	private SelfCheckoutStationGold checkoutStationG;
	private SelfCheckoutStationSilver checkoutStationS;
	private SelfCheckoutStationBronze checkoutStationB;
	private Banknote banknote1, banknote2;

	@Before
	public void setUp() {

		BigDecimal[] banknoteDenominations = { new BigDecimal("5.0"), new BigDecimal("10.0"), new BigDecimal("20.0"),
				new BigDecimal("50.0"), new BigDecimal("100.0")};

		// Set up Gold selfCheckoutStation
		SelfCheckoutStationGold.resetConfigurationToDefaults();
		SelfCheckoutStationGold.configureBanknoteDenominations(banknoteDenominations);
		this.checkoutStationG = new SelfCheckoutStationGold();
		this.checkoutStationG.plugIn(PowerGrid.instance());
		this.checkoutStationG.turnOn();
		this.station = new SelfCheckoutStationSoftware(checkoutStationG);

		// Set up Silver selfCheckoutStation
		SelfCheckoutStationSilver.resetConfigurationToDefaults();
		SelfCheckoutStationSilver.configureBanknoteDenominations(banknoteDenominations);
		this.checkoutStationS = new SelfCheckoutStationSilver();
		this.checkoutStationS.plugIn(PowerGrid.instance());
		this.checkoutStationS.turnOn();
		this.station = new SelfCheckoutStationSoftware(checkoutStationS);

		// Set up Silver selfCheckoutStation
		SelfCheckoutStationBronze.resetConfigurationToDefaults();
		SelfCheckoutStationBronze.configureBanknoteDenominations(banknoteDenominations);
		this.checkoutStationB = new SelfCheckoutStationBronze();
		this.checkoutStationB.plugIn(PowerGrid.instance());
		this.checkoutStationB.turnOn();
		this.station = new SelfCheckoutStationSoftware(checkoutStationB);

	}

	@Test
	public void banknoteAddedTestG() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some banknotes
		banknote1 = new Banknote(currency, new BigDecimal("10.0"));
		BanknoteInsertionSlot bs = this.checkoutStationG.getBanknoteInput();
		bs.receive(banknote1);
	}

	@Test
	public void banknoteAddedTestS() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some banknotes
		banknote1 = new Banknote(currency, new BigDecimal("10.0"));
		BanknoteInsertionSlot bs = this.checkoutStationS.getBanknoteInput();
		bs.receive(banknote1);
	}

	@Test
	public void banknoteAddedTestB() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some banknotes
		banknote1 = new Banknote(currency, new BigDecimal("10.0"));
		BanknoteInsertionSlot bs = this.checkoutStationB.getBanknoteInput();
		bs.receive(banknote1);
	}

	@Test
	public void badBanknoteTestG() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		banknote1 = new Banknote(currency, BigDecimal.valueOf(5.56));
		BanknoteInsertionSlot bs = this.checkoutStationG.getBanknoteInput();
		bs.receive(banknote1);
	}

	@Test
	public void badBanknoteTestS() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		banknote1 = new Banknote(currency, BigDecimal.valueOf(5.56));
		BanknoteInsertionSlot bs = this.checkoutStationS.getBanknoteInput();
		bs.receive(banknote1);
	}

	@Test
	public void badBanknoteTestB() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		banknote1 = new Banknote(currency, BigDecimal.valueOf(5.56));
		BanknoteInsertionSlot bs = this.checkoutStationB.getBanknoteInput();
		bs.receive(banknote1);
	}

	@Test
	public void banknotesLoadedTestG() throws CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		banknote1 = new Banknote(currency, new BigDecimal("10.0"));
		banknote2 = new Banknote(currency, new BigDecimal("10.0"));
		checkoutStationG.getBanknoteDispensers().get(new BigDecimal("10.0")).load(banknote1,banknote2);
	}
	
	@Test
	public void banknotesLoadedTestS() throws CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		banknote1 = new Banknote(currency, new BigDecimal("10.0"));
		banknote2 = new Banknote(currency, new BigDecimal("10.0"));
		checkoutStationS.getBanknoteDispensers().get(new BigDecimal("10.0")).load(banknote1,banknote2);
	}

	@Test
	public void banknotesLoadedTestB() throws CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		banknote1 = new Banknote(currency, new BigDecimal("10.0"));
		banknote2 = new Banknote(currency, new BigDecimal("10.0"));
		checkoutStationB.getBanknoteDispensers().get(new BigDecimal("10.0")).load(banknote1,banknote2);
	}
	
	@Test
	public void banknoteRemovedTestG() throws DisabledException, CashOverloadException, NoCashAvailableException {
		Currency currency = Currency.getInstance("CAD");
		banknote1 = new Banknote(currency, new BigDecimal("20.0"));
		banknote2 = new Banknote(currency, new BigDecimal("20.0"));
		checkoutStationG.getBanknoteDispensers().get(new BigDecimal("20.0")).load(banknote1,banknote2);
		// remove banknote
		checkoutStationG.getBanknoteDispensers().get(new BigDecimal("20.0")).emit();
		
	}
	
	@Test
	public void banknoteRemovedTestS() throws DisabledException, CashOverloadException, NoCashAvailableException {
		Currency currency = Currency.getInstance("CAD");
		banknote1 = new Banknote(currency, new BigDecimal("20.0"));
		banknote2 = new Banknote(currency, new BigDecimal("20.0"));
		checkoutStationS.getBanknoteDispensers().get(new BigDecimal("20.0")).load(banknote1,banknote2);
		// remove banknote
		checkoutStationS.getBanknoteDispensers().get(new BigDecimal("20.0")).emit();
		
	}
	
	@Test
	public void banknoteRemovedTestB() throws CashOverloadException, NoCashAvailableException, DisabledException {
		Currency currency = Currency.getInstance("CAD");
		banknote1 = new Banknote(currency, new BigDecimal("20.0"));
		banknote2 = new Banknote(currency, new BigDecimal("20.0"));
		checkoutStationB.getBanknoteDispensers().get(new BigDecimal("20.0")).load(banknote1,banknote2);
		// remove banknote
		checkoutStationB.getBanknoteDispensers().get(new BigDecimal("20.0")).emit();
		
	}
	
	@Test
	public void banknotesUnloadedTestG() throws CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		banknote1 = new Banknote(currency, new BigDecimal("20.0"));
		banknote2 = new Banknote(currency, new BigDecimal("20.0"));
		checkoutStationG.getBanknoteDispensers().get(new BigDecimal("20.0")).load(banknote1,banknote2);
		// unload banknote
		checkoutStationG.getBanknoteDispensers().get(new BigDecimal("20.0")).unload();
	}
	
	@Test
	public void banknotesUnloadedTestS() throws CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		banknote1 = new Banknote(currency, new BigDecimal("20.0"));
		banknote2 = new Banknote(currency, new BigDecimal("20.0"));
		checkoutStationS.getBanknoteDispensers().get(new BigDecimal("20.0")).load(banknote1,banknote2);
		// unload banknote
		checkoutStationS.getBanknoteDispensers().get(new BigDecimal("20.0")).unload();
	}
	
	@Test
	public void banknotesUnloadedTestB() throws CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		banknote1 = new Banknote(currency, new BigDecimal("20.0"));
		banknote2 = new Banknote(currency, new BigDecimal("20.0"));
		checkoutStationB.getBanknoteDispensers().get(new BigDecimal("20.0")).load(banknote1,banknote2);
		// remove banknote
		checkoutStationB.getBanknoteDispensers().get(new BigDecimal("20.0")).unload();
	}
}
