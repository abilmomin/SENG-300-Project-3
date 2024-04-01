package com.thelocalmarketplace.software.funds;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.printer.IReceiptPrinter;
import com.jjjwelectronics.printer.ReceiptPrinterBronze;
import com.jjjwelectronics.printer.ReceiptPrinterListener;

public class ReceiptHandler implements ReceiptPrinterListener{
	private IReceiptPrinter receiptPrinter;
	
	public ReceiptHandler(IReceiptPrinter printer) {
        receiptPrinter = printer;
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

	@Override
	public void thePrinterIsOutOfPaper() {
		// TODO Auto-generated method stub
		
	}

	 @Override
	    public void thePrinterIsOutOfInk() {
	        try {
	            this.receiptPrinter.addInk(ReceiptPrinterBronze.MAXIMUM_INK);
	        } catch (OverloadedDevice e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }

	    }

	    @Override
	    public void thePrinterHasLowInk(){
	        try {
	            this.receiptPrinter.addInk(ReceiptPrinterBronze.MAXIMUM_INK - this.receiptPrinter.inkRemaining());
	        } catch (OverloadedDevice e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }

	    }

	@Override
	public void thePrinterHasLowPaper() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void paperHasBeenAddedToThePrinter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inkHasBeenAddedToThePrinter() {
		// TODO Auto-generated method stub
		
	}

}
