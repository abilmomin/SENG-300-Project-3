package com.thelocalmarketplace.software;

import java.math.BigDecimal;
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

public class Initialization {
	private static SelfCheckoutStationSoftware bronzeStation1;

	private static Map<Barcode, BarcodedProduct> productDatabase = ProductDatabases.BARCODED_PRODUCT_DATABASE;
	private Map<Product, Integer> inventoryDatabase = ProductDatabases.INVENTORY; 
	private static Map<PriceLookUpCode, PLUCodedProduct> PLUDatabase = ProductDatabases.PLU_PRODUCT_DATABASE; 

	// private ArrayList<BarcodedItems> items 


	private static void InitializeItems() {

		// how to integrate with scs instance??
		Currency c = Currency.getInstance(Locale.CANADA);
		int[] noteAmounts = {5, 10, 20, 50, 100};
		BigDecimal[] coinAmounts = {new BigDecimal("0.05"), new BigDecimal("0.10"),  new BigDecimal("0.25"), new BigDecimal("1.00"), new BigDecimal("2.00")};

		SelfCheckoutStationBronze bs = new SelfCheckoutStationBronze();

		bronzeStation1 = new SelfCheckoutStationSoftware(bs);

		Numeral[] code1Digits = {Numeral.one, Numeral.two, Numeral.three, Numeral.four};
		Barcode barcode1 = new Barcode(code1Digits);

		Numeral[] code2Digits = {Numeral.two, Numeral.three, Numeral.four, Numeral.five};
		Barcode barcode2 = new Barcode(code2Digits);

		Numeral[] code3Digits = {Numeral.six, Numeral.seven, Numeral.eight, Numeral.nine};
		Barcode barcode3 = new Barcode(code3Digits);

		// Add to each station? Or add to a database that each station can then access
			// I think we'd need to add a function in SelfCheckoutStationSoftware either way

		// Not sure whether we use BarcodedItem or BarcodedProduct but here's both;

			// BarcodedItem(Barcode barcode, Mass mass)
		BarcodedItem i_milk = new BarcodedItem(barcode1, new Mass(new BigDecimal("3894.8")));

			// BarcodedProduct(Barcode barcode, String description, long price, double expectedWeightInGrams)
				// I assumed price is in cents since longs can't have decimals
		BarcodedProduct milk = new BarcodedProduct(barcode1, "Milk", 350, 3894.8);

		productDatabase.put(barcode1, milk); // Adding milk to the product database with its barcode as the key

		// PLU Code Items

		PriceLookUpCode plu1 = new PriceLookUpCode("1234");

			//PLUCodedItem(PriceLookUpCode pluCode, Mass mass) 
		PLUCodedItem i_apple = new PLUCodedItem(plu1, new Mass(new BigDecimal("150.0")));

			//PLUCodedProduct(PriceLookUpCode pluCode, String description, long price)
				// Does not have expectedWeight >> BUG??
		PLUCodedProduct apple = new PLUCodedProduct(plu1, "Apple", 150);

		PLUDatabase.put(plu1, apple);

	}

}