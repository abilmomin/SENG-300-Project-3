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

import ca.ucalgary.seng300.simulation.InvalidStateSimulationException;
import static com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation.resetConfigurationToDefaults;
import java.util.ArrayList;
import java.util.Scanner;
import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.bag.IReusableBagDispenser;
import com.jjjwelectronics.card.AbstractCardReader;
import com.jjjwelectronics.card.ICardReader;
import com.jjjwelectronics.printer.IReceiptPrinter;
import com.jjjwelectronics.scale.IElectronicScale;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.tdc.coin.CoinSlot;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.ISelfCheckoutStation;
import com.thelocalmarketplace.hardware.PLUCodedItem;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.funds.PaymentHandler;
import com.thelocalmarketplace.software.oldCode.BaggingAreaListener;
import com.thelocalmarketplace.software.product.ProductHandler;
import com.thelocalmarketplace.software.product.ScannerListener;

/**
 * This class acts as the central unit that communicates with 
 * all other functionalities and listeners of the software.
 */
public class SelfCheckoutStationSoftware {
	// Things to listen to (hardware)
	public AbstractSelfCheckoutStation station;
	private IElectronicScale baggingArea;
	private IReusableBagDispenser reusableBagDispenser;
	private IReceiptPrinter printer;
	private ICardReader cardReader;
	private IBarcodeScanner mainScanner;
	private IBarcodeScanner handheldScanner;
	private CoinSlot coinSlot;
	
	// Listeners
	public BaggingAreaListener baggingAreaListener;
	public ScannerListener scannerListener;

	// Order stuff
	private ArrayList<Item> order;
	private double totalOrderWeight;
	private long totalOrderPrice;
	
	// Facades
	private PaymentHandler funds;
	private ProductHandler products;

	private boolean blocked = false;
	private boolean activeSession = false;

	/**
	 * Creates an instance of the software for a self-checkout station.
	 * 
	 * @param station The self-checkout station that requires the software.
	 */
	public SelfCheckoutStationSoftware(ISelfCheckoutStation station) {
		if (station == null) {
			throw new IllegalArgumentException("The station cannot be null");		// IS THIS IS THE RIGHT ERROR TO THROW HERE
		}
		this.station = (AbstractSelfCheckoutStation) station;

		// Initialize a new order and all its info
		this.order = new ArrayList<Item>();
		this.totalOrderWeight = 0;
		this.totalOrderPrice = 0;
		
		// Make facades
		funds = new PaymentHandler(this);
		products = new ProductHandler(this);

		setStationActive(false);

	}

	/**
	 * Set function to change the blocked variable value.
	 * @param value The new value for station block status
	 */
	public void setStationBlock(boolean value) {
		blocked = value;
		
		station.getCardReader().disable();
		station.getCoinSlot().disable();
		station.getHandheldScanner().disable();
		station.getMainScanner().disable();
	}
	
	/**
	 * Set function to unblock the system
	 * enables functions again
	 */
	public void setStationUnblock() {
		blocked = false;
		
		station.getCardReader().enable();
		station.getCoinSlot().enable();
		station.getHandheldScanner().enable();
		station.getMainScanner().enable();
	}

	/**
	 * Get function to get the blocked station status.
	 */
	public boolean getStationBlock() {
		return blocked;
	}
	
	/**
	 * Set function to change the active variable value.
	 */
	public void setStationActive(boolean value) {
		activeSession = value;
	}

	/**
	 * Get function to get the blocked station status.
	 */
	public boolean getStationActive() {
		return activeSession;
	}
	
	/**
	 * Function to start a session for self-checkout machine
	 * @param scanner The scanner used to obtain user input.
	 * @throws InvalidStateSimulationException If a session is already active.
	 */
	public void startSession(Scanner scanner) {
		if (activeSession) {
			throw new InvalidStateSimulationException("Session already started.");
		}
		
		resetConfigurationToDefaults();
		

		// Prompt the user to touch anywhere to start and wait for an input.
		System.out.println("Welcome to The Local Marketplace. Touch anywhere to start.");
		
		// assume the user gives some kind of input.
		scanner.nextLine();

		setStationActive(true);

	}
	
	/**
	 * Resets the current order monitored by the software.
	 */
	public void resetOrder() {
		this.order = new ArrayList<Item>();
		this.totalOrderWeight = 0;
		this.totalOrderPrice = 0;
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
	 * @return true if the item was successfully removed, false otherwise.
	 */
	public boolean removeItemFromOrder(Item item) {
		if (this.order.contains(item)) {
			this.order.remove(item);
			
			setStationBlock(true);
			
			if (item instanceof BarcodedItem) {
				Barcode barcode = ((BarcodedItem) item).getBarcode();
				BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
				if (product != null) {
					double productWeight = product.getExpectedWeight();
					long productPrice = product.getPrice();
					
					removeTotalOrderWeightInGrams(productWeight);
					removeTotalOrderPrice(productPrice);
					
					System.out.println("Please remove item from the bagging area");
				}
				return true;
			} 
			
			if (item instanceof PLUCodedItem) {
				PriceLookUpCode PLUCode = ((PLUCodedItem) item).getPLUCode();
				PLUCodedProduct product = ProductDatabases.PLU_PRODUCT_DATABASE.get(PLUCode);
				if (product != null) {
					Mass itemMass = item.getMass();
					double productWeight = itemMass.inGrams().doubleValue();
					long productPrice = product.getPrice();
					
					removeTotalOrderWeightInGrams(productWeight);
					removeTotalOrderPrice(productPrice);
					
					System.out.println("Please remove item from the bagging area");
				}
				return true;
			}
			
		} else {
			System.out.println("Item not found in the order.");
			return false;
		}
		
		return false;
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
	public double getTotalOrderWeightInGrams() {
		return this.totalOrderWeight;
	}

	/**
	 * Gets the total price of the order
	 * 
	 * @return The total price of order.
	 */
	public long getTotalOrderPrice() {
		return this.totalOrderPrice;
	}
	public void removeTotalPrice(long price) {
		this.totalOrderPrice -= price;
	}
	
	/**
	 * Updates the total weight of the order (in grams)
	 */
	public void addTotalOrderWeightInGrams(double weight) {
		this.totalOrderWeight += weight;
	}
	public void removeTotalOrderWeightInGrams(double weight) {
		this.totalOrderWeight -= weight;
	}
	
	/**
	 * Updates the total price of the order
	 */
	public void addTotalOrderPrice(long price) {
		this.totalOrderPrice += price;
	}
	public void removeTotalOrderPrice(long price) {
		this.totalOrderPrice -= price;
	}

	/**
	 * Checks whether the order is empty or not.
	 * 
	 * @return true if the order contains no items, false otherwise.
	 */
	public boolean isOrderEmpty() {
		return order.isEmpty();
	}
	
	public void notifyUserOfOverload() {
		System.out.println("Scale Overload. Please remove some items.");
	}

	public ISelfCheckoutStation getStationHardware() {
		return station;
	}

}
