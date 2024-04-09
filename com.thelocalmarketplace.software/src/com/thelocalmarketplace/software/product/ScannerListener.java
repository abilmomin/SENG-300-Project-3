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

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodeScannerListener;
import com.jjjwelectronics.scanner.IBarcodeScanner;


/**
 * Listens for barcode scanner events and adds scanned items to the customer's order.
 */
public class ScannerListener implements BarcodeScannerListener {

	private final Products productHandler;

	/**
	 * Constructor for the listener.
	 *
	 * @param productHandler
	 * 				The handler, in this case Product Handler.
	 */
	public ScannerListener(Products productHandler) {
		this.productHandler = productHandler;
	}
	
	/**
	 * Adds an item to the customer's order when a barcode has been scanned
	 * 
	 * @param barcodeScanner
	 * 				The scanner that scanned the barcode.
	 * @param barcode
	 * 				The barcode that got scanned.
	 */
	@Override
	public void aBarcodeHasBeenScanned(IBarcodeScanner barcodeScanner, Barcode barcode) {
		productHandler.addItemViaBarcodeScan(barcode);
	}
	
	/**
	 * Notifies observers when a device has been enabled.
	 * 
	 * @param device 
	 * 			The device that has been enabled.
	 */
	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Notifies observers when a device has been disabled.
	 * 
	 * @param device 
	 * 			The device that has been disabled.
	 */
	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Notifies observers when a device has been turned on.
	 * 
	 * @param device 
	 * 			The device that has been turned on.
	 */
	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Notifies observers when a device has been turned off.
	 * 
	 * @param device 
	 * 			The device that has been turned off.
	 */
	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}
}
