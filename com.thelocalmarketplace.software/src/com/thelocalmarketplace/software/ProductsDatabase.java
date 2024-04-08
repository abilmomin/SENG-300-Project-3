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


package com.thelocalmarketplace.software;

import java.util.ArrayList;
import java.util.Map;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

/**
 * The ProductsDatabase class initializes and maintains the databases for 
 * barcoded and PLU-coded products available.
 */
public class ProductsDatabase {

	private static Map<Barcode, BarcodedProduct> productDatabase = ProductDatabases.BARCODED_PRODUCT_DATABASE;
	private static Map<PriceLookUpCode, PLUCodedProduct> PLUDatabase = ProductDatabases.PLU_PRODUCT_DATABASE; 
	private static ArrayList<Barcode> barcodes = new ArrayList<>();

	/**
	 * Constructor for ProductsDatabase class.
	 */
	public ProductsDatabase() {
		InitializeBarcodedProducts();
		InitializePLUProducts();
	}

	/**
	 * Initialize barcodes and assign them to products and
	 * adds barcoded products to the database.
	 */
	private static void InitializeBarcodedProducts() {
		Numeral[] code1Digits = {Numeral.one, Numeral.two, Numeral.three, Numeral.four};
		Barcode barcode1 = new Barcode(code1Digits);
		Numeral[] code2Digits = {Numeral.two, Numeral.three, Numeral.four, Numeral.five};
		Barcode barcode2 = new Barcode(code2Digits);
		Numeral[] code3Digits = {Numeral.six, Numeral.seven, Numeral.eight, Numeral.nine};
		Barcode barcode3 = new Barcode(code3Digits);
		
		barcodes.add(barcode1);
		barcodes.add(barcode2);
		barcodes.add(barcode3);

		BarcodedProduct milk = new BarcodedProduct(barcode1, "Milk", 3, 3894);
		BarcodedProduct chocolate = new BarcodedProduct(barcode2, "Chocolate bar", 2, 49);
		BarcodedProduct sushi = new BarcodedProduct(barcode3, "20pc Sushi", 10, 600);
		 
		productDatabase.put(barcode1, milk); 
		productDatabase.put(barcode2, chocolate);
		productDatabase.put(barcode3, sushi);
	}

	/**
	 * Initialize PLU codes and assign them to products and
	 * adds the PLU-coded products to the PLU database. 
	 */
	private static void InitializePLUProducts() {
		PriceLookUpCode plu1 = new PriceLookUpCode("1234");
		PriceLookUpCode plu2 = new PriceLookUpCode("2345");
		PriceLookUpCode plu3 = new PriceLookUpCode("6789");
		PriceLookUpCode plu4 = new PriceLookUpCode("1111");
		
		PLUCodedProduct apple = new PLUCodedProduct(plu1, "Apple", 1);
		PLUCodedProduct banana = new PLUCodedProduct(plu2, "Banana", 1);
		PLUCodedProduct carrot = new PLUCodedProduct(plu3, "Carrot", 2);
		PLUCodedProduct asparagus = new PLUCodedProduct(plu4, "Asparagus", 2);
		
		PLUDatabase.put(plu1, apple);
		PLUDatabase.put(plu2, banana);
		PLUDatabase.put(plu3, carrot);
		PLUDatabase.put(plu4, asparagus);
	}
	
	/**
	 * Returns a list of all the barcodes available for products.
	 * 
	 * @return The list of available barcodes.
	 */
	public static ArrayList<Barcode> getBarcodes() {
		return barcodes;
	}
}