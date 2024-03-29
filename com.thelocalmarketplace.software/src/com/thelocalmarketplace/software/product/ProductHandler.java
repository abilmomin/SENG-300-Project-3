package com.thelocalmarketplace.software.product;

import com.jjjwelectronics.bag.IReusableBagDispenser;
import com.jjjwelectronics.scale.IElectronicScale;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

public class ProductHandler {
	// Things to listen to (hardware)
	public SelfCheckoutStationSoftware software;
	public AbstractSelfCheckoutStation station;
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
	 * @param station
	 * 		The hardware of the self checkout station.
	 */
	public ProductHandler(SelfCheckoutStationSoftware software, AbstractSelfCheckoutStation station) {
		this.software = software;
		this.station = station;
		
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
	
	// all product logic goes here
	
	// In order to access the hardware of the SelfCheckoutStation, use software.HARDWARE_YOU_WANNA_GET()
	
	// bulky item code also goes in this class i can do it later. Button needed in GUI -Tara
	// request attendants attention also goes here. Button needed in GUI
	// safe to say most things go here
	
}
