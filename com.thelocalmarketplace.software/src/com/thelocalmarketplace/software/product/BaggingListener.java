package com.thelocalmarketplace.software.product;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.bag.ReusableBagDispenserListener;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

public class BaggingListener implements ReusableBagDispenserListener {
	// In order to access the hardware of the SelfCheckoutStation, use software.getHARDWARE_YOU_WANNA_GET()
	
	private SelfCheckoutStationSoftware software;
	private ProductHandler handler;
	
	public BaggingListener(SelfCheckoutStationSoftware software, ProductHandler handler) {
		this.software = software;
		this.handler = handler;	
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
	public void aBagHasBeenDispensedByTheDispenser() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void theDispenserIsOutOfBags() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bagsHaveBeenLoadedIntoTheDispenser(int count) {
		// TODO Auto-generated method stub
		
	}

}
