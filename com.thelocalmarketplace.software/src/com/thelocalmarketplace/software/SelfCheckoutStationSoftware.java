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

import java.util.*;
import com.jjjwelectronics.Item;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.ISelfCheckoutStation;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantPageGUI;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.CustomerStation;
import com.thelocalmarketplace.software.funds.Funds;
import com.thelocalmarketplace.software.funds.Receipt;
import com.thelocalmarketplace.software.product.Products;
import com.thelocalmarketplace.hardware.external.CardIssuer;
import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.scale.IElectronicScale;

/**
 * This class acts as the central unit that communicates with 
 * all other functionalities and listeners of the software.
 */
public class SelfCheckoutStationSoftware {
	
	private ArrayList<Item> order;
	private double totalOrderWeight;
	private double totalOrderPrice;
	private boolean blocked = false;
	private boolean activeSession = false;
	private Set<CardIssuer> banks = new HashSet<>();
	private Card creditCard;
	private Card debitCard;
	
	// Things to listen to (hardware)
	public AbstractSelfCheckoutStation station;
	private CustomerStation gui;
	private ProductsDatabase allProducts; 
	
	// Facades and listeners
	private Funds funds;
	private Products products;
	private SelfCheckoutCoordinator selfCheckoutCoordinator;
	private AttendantPageGUI Agui;

	/**
	 * Creates an instance of the software for a self-checkout station.
	 * 
	 * @param station 
	 * 			The self-checkout station that requires the software.
	 */
	public SelfCheckoutStationSoftware(AbstractSelfCheckoutStation station) {
		if (station == null)
			throw new IllegalArgumentException("The station cannot be null");	
		
		this.station = station;

		this.order = new ArrayList<Item>();
		this.totalOrderWeight = 0;
		this.totalOrderPrice = 0;
		
		this.funds = new Funds(this);
		this.products = new Products(this);
		this.selfCheckoutCoordinator = new SelfCheckoutCoordinator(this, funds, products);
		
		products.register(selfCheckoutCoordinator);
		funds.register(selfCheckoutCoordinator);

		allProducts = new ProductsDatabase();
		setStationActive(false);
	}
	
	/**
	 * Set the GUI for the station and for coordination.
	 * 
	 * @param gui
	 * 			The GUI for the self checkout station.
	 */
	public void setGUI(CustomerStation gui) {
		this.gui = gui;
		selfCheckoutCoordinator.setGUI(gui);
	}
	
	/**
	 * Get the GUI for the station
	 * 
	 * @return The GUI for the self checkout station.
	 */
	public CustomerStation getGUI() {
		return gui;
	}
	  
	/** 
	 * Set the GUI for the station and for coordination.
	 * 
	 * @param gui
	 * 			The GUI for the self checkout station.
	 */
	public void setAGUI(AttendantPageGUI gui) {
		this.Agui = gui;
		selfCheckoutCoordinator.setAGUI(gui);
	}
	
	/**
	 * Get the GUI for the station
	 * 
	 * @return The GUI for the self checkout station.
	 */
	public AttendantPageGUI getAGUI() {
		return Agui;
	}
	
	/**
	 * Get the funds facade for the station.
	 * 
	 * @return The funds Facade of type Funds.
	 */
	public Funds getFunds() {
		return funds;
	}
	
	/**
	 * Get the database of products.
	 * 
	 * @return The database of all products of type ProductsDatabase.
	 */
	public ProductsDatabase getAllProducts() {
		return allProducts;
	}

	/**
	 * Set function to block the station.
	 * Disables parts of the station to block further customer interaction.
	 */
	public void setStationBlock() {
		blocked = true;
		
		station.getCardReader().disable();
		station.getCoinSlot().disable();
		station.getHandheldScanner().disable();
		station.getMainScanner().disable();
	}
	
	/**
	 * Set function to unblock the station.
	 * Enables parts of the station to allow further customer interaction.
	 */
	public void setStationUnblock() {
		blocked = false;
		
		station.getCardReader().enable();
		station.getCoinSlot().enable();
		station.getHandheldScanner().enable();
		station.getMainScanner().enable();
	}

