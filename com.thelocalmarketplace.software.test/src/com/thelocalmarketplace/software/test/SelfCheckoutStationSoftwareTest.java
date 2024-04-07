/**
 * Yotam Rojnov (UCID: 30173949)
 * Duncan McKay (UCID: 30177857)
 * Mahfuz Alam (UCID:30142265)
 * Luis Trigueros Granillo (UCID: 30167989)
 * Lilia Skumatova (UCID: 30187339)
 * Abdelrahman Abbas (UCID: 30110374)
 * Talaal Irtija (UCID: 30169780)
 * Alejandro Cardona (UCID: 30178941)
 * Alexandre Duteau (UCID: 30192082)
 * Grace Johnson (UCID: 30149693)
 * Abil Momin (UCID: 30154771)
 * Tara Ghasemi M. Rad (UCID: 30171212)
 * Izabella Mawani (UCID: 30179738)
 * Binish Khalid (UCID: 30061367)
 * Fatima Khalid (UCID: 30140757)
 * Lucas Kasdorf (UCID: 30173922)
 * Emily Garcia-Volk (UCID: 30140791)
 * Yuinikoru Futamata (UCID: 30173228)
 * Joseph Tandyo (UCID: 30182561)
 * Syed Haider (UCID: 30143096)
 * Nami Marwah (UCID: 30178528)
 */

package com.thelocalmarketplace.software.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

import org.junit.Before;

import org.junit.Test;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.bag.ReusableBag;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedItem;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.external.CardIssuer;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

import ca.ucalgary.seng300.simulation.InvalidStateSimulationException;
import powerutility.PowerGrid;

public class SelfCheckoutStationSoftwareTest {

    
    private SelfCheckoutStationSoftware software;

	
	
	
    @Before
    public void setUp() {
    
    	SelfCheckoutStationBronze stationB = new SelfCheckoutStationBronze();
    	stationB.plugIn(PowerGrid.instance());
    	stationB.turnOn();
    	software = new SelfCheckoutStationSoftware(stationB);
        
    	
    	
    	// Adding a sample BarcodedItem item 
    	Mass mass = new Mass(BigInteger.valueOf(50));
    	Numeral[] barcodeDigits = new Numeral[]{Numeral.one, Numeral.two, Numeral.three, Numeral.four, Numeral.five};
    	Barcode barcode = new Barcode(barcodeDigits);
    	Item newItem = new BarcodedItem( barcode, mass);
    	
    	// Adding a sample BarcodedItem item 
    	
    	PriceLookUpCode plucode = new PriceLookUpCode("1234");
    	Item newItem1 = new PLUCodedItem(  plucode, mass);
    	
    	
    	software.addItemToOrder(newItem);
    	software.addItemToOrder(newItem1);
    
    }
    

//    @After
//    public void teardown() {
//        paymentHandlerG = null;
//        checkoutStationG = null;
//        
//        paymentHandlerS = null;
//        checkoutStationS = null;
//        
//        paymentHandlerB = null;
//        checkoutStationB = null;
//        SelfCheckoutStationSoftware.setStationBlock(false);
//
//    }
    
    
    
    
    @Test 
    public void testSetStationBlock(){
    	// have to give it power first 
    	software.setStationBlock();
    	assertFalse(software.getStationBlock());
    	
    }
    
    
    @Test 
    public void testSetStationUnblock(){
    	
    	software.setStationUnblock();
    	assertFalse(software.getStationBlock());
    	
    }
    
    @Test
    public void testsetStationActive(){
    	
    	software.setStationActive(true);
    	assertFalse(software.getStationActive());
    	
    }
    
    
    @Test
    public void testStartSession(){
    	
    	String input = "Touch Screen";
    	Scanner scanner = new Scanner(input);
    	
    	software.startSession(scanner);;
    
    	assertTrue(software.getStationActive());
    	
    }
    
    
    @Test
    public void testResetOrder(){
    	
    	software.resetOrder();
    	
    	ArrayList<Item> order = software.getOrder();
    
    	assertEquals(0,software.getTotalOrderWeightInGrams(),0.0001);
    	assertEquals(0,software.getTotalOrderPrice(),0.0001);
    	assertTrue("The order should be empty", order.isEmpty());
    	
    	
    }
    
    
    
