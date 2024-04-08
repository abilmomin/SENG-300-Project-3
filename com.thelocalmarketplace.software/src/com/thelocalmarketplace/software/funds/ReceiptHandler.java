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

package com.thelocalmarketplace.software.funds;

import com.jjjwelectronics.IDevice;

import com.jjjwelectronics.IDeviceListener;

import com.jjjwelectronics.OverloadedDevice;

import com.jjjwelectronics.printer.IReceiptPrinter;

import com.jjjwelectronics.printer.ReceiptPrinterBronze;

import com.jjjwelectronics.printer.ReceiptPrinterListener;
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantLogic;

/**
 * This class is a listener which implements and overrides the receipt printer listener in the hardware.
 */
public class ReceiptHandler implements ReceiptPrinterListener{
	private IReceiptPrinter receiptPrinter;
	private Receipt receipt;
	AttendantLogic logic = new AttendantLogic();


	public ReceiptHandler(Receipt receipt) {
		receiptPrinter = receipt.receiptPrinter;
		this.receipt = receipt;
	}

	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub

	}


	/**
	 * Overrides the hardware listener's out of paper function
	 */

	@Override
	public void thePrinterIsOutOfPaper() {
		this.receipt.notifyPaperEmpty(receiptPrinter);
	}

	/**
	 * Overrides the hardware listener's out of ink function
	 */
	@Override
	public void thePrinterIsOutOfInk() {
		this.receipt.notifyInkEmpty(receiptPrinter);
	}


	@Override
	public void thePrinterHasLowInk(){
		this.receipt.notifyInkLow(receiptPrinter);

	}

	@Override
	public void thePrinterHasLowPaper() {
		this.receipt.notifyPaperLow(receiptPrinter);


	}

	@Override
	public void paperHasBeenAddedToThePrinter() {
		this.receipt.notifyPaperAdded(receiptPrinter);

	}

	@Override
	public void inkHasBeenAddedToThePrinter() {
		this.receipt.notifyInkAdded(receiptPrinter);

	}

}
