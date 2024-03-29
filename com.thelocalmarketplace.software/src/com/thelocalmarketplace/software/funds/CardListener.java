package com.thelocalmarketplace.software.funds;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.card.Card.CardData;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.jjjwelectronics.card.CardReaderListener;

public class CardListener implements CardReaderListener {
	// In order to access the hardware of the SelfCheckoutStation, use software.getHARDWARE_YOU_WANNA_GET()
	
	private SelfCheckoutStationSoftware software;
	private PaymentHandler handler;
	
	public CardListener(SelfCheckoutStationSoftware software, PaymentHandler handler) {
		this.software = software;
		this.handler = handler;	
	}

	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aCardHasBeenInserted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void theCardHasBeenRemoved() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aCardHasBeenTapped() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aCardHasBeenSwiped() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void theDataFromACardHasBeenRead(CardData data) {
		// TODO Auto-generated method stub
		
	}

}
