package com.thelocalmarketplace.software.test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

import powerutility.PowerGrid;

public class MockScale extends AbstractElectronicScale {
	
	//mockScale constructor 
	protected Mass sensitivityLimit1;
	public MockScale(Mass limit, Mass sensitivityLimit) {
		super(limit, sensitivityLimit);
		sensitivityLimit1 = limit; 
		massLimit = limit; 
	}
	

	@Override 
	public Mass getMassLimit() {
		return massLimit;
	}
	
	
	@Override
	public Mass getSensitivityLimit() {
		return sensitivityLimit1; 
	}
	
	@Override 
	public synchronized void addAnItem(Item item) {
		currentMass = currentMass.sum(item.getMass());
		items.add(item);
	}
	
	private Mass calculateCurrentMass() {
		Mass newMass = Mass.ZERO; 
		for(Item item : items) {
			newMass = newMass.sum(item.getMass()); 
		}
		return newMass;
	}
	
	@Override
	    public Mass getCurrentMassOnTheScale() throws OverloadedDevice {
	       return currentMass; 
		}
	
		public void plugIn(PowerGrid grid) {
		
	}
		public void turnOn() {
		}
		public void enable() {
		}

}
