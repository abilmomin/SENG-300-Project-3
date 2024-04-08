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
    	PowerGrid.engageUninterruptiblePowerSource();
    	stationB.plugIn(PowerGrid.instance());
    	stationB.turnOn();
    	software = new SelfCheckoutStationSoftware(stationB);

    	// Adding a sample BarcodedItem
    	Numeral[] barcodeDigits = new Numeral[]{Numeral.one, Numeral.two, Numeral.three, Numeral.four, Numeral.five};
    	Barcode barcode = new Barcode(barcodeDigits);
    	Mass mass = new Mass(BigInteger.valueOf(50));
    	Item barcodedItem = new BarcodedItem(barcode, mass);
    	software.addItemToOrder(barcodedItem);

    	// Adding a sample PLUCodedItem 
    	PriceLookUpCode plucode = new PriceLookUpCode("1234");
    	mass = new Mass(BigInteger.valueOf(60));
    	Item plucodedItem = new PLUCodedItem(plucode, mass);
    	software.addItemToOrder(plucodedItem);
    }

    @Test
    public void testStationBlock() {
    	assertFalse(software.getStationBlock());
    	software.setStationBlock();
    	assertTrue(software.getStationBlock());
    	software.setStationUnblock();
    	assertFalse(software.getStationBlock());
    }
    
    @Test
    public void testSetStationActive() {
    	assertFalse(software.getStationActive());
    	software.setStationActive(true);
    	assertTrue(software.getStationActive());
    	software.setStationActive(false);
    	assertFalse(software.getStationActive());
    }
    
    @Test
    public void testResetOrder() {
    	software.resetOrder();
    	
    	ArrayList<Item> order = software.getOrder();
    
    	assertEquals(0, software.getTotalOrderWeightInGrams(), 0.0001);
    	assertEquals(0, software.getTotalOrderPrice(), 0.0001);
    	assertTrue("The order should be empty.", order.isEmpty());
    }
    
    @Test
    public void testAddItemToOrder() {
    	software.resetOrder();
    	
    	Item bag = new ReusableBag();
    	software.addItemToOrder(bag);
    	
    	ArrayList<Item> order = software.getOrder();
    
    	assertEquals(0, software.getTotalOrderWeightInGrams(), 0.0001);
    	assertEquals(0, software.getTotalOrderPrice(), 0.0001);
    	assertTrue("The order should contain a bag.", order.contains(bag));
    }
    
    @Test
    public void testRemoveItemFromOrder() {
    	software.resetOrder();
    	
    	Mass mass = new Mass(BigInteger.valueOf(54));
    	Numeral[] barcodeDigits = new Numeral[]{Numeral.one, Numeral.two, Numeral.three, Numeral.four, Numeral.five};
    	Barcode barcode = new Barcode(barcodeDigits);
    	
    	BarcodedProduct barcodeProduct = new BarcodedProduct(barcode, "Just a testing product", 10, 50);
    	ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, barcodeProduct);
    	
    	PriceLookUpCode plucode = new PriceLookUpCode("1234");    	
    	PLUCodedProduct PLUProduct = new PLUCodedProduct(plucode, "Just a testing product", 10);
    	ProductDatabases.PLU_PRODUCT_DATABASE.put(plucode, PLUProduct);
    	
    	Item newItem = new BarcodedItem(barcode, mass);
    	software.addItemToOrder(newItem);
    	software.removeFromOrder(newItem);
    	
    	Item newItem1 = new PLUCodedItem(plucode, mass);
    	software.addItemToOrder(newItem1);
    	software.removeFromOrder(newItem1);
    	
    	ArrayList<Item> order = software.getOrder();
    	assertEquals(0, order.size());
    	assertTrue("The order should be empty", software.isOrderEmpty());
    }
    
    @Test
    public void testRemoveNotFoundItemFromOrder() {
    	Mass mass = new Mass(BigInteger.valueOf(60));
    	PriceLookUpCode plucode = new PriceLookUpCode("1111");
    	Item nonExistantItem = new PLUCodedItem(plucode, mass);
    	software.removeFromOrder(nonExistantItem);
    }
    
    @Test
    public void testTotalOrderWeightInGrams() {
    	double oldTotalOrderPrice = software.getTotalOrderWeightInGrams();
    	
    	double priceToAdd = 10;
    	software.addTotalOrderWeightInGrams(priceToAdd);
    	assertEquals(oldTotalOrderPrice + priceToAdd, software.getTotalOrderWeightInGrams(), 0.0001);

    	oldTotalOrderPrice = software.getTotalOrderWeightInGrams();
    	
    	double priceToRemove = -20;
    	software.addTotalOrderWeightInGrams(priceToRemove);
    	assertEquals(oldTotalOrderPrice + priceToRemove, software.getTotalOrderWeightInGrams(), 0.0001);
    }
    
    @Test
    public void testTotalOrderPrice() {
    	double oldTotalOrderPrice = software.getTotalOrderPrice();
    	
    	double priceToAdd = 20;
    	software.addTotalOrderPrice(priceToAdd);
    	assertEquals(oldTotalOrderPrice + priceToAdd, software.getTotalOrderPrice(), 0.0001);
    	
    	oldTotalOrderPrice = software.getTotalOrderPrice();
    	
    	double priceToRemove = -10;
    	software.addTotalOrderPrice(priceToRemove);
    	assertEquals(oldTotalOrderPrice + priceToRemove, software.getTotalOrderPrice(), 0.0001);
    }
    
    @Test
    public void testSetTotalOrderPrice() {
    	software.setOrderTotalPrice(20);
    	software.addTotalOrderPrice(-10);
    	assertEquals(10, software.getTotalOrderPrice(), 0.0001);
    	software.addTotalOrderPrice(20);
    	assertEquals(30, software.getTotalOrderPrice(), 0.0001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void SelfCheckoutStationSoftwareNull() {
    	SelfCheckoutStationSoftware nullSoftware = new SelfCheckoutStationSoftware(null);
    }
    
    @Test
	public void testGetBanks() {
    	CardIssuer chaseBank = new CardIssuer("chasebank", 100);
	    software.addBank(chaseBank);
	    
	    Set<CardIssuer> allbanks = software.getBanks();
	    
	    assertTrue(allbanks.contains(chaseBank));
    }
}