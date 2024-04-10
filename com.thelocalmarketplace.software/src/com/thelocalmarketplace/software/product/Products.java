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

package com.thelocalmarketplace.software.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.Item;
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
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantPageGUI;

/**
 * Represents the product facade for the whole self-checkout station software. Handles most product related things.
 */
public class Products {

	public SelfCheckoutStationSoftware software;
	public ISelfCheckoutStation station;
	public IElectronicScale baggingArea;
	public IReusableBagDispenser reusableBagDispenser;
	public IBarcodeScanner mainScanner;
	public IBarcodeScanner handheldScanner;
	public ScaleListener scaleListener;
	public ScannerListener scannerListener;
	public Set<ProductsListener> listeners = new HashSet<>();
	private final ArrayList<Item> bulkyItems;

	/**
	 * Basic constructor
	 * 
	 * @param software
	 * 				The main software hub.
	 */
	public Products(SelfCheckoutStationSoftware software) {
		this.software = software;
		this.station = software.getStationHardware();

		this.mainScanner = station.getMainScanner();
		this.handheldScanner = station.getHandheldScanner();
		this.baggingArea = station.getBaggingArea();
		this.reusableBagDispenser = station.getReusableBagDispenser();

		this.scannerListener = new ScannerListener(this);
		this.scaleListener = new ScaleListener(software);

		mainScanner.register(scannerListener);
		handheldScanner.register(scannerListener);
		baggingArea.register(scaleListener);
		
		bulkyItems = new ArrayList<Item>();
	}


	/**
	 * Handles bulky item requests.
	 * 
	 * @param productWeight
	 * 				The weight of the bulky item.
	 * @param attendantGUI 
	 * 				The GUI of the attendant station.
	 */
    public void handleBulkyItemRequest(double productWeight, AttendantPageGUI attendantGUI) {
        software.setStationBlock();
        
         if (attendantGUI.bulkItemRequest("No-bagging request is in progress, approve below"))
			 handleBulkyItem(productWeight);
    }


	/**
	 * Handles bulky item logic.
	 *
	 * @param productWeight
	 * 				The weight of the bulky item.
	 */
	public void handleBulkyItem(double productWeight) {
		double currentWeight = software.getTotalOrderWeightInGrams();
		double finalWeight = currentWeight-productWeight;
		if (finalWeight >= 0)
			software.addTotalOrderWeightInGrams(-productWeight);

		ArrayList<Item> order = software.getOrder();
		Item item = order.get(order.size() - 1);
		bulkyItems.add(item);

		software.setStationUnblock();
	}


	/**
	 * Removes item and updates all order info.
	 * 
	 * @param item
	 * 				The item being removed from the order.
	 */
    public boolean removeItemFromOrder(Item item) {
        if (software.getOrder().contains(item)) {
            software.getOrder().remove(item);
            
            software.setStationBlock();
            
            if (item instanceof BarcodedItem) {
            	removeBarcodedItemFromOrder(item);
                return true;
            } 
            
            if (item instanceof PLUCodedItem) {
                removePLUCodedItemFromOrder(item);
                return true;
            }
        }
        return false;
    }


    /**
     * Removes a barcoded item from the order.
     * 
     * @param item
     * 				The barcoded item being removed.
     */
    public void removeBarcodedItemFromOrder(Item item) {
    	Barcode barcode = ((BarcodedItem) item).getBarcode();
        BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
		if (product == null)
			return;

		double productWeight = product.getExpectedWeight();
		long productPrice = product.getPrice();

		if (!bulkyItems.contains(item))
			software.addTotalOrderWeightInGrams(-productWeight);

		software.addTotalOrderPrice(-productPrice);
		notifyProductRemoved(product);
    }


    /**
     * Removes a PLU coded item from the order.
     * 
     * @param item
     * 				The PLU coded item being removed from the order.
     */
    public void removePLUCodedItemFromOrder(Item item) {
    	PriceLookUpCode PLUCode = ((PLUCodedItem) item).getPLUCode();
        PLUCodedProduct product = ProductDatabases.PLU_PRODUCT_DATABASE.get(PLUCode);

		if (product == null)
			return;

		Mass itemMass = item.getMass();
		double productWeight = itemMass.inGrams().doubleValue();
		long productPrice = product.getPrice();

		if (!bulkyItems.contains(item))
			software.addTotalOrderWeightInGrams(-productWeight);

		software.addTotalOrderPrice(-productPrice);

		notifyProductRemoved(product);
    }


