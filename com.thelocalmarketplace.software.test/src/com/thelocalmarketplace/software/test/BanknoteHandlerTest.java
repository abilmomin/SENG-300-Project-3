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
import java.util.List;
import java.util.Map;

import org.junit.*;

import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.NoCashAvailableException;
import com.tdc.banknote.Banknote;
import com.tdc.banknote.BanknoteInsertionSlot;
import com.tdc.banknote.BanknoteStorageUnit;
import com.tdc.banknote.IBanknoteDispenser;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.funds.BanknoteHandler;
import com.thelocalmarketplace.software.funds.Funds;

import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import ca.ucalgary.seng300.simulation.SimulationException;
import powerutility.PowerGrid;

public class BanknoteHandlerTest {
	private SelfCheckoutStationSoftware stationG;
	private SelfCheckoutStationSoftware stationB;
	private SelfCheckoutStationSoftware stationS;
	private SelfCheckoutStationGold checkoutStationG;
	private SelfCheckoutStationSilver checkoutStationS;
	private SelfCheckoutStationBronze checkoutStationB;
	private Banknote banknote1, banknote2;

	@Before
	public void setUp() {

		BigDecimal[] banknoteDenominations = { new BigDecimal("5.0"), new BigDecimal("10.0"), new BigDecimal("20.0"),
				new BigDecimal("50.0"), new BigDecimal("100.0") };

		// Set up Gold selfCheckoutStation
		SelfCheckoutStationGold.resetConfigurationToDefaults();
		SelfCheckoutStationGold.configureBanknoteDenominations(banknoteDenominations);
		SelfCheckoutStationGold.configureCurrency(Currency.getInstance("CAD"));
		this.checkoutStationG = new SelfCheckoutStationGold();
		this.checkoutStationG.plugIn(PowerGrid.instance());
		this.checkoutStationG.turnOn();
		this.stationG = new SelfCheckoutStationSoftware(checkoutStationG);

		// Set up Silver selfCheckoutStation
		SelfCheckoutStationSilver.resetConfigurationToDefaults();
		SelfCheckoutStationSilver.configureBanknoteDenominations(banknoteDenominations);
		SelfCheckoutStationSilver.configureCurrency(Currency.getInstance("CAD"));
		this.checkoutStationS = new SelfCheckoutStationSilver();
		this.checkoutStationS.plugIn(PowerGrid.instance());
		this.checkoutStationS.turnOn();
		this.stationS = new SelfCheckoutStationSoftware(checkoutStationS);

		// Set up Bronze selfCheckoutStation
		SelfCheckoutStationBronze.resetConfigurationToDefaults();
		SelfCheckoutStationBronze.configureBanknoteDenominations(banknoteDenominations);
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
		// Prepare some Banknotes
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
	public void coinAddedTestDIspenserChangeCoverage() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some coins
		banknote1 = new Banknote(currency, new BigDecimal("10.0"));
		banknote2 = new Banknote(currency, new BigDecimal("5.0"));

		BanknoteInsertionSlot cs = this.checkoutStationG.getBanknoteInput();
		checkoutStationG.getBanknoteStorage().load(banknote2);
		stationG.setOrderTotalPrice(5);
		cs.receive(banknote1);
		System.out.println(checkoutStationG.getBanknoteStorage().getBanknoteCount());
		assertTrue(checkoutStationG.getBanknoteStorage().getBanknoteCount() == 1);
		assertTrue(stationG.getTotalOrderPrice() == 0);
		checkoutStationG.getBanknoteDispensers().get(new BigDecimal("5.0")).load(banknote2);
		stationG.setOrderTotalPrice(0.05);
		cs.receive(new Banknote(currency, new BigDecimal("5.0")));
	}

	@Test
	public void validBanknoteDetectedGoldTest() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some banknotes
		banknote1 = new Banknote(currency, BigDecimal.valueOf(20.0));

