package com.thelocalmarketplace.software.test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.software.AddownBag;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantPageGUI;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.CustomerStation;
import com.thelocalmarketplace.software.oldCode.AddOwnBag;
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
    	scale = new mockScale(new Mass(40000000),new Mass(40000000));
    	customerStation = new CustomerStation(0, station, scale, attendantGUI); 
    	attendantGUI = new AttendantPageGUI(); 
    	addownBag = new AddownBag(station, scale, customerStation, attendantGUI); 
    	
    	//SelfCheckoutStationBronze.resetConfigurationToDefaults();
		//this.checkoutSB = new SelfCheckoutStationBronze();
		//this.checkoutSB.plugIn(PowerGrid.instance());
		//this.checkoutSB.turnOn();
		//this.station = SelfCheckoutStationSoftware();
		//listen = new ScaleListener(station, null);
		
    	
    	this.scale.plugIn(PowerGrid.instance());
		this.scale.turnOn(); 
    	
		//Assert.assertNotNull("SelfCheckoutStationSoftware instance is null", station);
	
    }
    
    class MockItem extends Item {
        public MockItem(Mass mass) {
            super(mass);
        }
    }   
    
    /**
	 * Test for getBagWeight when a bag has been added to the scale. Creating a scale heavier than the order value 
	 * calling getBagWeight, using order mass and new mockscale mass created. 
	 * expected weight = bag weight, calculated from the scale and order weight difference
	 * @throws OverloadedDevice
	 */
	
	@Test 
	public void testGetBagWeight_BagAdded() throws OverloadedDevice {
		//the order has a weight of the mock scale in before (40000000)
		// weight of the scale with the order and the bag added is 10000000 (10 grams) more than the weight of the order
		// MockItem item1 = new MockItem(new Mass(100));
        
         MockItem item = new MockItem(new Mass(10000000)); 
        station.addItemToOrder(item);
        scale.addAnItem(item);
         //this.checkoutSB.getBaggingArea();
		
		
		mockScale orderAndBagScale = new mockScale(new Mass(5000000), new Mass(5000000)); 
		AddOwnBag addownBag = new AddOwnBag(station, orderAndBagScale);
		//bag weight being calculated
		double bagWeight = addownBag.getBagWeight(station, orderAndBagScale); 
		Assert.assertEquals(10.0, bagWeight, 10.0);
		
	}
 
	
	@Test 
	public void testGetBagWeight_noBagAdded() throws OverloadedDevice {
		
	}
	
	@Test 
	public void testAddBagWeight_OverThreshold () throws OverloadedDevice {
		
	}
	
	
	@Test
	public void testAddBagWeight_InThreshold() throws OverloadedDevice {
		
	}
	
	@Test 
	public void testPrint_mess() {
		ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
	    System.setOut(new PrintStream(outputStreamCaptor));
        addownBag.printMessage();
        Assert.assertEquals("You may now continue", outputStreamCaptor.toString().trim()); 
	}
	
	
}
