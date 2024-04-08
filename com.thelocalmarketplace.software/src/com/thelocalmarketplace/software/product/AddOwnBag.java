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


public class AddOwnBag implements ElectronicScaleListener {
	
	/**
	 * The AddOwnBag class allows for the customer to add their own bags 
	 * Ensures they are of an acceptable weight, and calls for attendant if not
	 */
	
	private SelfCheckoutStationSoftware stationHardware;
    private AbstractElectronicScale scale1;
    private Mass massTest;
    private int stationNumber;
    private CustomerStation customerStation;
    private AttendantPageGUI attendantGUI;
    
	/**
	 * Constructor for AddOwnBag
	 * 
	 * @param stationHardware
	 * 			The self checkout station hardware
	 * @param scale1
	 * 			The associated scale
	 * @param customerStation
	 * 			The customer self checkout station
	 * @param attendantGUI
	 * 			The attendant station gui
	 */
	public AddOwnBag(SelfCheckoutStationSoftware stationHardware, AbstractElectronicScale scale1, CustomerStation customerStation, AttendantPageGUI attendantGUI) {
		this.stationHardware = stationHardware;
		this.scale1 = scale1;
		this.customerStation = customerStation;
		this.attendantGUI = attendantGUI;
        theMassOnTheScaleHasChanged(scale1, massTest);
	}
			
	/**
	 * Override of theMassOnTheScaleHasChanged from ElectronicScaleListener
	 * Notifies that a bag has been added to the scale
	 */
	@Override
	public void theMassOnTheScaleHasChanged(IElectronicScale scale, Mass mass) {
		double bag_grams = getBagWeight(stationHardware, scale1); 
		addBagWeight(stationHardware, scale1, bag_grams,stationNumber); 
		}
		
	
	/** in this method order and scale are passed in, we get the total order weight convert to a big decimal
	 *  next we get the scale weight and we compare the order weight and the scale weight
	 *  subtract the two values, that value will equal the bag weight, if any errors catch and print message to console, and return bag weight
	 * @return
	 */
	public double getBagWeight(SelfCheckoutStationSoftware p1, AbstractElectronicScale p2 ) {  
		double orderWeight = stationHardware.getTotalOrderWeightInGrams(); 
		double bagWeight = 0;
		BigDecimal orderWeightDouble = new BigDecimal(Double.toString(orderWeight));
		BigDecimal scaleWeight;
		try {
			Mass getMass = p2.getCurrentMassOnTheScale();
			scaleWeight = getMass.inGrams();
			BigDecimal bagWeightGrams = scaleWeight.subtract(orderWeightDouble);
			bagWeight = bagWeightGrams.doubleValue();  
		} catch (OverloadedDevice e) {
			e.printStackTrace();
		}
		
		return bagWeight;
	}
	
	
	/** In add bag weight, we pass in order, scale, and weight of bag, we first determine the mass limit
	 * and we compare the current mass on the scale to this limit, if there is a difference 
	 * print out bag to heavy and block the station, we then call attendant to deal with the problem, the attendant will fix it 
	 * next if there is no difference, we set station block to false add weight to order and print you may now continue
	 * if any exceptions are called catch and print message
	 * @param scale
	 * @param weightOfBag
	 */
	public void addBagWeight(SelfCheckoutStationSoftware p1, AbstractElectronicScale scale, double weightOfBag, int stationNum) {		
		BigInteger threshold = scale.getMassLimit().inMicrograms();
		
		try {
			int compareToThreshold = scale.getCurrentMassOnTheScale().compareTo(new Mass(threshold));
			
			if (compareToThreshold>=0) {
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
	public void theMassOnTheScaleHasExceededItsLimit(IElectronicScale scale) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void theMassOnTheScaleNoLongerExceedsItsLimit(IElectronicScale scale) {
		// TODO Auto-generated method stub
		
	}
		
			
	
}
