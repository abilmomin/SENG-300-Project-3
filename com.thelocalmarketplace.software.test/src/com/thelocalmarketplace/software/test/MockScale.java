package com.thelocalmarketplace.software.test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

public class MockScale extends AbstractElectronicScale {

	
	// mockScale constructor 
	 protected MockScale(Mass limit, Mass sensitivityLimit) {
		super(limit, sensitivityLimit);
		this.currentMass = currentMass; // just added this idk? 
		// TODO Auto-generated constructor stub
	}

	@Override
	    public Mass getCurrentMassOnTheScale() throws OverloadedDevice {
	        // Stub implementation for getCurrentMassOnTheScale method
	        // Return a predefined Mass object for testing purposes
	        
	        if(currentMass.compareTo(massLimit) <= 0) {
	        	 return new Mass(BigDecimal.valueOf(120)); // Stubbed mass
			}

			throw new OverloadedDevice();
		}
	    }
	







