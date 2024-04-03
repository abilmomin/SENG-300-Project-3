package com.thelocalmarketplace.software.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import org.junit.*;

import com.tdc.coin.Coin;
import com.tdc.coin.CoinDispenserBronze;
import com.tdc.coin.CoinDispenserGold;
import com.tdc.coin.CoinValidator;

import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.funds.*;

import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import powerutility.PowerGrid;

public class CoinHandlerTest {
	private SelfCheckoutStationSoftware station;
	private SelfCheckoutStationGold checkoutStationG;
	private SelfCheckoutStationSilver checkoutStationS;
    private SelfCheckoutStationBronze checkoutStationB;
    private ArrayList<Product> coinsList;
    private Coin coin1, coin2;
    private CoinHandler coinHandlerG;
    private CoinHandler coinHandlerS;
    private CoinHandler coinHandlerB;
    private Funds fundsG;
    private Funds fundsS;
    private Funds fundsB;
    

    @Before
    public void setUp() {
    	BigDecimal[] coinDenominations = {new BigDecimal("0.25"), new BigDecimal("0.10"), new BigDecimal("0.50"), new BigDecimal("1.0")};
    	
    	//Set up Gold selfCheckoutStation
    	SelfCheckoutStationGold.resetConfigurationToDefaults();
    	SelfCheckoutStationGold.configureCoinDenominations(coinDenominations);
    	SelfCheckoutStationGold.configureCurrency(Currency.getInstance("CAD"));
        this.checkoutStationG = new SelfCheckoutStationGold();
        this.checkoutStationG.plugIn(PowerGrid.instance());
        this.checkoutStationG.turnOn();
        this.station = new SelfCheckoutStationSoftware(checkoutStationG);
        this.fundsG = new Funds(station);
        this.coinHandlerG = new CoinHandler(fundsG);
        
        
        //Set up Silver selfCheckoutStation
    	SelfCheckoutStationSilver.resetConfigurationToDefaults();
    	SelfCheckoutStationSilver.configureCoinDenominations(coinDenominations);
    	SelfCheckoutStationSilver.configureCurrency(Currency.getInstance("CAD"));
        this.checkoutStationS = new SelfCheckoutStationSilver();
        this.checkoutStationS.plugIn(PowerGrid.instance());
        this.checkoutStationS.turnOn();
        this.station = new SelfCheckoutStationSoftware(checkoutStationS);
        this.fundsS = new Funds(station);
        this.coinHandlerS = new CoinHandler(fundsS);
        
        //Set up Bronze selfCheckoutStation
    	SelfCheckoutStationBronze.resetConfigurationToDefaults();
    	SelfCheckoutStationBronze.configureCoinDenominations(coinDenominations);
    	SelfCheckoutStationBronze.configureCurrency(Currency.getInstance("CAD"));
        this.checkoutStationB = new SelfCheckoutStationBronze();
        this.checkoutStationB.plugIn(PowerGrid.instance());
        this.checkoutStationB.turnOn();
        this.station = new SelfCheckoutStationSoftware(checkoutStationB);
        this.fundsB = new Funds(station);
        this.coinHandlerB = new CoinHandler(fundsB);
        
    }
    
    @Test
    public void validCoinDetectedGoldTest() {
    	Currency currency = Currency.getInstance("CAD");
    	List<BigDecimal> denominations = new ArrayList<BigDecimal>();
    	denominations.add(new BigDecimal("0.05"));
    	denominations.add(new BigDecimal("0.10"));
    	denominations.add(new BigDecimal("0.25"));
    	denominations.add(new BigDecimal("1.0"));
    	denominations.add(new BigDecimal("2.0"));
    	
    	CoinValidator validator = new CoinValidator(currency, denominations);
    	BigDecimal denomination = new BigDecimal("0.10");
    	coinHandlerG.validCoinDetected(validator, denomination);
		
    }
    
