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

import com.thelocalmarketplace.software.product.AddOwnBag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.scale.AbstractElectronicScale;

import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;

import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantPageGUI;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.CustomerStation;

import powerutility.PowerGrid;

public class AddOwnBagTest {
	PowerGrid grid;
	private SelfCheckoutStationSoftware station; 
	private SelfCheckoutStationBronze checkoutStationBronze;
	private AbstractElectronicScale scale; 
    private CustomerStation customerStation; 
    private AttendantPageGUI attendantGUI; 
    private Mass massLimit; 
    
    @Before
    public void setUp() {
    	grid = PowerGrid.instance();
		PowerGrid.engageUninterruptiblePowerSource();
		grid.forcePowerRestore();
    	checkoutStationBronze = new SelfCheckoutStationBronze();
		checkoutStationBronze.plugIn(grid);
		checkoutStationBronze.turnOn();
		
    	station = new SelfCheckoutStationSoftware(checkoutStationBronze); 
    	station.setStationActive(true);
    	scale = new MockScale(new Mass(40000000),new Mass(40000000));
    	customerStation = new CustomerStation(0, station, scale, attendantGUI); 
    	attendantGUI = new AttendantPageGUI(); 
    	massLimit = scale.getMassLimit(); 
    	scale.plugIn(grid);
		scale.turnOn();
   
    	this.scale.plugIn(PowerGrid.instance());
		this.scale.turnOn(); 
    }    
    
   /**
    * Testing when a bag above the weight threshold is added. The item instance has a mass higher than the scale mass 
    * expected station block from the bag being over the threshold limit 
    * @throws OverloadedDevice
    */
	@Test 
	public void testAddBagWeight_OverThreshold() throws OverloadedDevice {
		MockScale scaleOverLimit = new MockScale(new Mass(40000000), new Mass(40000000)); 
		MockItem item = new MockItem(new Mass(50000000)); 
		//adding a new item to exceed the mass limit 
		station.addItemToOrder(item);
        scale.addAnItem(item);
        AddOwnBag addownBag = new AddOwnBag(station, scale, customerStation, attendantGUI);
		//bag weight being calculated
		addownBag.addBagWeight(station, scaleOverLimit, 1000000, 1);
        assertFalse(station.getStationBlock());	
	}
	
	/**
	 * Testing the circumstance of adding a bag that is within the threshold weight of the scale. 
	 * System is expected to remain unblocked 
	 * @throws OverloadedDevice
	 */
	@Test
	public void testAddBagWeight_UnderThreshold() throws OverloadedDevice {
		MockScale scaleInLimit = new MockScale(new Mass(40000000), new Mass(40000000));
		// adding an item to the scale that is within the mass limit weight
		MockItem item = new MockItem(new Mass(30000000)); 
		station.addItemToOrder(item);
		scale.addAnItem(item);
		
		AddOwnBag addownBag = new AddOwnBag(station, scale, customerStation, attendantGUI);
		addownBag.addBagWeight(station, scaleInLimit, 1000000, 1);
		assertFalse(station.getStationBlock()); 
	}
	
	/**
	 * Testing the circumstance of no bag being added and making sure that
	 * an expected bag weight of 0.0 is calculated. The scale and station will be the same weight 
	 * @throws OverloadedDevice
	 */
	@Test 
	public void testGetBagWeight_noBagAdded() throws OverloadedDevice {
		MockScale orderNoBagScale = new MockScale(new Mass(4000000), new Mass (4000000));
		AddOwnBag addOwnBag = new AddOwnBag(station, orderNoBagScale, customerStation, attendantGUI);
		double bagWeight = addOwnBag.getBagWeight(station, orderNoBagScale);
		assertEquals(0.0, bagWeight, 0.0); //120.0? 
	}
	
	/**
	 * Testing the circumstance when a bag is added. The scale is heavier than the station indicating addition of a bag. 
	 * making sure the correct bag weight is being calculated 
	 * @throws OverloadedDevice
	 */
	
	@Test 
	public void testGetBagWeight_BagAdded () throws OverloadedDevice {
		MockScale orderAndBagScale = new MockScale(new Mass(50000000), new Mass(50000000)); 
		//adding bag (1 gram) to the over limit scale 
		AddOwnBag addownBag = new AddOwnBag(station, orderAndBagScale, customerStation, attendantGUI); 
		double bagWeight = addownBag.getBagWeight(station, orderAndBagScale); 
		assertEquals(10.0, bagWeight, 10.0); //there should be weight becasue the bag has been added
	}
	
	//Overload Test 1
	@Test 
	public void testOverloadThrown_1() {
		MockScale scale = new MockScale(massLimit, massLimit) {
			@Override
			public Mass getCurrentMassOnTheScale() throws OverloadedDevice{
				throw new OverloadedDevice(); 
			}
		};
		AddOwnBag addownBag = new AddOwnBag(station,scale, customerStation, attendantGUI); 
		double bagWeight = addownBag.getBagWeight(station, scale); 
		Assert.assertEquals(0.0, bagWeight, 0.001);
	}

	//Overload Test 2
	@Test 
	public void testOverloadThrown_2() {
		MockScale scale = new MockScale(massLimit, massLimit) {
			@Override 
			public Mass getCurrentMassOnTheScale() throws OverloadedDevice {
				throw new OverloadedDevice();
				
			}
			
			@Override 
			public Mass getMassLimit() {
				return new Mass(BigInteger.valueOf(1000000)); 
			}
		};
		double weightOfBag = 50.0;
		AddOwnBag addownBag = new AddOwnBag(station, scale, customerStation, attendantGUI);
		addownBag.addBagWeight(station, scale, weightOfBag, 1);
			assertFalse(false); 
	}
	
	/**
	 * creating a mock of Item for testing purposes. MockItem Extends the Item class 
	 */
	class MockItem extends Item { // need power for addAnItem
        public MockItem(Mass mass) {
            super(mass);
        }
	}
}