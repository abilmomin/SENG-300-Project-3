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

package com.thelocalmarketplace.software.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.OverloadedDevice;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;

import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.product.ScaleListener;

import powerutility.PowerGrid;

public class WeightchangeTest {
	private MockScale scale;
	private MockScale scale1;
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
		
		scale = new MockScale(new Mass(6000000),new Mass (6000000));
		scale1 = new MockScale(new Mass(600000000000L),new Mass (60000000000l));
		this.scale.plugIn(PowerGrid.instance());
		this.scale.turnOn();
	}
	
	@Test
	public void unBlockTest() throws Exception {
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
		MockScale scale2 = new MockScale(new Mass(6),new Mass (6));
		scale2.plugIn(PowerGrid.instance());
		scale2.turnOn();
        MockItem item2 = new MockItem(new Mass(200000000000000000L));
   
        scale2.addAnItem(item2);
        listen.theMassOnTheScaleHasChanged(scale2,null);
        assertTrue(station.getStationBlock());    
    }

	@Test
	public void testTheMassOnTheScaleNoLongerExceedsItsLimit() {
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