	/**
	 * 
	 * Adds a price look-up coded item to the order.
	 * 
	 * @param pluItem
	 * 				The PLU item being added to the order.			
	 */
	public boolean addItemByPLUCode(PLUCodedItem pluItem) {
		if (!software.getStationActive() || software.getStationBlock())
			return false;

		software.setStationBlock();

		double itemWeightInGrams = pluItem.getMass().inGrams().doubleValue();
		PriceLookUpCode PLUCode = pluItem.getPLUCode();
		PLUCodedProduct product = ProductDatabases.PLU_PRODUCT_DATABASE.get(PLUCode);

		if (product == null)
			return true;

		long productPrice = product.getPrice();
		software.addTotalOrderWeightInGrams(itemWeightInGrams);
		software.addTotalOrderPrice(productPrice);
		software.addItemToOrder(pluItem);
		notifyProductAdded(product);
		return true;
	}


	/**
	 * Adds item to order via a barcode input.
	 * 
	 * @param barcode
	 * 				The barcode that got scanned.
	 */
	public void addItemViaBarcodeScan(Barcode barcode) {
		if (!software.getStationActive() || software.getStationBlock())
			return;

		software.setStationBlock();

		BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
		if (product == null)
			return;

		double productWeight = product.getExpectedWeight();
		long productPrice = product.getPrice();

		software.addTotalOrderWeightInGrams(productWeight);
		software.addTotalOrderPrice(productPrice);

		Mass mass = new Mass(productWeight);
		BarcodedItem barcodedItem = new BarcodedItem(barcode, mass);
		software.addItemToOrder(barcodedItem);
		notifyProductAdded(product);
	}

	
	/**
     * Adds an item to the customer's order by text search.
     *
     * @param searchText 
     * 				The text to search for the product.
     */
    public void addItemByTextSearch(String searchText) {
        Product product = findProductByTextSearch(searchText);

        if (product != null && software.getStationActive() && !software.getStationBlock()) {
            software.setStationBlock();
     
        	if (product instanceof BarcodedProduct) {
        		BarcodedProduct barcodedProduct = (BarcodedProduct) product;
        		
				double productWeight = barcodedProduct.getExpectedWeight(); 
				long productPrice = product.getPrice();

				software.addTotalOrderWeightInGrams(productWeight); 
				software.addTotalOrderPrice(productPrice); 

				Mass mass = new Mass(productWeight);
				BarcodedItem barcodedItem = new BarcodedItem(barcodedProduct.getBarcode(), mass);

                software.addItemToOrder(barcodedItem); 
                
                notifyProductAdded(product);
                notifyAddProductToBaggingArea(product);
                
        	} else {
        		PLUCodedProduct pluProduct = (PLUCodedProduct) product;
        		PLUCodedItem pluItem = new PLUCodedItem(pluProduct.getPLUCode(), new Mass(1.0));
        		
        		addItemByPLUCode(pluItem);
        		
                software.addItemToOrder(pluItem);
                
				software.addTotalOrderWeightInGrams(1); 
				software.addTotalOrderPrice(pluProduct.getPrice()); 
                
                notifyProductAdded(product);
                notifyAddProductToBaggingArea(product);
        	}
        }
    }
	    
    /**
     * Searches for a product with the provided text to find a match in either barcode or PLU code databases.
     * 
     * @param searchText
     * 				The string containing the text used to find either type of product.
     * 						
     * @return a BarcodedProduct or PLUCodedProduct if found, otherwise null.
     */
    public Product findProductByTextSearch(String searchText) {
        // Split the search text into keywords
        String[] keywords = searchText.toLowerCase().split("\\s+");
        
        for (Map.Entry<Barcode, BarcodedProduct> entry : ProductDatabases.BARCODED_PRODUCT_DATABASE.entrySet()) {
            BarcodedProduct product = entry.getValue();
            
            if (containsAllKeywords(product.getDescription().toLowerCase(), keywords)) {
                return product;
            }
        }
        
        for (Map.Entry<PriceLookUpCode, PLUCodedProduct> entry : ProductDatabases.PLU_PRODUCT_DATABASE.entrySet()) {
            PLUCodedProduct product = entry.getValue();

            if (containsAllKeywords(product.getDescription().toLowerCase(), keywords)) {
                return product;
            }
        }
        return null;
    }

