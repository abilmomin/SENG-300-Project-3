package com.thelocalmarketplace.software;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.jjjwelectronics.scale.ElectronicScaleListener;
import com.jjjwelectronics.scale.IElectronicScale;

public class ScaleListener implements ElectronicScaleListener {
	private SelfCheckoutStationSoftware software;
	
	public ScaleListener (SelfCheckoutStationSoftware software) {
		 this.software = software;
		
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
	public void theMassOnTheScaleHasChanged(IElectronicScale scale, Mass mass) {
		Mass actual;
	    Mass expected;
	    long tolerance;

	    try {
	    	AbstractElectronicScale allScales = (AbstractElectronicScale) scale;
	        actual = allScales.getCurrentMassOnTheScale();
	        expected = new Mass(software.getTotalOrderWeightInGrams());
	        tolerance = allScales.getSensitivityLimit().inMicrograms().longValue() / 2;

	        long actualInMicrograms = actual.inMicrograms().longValue();
	        long expectedInMicrograms = expected.inMicrograms().longValue();

	        // Calculate the absolute difference and compare it with the tolerance
	        long difference = Math.abs(actualInMicrograms - expectedInMicrograms);

	        if (difference <= tolerance) {
	            software.setStationBlock(false);
	        }
	    } catch (OverloadedDevice e) {
	        software.setStationBlock(true);
	    } 
		
	}

	@Override
	public void theMassOnTheScaleHasExceededItsLimit(IElectronicScale scale) {
		software.setStationBlock(true);
		software.notifyUserOfOverload();
		
	}

	@Override
	public void theMassOnTheScaleNoLongerExceedsItsLimit(IElectronicScale scale) {
		software.setStationBlock(false);	
	}
	
}
