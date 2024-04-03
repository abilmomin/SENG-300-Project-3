/**

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

package com.thelocalmarketplace.software.product;

import java.math.BigDecimal;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.bag.IReusableBagDispenser;
import com.jjjwelectronics.scale.IElectronicScale;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.*;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

public class ProductHandler {
	// Things to listen to (hardware)
	public SelfCheckoutStationSoftware software;
	public ISelfCheckoutStation station;
	public IElectronicScale baggingArea;
	public IReusableBagDispenser reusableBagDispenser;
	public IBarcodeScanner mainScanner;
	public IBarcodeScanner handheldScanner;

	// Listeners
	public ScaleListener scaleListener;
	public ScannerListener scannerListener;
	public BaggingListener baggingListener;

	/**
	 * Basic constructor.
	 * 
	 * @param software
	 * 		The main software hub.
	 */
	public ProductHandler(SelfCheckoutStationSoftware software) {
		this.software = software;
		this.station = software.getStationHardware();

		// Get all the hardware the listeners need to listen to
		this.mainScanner = station.getMainScanner();
		this.handheldScanner = station.getHandheldScanner();
		this.baggingArea = station.getBaggingArea();
		this.reusableBagDispenser = station.getReusableBagDispenser();

		// Make the listener objects
		this.scannerListener = new ScannerListener(software, this);
		this.scaleListener = new ScaleListener(software, this);
		this.baggingListener = new BaggingListener(software, this);

		// Attach the listeners to the hardware
		mainScanner.register(scannerListener);
		handheldScanner.register(scannerListener);
	}
	
	/**
	 * Handle bulky item 
	 * 
	 * @param productWeight
	 * 			The weight of the bulky item 
	 * */
	public void handleBulkyItem(double productWeight) {
		software.setStationBlock();
		System.out.println("No-bagging request is in progress.");

		System.out.println("Request has been approved");
		double currentWeight = software.getTotalOrderWeightInGrams();
		double finalWeight = currentWeight-productWeight;
		if (finalWeight >= 0) software.addTotalOrderWeightInGrams(-productWeight); 
	}
	
	/**
	 * 
	 * Adds item to order by PLU code
	 * 
	 * @param pluItem
	 */

	public void addItemByPLUCode(PLUCodedItem pluItem) {
		if (software.getStationActive() && !software.getStationBlock()) {
			software.setStationBlock();

			BigDecimal itemWeightInGrams = pluItem.getMass().inGrams();
			double itemWeight = itemWeightInGrams.doubleValue();
			PriceLookUpCode PLUCode = pluItem.getPLUCode();
			PLUCodedProduct product = ProductDatabases.PLU_PRODUCT_DATABASE.get(PLUCode);


			if(product != null) {
				long productPrice = product.getPrice();

				software.addTotalOrderWeightInGrams(itemWeight);
				software.addTotalOrderPrice(productPrice);

				Mass mass = new Mass(itemWeight);
				PLUCodedItem newItem = new PLUCodedItem(PLUCode, mass);
				software.addItemToOrder(newItem);			
			}
		}
	}
	/**
     * Adds an item to the order by text search of the description.
     * 
     * @param searchText The text to search for in the product descriptions.
     */
    public void addItemByTextSearch(String searchText, PLUCodedItem pluItem) {
       
            // Search in barcoded products
            for(BarcodedProduct barcodedProduct : ProductDatabases.BARCODED_PRODUCT_DATABASE.values()) {
                if(barcodedProduct.getDescription().toLowerCase().contains(searchText.toLowerCase())) {
                    
                    addBarcodedProductToOrder(barcodedProduct);
                    
                    return; 
                }
            }
            
            // Search in PLU-coded products
            for(PLUCodedProduct pluCodedProduct : ProductDatabases.PLU_PRODUCT_DATABASE.values()) {
                if(pluCodedProduct.getDescription().toLowerCase().contains(searchText.toLowerCase())) {
                	BigDecimal itemWeightInGrams = pluItem.getMass().inGrams();
        			double itemWeight = itemWeightInGrams.doubleValue();
                    addPLUCodedProductToOrder(pluCodedProduct, itemWeight);
                    
                    return; 
                }
            }

            
        }
    
    /**
     * Adds a barcoded product to the current order.
     *
     * @param product The barcoded product to add to the order.
     */
    private void addBarcodedProductToOrder(BarcodedProduct product) {
        // Check for an active and unblocked station before proceeding
        if(software.getStationActive() && !software.getStationBlock()) {
        	Mass itemMass = new Mass(product.getExpectedWeight()); //  Mass has a constructor that accepts a double representing grams

            BarcodedItem item = new BarcodedItem(product.getBarcode(), itemMass);
            software.addItemToOrder(item); // Add the item to the order
            
            // Update the total weight and price in the software
            software.addTotalOrderWeightInGrams(product.getExpectedWeight());
            software.addTotalOrderPrice(product.getPrice());
        } else {
            System.out.println("Unable to add product - station is inactive or blocked.");
        }
    }

    /**
     * Adds a PLU-coded product to the current order.
     *
     * @param product The PLU-coded product to add to the order.
     * @param weight The weight of the PLU-coded product to add.
     */
    private void addPLUCodedProductToOrder(PLUCodedProduct product, double weight) {
        // Check for an active and unblocked station before proceeding
        if(software.getStationActive() && !software.getStationBlock()) {
            PLUCodedItem item = new PLUCodedItem(product.getPLUCode(), new Mass(weight));
            software.addItemToOrder(item); // Add the item to the order
            
            // Update the total weight and price in the software
            // price is per-kilogram and we need to convert weight to kilograms
            double weightInKg = weight / 1000;
            long priceForWeight = (long)(product.getPrice() * weightInKg);
            
            software.addTotalOrderWeightInGrams(weight);
            software.addTotalOrderPrice(priceForWeight);
        } else {
            System.out.println("Unable to add product - station is inactive or blocked.");
        }
    }

	/**
	 * Adds an item after customer selects it from the visual catalog
	 * @param visualCatalogueItem
	 */
	public void addItemByVisualCatalogue(PLUCodedItem visualCatalogueItem) {
		if (software.getStationActive()) {
			if (!software.getStationBlock()) {
				software.setStationBlock();

				BigDecimal itemWeightInGrams = visualCatalogueItem.getMass().inGrams();
				double itemWeight = itemWeightInGrams.doubleValue();
				PriceLookUpCode PLUCode = visualCatalogueItem.getPLUCode();
				PLUCodedProduct PLUProduct = ProductDatabases.PLU_PRODUCT_DATABASE.get(PLUCode);

				if(PLUProduct != null) {
					long productPrice = PLUProduct.getPrice();
					software.addTotalOrderWeightInGrams(itemWeight);
					software.addTotalOrderPrice(productPrice);
					Mass mass = new Mass(itemWeight);
					PLUCodedItem newVisualCatalogueItem = new PLUCodedItem(PLUCode, mass);
					software.addItemToOrder(newVisualCatalogueItem);
				}
			}
		}
	}
	// request attendants attention also goes in communication, but where? idk. Button needed in GUI
	// safe to say most things go here for group 4 :))
}
