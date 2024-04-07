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

package com.thelocalmarketplace.software.product;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.jjjwelectronics.scale.ElectronicScaleListener;
import com.jjjwelectronics.scale.IElectronicScale;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

public class ScaleListener implements ElectronicScaleListener {
	// In order to access the hardware of the SelfCheckoutStation, use software.HARDWARE_YOU_WANNA_GET
	
	private SelfCheckoutStationSoftware software;
	private Products handler;
	
	public ScaleListener (SelfCheckoutStationSoftware software, Products handler) {
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
	public void theMassOnTheScaleHasChanged(IElectronicScale scale, Mass mass) {
		Mass actual;
	    Mass expected;
	    long tolerance;

	    try {
	    	AbstractElectronicScale allScales = (AbstractElectronicScale) scale;
	        actual = allScales.getCurrentMassOnTheScale();
	        expected = new Mass(software.getTotalOrderWeightInGrams());
	        tolerance = allScales.getSensitivityLimit().inMicrograms().longValue() / 2;
	        
	        System.out.println("actual: "+ actual + " expected: " + expected);

	        long actualInMicrograms = actual.inMicrograms().longValue();
	        long expectedInMicrograms = expected.inMicrograms().longValue();

	        // Calculate the absolute difference and compare it with the tolerance
	        long difference = Math.abs(actualInMicrograms - expectedInMicrograms);

	        if (difference <= tolerance) {
	            software.setStationUnblock();
	        }
	       
	        else {
	        	software.setStationBlock();
	        }
	    } catch (OverloadedDevice e) {
	        software.setStationBlock();
	    } 	
	}

	@Override
	public void theMassOnTheScaleHasExceededItsLimit(IElectronicScale scale) {
		software.setStationBlock();
		software.notifyUserOfOverload();
		
	}

	@Override
	public void theMassOnTheScaleNoLongerExceedsItsLimit(IElectronicScale scale) {
		software.setStationUnblock();	
	}
	
}
