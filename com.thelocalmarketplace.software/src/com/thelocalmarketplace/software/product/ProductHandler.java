package com.thelocalmarketplace.software.product;

import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.bag.IReusableBagDispenser;
import com.jjjwelectronics.scale.IElectronicScale;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.ISelfCheckoutStation;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.oldCode.Order;

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
		software.setStationBlock(true);
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
	 * @param plucode
	 */
	
	public void addItemByPLUCode(String plucode) {
		if (software.getStationActive() && !software.getStationBlock()) {
			software.setStationBlock(true);
		
			PriceLookUpCode PLUcode = new PriceLookUpCode(plucode);
			PLUCodedProduct product = ProductDatabases.PLU_PRODUCT_DATABASE.get(PLUcode);
			
			
//			if (product != null) {
//				double productWeight = product.get...
			
//			} NOT COMPLETE, need to talk to TA
			
		}
		
	}
	

	// all product logic goes here
	
	// In order to access the hardware of the SelfCheckoutStation, use software.HARDWARE_YOU_WANNA_GET()
	
	// request attendants attention also goes in communication, but where? idk. Button needed in GUI
	// safe to say most things go here for group 4 :))
}
