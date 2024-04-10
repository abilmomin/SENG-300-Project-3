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

import java.math.BigDecimal;
import java.math.BigInteger;
import com.jjjwelectronics.IDevice;  
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.jjjwelectronics.scale.ElectronicScaleListener;
import com.jjjwelectronics.scale.IElectronicScale;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantPageGUI;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.CustomerStation;


/**
 * The PersonalBag class allows for the customer to add their own bags 
 * Ensures they are of an acceptable weight, and calls for attendant if not
 */
public class PersonalBag implements ElectronicScaleListener {
	private final SelfCheckoutStationSoftware stationHardware;
    private final AbstractElectronicScale electronicScale;
    private final CustomerStation customerStation;
    private final AttendantPageGUI attendantGUI;
    
	/**
	 * Constructor for PersonalBag.
	 * 
	 * @param stationHardware
	 * 				The self-checkout station hardware
	 * 
	 * @param electronicScale
	 * 				The associated scale
	 * 
	 * @param customerStation
	 * 				The customer self-checkout station
	 * 
	 * @param attendantGUI
	 * 				The attendant station GUI
	 */
	public PersonalBag(SelfCheckoutStationSoftware stationHardware, AbstractElectronicScale electronicScale, CustomerStation customerStation, AttendantPageGUI attendantGUI) {
		this.stationHardware = stationHardware;
		this.electronicScale = electronicScale;
		this.customerStation = customerStation;
		this.attendantGUI = attendantGUI;
        theMassOnTheScaleHasChanged(electronicScale, null);
	}
			
	/**
	 * Override of theMassOnTheScaleHasChanged from ElectronicScaleListener.
	 * Notifies that a bag has been added to the scale.
	 */
	@Override
	public void theMassOnTheScaleHasChanged(IElectronicScale scale, Mass mass) {
		double bag_grams = getBagWeight(electronicScale);
		addBagWeight(electronicScale, bag_grams);
	}
		
	
	/**
	 * Calculates the weight of the bag by subtracting the order weight from the scale weight.
	 * 
	 * @param electronicScale
	 * 				The AbstractElectronicScale instance.
	 * 
	 * @return The weight of the bag.
	 */
	public double getBagWeight(AbstractElectronicScale electronicScale) {
		double orderWeight = stationHardware.getTotalOrderWeightInGrams(); 
		double bagWeight = 0;
		BigDecimal orderWeightDouble = new BigDecimal(Double.toString(orderWeight));
		BigDecimal scaleWeight;
		try {
			Mass getMass = electronicScale.getCurrentMassOnTheScale();
			scaleWeight = getMass.inGrams();
			BigDecimal bagWeightGrams = scaleWeight.subtract(orderWeightDouble);
			bagWeight = bagWeightGrams.doubleValue();  
		} catch (OverloadedDevice e) {
			e.printStackTrace();
		}
		
		return bagWeight;
	}
	
	/**
	 * Adds the weight of the bag to the total order weight and handles station blocking if necessary.
	 * 
	 * @param scale
	 * 				The AbstractElectronicScale instance.
	 * 
	 * @param weightOfBag 
	 * 				The weight of the bag.
	 *
	 */
	public void addBagWeight(AbstractElectronicScale scale, double weightOfBag) {
		BigInteger threshold = scale.getMassLimit().inMicrograms();
		try {
			int compareToThreshold = scale.getCurrentMassOnTheScale().compareTo(new Mass(threshold));
			
			if (compareToThreshold >= 0) {
				this.customerStation.customerPopUp("Bags too heavy, please wait for attendant.");
				stationHardware.setStationBlock();
				this.attendantGUI.bagdiscpreancydectected(stationHardware,this.customerStation);
			}
			else {
				stationHardware.setStationUnblock();  
				stationHardware.addTotalOrderWeightInGrams(weightOfBag);
				this.customerStation.customerPopUp("You may now continue");
			}
		} catch (OverloadedDevice e) {
			e.printStackTrace();
		}	
	}  
	
	// The below methods are unused, therefore have no JavaDoc.
	
	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {}
	
	@Override
	public void theMassOnTheScaleHasExceededItsLimit(IElectronicScale scale) {}

	@Override
	public void theMassOnTheScaleNoLongerExceedsItsLimit(IElectronicScale scale) {}
}
