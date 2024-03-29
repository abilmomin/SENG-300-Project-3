/**
 * Yotam Rojnov (UCID: 30173949)
 * Duncan McKay (UCID: 30177857)
 * Mahfuz Alam (UCID:30142265)
 * Luis Trigueros Granillo (UCID: 30167989)
 * Lilia Skumatova (UCID: 30187339)
 * Abdelrahman Abbas (UCID: 30110374)
 * Talaal Irtija (UCID: 30169780)
 * Alejandro Cardona (UCID: 30178941)
 * Alexandre Duteau (UCID: 30192082)
 * Grace Johnson (UCID: 30149693)
 * Abil Momin (UCID: 30154771)
 * Tara Ghasemi M. Rad (UCID: 30171212)
 * Izabella Mawani (UCID: 30179738)
 * Binish Khalid (UCID: 30061367)
 * Fatima Khalid (UCID: 30140757)
 * Lucas Kasdorf (UCID: 30173922)
 * Emily Garcia-Volk (UCID: 30140791)
 * Yuinikoru Futamata (UCID: 30173228)
 * Joseph Tandyo (UCID: 30182561)
 * Syed Haider (UCID: 30143096)
 * Nami Marwah (UCID: 30178528)
 */

package com.thelocalmarketplace.software;

import java.util.ArrayList;
import java.util.Scanner;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

/**
 * Represents the Customer's order that different use cases can interact with.
 */
public class Order {
	private ArrayList<Item> order;
	private double totalWeight;
	private long totalPrice;
	private BarcodedItem barcodedItem;
	private Mass mass;
	private AbstractElectronicScale scale;

	/**
	 * Constructs an empty order.
	 * @throws OverloadedDevice 
	 */
	public Order(AbstractElectronicScale scale) throws OverloadedDevice {
		this.order = new ArrayList<Item>();
		this.totalWeight = 0; 
		this.totalPrice = 0;
		this.scale = scale;
	}

	/**
	 * Adds an item to the order.
	 *
	 * @param item The item to add to the order.
	 */
	public void addItemToOrder(Item item) {
		this.order.add(item);
	}
	/**
	 * Removes an item from the order.
	 *
	 * @param item The item to remove from order.
	 * @return true if the item was successfully removed, false otherwise
	 */
	public boolean removeItemFromOrder(BarcodedItem item) {
		if (this.order.contains(item)) {
			this.order.remove(item);
			
			Barcode barcode = item.getBarcode();
			BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
			if (product != null) {
				double productWeight = product.getExpectedWeight();
				long productPrice = product.getPrice();
				
				removeTotalWeightInGrams(productWeight);
				removeTotalPrice(productPrice);

			}
			return true;
		}
		else {
			System.out.println("Item not found in the order.");
			return false;
		}
	}

	/**
	 * Gets the order.
	 *
	 * @return The order.
	 */
	public ArrayList<Item> getOrder() {
		return this.order;
	}

	/**
	 * Gets the total weight of the order (in grams).
	 * 
	 * @return The total weight of order (in grams).
	 */
	public double getTotalWeightInGrams() {
		return this.totalWeight;
	}

	/**
	 * Gets the total price of the order
	 * 
	 * @return The total price of order.
	 */
	public long getTotalPrice() {
		return this.totalPrice;
	}

	/**
	 * Updates the total weight of the order (in grams)
	 */
	public void addTotalWeightInGrams(double weight) {
		this.totalWeight += weight;
	}
	public void removeTotalWeightInGrams(double weight) {
		this.totalWeight -= weight;
	}


	/**
	 * Updates the total price of the order
	 */
	public void addTotalPrice(long price) {
		this.totalPrice += price;
	}
	public void removeTotalPrice(long price) {
		this.totalPrice -= price;
	}

	/**
	 * Adds an item to the order via barcode scan
	 * Accounting for Bronze, Silver and Gold checkout Stations
	 */
	public BarcodedItem addItemViaBarcodeScan(Barcode barcode) {
		// Gets the product from the hardware's database
		// All the barcodes are accepted from the bronze, silver and gold
		BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);

