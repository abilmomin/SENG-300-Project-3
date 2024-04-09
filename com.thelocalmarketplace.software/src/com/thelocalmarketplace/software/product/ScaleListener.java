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
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.jjjwelectronics.scale.ElectronicScaleListener;
import com.jjjwelectronics.scale.IElectronicScale;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;


/**
 * This class represents a listener for changes in an electronic scale attached to a self-checkout station,
 * managing events related to weight discrepancies and device status.
 */
public class ScaleListener implements ElectronicScaleListener {
	
	private SelfCheckoutStationSoftware software;
	
	/**
	 * Constructor for the listener.
	 * 
	 * @param software
	 * 				The instance of the self checkout station software.
	 * @param handler
	 * 				The handler, in this case Product Handler.
	 */
	public ScaleListener (SelfCheckoutStationSoftware software, Products handler) {
		this.software = software;	
	}
	
	/**
	 * Handles the event when the mass on the scale changes.
	 *
	 * @param scale 
	 * 				The electronic scale on which the event occurred.
	 * @param mass  
	 * 				The current mass measured by the scale.
	 */
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
	            software.setStationUnblock();
	        }
	       
	        else {
	        	software.setStationBlock();
	        	software.getGUI().customerPopUp("Weight Discrepency Detected, Adds or removes the item." );
	        	software.getGUI().getAttendantGUI().weightDiscpreancydNotify(software);
	        }
	    } catch (OverloadedDevice e) {
	        software.setStationBlock();
	    } 	
	}
	
	/**
	 * Handles the event when the mass on the scale exceeds its limit.
	 * It blocks the station and notifies the user of the overload.
	 *
	 * @param scale 
	 * 				The electronic scale on which the event occurred.
	 */
	@Override
	public void theMassOnTheScaleHasExceededItsLimit(IElectronicScale scale) {
		software.setStationBlock();
		software.notifyUserOfOverload();
		
	}

	/**
	 * Handles the event when the mass on the scale no longer exceeds its limit.
	 * It unblocks the station.
	 *
	 * @param scale 
	 * 				The electronic scale on which the event occurred.
	 */
	@Override
	public void theMassOnTheScaleNoLongerExceedsItsLimit(IElectronicScale scale) {
		software.setStationUnblock();	
	}
	
	/**
	 * Notifies when a device has been enabled.
	 * This method is invoked when a device has been enabled.
	 * 
	 * @param device 
	 * 			The device that has been enabled.
	 */
	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Notifies when a device has been disabled.
	 * This method is invoked when a device has been disabled.
	 * 
	 * @param device 
	 * 			The device that has been disabled.
	 */
	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Notifies when a device has been turned on.
	 * This method is invoked when a device has been turned on.
	 * 
	 * @param device 
	 * 			The device that has been turned on.
	 */
	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Notifies when a device has been turned off.
	 * This method is invoked when a device has been turned off.
	 * 
	 * @param device 
	 * 			The device that has been turned off.
	 */
	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}
}
