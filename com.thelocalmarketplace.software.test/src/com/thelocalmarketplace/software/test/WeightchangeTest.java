package com.thelocalmarketplace.software.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.product.ScaleListener;

import powerutility.PowerGrid;

public class WeightchangeTest {
	 
	private mockScale scale;
	private mockScale scale1;
	private AbstractElectronicScale tehst;
	private SelfCheckoutStationSoftware station;
	private SelfCheckoutStationBronze checkoutSB;
	private ScaleListener listen;
	
	@Before
	public void setUp() {
		// we need scale first
		SelfCheckoutStationBronze.resetConfigurationToDefaults();
		this.checkoutSB = new SelfCheckoutStationBronze();
		this.checkoutSB.plugIn(PowerGrid.instance());
		this.checkoutSB.turnOn();
		this.station = new SelfCheckoutStationSoftware(checkoutSB);
		listen = new ScaleListener(station, null);
		
		scale = new mockScale(new Mass(6000000),new Mass (6000000));
		scale1 = new mockScale(new Mass(600000000000L),new Mass (60000000000l));
		this.scale.plugIn(PowerGrid.instance());
		this.scale.turnOn();
		
		
	}
	
	
	@Test
	public void unBlockTest() throws Exception {
		
    	//scale = new mockScale(new Mass(6000000),new Mass (6000000));
    	
        
    	 MockItem item1 = new MockItem(new Mass(100));
         MockItem item2 = new MockItem(new Mass(150));
         
         station.addItemToOrder(item1);
         station.addItemToOrder(item2);
         
         scale.addAnItem(item1);
         this.checkoutSB.getBaggingArea();
    	
        listen.theMassOnTheScaleHasChanged(scale,null);
          
        assertTrue(station.getStationBlock());     
        
        
        
            }
	
	@Test
	public void unBlockTestTolerance() throws Exception {
    	
		 
		 scale1.plugIn(PowerGrid.instance());
		 scale1.turnOn();
    	 MockItem item1 = new MockItem(new Mass(100));
         MockItem item2 = new MockItem(new Mass(150));
         
         station.addItemToOrder(item1);
         station.addItemToOrder(item2);
         
         scale1.addAnItem(item1);
         this.checkoutSB.getBaggingArea();
    	
        listen.theMassOnTheScaleHasChanged(scale1,null);
          
        assertFalse(station.getStationBlock());     

            }
	
	@Test
	public void unBlockCatchExceptionTest() throws OverloadedDevice{
		mockScale scale2 = new mockScale(new Mass(6),new Mass (6));
		scale2.plugIn(PowerGrid.instance());
		scale2.turnOn();
        MockItem item2 = new MockItem(new Mass(200000000000000000L));
   
        scale2.addAnItem(item2);
        listen.theMassOnTheScaleHasChanged(scale2,null);
        assertTrue(station.getStationBlock());    
 
            }
	
	
	
	
	

	@Test
	public void testTheMassOnTheScaleNoLongerExceedsItsLimit() {
		// Set the station to be blocked
		station.setStationBlock();
		listen.theMassOnTheScaleNoLongerExceedsItsLimit(scale);
		assertFalse(station.getStationBlock());
	}
	
	@Test
	public void testTheMassOnTheScaleHasExceededItsLimit() {
		listen.theMassOnTheScaleHasExceededItsLimit(scale);
		assertTrue(station.getStationBlock());
	
	}
	
	
	 class MockItem extends Item {
	        public MockItem(Mass mass) {
	            super(mass);
	        }
	    }   
}