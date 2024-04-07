package com.thelocalmarketplace.software;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
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

public class ProductsDatabase {

	private static Map<Barcode, BarcodedProduct> productDatabase = ProductDatabases.BARCODED_PRODUCT_DATABASE;
	private Map<Product, Integer> inventoryDatabase = ProductDatabases.INVENTORY; 
	private static Map<PriceLookUpCode, PLUCodedProduct> PLUDatabase = ProductDatabases.PLU_PRODUCT_DATABASE; 
	private static ArrayList<Barcode> barcodes = new ArrayList<>();

	// private ArrayList<BarcodedItems> items 
	public ProductsDatabase() {
		InitializeBarcodedProducts();
		InitializePLUProducts();
	}

	private static void InitializeBarcodedProducts() {
		// Do we need to initialize machine currency?
		Currency c = Currency.getInstance(Locale.CANADA);
		int[] noteAmounts = {5, 10, 20, 50, 100};
		BigDecimal[] coinAmounts = {new BigDecimal("0.05"), new BigDecimal("0.10"),  new BigDecimal("0.25"), new BigDecimal("1.00"), new BigDecimal("2.00")};

		Numeral[] code1Digits = {Numeral.one, Numeral.two, Numeral.three, Numeral.four};
		Barcode barcode1 = new Barcode(code1Digits);

		Numeral[] code2Digits = {Numeral.two, Numeral.three, Numeral.four, Numeral.five};
		Barcode barcode2 = new Barcode(code2Digits);

		Numeral[] code3Digits = {Numeral.six, Numeral.seven, Numeral.eight, Numeral.nine};
		Barcode barcode3 = new Barcode(code3Digits);
		
		barcodes.add(barcode1);
		barcodes.add(barcode2);
		barcodes.add(barcode3);

			// BarcodedProduct(Barcode barcode, String description, long price, double expectedWeightInGrams)
				// I assumed price is in cents since longs can't have decimals
		BarcodedProduct milk = new BarcodedProduct(barcode1, "Milk", 3, 3894);
		BarcodedProduct chocolate = new BarcodedProduct(barcode1, "Chocolate bar", 2, 49);
		BarcodedProduct sushi = new BarcodedProduct(barcode1, "20pc Sushi", 10, 600);
		 
		productDatabase.put(barcode1, milk); // Adding milk to the product database with its barcode as the key
		productDatabase.put(barcode2, chocolate);
		productDatabase.put(barcode3, sushi);
	}

	private static void InitializePLUProducts() {
		PriceLookUpCode plu1 = new PriceLookUpCode("1234");
		PriceLookUpCode plu2 = new PriceLookUpCode("2345");
		PriceLookUpCode plu3 = new PriceLookUpCode("6789");
		
			//PLUCodedProduct(PriceLookUpCode pluCode, String description, long price)
				// Assuming price in cents
		PLUCodedProduct apple = new PLUCodedProduct(plu1, "Apple", 150);
		PLUCodedProduct banana = new PLUCodedProduct(plu1, "Banana", 125);
		PLUCodedProduct kiwi = new PLUCodedProduct(plu1, "Kiwi", 175);
		PLUDatabase.put(plu1, apple);
		PLUDatabase.put(plu2, banana);
		PLUDatabase.put(plu3, kiwi);
	}
	
	/**
	 * Returns a list of all the barcodes available for products.
	 * @return
	 */
	public static ArrayList<Barcode> getBarcodes() {
		return barcodes;
	}
}