package com.thelocalmarketplace.software.funds;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.card.CardReaderListener;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

import java.util.HashSet;
import java.util.Set;

public class CardHandler {
    private SelfCheckoutStationSoftware stationSoftware;
    private Set<CardHandlerListener> listeners = new HashSet<>();

    public CardHandler(SelfCheckoutStationSoftware stationSoftware) {
        if (stationSoftware == null) {
            throw new IllegalArgumentException("The argument cannot be null");
        }
        this.stationSoftware = stationSoftware;
        stationSoftware.getStationHardware().getCardReader().register(new CardHandlerListener());
    }



//    public void register(CardHandlerListener listener) {
//        listeners.add(listener);
//    }
//
//    public void deregister(CardHandlerListener listener) {
//        listeners.add(listener);
//    }
}