		BanknoteInsertionSlot bs = this.checkoutStationG.getBanknoteInput();
		bs.receive(banknote1);
	}

	@Test
	public void validBanknoteDetectedSilverTest() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some banknotes
		banknote1 = new Banknote(currency, BigDecimal.valueOf(20.0));

		BanknoteInsertionSlot bs = this.checkoutStationS.getBanknoteInput();
		bs.receive(banknote1);
	}

	@Test
	public void validBanknoteDetectedBronzeTest() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some banknotes
		banknote1 = new Banknote(currency, BigDecimal.valueOf(20.0));

		BanknoteInsertionSlot bs = this.checkoutStationB.getBanknoteInput();
		bs.receive(banknote1);
	}

	@Test(expected = NullPointerSimulationException.class)
	public void invalidNullBanknoteDetectedGoldTest() throws DisabledException, CashOverloadException {

		BanknoteInsertionSlot bs = this.checkoutStationG.getBanknoteInput();
		bs.receive(null);
	}

	@Test(expected = NullPointerSimulationException.class)
	public void invalidNullBanknoteDetectedSilverTest() throws DisabledException, CashOverloadException {

		BanknoteInsertionSlot bs = this.checkoutStationS.getBanknoteInput();
		bs.receive(null);
	}

	@Test(expected = NullPointerSimulationException.class)
	public void invalidNullBanknoteDetectedBronzeTest() throws DisabledException, CashOverloadException {

		BanknoteInsertionSlot bs = this.checkoutStationB.getBanknoteInput();
		bs.receive(null);
	}

	@Test
	public void invalidBanknoteDetectedGoldTest() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some banknotes
		banknote1 = new Banknote(currency, BigDecimal.valueOf(2.456));

		BanknoteInsertionSlot bs = this.checkoutStationG.getBanknoteInput();
		bs.receive(banknote1);
	}

	@Test
	public void invalidBanknoteDetectedSilverTest() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some banknotes
		banknote1 = new Banknote(currency, BigDecimal.valueOf(2.456));

		BanknoteInsertionSlot bs = this.checkoutStationS.getBanknoteInput();
		bs.receive(banknote1);
	}

	@Test
	public void invalidBanknoteDetectedBronzeTest() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some banknotes
		banknote1 = new Banknote(currency, BigDecimal.valueOf(2.456));

		BanknoteInsertionSlot bs = this.checkoutStationB.getBanknoteInput();
		bs.receive(banknote1);
	}

	@Test
	public void banknotesLoadedG() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some banknotes
		banknote1 = new Banknote(currency, BigDecimal.valueOf(20.0));

		checkoutStationG.getBanknoteDispensers().get(new BigDecimal("20.0")).load(banknote1);

	}

	@Test
	public void banknotesLoadedS() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some banknotes
		banknote1 = new Banknote(currency, BigDecimal.valueOf(20.0));

		checkoutStationS.getBanknoteDispensers().get(new BigDecimal("20.0")).load(banknote1);

	}

	@Test
	public void banknotesLoadedB() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		// Prepare some banknotes
		banknote1 = new Banknote(currency, BigDecimal.valueOf(20.0));

		checkoutStationB.getBanknoteDispensers().get(new BigDecimal("20.0")).load(banknote1);

	}

	@Test
	public void banknotesUnloadedTestG() throws CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		banknote1 = new Banknote(currency, new BigDecimal("20.0"));
		banknote2 = new Banknote(currency, new BigDecimal("20.0"));
		checkoutStationG.getBanknoteDispensers().get(new BigDecimal("20.0")).load(banknote1, banknote2);
		// unload banknote
		checkoutStationG.getBanknoteDispensers().get(new BigDecimal("20.0")).unload();
	}

	@Test
	public void banknotesUnloadedTestS() throws CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		banknote1 = new Banknote(currency, new BigDecimal("20.0"));
		banknote2 = new Banknote(currency, new BigDecimal("20.0"));
		checkoutStationS.getBanknoteDispensers().get(new BigDecimal("20.0")).load(banknote1, banknote2);
		// unload banknote
		checkoutStationS.getBanknoteDispensers().get(new BigDecimal("20.0")).unload();
	}

	@Test
	public void banknotesUnloadedTestB() throws CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		banknote1 = new Banknote(currency, new BigDecimal("20.0"));
		banknote2 = new Banknote(currency, new BigDecimal("20.0"));
		checkoutStationB.getBanknoteDispensers().get(new BigDecimal("20.0")).load(banknote1, banknote2);
		// unload banknote
		checkoutStationB.getBanknoteDispensers().get(new BigDecimal("20.0")).unload();
	}

	@Test
	public void banknoteRemovedTestG() throws DisabledException, CashOverloadException, NoCashAvailableException {
		Currency currency = Currency.getInstance("CAD");
		banknote1 = new Banknote(currency, new BigDecimal("20.0"));
		banknote2 = new Banknote(currency, new BigDecimal("20.0"));
		checkoutStationG.getBanknoteDispensers().get(new BigDecimal("20.0")).load(banknote1, banknote2);
		// remove banknote
		checkoutStationG.getBanknoteDispensers().get(new BigDecimal("20.0")).emit();

	}

	@Test
	public void banknoteRemovedTestS() throws DisabledException, CashOverloadException, NoCashAvailableException {
		Currency currency = Currency.getInstance("CAD");
		banknote1 = new Banknote(currency, new BigDecimal("20.0"));
		banknote2 = new Banknote(currency, new BigDecimal("20.0"));
		checkoutStationS.getBanknoteDispensers().get(new BigDecimal("20.0")).load(banknote1, banknote2);
		// remove banknote
		checkoutStationS.getBanknoteDispensers().get(new BigDecimal("20.0")).emit();

	}

	@Test
	public void banknoteRemovedTestB() throws DisabledException, CashOverloadException, NoCashAvailableException {
		Currency currency = Currency.getInstance("CAD");
		banknote1 = new Banknote(currency, new BigDecimal("20.0"));
		banknote2 = new Banknote(currency, new BigDecimal("20.0"));
		checkoutStationB.getBanknoteDispensers().get(new BigDecimal("20.0")).load(banknote1, banknote2);
		// remove banknote
		checkoutStationB.getBanknoteDispensers().get(new BigDecimal("20.0")).emit();

	}

	// Banknote Dispenser Tests

	@Test
	public void testBanknotesEmptyDG()
			throws SimulationException, CashOverloadException, NoCashAvailableException, DisabledException {
		Currency currency = Currency.getInstance("CAD");
		BigDecimal[] denominations = checkoutStationG.getBanknoteDenominations();
		Map<BigDecimal, IBanknoteDispenser> dispensers = checkoutStationG.getBanknoteDispensers();
		for (BigDecimal denomination : denominations) {
			IBanknoteDispenser dispenser = dispensers.get(denomination);
			Banknote banknote = new Banknote(currency, denomination);
			dispenser.load(banknote);
			dispenser.emit();
		}
	}

	@Test
	public void testBanknotesEmptyDS()
			throws SimulationException, CashOverloadException, NoCashAvailableException, DisabledException {
		Currency currency = Currency.getInstance("CAD");
		BigDecimal[] denominations = checkoutStationS.getBanknoteDenominations();
		Map<BigDecimal, IBanknoteDispenser> dispensers = checkoutStationS.getBanknoteDispensers();
		for (BigDecimal denomination : denominations) {
			IBanknoteDispenser dispenser = dispensers.get(denomination);
			Banknote banknote = new Banknote(currency, denomination);
			dispenser.load(banknote);
			dispenser.emit();
		}
	}

	@Test
	public void testBanknotesEmptyDB()
			throws SimulationException, CashOverloadException, NoCashAvailableException, DisabledException {
		Currency currency = Currency.getInstance("CAD");
		BigDecimal[] denominations = checkoutStationB.getBanknoteDenominations();
		Map<BigDecimal, IBanknoteDispenser> dispensers = checkoutStationB.getBanknoteDispensers();
		for (BigDecimal denomination : denominations) {
			IBanknoteDispenser dispenser = dispensers.get(denomination);
			Banknote banknote = new Banknote(currency, denomination);
			dispenser.load(banknote);
			dispenser.emit();
		}
	}

	// Banknote Storage Unit Tests

	@Test
	public void testBanknotesFullSG() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		BanknoteStorageUnit storage = checkoutStationG.getBanknoteStorage();
		Funds funds = new Funds(stationG);
		BanknoteHandler handler = new BanknoteHandler(funds);
		storage.attach(handler);
		while (storage.hasSpace()) {
			storage.receive(new Banknote(currency, BigDecimal.valueOf(10.0)));
		}
	}

	@Test
	public void testBanknotesFullSS() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		BanknoteStorageUnit storage = checkoutStationS.getBanknoteStorage();
		Funds funds = new Funds(stationS);
		BanknoteHandler handler = new BanknoteHandler(funds);
		storage.attach(handler);
		while (storage.hasSpace()) {
			storage.receive(new Banknote(currency, BigDecimal.valueOf(10.0)));
		}
	}

	@Test
	public void testBanknotesFullSB() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		BanknoteStorageUnit storage = checkoutStationB.getBanknoteStorage();
		Funds funds = new Funds(stationB);
		BanknoteHandler handler = new BanknoteHandler(funds);
		storage.attach(handler);
		while (storage.hasSpace()) {
			storage.receive(new Banknote(currency, BigDecimal.valueOf(10.0)));
		}
	}

	@Test
	public void testBanknotesLoadSG() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		BanknoteStorageUnit storage = checkoutStationG.getBanknoteStorage();
		Funds funds = new Funds(stationG);
		BanknoteHandler handler = new BanknoteHandler(funds);
		storage.attach(handler);
		while (storage.hasSpace()) {
			storage.load(new Banknote(currency, BigDecimal.valueOf(20.0)));
		}
	}

	@Test
	public void testBanknotesLoadSS() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		BanknoteStorageUnit storage = checkoutStationS.getBanknoteStorage();
		Funds funds = new Funds(stationS);
		BanknoteHandler handler = new BanknoteHandler(funds);
		storage.attach(handler);
		while (storage.hasSpace()) {
			storage.load(new Banknote(currency, BigDecimal.valueOf(20.0)));
		}
	}

	@Test
	public void testBanknotesLoadSB() throws DisabledException, CashOverloadException {
		Currency currency = Currency.getInstance("CAD");
		BanknoteStorageUnit storage = checkoutStationB.getBanknoteStorage();
		Funds funds = new Funds(stationB);
		BanknoteHandler handler = new BanknoteHandler(funds);
		storage.attach(handler);
		while (storage.hasSpace()) {
			storage.load(new Banknote(currency, BigDecimal.valueOf(20.0)));
		}
	}
}
