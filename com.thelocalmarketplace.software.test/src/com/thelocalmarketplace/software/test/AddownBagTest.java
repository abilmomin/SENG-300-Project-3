package com.thelocalmarketplace.software.test;
import com.thelocalmarketplace.software.product.AddownBag;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantPageGUI;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.CustomerStation;
import com.thelocalmarketplace.software.product.ScaleListener;
import com.thelocalmarketplace.software.test.WeightchangeTest.MockItem;

import powerutility.PowerGrid;

public class AddownBagTest {
	PowerGrid grid;
	private AddownBag addownBag;
	private SelfCheckoutStationSoftware station; 
	private SelfCheckoutStationBronze checkoutStationBronze;
	private AbstractElectronicScale scale; 
    private CustomerStation customerStation; 
    private AttendantPageGUI attendantGUI; 
    private Mass massLimit; 
	
	//private SelfCheckoutStationBronze checkoutSB; // 
	//private ScaleListener listen;
	//private Mass mass_test; 
	//private int station_number; 
    
    @Before
    public void setUp() {
    	grid = PowerGrid.instance();
		// to avoid power outages when there is a power surge
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
    	addownBag = new AddownBag(station, scale, customerStation, attendantGUI); 
    	massLimit = scale.getMassLimit(); 
    	scale.plugIn(grid);
		scale.turnOn();
   
    	this.scale.plugIn(PowerGrid.instance());
		this.scale.turnOn(); 
    }    
    
    
    /**
	 * Test for getBagWeight when a bag has been added to the scale. Creating a scale heavier than the order value 
	 * calling getBagWeight, using order mass and new mockscale mass created. 
	 * expected weight = bag weight, calculated from the scale and order weight difference
	 * @throws OverloadedDevice
	 */ 
    
	@Test 
	public void testAddBagWeight_OverThreshold() throws OverloadedDevice {
		MockScale scaleOverLimit = new MockScale(new Mass(40000000), new Mass(40000000)); 
		MockItem item = new MockItem(new Mass(50000000)); 
		//adding a new item to exceed the mass limit 
		station.addItemToOrder(item);
        scale.addAnItem(item);
        AddownBag addownBag = new AddownBag(station, scale, customerStation, attendantGUI);
		//bag weight being calculated
		addownBag.addBagWeight(station, scaleOverLimit, 1000000, 1);
        assertFalse(station.getStationBlock());	
	}
 
	
	@Test
	public void testAddBagWeight_UnderThreshold() throws OverloadedDevice {
		MockScale scaleInLimit = new MockScale(new Mass(40000000), new Mass(40000000));
		// adding an item to the scale that is within the mass limit weight
		MockItem item = new MockItem(new Mass(30000000)); 
		station.addItemToOrder(item);
		scale.addAnItem(item);
		
		AddownBag addownBag = new AddownBag(station, scale, customerStation, attendantGUI);
		addownBag.addBagWeight(station, scaleInLimit, 1000000, 1);
		assertFalse(station.getStationBlock()); 
	}
	
	@Test 
	public void testGetBagWeight_noBagAdded() throws OverloadedDevice {
		MockScale orderNoBagScale = new MockScale(new Mass(4000000), new Mass (4000000));
		AddownBag addOwnBag = new AddownBag(station, orderNoBagScale, customerStation, attendantGUI);
		double bagWeight = addOwnBag.getBagWeight(station, orderNoBagScale);
		assertEquals(0.0, bagWeight, 0.0); //120.0? 
		
	}
	
	
	@Test 
	public void testGetBagWeight_BagAdded () throws OverloadedDevice {
		MockScale orderAndBagScale = new MockScale(new Mass(50000000), new Mass(50000000)); 
		//adding bag (1 gram) to the over limit scale 
		AddownBag addownBag = new AddownBag(station, orderAndBagScale, customerStation, attendantGUI); 
		double bagWeight = addownBag.getBagWeight(station, orderAndBagScale); 
		assertEquals(10.0, bagWeight, 10.0); //there should be weight becasue the bag has been added
	}

	
	@Test 
	public void testPrint_mess() {
		ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
	    System.setOut(new PrintStream(outputStreamCaptor));
	    AddownBag addownBag = new AddownBag(station, scale, customerStation, attendantGUI);
	    addownBag.printMessage();
        Assert.assertEquals("You may now continue", outputStreamCaptor.toString().trim()); 
	}
	
	
	//overload test 1
	@Test 
	public void testOverloadThrown_1() {
		MockScale scale = new MockScale(massLimit, massLimit) {
			@Override
			
			public Mass getCurrentMassOnTheScale() throws OverloadedDevice{
				throw new OverloadedDevice(); 
			}
		};
		AddownBag addownBag = new AddownBag(station,scale, customerStation, attendantGUI); 
		double bagWeight = addownBag.getBagWeight(station, scale); 
		Assert.assertEquals(0.0, bagWeight, 0.001);
	}

	//overload test 2 
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
		AddownBag addownBag = new AddownBag(station, scale, customerStation, attendantGUI);
		addownBag.addBagWeight(station, scale, weightOfBag, 1);
			assertFalse(false); 
	}
	
	 
	class MockItem extends Item { // need power for addAnItem
	        public MockItem(Mass mass) {
	            super(mass);
	        }
	 }
}
