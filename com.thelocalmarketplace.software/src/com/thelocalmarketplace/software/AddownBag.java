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


public class AddownBag implements ElectronicScaleListener {
	
	private SelfCheckoutStationSoftware weight_order;
	private SelfCheckoutStationSoftware instance;
    private AbstractElectronicScale scale1;
    private Mass mass_test;
    private int station_number;
    
	
	//constructor
	public AddownBag(SelfCheckoutStationSoftware weight_order, AbstractElectronicScale scale1) {

        theMassOnTheScaleHasChanged(scale1, mass_test);
	}
			
	
	@Override
	public void theMassOnTheScaleHasChanged(IElectronicScale scale, Mass mass) {
		// TODO Auto-generated method stub
		double bag_grams = getBagWeight(weight_order, scale1); 
		addbagweight(weight_order, scale1, bag_grams,station_number); 
		}
		
	
	/** in this method order and scale are passed in, we get the total order weight convert to a big decimal
	 *  next we get the scale weight and we compare the order weight and the scale weight
	 *  subtract the two values, that value will equal the bag weight, if any errors catch and print message to console, and return bag weight
	 * @return
	 */
	public double getBagWeight(SelfCheckoutStationSoftware p1, AbstractElectronicScale p2 ) {  
		double order_weight = p1.getTotalOrderWeightInGrams(); 
		double bag_weight = 0;
		//get order weight
		BigDecimal order_weight_double = new BigDecimal(Double.toString(order_weight));
		BigDecimal scale_weight;
		try {
			//scale - order = bag weight
			Mass get_mass = p2.getCurrentMassOnTheScale();
			scale_weight = get_mass.inGrams();
			BigDecimal bag_weight_grams = scale_weight.subtract(order_weight_double);
			bag_weight = bag_weight_grams.doubleValue(); //convert to double 
		} catch (OverloadedDevice e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bag_weight;
	}
	
	
	/** In add bag weight, we pass in order, scale, and weight of bag, we first determine the mass limit
	 * and we compare the current mass on the scale to this limit, if there is a difference 
	 * print out bag to heavy and block the station, we then call attendant to deal with the problem, the attendant will fix it 
	 * next if there is no difference, we set station block to false add weight to order and print you may now continue
	 * if any exceptions are called catch and print message
	 * @param scale
	 * @param weight_of_bag
	 */
	// now that customer has signaled they want to add their own bags, pass in the weight of their own bags
	public void addbagweight(SelfCheckoutStationSoftware p1, AbstractElectronicScale scale, double weight_of_bag, int station_num) {
		
		//threshold = scale limit in mcg 
		BigInteger threshold = scale.getMassLimit().inMicrograms();
		
		try {
			//compare scale mass which bag mass to mass limit 
			int compare_to_threshold = scale.getCurrentMassOnTheScale().compareTo(new Mass(threshold));
			
			if (compare_to_threshold>=0) {
				System.out.println("Bags too heavy, not allowed");
				instance.setStationBlock(); // block station
				double order = p1.getTotalOrderWeightInGrams();
				AttendantPageGUI attendant_test = new AttendantPageGUI();
				attendant_test.bagdiscpreancydectected();
				
			}
			else {
				//bag weight is fine, add weight of bag to order, system unblocks
				instance.setStationUnblock();  // change to unblock and continue
				p1.addTotalOrderWeightInGrams(weight_of_bag);
				System.out.println("You may now continue");
			}

		} catch (OverloadedDevice e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}  
	
	public void print_mess() {
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