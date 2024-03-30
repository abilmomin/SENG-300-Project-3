package com.thelocalmarketplace.software.funds;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.card.CardReaderListener;

public class CardHandlerListener implements CardReaderListener {
    @Override
    public void aCardHasBeenInserted() {}
    @Override
    public void theCardHasBeenRemoved() {}
    @Override
    public void aCardHasBeenTapped() {}
    @Override
    public void aCardHasBeenSwiped() {}
    @Override
    public void theDataFromACardHasBeenRead(Card.CardData data) {}
    @Override
    public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {}
    @Override
    public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {}
    @Override
    public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {}
    @Override
    public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {}
}