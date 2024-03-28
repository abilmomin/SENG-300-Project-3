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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.bag.IReusableBagDispenser;
import com.jjjwelectronics.card.ICardReader;
import com.jjjwelectronics.printer.IReceiptPrinter;
import com.jjjwelectronics.scale.IElectronicScale;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.jjjwelectronics.screen.ITouchScreen;
import com.tdc.banknote.BanknoteDispensationSlot;
import com.tdc.banknote.BanknoteInsertionSlot;
import com.tdc.banknote.BanknoteStorageUnit;
import com.tdc.banknote.BanknoteValidator;
import com.tdc.banknote.IBanknoteDispenser;
import com.tdc.coin.CoinSlot;
import com.tdc.coin.CoinStorageUnit;
import com.tdc.coin.CoinValidator;
import com.tdc.coin.ICoinDispenser;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.CoinTray;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

/**
 * This class is for the software required for the self-checkout station session
 */
public class SelfCheckoutStationSoftware {
	// Things to listen to (hardware)
	public AbstractSelfCheckoutStation selfCheckoutStation;
	public IElectronicScale baggingArea;
	public IReusableBagDispenser reusableBagDispenser;
	public IReceiptPrinter printer;
	public ICardReader cardReader;
	public IBarcodeScanner mainScanner;
	public IBarcodeScanner handheldScanner;
	public CoinSlot coinSlot;
	
	// Listeners
	public BaggingAreaListener baggingAreaListener;
	public ScannerListener scannerListener;

	// Order stuff
	private ArrayList<Item> order;
	private double totalOrderWeight;
	private long totalOrderPrice;

	private boolean blocked = false;
	private boolean active = false;
	
	// bulky item code also goes in this class i can do it later. Button needed in GUI -Tara
	// request attendants attention also goes here. Button needed in GUI
	// safe to say most things go here
	
	/**
	 * Creates an instance of the software for a self checkout station.
	 * 
	 * @param selfCheckoutStation
	 * 		The self checkout station that requires the software.
	 */
	public SelfCheckoutStationSoftware(AbstractSelfCheckoutStation selfCheckoutStation) {
		this.selfCheckoutStation = selfCheckoutStation;
		
		// Make the listener objects
		this.scannerListener = new ScannerListener(this);
		
		// Get all the hardware the listeners need to listen to
		this.mainScanner = selfCheckoutStation.getMainScanner();
		this.handheldScanner = selfCheckoutStation.getHandheldScanner();
		
		// Attach the listeners to the hardware
		mainScanner.register(scannerListener);
		handheldScanner.register(scannerListener);
		
		// Initialize a new order and all its info
		this.order = new ArrayList<Item>();
		this.totalOrderWeight = 0;
		this.totalOrderPrice = 0;
	}

	/**
	 * Set function to change the blocked variable value.
	 * @param value The new value for station block status
	 */
	public void setStationBlock(boolean value) {
		blocked = value;
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
		active = value;
	}

	/**
	 * Get function to get the blocked station status.
	 */
	public boolean getStationActive() {
		return active;
	}
	
	/**
	 * Function to start a session for self-checkout machine
	 * @param scanner The scanner used to obtain user input.
	 * @throws InvalidStateSimulationException If a session is already active.
	 */
	public void startSession(Scanner scanner) {
		if (active) {
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
	public boolean removeItemFromOrder(BarcodedItem item) {
		if (this.order.contains(item)) {
			this.order.remove(item);
			
			setStationBlock(true);
			
			Barcode barcode = item.getBarcode();
			BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
			if (product != null) {
				double productWeight = product.getExpectedWeight();
				long productPrice = product.getPrice();
				
				removeTotalOrderWeightInGrams(productWeight);
				removeTotalOrderPrice(productPrice);
				
				System.out.println("Please remove item from the bagging area");
			}
			return true;
			
		} else {
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
}
