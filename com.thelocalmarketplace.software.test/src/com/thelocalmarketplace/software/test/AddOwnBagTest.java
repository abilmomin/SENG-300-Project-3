package com.thelocalmarketplace.software.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.OverloadedDevice;
import com.thelocalmarketplace.software.AddownBag;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.oldCode.AddOwnBag;

public class AddOwnBagTest {
	private AddownBag addownBag;
    private mockScale scale;
    private SelfCheckoutStationSoftware order_weight; 
    
    @Before
    public void setUp() {
    	scale = new mockScale(new Mass(40000000),new Mass(40000000));
    	// order_weight = new SelfCheckoutStationSoftware(new Mass(40000000)); 
    	addownBag = new AddownBag(order_weight, scale); 
    	
    }
    
    /**
	 * Test for getBagWeight when a bag has been added to the scale. Creating a scale heavier than the order value 
	 * calling getBagWeight, using order mass and new mockscale mass created. 
	 * expected weight = bag weight, calculated from the scale and order weight difference
	 * @throws OverloadedDevice
	 */
	
	@Test 
	public void testGetBagWeightBagAdded() throws OverloadedDevice {
		//the order has a weight of the mock scale in before (40000000)
		// weight of the scale with the order and the bag added is 10000000 (10 grams) more than the weight of the order
		mockScale orderAndBagScale = new mockScale(new Mass(5000000), new Mass(5000000)); 
		AddOwnBag addOwnBag = new AddOwnBag(order_weight, orderAndBagScale);
		//bag weight being calculated
		double bagWeight = addOwnBag.getBagWeight(order_weight, orderAndBagScale); 
		Assert.assertEquals(10.0, bagWeight, 10.0);
	}
 

}
