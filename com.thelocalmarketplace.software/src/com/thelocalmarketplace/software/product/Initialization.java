package com.thelocalmarketplace.software.product;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

public class Initialization {
	private static SelfCheckoutStationSoftware bronzeStation1;
	
	private Map<Barcode, BarcodedProduct> productDatabase = ProductDatabases.BARCODED_PRODUCT_DATABASE;
	private Map<Product, Integer> inventoryDatabase = ProductDatabases.INVENTORY; 
	private Map<PriceLookUpCode, PLUCodedProduct> PLUDatabase = ProductDatabases.PLU_PRODUCT_DATABASE; 
	
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
		
	}
	
}
