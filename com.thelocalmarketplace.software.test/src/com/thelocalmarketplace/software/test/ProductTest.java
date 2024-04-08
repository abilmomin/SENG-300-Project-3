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
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.bag.ReusableBag;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedItem;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantPageGUI;
import com.thelocalmarketplace.software.product.Products;
import com.thelocalmarketplace.software.product.ProductsListener;

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
	private Product product;
	private MockReusableBagDispenser dispenser; 
	
	@Before
	public void setup() {
		grid = PowerGrid.instance();
		PowerGrid.engageUninterruptiblePowerSource();
		grid.forcePowerRestore();
		
		checkoutStationBronze = new SelfCheckoutStationBronze();
		checkoutStationBronze.plugIn(grid);
		checkoutStationBronze.turnOn();
		
		dispenser = new MockReusableBagDispenser(3, 100); 
		dispenser.plugIn(grid);
		dispenser.turnOn();
		
		station = new SelfCheckoutStationSoftware(checkoutStationBronze);
		station.setStationActive(true);
		pluCode = new PriceLookUpCode("1234");
		itemMass = new Mass(1000000000);
		pluCodedItem = new PLUCodedItem(pluCode, itemMass);
		
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
	        /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

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
	public void testRemoveBarcodedItemFromOrder() {
	    
	    Numeral[] barcodeDigits = {Numeral.one, Numeral.two, Numeral.three, Numeral.four};
	    Barcode barcode = new Barcode(barcodeDigits);
	    double initialItemWeightInGrams = 500.0;
	    long initialItemPrice = 10;
	    BarcodedProduct barcodedProduct = new BarcodedProduct(barcode, "Barcoded Product", initialItemPrice, initialItemWeightInGrams);
	    ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, barcodedProduct);
	    double productWeight = barcodedProduct.getExpectedWeight(); 
	    Mass mass = new Mass(productWeight);
	    BarcodedItem barcodedItem = new BarcodedItem(barcode, mass);
	    station.addItemToOrder(barcodedItem);  

	    
	    assertTrue(station.getOrder().contains(barcodedItem));
	    double initialTotalWeight = station.getTotalOrderWeightInGrams();
	    double initialTotalPrice = station.getTotalOrderPrice();

	    
	    boolean result = testProducts.removeItemFromOrder(barcodedItem);

	   
	    assertTrue("The item should be successfully removed from the order", result);
	    assertFalse("The order should no longer contain the removed item", station.getOrder().contains(barcodedItem));
	    assertEquals("The total order weight should be reduced by the weight of the removed item",
	                 initialTotalWeight - initialItemWeightInGrams, station.getTotalOrderWeightInGrams(), 0.001);
	    assertEquals("The total order price should be reduced by the price of the removed item",
	                 initialTotalPrice - initialItemPrice, station.getTotalOrderPrice());
	    
	    
	}
	@Test
	public void testRemovePLUCodedItemFromOrder() {
	    // Setup: Add a PLUCodedItem to the order
	    PriceLookUpCode pluCode = new PriceLookUpCode("1234");
	    double initialItemWeightInGrams = 300.0;
	    long initialItemPrice = 5;
	    PLUCodedProduct pluCodedProduct = new PLUCodedProduct(pluCode, "PLU Coded Product", initialItemPrice);
	    ProductDatabases.PLU_PRODUCT_DATABASE.put(pluCode, pluCodedProduct);
        double productWeight = itemMass.inGrams().doubleValue();
	    Mass mass = new Mass(productWeight);
	    PLUCodedItem pluCodedItem = new PLUCodedItem(pluCode, mass);
	    station.addItemToOrder(pluCodedItem);  // Assuming there's a method to add items directly to the order for setup

	    // Initial assertions
	    assertTrue(station.getOrder().contains(pluCodedItem));
	    double initialTotalWeight = station.getTotalOrderWeightInGrams();
	    double initialTotalPrice = station.getTotalOrderPrice();

	    // Action: Remove the PLUCodedItem from the order
	    boolean result = testProducts.removeItemFromOrder(pluCodedItem);

	    // Assertions
	    assertTrue("The item should be successfully removed from the order", result);
	    assertFalse("The order should no longer contain the removed item", station.getOrder().contains(pluCodedItem));
	    assertEquals("The total order weight should be reduced by the weight of the removed item",
	                 initialTotalWeight - initialItemWeightInGrams, station.getTotalOrderWeightInGrams(), 0.001);
	    assertEquals("The total order price should be reduced by the price of the removed item",
	                 initialTotalPrice - initialItemPrice, station.getTotalOrderPrice());
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
	public void testAddItemViaPluCodeWithValidPluCode() {
		station.setStationActive(true);
	        boolean addItemResult = testProducts.addItemByPLUCode(pluCodedItem);
		assertTrue("The PlUCodedItem should be added sucessfully", addItemResult);
	}
	
	@Test
	public void testAddItemViaPluCodeWhileStationIsInactive() {
		station.setStationActive(false);
		boolean result = testProducts.addItemByPLUCode(pluCodedItem);
		assertFalse("The item should not be added to order since station is inactive", result);
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
		MockReusableBagDispenser dispenser = new MockReusableBagDispenser(2, 100); 
		ReusableBag bag1 = new ReusableBag(); 
        ReusableBag bag2 = new ReusableBag();
        ReusableBag bag3 = new ReusableBag();
        ReusableBag[] bags = {bag1, bag2, bag3}; 
        
        dispenser.load(bags);
        testProducts.PurchaseBags(bags);  
	}
	
	//Testing when there are enough bags in the dispenser 
	@Test 
	public void testPurchaseBags_EnoughBags() throws OverloadedDevice, EmptyDevice {
		MockReusableBagDispenser dispenser = new MockReusableBagDispenser(2, 100); 
		ReusableBag bag1 = new ReusableBag(); 
        ReusableBag bag2 = new ReusableBag();
        ReusableBag[] bags = {bag1, bag2}; 
        dispenser.load(bags);
        testProducts.PurchaseBags(bags);
	}
		
	//Testing when the amount of bags in dispenser are being loaded (dispenser bags and purchased bags = same) 
	// this one should notify that the bags are empty after its been dispensed
	@Test 
	public void testPurchaseBags_JustEnoughBags() throws OverloadedDevice, EmptyDevice{
		MockReusableBagDispenser dispenser = new MockReusableBagDispenser(3, 100); 
		ReusableBag[] bags = new ReusableBag[3];
	    for (int i = 0; i < 3; i++) {
	        bags[i] = new ReusableBag();
	    }
	    dispenser.load(bags);
	    
	    // Ensure dispenser notifies that bags are empty after being dispensed
	    testProducts.PurchaseBags(bags);
	   // assertTrue(dispenser.getQuantityRemaining() == 0);
	    assertTrue("Dispenser is now out of bags",
	               dispenser.getQuantityRemaining() == 0);
	}

	@Test
	public void testRegisterAndNotifyProductAdded() {
	    final boolean[] wasCalled = {false};
	    final Product[] notifiedProduct = {null};

	    ProductsListener listener = new ProductsListener() {
	        @Override
	        public void productAdded(Products productFacade, Product product) {
	            wasCalled[0] = true;
	            notifiedProduct[0] = product;
	        }
	    };

	    testProducts.register(listener);
	    testProducts.notifyProductAdded(product);

	    assertTrue("Listener should be notified", wasCalled[0]);
	    assertEquals("Notified product should be the same as added", product, notifiedProduct[0]);
	}
	
	@Test
	public void testDeregisterAndNotNotifyProduct() {
	    final boolean[] wasCalled = {false};

	    ProductsListener listener = new ProductsListener() {
	        @Override
	        public void productAdded(Products productFacade, Product product) {
	            wasCalled[0] = true;
	        }
	    };

	    testProducts.register(listener);
	    testProducts.deregister(listener);
	    testProducts.notifyProductAdded(product);

	    assertFalse("Listener should not be notified after deregistration", wasCalled[0]);
	}

	@Test
	public void testNotifyProductRemoved() {
	    final boolean[] wasCalled = {false};
	    final Product[] notifiedProduct = {null};

	    ProductsListener listener = new ProductsListener() {
	        @Override
	        public void productRemoved(Products productFacade, Product product) {
	            wasCalled[0] = true;
	            notifiedProduct[0] = product;
	        }
	    };

	    testProducts.register(listener);
	    testProducts.notifyProductRemoved(product);

	    assertTrue("Listener should be notified", wasCalled[0]);
	    assertEquals("Notified product should be the same as removed", product, notifiedProduct[0]);
	}
	
	@Test
	public void testNotifyAddProductToBaggingArea() {
	    final boolean[] wasCalled = {false};
	    final Product[] notifiedProduct = {null};

	    ProductsListener listener = new ProductsListener() {
	        @Override
	        public void productToBaggingArea(Products productFacade, Product product) {
	            wasCalled[0] = true;
	            notifiedProduct[0] = product;
	        }
	    };

	    testProducts.register(listener);
	    testProducts.notifyAddProductToBaggingArea(product);

	    assertTrue("Listener should be notified", wasCalled[0]);
	    assertEquals("Notified product should be the same as the one added to bagging area", product, notifiedProduct[0]);
	}
	
	@Test
	public void testNotifyBagsPurchased() {
	    final boolean[] wasCalled = {false};
	    final long[] notifiedTotalCost = {0};

	    ProductsListener listener = new ProductsListener() {
	        @Override
	        public void bagsPurchased(Products productFacade, long totalCost) {
	            wasCalled[0] = true;
	            notifiedTotalCost[0] = totalCost;
	        }
	    };

	    testProducts.register(listener);
	    long totalCost = 150;
	    testProducts.notifyBagsPurchased(totalCost);

	    assertTrue("Listener should be notified", wasCalled[0]);
	    assertEquals("Notified total cost should match", totalCost, notifiedTotalCost[0]);
	}
	@Test
		public void testProductAddedNotification() {
		   
		    ProductsListenerStub listenerStub = new ProductsListenerStub();
		    testProducts.register(listenerStub); 

		    
		    testProducts.notifyProductAdded(product); 

		    // Assert
		    assertTrue("Product added should be called", listenerStub.isProductAddedCalled());
		    assertEquals("The added product should be the same as notified", product, listenerStub.getLastAddedProduct());
		}
		
		@Test
		public void testProductRemovedNotification() {
		    
		    ProductsListenerStub listenerStub = new ProductsListenerStub();
		    testProducts.register(listenerStub); 

		    
		    testProducts.notifyProductRemoved(product); 

		    // Assert
		    assertTrue("Product removed should be called", listenerStub.isProductRemovedCalled());
		    assertEquals("The removed product should be the same as notified", product, listenerStub.getLastRemovedProduct());
		}
		
		@Test
		public void testProductToBaggingAreaNotification() {
		    
		    ProductsListenerStub listenerStub = new ProductsListenerStub();
		    testProducts.register(listenerStub);

		    
		    testProducts.notifyAddProductToBaggingArea(product); 

		    // Assert
		    assertTrue("productToBaggingArea should be called", listenerStub.isProductToBaggingAreaCalled());
		    assertEquals("The product notified for bagging area should be the same as provided", product, listenerStub.getLastBaggingAreaProduct());
		}

		@Test
		public void testBagsPurchasedNotification() {
		    
		    ProductsListenerStub listenerStub = new ProductsListenerStub();
		    testProducts.register(listenerStub);
		    long totalCost = 150; 

		    
		    testProducts.notifyBagsPurchased(totalCost); 

		    // Assert
		    assertTrue("bagsPurchased should be called", listenerStub.isBagsPurchasedCalled());
		    assertEquals("The total cost notified should match the provided cost", totalCost, listenerStub.getLastBagsPurchasedTotalCost());
		}
}