    /**
     * Checks if the product text search contains all the keywords.
     * 
     * @param text 
     * 				The text to search in.
     * @param keywords 
     * 				The keywords to search for.
     * @return true if all keywords are found in the text, false otherwise.
     */
    private boolean containsAllKeywords(String text, String[] keywords) {
        for (String keyword : keywords) {
            if (!text.contains(keyword)) {
                return false;
            }
        }
        return true;
    }
	
	/**
	 * Adds an item after customer selects it from the visual catalog.
	 * 
	 * @param visualCatalogueItem
	 * 				The visual catalogue item being added to the order.
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
	 * Loads and dispenses the selected quantity of purchased bags to the bagging area, updating the order weight and total cost accordingly.
	 * 
	 * @param bags 
	 * 				The reusable bags to be purchased.
	 * @throws OverloadedDevice 
	 * 				If the bag dispenser is overloaded.
	 * @throws EmptyDevice 
	 * 				If the dispenser is out of bags.
	 */
	public void PurchaseBags(ReusableBag...bags)throws OverloadedDevice, EmptyDevice {
		if (software.getStationActive()) {
			if (!software.getStationBlock()) {
				software.setStationBlock();
			
			try {
				//loading the bags to the dispenser 
				reusableBagDispenser.load(bags); //requires power
				//making sure the capacity of the dispenser is not surpassed by the loaded bags added 
				//changed here 
				if(reusableBagDispenser.getQuantityRemaining() > reusableBagDispenser.getCapacity()) {
					throw new OverloadedDevice();
				}
				else {
					//reusableBagDispenser.load(bags);
					//dispensing appropriate number of bags
						reusableBagDispenser.dispense();
					//When the customer required the exact amount of bags that are in the dispenser and empty it when their bags are dispensed 
					if(reusableBagDispenser.getQuantityRemaining() == 0) {
						reusableBagDispenser.notify(); 
						throw new EmptyDevice("Dispenser is now out of bags");
					}	
				}
				double reusableBagWeight = 0; 	
				long reusableBagPrice = 1; // $1
				
				for(ReusableBag bag: bags) {
					reusableBagWeight = bag.getMass().inGrams().doubleValue(); 
					software.addTotalOrderPrice(reusableBagPrice); 
					software.addTotalOrderWeightInGrams(reusableBagWeight);
					
					notifyBagsPurchased(reusableBagPrice);
					
					software.getStationHardware().getBaggingArea().addAnItem(bag);
				}
			
			} catch (OverloadedDevice | EmptyDevice e) {
	            System.out.println("Unable to add bags: " + e.getMessage());
	            throw e;
	        } finally {
	            software.getStationBlock();}
			}
		}
	}
	
	/**
	 * Registers the given listener with this facade so that the listener will be notified of events emanating from here.
	 * 
	 * @param listener
	 *            	The listener to be registered. No effect if it is already
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
	 * Notifies listeners that a product has been added.
	 * 
	 * @param product 
	 * 			The product that has been added.
	 */
	public void notifyProductAdded(Product product) {
		for(ProductsListener listener : listeners)
			listener.productAdded(this, product);
	}
	
	/**
	 * Notifies listeners that a product has been removed.
	 * 
	 * @param product 
	 * 			The product that has been removed.
	 */
	public void notifyProductRemoved(Product product) {
		for(ProductsListener listener : listeners)
			listener.productRemoved(this, product);
	}
	
	/**
	 * Notifies listeners to add a product to the bagging area.
	 * 
	 * @param product 
	 * 			The product to be added to the bagging area.
	 */
	public void notifyAddProductToBaggingArea(Product product) {
		for(ProductsListener listener : listeners)
			listener.productToBaggingArea(this, product);
	}
	
	/**
	 * Notifies listeners that bags have been purchased.
	 * 
	 * @param totalPrice 
	 * 				The total price of the bags purchased.
	 */
	public void notifyBagsPurchased(long totalPrice) {
		for(ProductsListener listener : listeners)
			listener.bagsPurchased(null, totalPrice);
	}
}
