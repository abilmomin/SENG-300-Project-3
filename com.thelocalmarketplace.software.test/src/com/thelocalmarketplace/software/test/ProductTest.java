package com.thelocalmarketplace.software.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.bag.AbstractReusableBagDispenser;
import com.jjjwelectronics.bag.IReusableBagDispenser;
import com.jjjwelectronics.bag.ReusableBag;
import com.jjjwelectronics.bag.ReusableBagDispenserBronze;
import com.jjjwelectronics.scanner.Barcode;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedItem;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.product.Products;
import powerutility.PowerGrid;

public class ProductTest {
	PowerGrid grid;
	private SelfCheckoutStationSoftware station;
	private SelfCheckoutStationBronze checkoutStationBronze;
	private PLUCodedItem pluCodedItem;
	private PLUCodedProduct pluCodedProduct;
	private Mass itemMass;
	private PriceLookUpCode pluCode;
	private Products testProducts;
	private ReusableBag bags; 
	//private ReusableBagDispenserBronze reusableBagDispenserBronze;
	private mockReusableBagDispenser dispenser; 
	
	
	@Before
	public void setup() {
		// create a power grid
		grid = PowerGrid.instance();
		// to avoid power outages when there is a power surge
		PowerGrid.engageUninterruptiblePowerSource();
		grid.forcePowerRestore();
		
		checkoutStationBronze = new SelfCheckoutStationBronze();
		checkoutStationBronze.plugIn(grid);
		checkoutStationBronze.turnOn();
		
		
		dispenser = new mockReusableBagDispenser(3, 100); 
		dispenser.plugIn(grid);
		dispenser.turnOn();
		
		
		station = new SelfCheckoutStationSoftware(checkoutStationBronze);
		station.setStationActive(true);
		pluCode = new PriceLookUpCode("1234");
		itemMass = new Mass(1000000000);
		pluCodedItem = new PLUCodedItem(pluCode, itemMass);
		bags = new ReusableBag(); 
		
		// Create a PlUCodedProduct and add it to the database
		String pluProductDescription = "Apple";
		long pluProductPrice = 5;
		pluCodedProduct = new PLUCodedProduct(pluCode, pluProductDescription, pluProductPrice);
		ProductDatabases.PLU_PRODUCT_DATABASE.put(pluCode, pluCodedProduct);
		
		testProducts = new Products(station);
		testProducts.reusableBagDispenser = dispenser; 
		
	}
	@Test
	public void testHandleBulkyItemReducesTotalOrderWeight() {
	    
	    double initialItemWeightInGrams = 2000.0; 
	    Numeral[] barcodeDigits = {Numeral.zero, Numeral.one, Numeral.two, Numeral.three, Numeral.four};
	    Barcode barcode = new Barcode(barcodeDigits);
	    BarcodedProduct initialProduct = new BarcodedProduct(barcode, "Initial Product", 20, initialItemWeightInGrams);
	    ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, initialProduct);
	    testProducts.addItemViaBarcodeScan(barcode); // Adds the initial item to the order

	    // The weight of the bulky item to handle
	    double bulkyItemWeightInGrams = 1000.0; 

	    // total order weight 
	    double initialTotalWeight = station.getTotalOrderWeightInGrams();

	    // Handle the bulky item
	    testProducts.handleBulkyItem(bulkyItemWeightInGrams);

	    // total order weight is reduced by the weight of the bulky item
	    double expectedTotalWeightAfterHandling = initialTotalWeight - bulkyItemWeightInGrams;
	    double actualTotalWeightAfterHandling = station.getTotalOrderWeightInGrams();

	    assertTrue("The total order weight should be reduced by the weight of the bulky item",
	               actualTotalWeightAfterHandling == expectedTotalWeightAfterHandling);
	}
	
	@Test
	public void testPriceChangeAfterAddItemByVisualCatalogue() {
		testProducts.addItemByVisualCatalogue(pluCodedItem);
		assertTrue(station.getTotalOrderPrice() == 5);
	}
	
	@Test
	public void testWeightChangeAfterAddItemByVisualCatalogue() {
		double prevWeight = station.getTotalOrderWeightInGrams();
		testProducts.addItemByVisualCatalogue(pluCodedItem);
		double newWeight = station.getTotalOrderWeightInGrams();
		assertTrue(prevWeight < newWeight);
	}
	
	@Test
    public void testAddItemByVisualCatalogueForInactiveStation() {
		station.setStationActive(false);
		testProducts.addItemByVisualCatalogue(pluCodedItem);
		assertTrue(station.getOrder().size() == 0);
	}
	
	@Test(expected = NullPointerException.class)
	public void testAddNullItemByVisualCatalogue() {
		testProducts.addItemByVisualCatalogue(null);
	}
	
	@Test
	public void testAddItemViaBarcodeScanWithValidBarcode() {
	    Numeral[] barcodeDigits = {Numeral.one, Numeral.two, Numeral.three, Numeral.four, Numeral.five};
	    Barcode validBarcode = new Barcode(barcodeDigits);
	    BarcodedProduct expectedProduct = new BarcodedProduct(validBarcode, "Test Product", 10, 100.0);
	    ProductDatabases.BARCODED_PRODUCT_DATABASE.put(validBarcode, expectedProduct);

	    station.setStationActive(true);
	    testProducts.addItemViaBarcodeScan(validBarcode);

	    assertTrue("The total order price should be updated to match the product price.",
	               station.getTotalOrderPrice() == expectedProduct.getPrice());
	    assertTrue("The total order weight should be updated to include the product weight.",
	               station.getTotalOrderWeightInGrams() == expectedProduct.getExpectedWeight());
	}
	
	
	//testing when not enough bags are in the dispenser 
	@Test (expected = OverloadedDevice.class)
	public void testPurchaseBags_notEnoughBags() throws OverloadedDevice, EmptyDevice {
		//initializing a new dispenser that has 2 bags loaded and a capacity of 100 reusable bags 
		mockReusableBagDispenser dispenser = new mockReusableBagDispenser(2, 100); 
		ReusableBag bag1 = new ReusableBag(); 
        ReusableBag bag2 = new ReusableBag();
        ReusableBag bag3 = new ReusableBag();
        ReusableBag[] bags = {bag1, bag2, bag3}; 
        
        // Invoke the PurchaseBags method
        testProducts.PurchaseBags(bags);  
        
       
	}
	
}
