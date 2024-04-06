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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.bag.IReusableBagDispenser;
import com.jjjwelectronics.bag.ReusableBag;
import com.jjjwelectronics.scale.IElectronicScale;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.*;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

public class Products {
	// request attendants attention also goes in communication, but where? idk. Button needed in GUI
	// safe to say most things go here for group 4 :))
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
	public Set<ProductsListener> listeners = new HashSet<>();

	/**
	 * Basic constructor.
	 * 
	 * @param software
	 * 		The main software hub.
	 */
	public Products(SelfCheckoutStationSoftware software) {
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

		// Attach the listeners to the hardware
		mainScanner.register(scannerListener);
		handheldScanner.register(scannerListener);
		baggingArea.register(scaleListener);
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
	 * @param code
	 */
	
	public boolean addItemByPLUCode(PLUCodedItem pluItem) {
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
				
				notifyProductAdded(product);
			}
			return true;
		} else {
			return false;
		}
	}
	/**
     * Adds an item to the customer's order by text search.
     *
     * @param searchText The text to search for the product.
     */
    public void addItemByTextSearch(String searchText) {
        BarcodedProduct product = findProductByTextSearch(searchText);

        if (product != null) {
            
            Mass itemMass = new Mass(product.getExpectedWeight());

            // Create a BarcodedItem with the product's barcode and expected weight.
            BarcodedItem barcodedItem = new BarcodedItem(product.getBarcode(), itemMass);

            // Add the barcodedItem to the order.
            software.addItemToOrder(barcodedItem);
            System.out.println("Product added to the order: " + product.getDescription());
        } else {
            // If the product is not found, inform the attendant.
            System.out.println("Product not found. Please try again.");
        }
    }
    
    /**
     * Finds a product in the product database using text search.
     *
     * @param searchText The text to search for in product descriptions.
     * @return The product if found, null otherwise.
     */
    private BarcodedProduct findProductByTextSearch(String searchText) {
        // Loop through each entry in the barcoded product database
        for (Map.Entry<Barcode, BarcodedProduct> entry : ProductDatabases.BARCODED_PRODUCT_DATABASE.entrySet()) {
            BarcodedProduct product = entry.getValue();
            // Check if the product description contains the search text, case insensitive
            if (product.getDescription().toLowerCase().contains(searchText.toLowerCase())) {
                // Return the first matching product
                return product;
            }
        }
        // Return null if no matching product is found
        return null;
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
				
				notifyProductAdded(PLUProduct);
			}
		}
	}
	/**
	 * The selected amount of purchased bags are dispensed and added to the bagging area. 
	 * The bag weight is added to the order weight 
	 */
	public void PurchaseBags(ReusableBag...bags)throws OverloadedDevice, EmptyDevice {
		if (software.getStationActive()) {
			if (!software.getStationBlock()) {
				software.setStationBlock();
			
			try {
				reusableBagDispenser.load(bags); //requires power
				// making sure dispenser has enough bags 
				if(reusableBagDispenser.getQuantityRemaining() < bags.length || reusableBagDispenser.getCapacity() <bags.length) {
					throw new EmptyDevice("Dispenser does not have enough bags");
				}
				else {
					reusableBagDispenser.load(bags);
					//dispensing appropriate number of bags and notifies that bags are dispensed
						reusableBagDispenser.dispense();
					if(reusableBagDispenser.getQuantityRemaining() == 0) {
						reusableBagDispenser.notify(); 
						throw new EmptyDevice("Dispenser is now out of bags");
					}	
				}
			double reusableBagWeightTotal = 0; 
			Product rbag = null;
			long reusableBagPrice = rbag.getPrice();
			for(ReusableBag bag: bags) {
				reusableBagWeightTotal += bag.getMass().inGrams().doubleValue(); 
				long totalBagPrice = reusableBagPrice * bags.length; 
				software.addTotalOrderPrice(totalBagPrice); 
			}
			// add the total weight of all purchased bags to the weight of the order 
			software.addTotalOrderWeightInGrams(reusableBagWeightTotal); 
			
			} catch (OverloadedDevice | EmptyDevice e) {
	            System.out.println("Unable to add bags: " + e.getMessage());
	            throw e;
	        } finally {
	            software.getStationBlock();}
			}
		}
	}
	
	
	/**
	 * Registers the given listener with this facade so that the listener will be
	 * notified of events emanating from here.
	 * 
	 * @param listener
	 *            The listener to be registered. No effect if it is already
	 *    
	 */
	public void register(ProductsListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * De-registers the given listener from this facade so that the listener will no
	 * longer be notified of events emanating from here.
	 * 
	 * @param listener
	 *            The listener to be de-registered. No effect if it is not already
	 *            registered or null.
	 */
	public void deregister(ProductsListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notifies observers that an item was added to the order.
	 * 
	 * @param product
	 * 		The product added.
	 */
	public void notifyProductAdded(Product product) {
		for(ProductsListener listener : listeners)
			listener.productAdded(this, product);
	}
	
	/**
	 * Notifies observers that an item was removed to the order.
	 * 
	 * @param product
	 * 		The product removed.
	 */
	public void notifyProductRemoved(Product product) {
		for(ProductsListener listener : listeners)
			listener.productRemoved(this, product);
	}
}