	/**
	 * Getter function to get the blocked station status.
	 * 
	 * @return true if the station is blocked, false otherwise.
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
	 * Getter function to get the session status.
	 * 
	 * @return true if there is a current session in progress, false otherwise.
	 */
	public boolean getStationActive() {
		return activeSession;
	}
	
	/**
	 * Get the receipt.
	 * 
	 * @return The receipt as type Receipt.
	 */
	public Receipt getReceipt() {
		return funds.receipt;
	}
	
    /**
     * Resets the current order monitored by the software.
     */
    public void resetOrder() {
        removeAllItemsFromScale();
        
        this.order = new ArrayList<Item>();
        this.totalOrderWeight = 0;
        this.totalOrderPrice = 0;
    }
    
    private void removeAllItemsFromScale() {
        IElectronicScale baggingArea = station.getBaggingArea();
        
        ArrayList<Item> itemsToRemove = new ArrayList<>();
                
        for (Item item : order) 
            itemsToRemove.add(item);
            
        for (Item item : itemsToRemove) {
            products.removeItemFromOrder(item);
            baggingArea.removeAnItem(item);
        }
    }
	
	/**
	 * Adds an item to the order.
	 *
	 * @param item 
	 * 			The item to add to the order.
	 */
	public void addItemToOrder(Item item) {
		this.order.add(item);
	}
	
	/**
	 * Removes an item from the order.
	 *
	 * @param item 
	 * 			The item to remove from order.
	 */
	public void removeFromOrder(Item item) {
		this.order.remove(item);
	}
	
	/**
	 * Find the associated product with a given PLU.
	 * 
	 * @param code 
	 * 			The PLU code of a product.
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
	 * Sets the total price of the order.
	 * 
	 * @param price
	 * 			The total price of the order (in dollars).
	 */
	public void setOrderTotalPrice(double price) {
		this.totalOrderPrice = price;
	}
	
	/**
	 * Updates the total weight of the order by adding a new weight to the total.
	 */
	public void addTotalOrderWeightInGrams(double weight) {
		this.totalOrderWeight += weight;
	}
	
	/**
	 * Updates the total price of the order by adding a new price to the total.
	 */
	public void addTotalOrderPrice(double price) {
		this.totalOrderPrice += price;
	}
	
	/**
	 * Calls the GUI to create a pop-up to notify the customer of the overload.
	 */
	public void notifyUserOfOverload() {
		gui.customerPopUp("Overload");}

	/**
	 * Checks whether the order is empty or not.
	 * 
	 * @return true if the order contains no items, false otherwise.
	 */
	public boolean isOrderEmpty() {
		return order.isEmpty();
	}

	/**
	 * Gets the hardware associated with the self checkout station software.
	 * 
	 * @return the hardware of this station.
	 */
	public ISelfCheckoutStation getStationHardware() {
		return station;
	}

	/**
	 * Gets the banks recognized by the software.
	 * 
	 * @return the set of banks.
	 */
	public Set<CardIssuer> getBanks() {
		return banks;
	}

	/**
	 * Adds a bank to the set of banks recognized by this software.
	 * 
	 * @param cardIssuer
	 * 			The new bank getting added to the set of banks.
	 */
	public void addBank(CardIssuer cardIssuer) {
		this.banks.add(cardIssuer);
	}

	/**
	 * Adds a card.
	 * 
	 * @param card
	 * 			The card being added.
	 * @param type
	 * 			The type of card (credit or debit).
	 */
	public void addPaymentCard(Card card, String type) {
		if (Objects.equals(type, "credit")) {
			creditCard = card;
		}
		else if (Objects.equals(type, "debit")) {
			debitCard = card;
		}
	}

	/**
	 * Gets the card given the type of card.
	 * 
	 * @param cardType
	 * 			The type of card (credit or debit).
	 * @return the corresponding card.
	 */
	public Card getCard(String cardType) {
		if (Objects.equals(cardType, "credit")) {
			return creditCard;
		}
		else {
			return debitCard;
		}
	}
	
	/**
	 * Gets the product facade associated with this station.
	 * 
	 * @return the product facade.
	 */
	public Products getProductHandler() {
		return products;
	}
}