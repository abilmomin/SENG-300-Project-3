package com.thelocalmarketplace.software.test;

import java.util.List;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.printer.IReceiptPrinter;
import com.jjjwelectronics.printer.ReceiptPrinterListener;

import powerutility.PowerGrid;

public class MockReceiptPrinter implements IReceiptPrinter {

	@Override
	public boolean isPluggedIn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPoweredUp() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void plugIn(PowerGrid grid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unplug() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnOn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnOff() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean deregister(ReceiptPrinterListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void deregisterAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void register(ReceiptPrinterListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDisabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ReceiptPrinterListener> listeners() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void print(char c) throws EmptyDevice, OverloadedDevice {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cutPaper() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String removeReceipt() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addInk(int quantity) throws OverloadedDevice {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addPaper(int units) throws OverloadedDevice {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int paperRemaining() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int inkRemaining() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}