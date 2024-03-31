package com.thelocalmarketplace.software.funds;

import com.jjjwelectronics.IDevice;

import com.jjjwelectronics.IDeviceListener;

import com.jjjwelectronics.card.Card.CardData;

import com.jjjwelectronics.card.CardReaderListener;

import com.thelocalmarketplace.hardware.external.CardIssuer;

import java.math.BigDecimal;

/**
 * CardHandler class which handles all transactions with debit and credit cards, including tap, swipe, and insert.
 */
public class CardHandler implements CardReaderListener {
    private final Funds fundController;

    /**
     * Constructor for CardHandler class
     * @param fundController the funds controller class
     */
    public CardHandler(Funds fundController) {
        if (fundController == null) {
            throw new IllegalArgumentException("The argument cannot be null");
        }
        this.fundController = fundController;
    }

    /**
     *  Function for approving a purchase using card and card issuer information
     * @param cardNumber The user's card number
     * @param amount Total amount to be charged
     */
    public boolean approvePurchase(String cardNumber, double amount) {
        for (CardIssuer bank : fundController.checkoutStationSoftware.getBanks()) {
            long holdNumber = bank.authorizeHold(cardNumber, amount);
            if (holdNumber != -1) 
                return bank.postTransaction(cardNumber, holdNumber, amount);
        }
        return false;
    }

    /**
     * Implemented method that uses the user's card information to process transaction
     * @param data The data of the user's card
     */
    @Override
    public void theDataFromACardHasBeenRead(CardData data) {
        PaymentKind.Kind cardType = PaymentKind.getCardType(data.getType());

        if (fundController.checkoutStationSoftware.getStationBlock()) {
            // Maybe return something from the gui??? Maybe raise an exception?
            return;
        }

        long totalOrderPrice = fundController.checkoutStationSoftware.getTotalOrderPrice();
        boolean purchaseStatus = approvePurchase(data.getNumber(), totalOrderPrice);

        if (purchaseStatus) {
            fundController.notifyPaidFunds(BigDecimal.ZERO);
        } else {
            fundController.notifyInvalidFunds(cardType);
        }

    }

    /**
     * Not used.
     */
    @Override
    public void aCardHasBeenInserted() {}

    /**
     * Not used.
     */
    @Override
    public void theCardHasBeenRemoved() {}

    /**
     * Not used.
     */
    @Override
    public void aCardHasBeenTapped() {}

    /**
     * Not used.
     */
    @Override
    public void aCardHasBeenSwiped() {}

    /**
     * Not used.
     */
    @Override
    public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {}

    /**
     * Not used.
     */
    @Override
    public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {}

    /**
     * Not used.
     */
    @Override
    public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {}

    /**
     * Not used.
     */
    @Override
    public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {}
}
