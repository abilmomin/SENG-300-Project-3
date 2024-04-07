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
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantPageGUI;
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
	private AttendantPageGUI attendantGUI;
	//private ReusableBagDispenserBronze reusableBagDispenserBronze;
	private mockReusableBagDispenser dispenser; 
	
	
	@Before
	public void setup() {
		// create a power grid
		grid = PowerGrid.instance();
		// to avoid power outages when there is a power surge
		PowerGrid.engageUninterruptiblePowerSource();
		grid.forcePowerRestore();
		
		attendantGUI = new AttendantPageGUI();
		
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
	public void testHandleBulkyItemReducesTotalOrderWeightWithAttendantApproval() {
	    double initialItemWeightInGrams = 2000.0;
	    Numeral[] barcodeDigits = {Numeral.zero, Numeral.one, Numeral.two, Numeral.three, Numeral.four};
	    Barcode barcode = new Barcode(barcodeDigits);
	    BarcodedProduct initialProduct = new BarcodedProduct(barcode, "Initial Product", 20, initialItemWeightInGrams);
	    ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, initialProduct);
	    testProducts.addItemViaBarcodeScan(barcode); // Adds the initial item to the order

	    // The weight of the bulky item to handle
	    double bulkyItemWeightInGrams = 1000.0;

	    // total order weight before handling the bulky item
	    double initialTotalWeight = station.getTotalOrderWeightInGrams();

	    // Create a stub for AttendantPageGUI that simulates attendant approval
	    AttendantPageGUI attendantGUIStub = new AttendantPageGUI() {
	        @Override
	        public boolean bulkItemRequest(String message) {
	            return true; // Simulate attendant approval
	        }
	    };

	    // Handle the bulky item with simulated attendant approval
	    testProducts.handleBulkyItem(bulkyItemWeightInGrams, attendantGUIStub);

	    // total order weight is expected to be reduced by the weight of the bulky item after approval
	    double expectedTotalWeightAfterHandling = initialTotalWeight - bulkyItemWeightInGrams;
	    double actualTotalWeightAfterHandling = station.getTotalOrderWeightInGrams();

	    assertEquals("The total order weight should be reduced by the weight of the bulky item upon attendant's approval",
	                 expectedTotalWeightAfterHandling, actualTotalWeightAfterHandling, 0.001);
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
	@Test
	public void testAddBarcodedProductByTextSearch() {
	    
	    station.setStationActive(true);
	    

	    // Create and add a barcoded product to the database for the test
	    Numeral[] barcodeDigits = {Numeral.one, Numeral.two, Numeral.three};
	    Barcode barcode = new Barcode(barcodeDigits);
	    BarcodedProduct testProduct = new BarcodedProduct(barcode, "Test", 100, 500.0);
	    ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, testProduct);

	    // Perform the action to add an item by text search
	    testProducts.addItemByTextSearch("Test");

	    // Assertions to verify the product was added successfully
	    assertTrue("The total order weight should include the weight of the added product",
	               station.getTotalOrderWeightInGrams() == 500.0);
	    assertEquals("The total order weight should include the weight of the added product", 500.0, station.getTotalOrderWeightInGrams(), 0.001);
	    assertEquals("The total order price should include the price of the added product", 100, station.getTotalOrderPrice(), 0.001);

	}
	@Test
	public void testProductNotFoundByTextSearch() {
	    
	    station.setStationActive(true);
	    

	    // Record the initial total order weight and price
	    double initialWeight = station.getTotalOrderWeightInGrams();
	    double initialPrice = station.getTotalOrderPrice();

	   
	    testProducts.addItemByTextSearch("Non-existent Product");

	    // Assertions to verify that nothing was added since the product was not found
	    assertTrue("The total order weight should not change when the product is not found",
	               station.getTotalOrderWeightInGrams() == initialWeight);
	    assertTrue("The total order price should not change when the product is not found",
	               station.getTotalOrderPrice() == initialPrice);
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
