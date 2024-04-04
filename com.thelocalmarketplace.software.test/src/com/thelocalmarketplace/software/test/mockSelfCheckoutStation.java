package com.thelocalmarketplace.software.test;

import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

public class mockSelfCheckoutStation {
	public class StubSelfCheckoutStationSoftware extends SelfCheckoutStationSoftware {

	    public StubSelfCheckoutStationSoftware(AbstractSelfCheckoutStation station) {
			super(station);
			// TODO Auto-generated constructor stub
		}

		@Override
	    public double getTotalOrderWeightInGrams() {
	        // Stub implementation for getTotalOrderWeightInGrams method
	        // Return a predefined value for testing purposes
	        return 100.0;
	    }
	}
}