		//Makes sure that the product that is recieved from the barcode 
		// Is not null, therefore making sure it exists.
		if (product != null) {
			//Gets both the products price and weight
			double productWeight = product.getExpectedWeight(); 
			long productPrice = product.getPrice();

			// Adds the weight and price of the product 
			// Adding it to the total weight/price
			addTotalWeightInGrams(productWeight); 
			addTotalPrice(productPrice); 

			mass = new Mass(productWeight); // Converts the weight of the product to a mass
			barcodedItem = new BarcodedItem(barcode, mass); // Adds the product to the order
			addItemToOrder(barcodedItem); // Adds the product to the order

			// Signal to the customer to place the scanned item in the bagging area
			System.out.println("Please place item in the bagging area.");

		}
		return barcodedItem;
	}

	/**
	 * Checks for weight discrepancy, is called by the baggingAreaListener after an item is added to the bagging area scale
	 * and the signalToRemoveItemFromOrder after the item is removed from the order.
	 * @throws OverloadedDevice
	 */
	public void checkForDiscrepancy() throws OverloadedDevice {
		WeightDiscrepancy weightDiscrepancy = new WeightDiscrepancy(this, scale);

		weightDiscrepancy.checkIfCanUnblock(); // Checks for a weight discrepancy, if none, it unblocks the system
	}



	/**
	 * Signals that a specific item is to be removed from the order
	 * @throws OverloadedDevice 
	 */
	public BarcodedItem signalToRemoveItemFromOrder(Scanner scanner) throws OverloadedDevice {
		// Signals to the customer which item they want to remove from the order
		System.out.println("Please select the item you want to remove from the order as a number from the order list.");

		displayOrder(); // Displays the order to the customer

		String itemToRemove = scanner.nextLine(); // this will be the order number of the item to remove

		// example:
		// 1. Banana
		// 2. Apple
		// 3. Orange
		// removing "1" will remove banana from the order

		BarcodedItem itemRemoved = (BarcodedItem) order.get(Integer.parseInt(itemToRemove) - 1);

		// check if there is an active session
		if (SelfCheckoutStationSoftware.getStationActive()) {
			// check if the station is not blocked
			if (!SelfCheckoutStationSoftware.getStationBlock()) {
				// blocks station from further  customer actions
				SelfCheckoutStationSoftware.setStationBlock(true);

				Barcode barcode = ((BarcodedItem) order.get(Integer.parseInt(itemToRemove) - 1)).getBarcode();
				// remove the item from the order
				removeItemFromOrder(itemRemoved);

				BarcodedProduct productRemoved = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);

				// Signals to the customer that the item has been removed from the order
				System.out.println("Item: " + productRemoved.getDescription() + " has been removed from the order.");

				displayOrder(); // Displays the order to the customer after removal
			}
		}
		
		// check for weight discrepancy, then unlock the station
		checkForDiscrepancy();

		return itemRemoved;
	}
	 


	/**
	 * Display the order to the customer one by one from the list of items in the order
	 */
	public void displayOrder(){
		System.out.println("\nOrder details:\n");
		// list the items in order
		for (int i = 1; i <= order.size(); i++) {
			Barcode barcode = ((BarcodedItem) order.get(i - 1)).getBarcode();

			BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
			System.out.println(i + ". " + product.getDescription() + "\nPrice of product: $" + product.getPrice() + "\nWeight of product: " + product.getExpectedWeight());
		}
		System.out.println("\nTotal price: " + getTotalPrice() + "\nTotal weight: " + getTotalWeightInGrams() + " \n");
	}
	
	/**
	 * Checks whether the order is empty or not.
	 * 
	 * @return true if the order contains no items, false otherwise.
	 */
	public boolean isEmpty() {		// primary purpose of this method is to help in edge cases of demoIterationTwo class
		return order.isEmpty();
	}
	
}
