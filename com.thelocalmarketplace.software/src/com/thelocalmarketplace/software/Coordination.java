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

import java.math.BigDecimal;

import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.Product;

import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.CustomerStation;
import com.thelocalmarketplace.software.funds.Funds;
import com.thelocalmarketplace.software.funds.FundsObserver;
import com.thelocalmarketplace.software.funds.PaymentKind.Kind;
import com.thelocalmarketplace.software.product.Products;
import com.thelocalmarketplace.software.product.ProductsListener;

public class Coordination implements FundsObserver, ProductsListener {
	SelfCheckoutStationSoftware software;
    Funds funds;
    Products products;
    CustomerStation gui;

    /**
     * Constructor for Coordination class
     * @param software 
     * 			the SelfCheckoutStation device 
     * @param funds 
     * 			the Funds facade to handle all payments and change 
     * @param products 
     * 			the Products facade for product handling 
     */
    public Coordination(SelfCheckoutStationSoftware software, Funds funds, Products products) {
        this.software = software;
        this.funds = funds;
        this.products = products;
    }
    
    /**
     * Set the gui
     * @param gui 
     * 			gui for this CustomerStation
     */
    public void setGUI(CustomerStation gui) {
    	this.gui = gui;
    }
    
    public void noValidChange(Funds fundsFacade, BigDecimal changeDue) {
    	if(gui != null) {
    		gui.handleRequestAssistance();
    		gui.displayAmountDuePopup(changeDue);
    	}
    }

    /**
     * Override of fundsAdded from FundsObserver interface
     * Notifies the gui and update that funds have been added 
     */
    @Override
    public void fundsAdded(Funds fundsFacade, BigDecimal funds) {
    	if(gui != null) {
    		gui.updatePaidDisplay(funds.doubleValue());
    		gui.updateStatusDisplay();
    	}
    }

    /**
     * Override of fundsRemoved from FundsObserver interface
     * Notifies the gui and update that funds have been removed
     */
    @Override
    public void fundsRemoved(Funds fundsFacade, BigDecimal funds) {
    	if(gui != null) {
    		gui.updatePaidDisplay(-1*funds.doubleValue());
			gui.updateStatusDisplay();
    	}
    }

    /**
     * Override of fundsStored from FundsObserver interface
     * Notifies the gui and update that funds have been removed, but are stored internally 
     */
    @Override
    public void fundsStored(Funds fundsFacade, BigDecimal funds) {
    	if(gui != null)
    		gui.updateStatusDisplay();
    }

    /**
     * Override of fundsInvalid from FundsObserver interface
     * Notifies the gui that the funds are invalid for the payment kind
     */
    @Override
    public void fundsInvalid(Funds fundsFacade, Kind kind) {
    	if(gui != null)
    		gui.customerPopUp("The payment method was invalid.");
    }

    /**
     * Override of fundsPaidInFull from FundsObserver interface 
     * Notifies the gui that funds have been paid in full and change has been returned 
     */
    @Override
    public void fundsPaidInFull(Funds fundsFacade, BigDecimal changeReturned) {
        if(gui != null)
			gui.setPaymentSuccesful(changeReturned.doubleValue());
    }

    /**
     * Override of fundsStationBlocked from FundsObserver interface
     * Notifies the gui that the station has been blocked during payment
     */
    @Override
    public void fundsStationBlocked(Funds fundsFacade) {
    	if(gui != null)
    		gui.customerPopUp("Payment failed due to the station being blocked.");
    }
    
    /**
     * Override of productAdded from ProductListener interface
     * Notifies the gui that a product has been added 
     */
    @Override
    public void productAdded(Products productFacade, Product product) {
    	if(gui != null) {
    		gui.updateTotalOwedDisplay();
    	
	    	String name = "";
	    	
	    	if (product instanceof BarcodedProduct) {
	    		BarcodedProduct barcodedProduct = (BarcodedProduct) product;
	    		name = barcodedProduct.getDescription();
	    	} else if (product instanceof PLUCodedProduct) {
	    		PLUCodedProduct pluCodedProduct = (PLUCodedProduct) product;
	    		name = pluCodedProduct.getDescription();
	    	}
	    	gui.addProductToCart(name, product.getPrice());
    	}
    }
    
    /**
     * Override of productRemoved from ProductsListener interface 
     * Notifies the gui that a product has been removed  
     */
    @Override
    public void productRemoved(Products productFacade, Product product) {
    	if(gui != null)
    		gui.updateTotalOwedDisplay();
    }
   
    /**
     * Override of productToBaggingArea from ProductsListener interface
     * Notifies the gui that a product must be added to the bagging area 
     */
    @Override
    public void productToBaggingArea(Products productFacade, Product product) {
    	if(gui != null)
    		gui.customerBaggingAreaPopUp(product);
    }

    /**
     * Override of bagsPurchased from ProductsListener interface
     * Notifies the gui that one or more reusable bags have been purchased
     */
    @Override
    public void bagsPurchased(Products productFacade, long totalCost) {
    	if(gui != null)
    		gui.addProductToCart("Reusable Bag", totalCost);
    }
}