    @Test
    public void validCoinDetectedSilverTest() {
    	Currency currency = Currency.getInstance("CAD");
    	List<BigDecimal> denominations = new ArrayList<BigDecimal>();
    	denominations.add(new BigDecimal("0.05"));
    	denominations.add(new BigDecimal("0.10"));
    	denominations.add(new BigDecimal("0.25"));
    	denominations.add(new BigDecimal("1.0"));
    	denominations.add(new BigDecimal("2.0"));
    	
    	CoinValidator validator = new CoinValidator(currency, denominations);
    	BigDecimal denomination = new BigDecimal("0.10");
    	coinHandlerS.validCoinDetected(validator, denomination);
		
    }
    
    @Test
    public void validCoinDetectedBronzeTest() {
    	Currency currency = Currency.getInstance("CAD");
    	List<BigDecimal> denominations = new ArrayList<BigDecimal>();
    	denominations.add(new BigDecimal("0.05"));
    	denominations.add(new BigDecimal("0.10"));
    	denominations.add(new BigDecimal("0.25"));
    	denominations.add(new BigDecimal("1.0"));
    	denominations.add(new BigDecimal("2.0"));
    	
    	CoinValidator validator = new CoinValidator(currency, denominations);
    	BigDecimal denomination = new BigDecimal("0.10");
    	coinHandlerB.validCoinDetected(validator, denomination);
		
    }
    
    @Test 
    public void invalidCoinDetectedGoldTest() {
    	Currency currency = Currency.getInstance("CAD");
    	List<BigDecimal> denominations = new ArrayList<BigDecimal>();
    	denominations.add(new BigDecimal("0.05"));
    	denominations.add(new BigDecimal("0.10"));
    	denominations.add(new BigDecimal("0.25"));
    	denominations.add(new BigDecimal("1.0"));
    	denominations.add(new BigDecimal("2.0"));
    	
    	CoinValidator validator = new CoinValidator(currency, denominations);
    	coinHandlerG.invalidCoinDetected(validator);
    }
    @Test 
    public void invalidCoinDetectedSilverTest() {
    	Currency currency = Currency.getInstance("CAD");
    	List<BigDecimal> denominations = new ArrayList<BigDecimal>();
    	denominations.add(new BigDecimal("0.05"));
    	denominations.add(new BigDecimal("0.10"));
    	denominations.add(new BigDecimal("0.25"));
    	denominations.add(new BigDecimal("1.0"));
    	denominations.add(new BigDecimal("2.0"));
    	
    	CoinValidator validator = new CoinValidator(currency, denominations);
    	coinHandlerS.invalidCoinDetected(validator);
    }
    
    @Test 
    public void invalidCoinDetectedBronzeTest() {
    	Currency currency = Currency.getInstance("CAD");
    	List<BigDecimal> denominations = new ArrayList<BigDecimal>();
    	denominations.add(new BigDecimal("0.05"));
    	denominations.add(new BigDecimal("0.10"));
    	denominations.add(new BigDecimal("0.25"));
    	denominations.add(new BigDecimal("1.0"));
    	denominations.add(new BigDecimal("2.0"));
    	
    	CoinValidator validator = new CoinValidator(currency, denominations);
    	coinHandlerB.invalidCoinDetected(validator);
    }
    
    @Test
    public void coinAddedTest() {
    	Currency currency = Currency.getInstance("CAD");
        // Prepare some coins
        coin1 = new Coin(currency, new BigDecimal("0.10"));
        
        //Prepare some dispensers there's no silver
        
        CoinDispenserGold dispenserG = new CoinDispenserGold(2);
        CoinDispenserBronze dispenserB = new CoinDispenserBronze(2);
        dispenserG.connect(PowerGrid.instance());
        coinHandlerG.coinAdded(dispenserG, coin1);
        coinHandlerB.coinAdded(dispenserB, coin1);
    }
    
    @Test (expected = NullPointerException.class)
    public void coinAddedNullGoldTest()throws NullPointerSimulationException{
    	CoinDispenserGold dispenserG = new CoinDispenserGold(5);
    	coinHandlerG.coinAdded(dispenserG, null);
    }
    
}
