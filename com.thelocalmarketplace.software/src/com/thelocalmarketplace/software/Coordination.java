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

    public Coordination(SelfCheckoutStationSoftware software, Funds funds, Products products) {
        this.software = software;
        this.funds = funds;
        this.products = products;
        this.gui = software.getGUI();
    }

    @Override
    public void fundsAdded(Funds fundsFacade, BigDecimal funds) {
        
    }

    @Override
    public void fundsRemoved(Funds fundsFacade, BigDecimal funds) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fundsRemoved'");
    }

    @Override
    public void fundsStored(Funds fundsFacade, BigDecimal funds) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fundsStored'");
    }

    @Override
    public void fundsInvalid(Funds fundsFacade, Kind kind) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fundsInvalid'");
    }

    @Override
    public void fundsPaidInFull(Funds fundsFacade, BigDecimal changeReturned) {
        gui.setPaymentSuccesful(changeReturned.doubleValue());
    }

    @Override
    public void fundsStationBlocked(Funds fundsFacade, boolean blockedStatus) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fundsStationBlocked'");
    }
    
    @Override
    public void productAdded(Products productFacade, Product product) {
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
    
    @Override
    public void productRemoved(Products productFacade, Product product) {
    	
    }
    
    @Override
    public void productToBaggingArea(Products productFacade, Product product) {
    	CustomerStation gui = software.getGUI();
    	gui.customerBaggingAreaPopUp(product);
    }
}