    @Test
    public void testAddItemToOrder(){
    	
    	software.resetOrder();
    	
    	Mass mass = new Mass(BigInteger.valueOf(54));
    	Item bag = new ReusableBag();
    	
    	
    	software.addItemToOrder(bag);
    	
    	ArrayList<Item> order = software.getOrder();
    
    	assertEquals(0,software.getTotalOrderWeightInGrams(),0.0001);
    	assertEquals(0,software.getTotalOrderPrice(),0.0001);
    	assertTrue("The order should contain a bag ", order.contains(bag));
    	
    	
    }
    
    @Test
    public void testRemoveItemFromOrder(){
    	
    	
    	
    	software.resetOrder();
    	
    	
    	Mass mass = new Mass(BigInteger.valueOf(54));
    	Numeral[] barcodeDigits = new Numeral[]{Numeral.one, Numeral.two, Numeral.three, Numeral.four, Numeral.five};
    	Barcode barcode = new Barcode(barcodeDigits);
    	Item newItem = new BarcodedItem( barcode, mass);
    	
    	
    	BarcodedProduct barcodeProduct = new BarcodedProduct(barcode, "Just a testing product", 10 ,50);
    	ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, barcodeProduct);
    	
    	
    	PriceLookUpCode plucode = new PriceLookUpCode("1234");
    	Item newItem1 = new PLUCodedItem(  plucode, mass);
    	
    	PLUCodedProduct PLUProduct = new PLUCodedProduct(plucode, "Just a testing product", 10);
    	ProductDatabases.PLU_PRODUCT_DATABASE.put(plucode, PLUProduct);
    
    	
    	
    	
    	software.addItemToOrder(newItem);
    	software.addItemToOrder(newItem1);
    	assertTrue(software.removeItemFromOrder(newItem));
    	assertTrue(software.removeItemFromOrder(newItem1));
    	
    	
    	ArrayList<Item> order = software.getOrder();
    
    	
    	assertTrue("The order should be empty", software.isOrderEmpty());
    	
    	
    }
    
    @Test
    public void testRemoveNotFoundItemFromOrder(){
    	
    	Mass mass = new Mass(BigInteger.valueOf(60));
    	
    	PriceLookUpCode plucode = new PriceLookUpCode("1111");
    	Item nonExistantItem = new PLUCodedItem(  plucode, mass);
    	
    	
    	
    	assertFalse(software.removeItemFromOrder(nonExistantItem));
    	
    	
    	
    }
    @Test
    public void testAddTotalOrderWeightInGrams() {
    	
    	software.addTotalOrderWeightInGrams(10);
    	assertEquals(110,software.getTotalOrderWeightInGrams(),0.0001);
    	
    	
    }
    
    @Test
    public void testRemoveTotalOrderWeightInGrams() {
    	
    	software.removeTotalOrderWeightInGrams(10);
    	assertEquals(90,software.getTotalOrderWeightInGrams(),0.0001);
    	
    	
    }
    
    @Test
    public void testAddTotalOrderPrice() {
    	double oldTotalOrderPrice = software.getTotalOrderPrice();
    	software.addTotalOrderPrice(10);
    	double newTotalOrderPrice = software.getTotalOrderPrice() + oldTotalOrderPrice ;
    	assertEquals(newTotalOrderPrice,software.getTotalOrderPrice(),0.0001);
    	
    
    }
    @Test
    public void testRemoveTotalOrderPrice() {
    	
    	software.setOrderTotalPrice(20);
    	software.removeTotalOrderPrice(10);
    	assertEquals(10,software.getTotalOrderPrice(),0.0001);
    	
    	
    }
    
    

    @Test (expected = IllegalArgumentException.class)
    public void SelfCheckoutStationSoftwareNull()  {
    	SelfCheckoutStationSoftware nullSoftware = new SelfCheckoutStationSoftware(null);
    }
    
    @Test (expected = InvalidStateSimulationException.class)
    public void testAlreadyStartSession(){
    	
    	String input = "Touch Screen";
    	Scanner scanner = new Scanner(input);
    	
    	software.startSession(scanner);
    	software.startSession(scanner);
    

    	
    }
    
    @Test
	public void testGetBanks(){
    	CardIssuer chaseBank = new CardIssuer("chasebank", 100);
	    software.addBank(chaseBank);
	    
	    Set<CardIssuer> allbanks = software.getBanks();
	    
	    assertTrue(allbanks.contains(chaseBank));
	    
	    
    }
    
    
    
    
}
