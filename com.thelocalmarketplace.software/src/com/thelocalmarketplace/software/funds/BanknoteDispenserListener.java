package com.thelocalmarketplace.software.funds;

import com.tdc.IComponent;
import com.tdc.IComponentObserver;
import com.tdc.banknote.Banknote;
import com.tdc.banknote.BanknoteDispenserObserver;
import com.tdc.banknote.IBanknoteDispenser;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

public class BanknoteDispenserListener implements BanknoteDispenserObserver {
	// In order to access the hardware of the SelfCheckoutStation, use software.getHARDWARE_YOU_WANNA_GET()
	
	private SelfCheckoutStationSoftware software;
	private PaymentHandler handler;
	
	public BanknoteDispenserListener(SelfCheckoutStationSoftware software, PaymentHandler handler) {
		this.software = software;
		this.handler = handler;	
	}

	@Override
	public void enabled(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disabled(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnedOn(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnedOff(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moneyFull(IBanknoteDispenser dispenser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void banknotesEmpty(IBanknoteDispenser dispenser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void banknoteAdded(IBanknoteDispenser dispenser, Banknote banknote) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void banknoteRemoved(IBanknoteDispenser dispenser, Banknote banknote) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void banknotesLoaded(IBanknoteDispenser dispenser, Banknote... banknotes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void banknotesUnloaded(IBanknoteDispenser dispenser, Banknote... banknotes) {
		// TODO Auto-generated method stub
		
	}

}
