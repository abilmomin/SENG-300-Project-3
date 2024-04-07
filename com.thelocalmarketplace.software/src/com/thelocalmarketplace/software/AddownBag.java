package com.thelocalmarketplace.software;


import java.math.BigDecimal;
import java.math.BigInteger;
import com.jjjwelectronics.IDevice;  
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.jjjwelectronics.scale.ElectronicScaleListener;
import com.jjjwelectronics.scale.IElectronicScale;
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantPageGUI;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.CustomerStation;


public class AddownBag implements ElectronicScaleListener {
	
	private SelfCheckoutStationSoftware weightOrder;
    private AbstractElectronicScale scale1;
    private Mass massTest;
    private int stationNumber;
    private CustomerStation customerStation;
    private AttendantPageGUI attendantGUI;
    
	
	//constructor
	public AddownBag(SelfCheckoutStationSoftware weightOrder, AbstractElectronicScale scale1, CustomerStation customerStation, AttendantPageGUI attendantGUI) {
		this.weightOrder = weightOrder;
		this.scale1 = scale1;
		this.customerStation = customerStation;
		this.attendantGUI = attendantGUI;
        theMassOnTheScaleHasChanged(scale1, massTest);
	}
			
	
	@Override
	public void theMassOnTheScaleHasChanged(IElectronicScale scale, Mass mass) {
		// TODO Auto-generated method stub
		double bag_grams = getBagWeight(weightOrder, scale1); 
		addBagWeight(weightOrder, scale1, bag_grams,stationNumber); 
		}
		
	
	/** in this method order and scale are passed in, we get the total order weight convert to a big decimal
	 *  next we get the scale weight and we compare the order weight and the scale weight
	 *  subtract the two values, that value will equal the bag weight, if any errors catch and print message to console, and return bag weight
	 * @return
	 */
	public double getBagWeight(SelfCheckoutStationSoftware p1, AbstractElectronicScale p2 ) {  
		double orderWeight = weightOrder.getTotalOrderWeightInGrams(); 
		double bagWeight = 0;
		//get order weight
		BigDecimal orderWeightDouble = new BigDecimal(Double.toString(orderWeight));
		BigDecimal scaleWeight;
		try {
			//scale - order = bag weight
			Mass getMass = p2.getCurrentMassOnTheScale();
			scaleWeight = getMass.inGrams();
			BigDecimal bagWeightGrams = scaleWeight.subtract(orderWeightDouble);
			bagWeight = bagWeightGrams.doubleValue(); //convert to double 
		} catch (OverloadedDevice e) {
			// TODO Auto-generated catch block
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
	// now that customer has signaled they want to add their own bags, pass in the weight of their own bags
	public void addBagWeight(SelfCheckoutStationSoftware p1, AbstractElectronicScale scale, double weightOfBag, int stationNum) {
		
		//threshold = scale limit in mcg 
		BigInteger threshold = scale.getMassLimit().inMicrograms();
		
		try {
			//compare scale mass which bag mass to mass limit 
			int compareToThreshold = scale.getCurrentMassOnTheScale().compareTo(new Mass(threshold));
			
			if (compareToThreshold>=0) {
				System.out.println("Bags too heavy, not allowed");
				weightOrder.setStationBlock(); // block station
				double order = weightOrder.getTotalOrderWeightInGrams();
				AttendantPageGUI attendantTest = new AttendantPageGUI();
				attendantTest.bagdiscpreancydectected(weightOrder);
				
			}
			else {
				//bag weight is fine, add weight of bag to order, system unblocks
				weightOrder.setStationUnblock();  // change to unblock and continue
				weightOrder.addTotalOrderWeightInGrams(weightOfBag);
				System.out.println("You may now continue");
			}

		} catch (OverloadedDevice e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}  
	
	public void printMessage() {
		System.out.print("You may now continue");
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
