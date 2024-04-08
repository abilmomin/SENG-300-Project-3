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

package com.thelocalmarketplace.software;

import ca.ucalgary.seng300.simulation.InvalidStateSimulationException;
import static com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation.resetConfigurationToDefaults;
import java.util.*;
import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.ISelfCheckoutStation;
import com.thelocalmarketplace.hardware.PLUCodedItem;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.CustomerStation;
import com.thelocalmarketplace.software.funds.Funds;
import com.thelocalmarketplace.software.funds.Receipt;
import com.thelocalmarketplace.software.product.Products;
import com.thelocalmarketplace.software.product.ScannerListener;
import com.thelocalmarketplace.hardware.external.CardIssuer;
import com.jjjwelectronics.card.Card;

/**
 * This class acts as the central unit that communicates with 
 * all other functionalities and listeners of the software.
 */
public class SelfCheckoutStationSoftware {
	// Things to listen to (hardware)
	public AbstractSelfCheckoutStation station;
	private CustomerStation gui;
	private ProductsDatabase allProducts; 

	// Listeners
	public ScannerListener scannerListener;

	// Order stuff
	private ArrayList<Item> order;
	private double totalOrderWeight;
	private double totalOrderPrice;
	
	// Facades and listeners
	private Funds funds;
	private Products products;
	private Coordination coordination;

	private boolean blocked = false;
	private boolean activeSession = false;

	private Set<CardIssuer> banks = new HashSet<>();

	private Card creditCard;
	private Card debitCard;

	/**
	 * Creates an instance of the software for a self-checkout station.
	 * 
	 * @param station 
	 * 			The self-checkout station that requires the software.
	 */
	public SelfCheckoutStationSoftware(AbstractSelfCheckoutStation station) {
		if (station == null) {
			throw new IllegalArgumentException("The station cannot be null");	
		}	
		this.station = station;

		this.order = new ArrayList<Item>();
		this.totalOrderWeight = 0;
		this.totalOrderPrice = 0;
		
		this.funds = new Funds(this);
		this.products = new Products(this);
		this.coordination = new Coordination(this, funds, products);
		
		products.register(coordination);
		funds.register(coordination);

		allProducts = new ProductsDatabase();
		setStationActive(false);
	}
	
	/**
	 * Set the gui for the station and for coordination
	 * @param gui
	 * 			The CustomerStation gui
	 */
	public void setGUI(CustomerStation gui) {
		this.gui = gui;
		coordination.setGUI(gui);
	}
	
	/**
	 * Get the gui for the station
	 * @return
	 * 			The gui of type CustomerStation
	 */
	public CustomerStation getGUI() {
		return gui;
	}

	/**
	 * Get the funds facade for the station
	 * @return
	 * 			The funds Facade of type Funds
	 */
	public Funds getFunds() {
		return funds;
	}
	
	/**
	 * Get the database of products
	 * @return
	 * 			The database of all products of type ProductsDatabase
	 */
	public ProductsDatabase getAllProducts() {
		return allProducts;
	}

	/**
	 * Set function to block the station
	 * Disables parts of the station to block further customer interaction
	 */
	public void setStationBlock() {
		blocked = true;
		
		station.getCardReader().disable();
		station.getCoinSlot().disable();
		station.getHandheldScanner().disable();
		station.getMainScanner().disable();
	}
	
	/**
	 * Set function to unblock the station
	 * Enables parts of the station to allow further customer interaction 
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
	 * Get the receipt
	 * @return
	 * 			The receipt as type Receipt
	 */
	public Receipt getReceipt() {
		return funds.receipt;
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
	 */
	public void removeFromOrder(Item item) {
		this.order.remove(item);
	}
	
	/**
	 * Find the associated product with a given PLU.
	 * 
	 * @param code The PLU code of a product.
	 * @return the product with the given PLU code.
	 */
	public PLUCodedProduct matchCodeAndPLUProduct(String code) {
		PriceLookUpCode plu = new PriceLookUpCode(code);
		PLUCodedProduct currentItem = ProductDatabases.PLU_PRODUCT_DATABASE.get(plu);
		return currentItem;	
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
	public double getTotalOrderPrice() {
		return this.totalOrderPrice;
	}

	/**
	 * sets the total price of the order
	 * 
	 * @return The total price of order.
	 */
	public void setOrderTotalPrice(double price) {
		this.totalOrderPrice = price;
	}
	
	/**
	 * Updates the total weight of the order (in grams)
	 */
	public void addTotalOrderWeightInGrams(double weight) {
		this.totalOrderWeight += weight;
	}
	
	/**
	 * Updates the total price of the order
	 */
	public void addTotalOrderPrice(double price) {
		this.totalOrderPrice += price;
	}
	
	public void notifyUserOfOverload() {
		gui.customerRemoveItemPopUp();
	}

	/**
	 * Checks whether the order is empty or not.
	 * 
	 * @return true if the order contains no items, false otherwise.
	 */
	public boolean isOrderEmpty() {
		return order.isEmpty();
	}

	public ISelfCheckoutStation getStationHardware() {
		return station;
	}

	public Set<CardIssuer> getBanks() {
		return banks;
	}

	public void addBank(CardIssuer cardIssuer) {
		this.banks.add(cardIssuer);
	}

	public void addPaymentCard(Card card, String type) {
		if (Objects.equals(type, "credit")) {
			creditCard = card;
		}
		else if (Objects.equals(type, "debit")) {
			debitCard = card;
		}
	}

	public Card getCard(String cardType) {
		if (Objects.equals(cardType, "credit")) {
			return creditCard;
		}
		else {
			return debitCard;
		}
	}
	
	public Products getProductHandler() {
		return products;
	}
}
