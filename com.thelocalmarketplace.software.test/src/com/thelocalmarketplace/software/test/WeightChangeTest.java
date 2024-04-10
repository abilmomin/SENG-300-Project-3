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
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;

import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantPageGUI;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.CustomerStation;
import com.thelocalmarketplace.software.product.ScaleListener;

import powerutility.PowerGrid;

public class WeightChangeTest {
	private MockScale scale;
	private MockScale scale1;
	private SelfCheckoutStationSoftware station;
	private SelfCheckoutStationBronze checkoutSB;
	private ScaleListener listen;

	/**
	 * Create set up that will be called before every test, intalize selfcheckoutstationbronze and turn it on.
	 * Initialze scalelistener and mockscale and turn these on. 
	 */

	@Before
	public void setUp() {
		SelfCheckoutStationBronze.resetConfigurationToDefaults();
		this.checkoutSB = new SelfCheckoutStationBronze();
		this.checkoutSB.plugIn(PowerGrid.instance());
		this.checkoutSB.turnOn();
		this.station = new SelfCheckoutStationSoftware(checkoutSB);
		listen = new ScaleListener(station);
		scale = new MockScale(new Mass(6000000),new Mass (6000000));
		scale1 = new MockScale(new Mass(600000000000L),new Mass (60000000000L));
		scale1 = new MockScale(new Mass(10),new Mass (10));
		PowerGrid.engageUninterruptiblePowerSource();
		this.scale.plugIn(PowerGrid.instance());
		this.scale.turnOn();
	}
	
	/**
	 * Create test that's adds 2 items to the scale but only one to the order, throws exception.
	 */

	@Test
	public void unBlockTest() {
		MockItem item1 = new MockItem(new Mass(100));
		MockItem item2 = new MockItem(new Mass(150));
		
		station.addItemToOrder(item1);
		station.addItemToOrder(item2);
		
		scale.addAnItem(item1);

        listen.theMassOnTheScaleHasChanged(scale,null);
          
        assertFalse(station.getStationBlock());     
    }
	
	
	/**
	 * create test that has a very low scale limit and sensitivty then add item that is much larger.
	 * Causes weight discrepancy and throws OverloadedDevice error.
	 */

	@Test
	public void unBlockCatchExceptionTest() {
		
		MockScale scale2 = new MockScale(new Mass(6),new Mass (6));
		scale1.plugIn(PowerGrid.instance());
		scale1.turnOn();
		CustomerStation cus_station = new CustomerStation(0, station, scale, null);
        MockItem item2 = new MockItem(new Mass(200000000000000000L));
        scale1.plugIn(PowerGrid.instance());
		scale1.turnOn();
        cus_station.customerPopUp("weight change");
        scale2.addAnItem(item2);
        AttendantPageGUI A_station = new AttendantPageGUI();
        station.setAGUI(A_station);
        cus_station.setAttendantGUI(A_station);
        A_station.weightDiscpreancydNotify(station);
        cus_station.getAttendantGUI().weightDiscpreancydNotify(station);
        listen.theMassOnTheScaleHasChanged(scale2,null);
        assertFalse(station.getStationBlock());    
    }

	/**
	 * Create test that tests that the station is blocked and after calling fuction should be not blocked.
	 */

	@Test
	public void testTheMassOnTheScaleNoLongerExceedsItsLimit() {
		station.setStationBlock();
		listen.theMassOnTheScaleNoLongerExceedsItsLimit(scale);
		assertFalse(station.getStationBlock());
	}
	
	/**
	 * Create test that sets a low scale limit, add ensure that a pop-up occurs and station should be blocked.
	 */

	@Test
	public void testTheMassOnTheScaleHasExceededItsLimit() {
		
		MockScale scale5 = new MockScale(new Mass(6),new Mass (6));
		SelfCheckoutStationSoftware test1 = new SelfCheckoutStationSoftware(checkoutSB);
		AttendantPageGUI A_station1 = new AttendantPageGUI();
		ScaleListener listen2 = new ScaleListener(test1);
		scale1.plugIn(PowerGrid.instance());
		scale1.turnOn();
		CustomerStation cus_Station_Test = new CustomerStation(0, test1, scale5, null);
	    cus_Station_Test.setAttendantGUI(A_station1);
		test1.setGUI(cus_Station_Test);
		test1.getGUI().customerPopUp("test");
		test1.notifyUserOfOverload();
		listen2.theMassOnTheScaleHasExceededItsLimit(scale5);
		assertTrue(test1.getStationBlock());
		
	}
	
	/**
	 * MockItem class created to be used in the tests.
	 */

	static class MockItem extends Item {
		public MockItem(Mass mass) {
			super(mass);
		}
	}   
}