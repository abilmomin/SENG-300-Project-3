package com.thelocalmarketplace.software.test;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import com.jjjwelectronics.Mass;
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
	}
	
	@Test(expected = NullPointerException.class)
	public void testAddNullItemByVisualCatalogue() {
		testProducts.addItemByVisualCatalogue(null);
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
	
	
	
}