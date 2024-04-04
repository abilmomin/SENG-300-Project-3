package com.thelocalmarketplace.software.test;

import java.math.BigDecimal;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

public class mockScale extends AbstractElectronicScale {

	
	// mockScale constructor 
	 protected mockScale(Mass limit, Mass sensitivityLimit) {
		super(limit, sensitivityLimit);
		// TODO Auto-generated constructor stub
	}

	@Override
	    public Mass getCurrentMassOnTheScale() {
	        // Stub implementation for getCurrentMassOnTheScale method
	        // Return a predefined Mass object for testing purposes
	        return new Mass(BigDecimal.valueOf(120)); // Stubbed mass
	    }

}





