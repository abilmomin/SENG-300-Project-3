package com.thelocalmarketplace.software.funds;

import com.jjjwelectronics.EmptyDevice;

import com.jjjwelectronics.IDevice;

import com.jjjwelectronics.IDeviceListener;

import com.jjjwelectronics.OverloadedDevice;

import com.jjjwelectronics.card.Card;

import com.jjjwelectronics.card.Card.CardData;

import com.jjjwelectronics.card.CardReaderListener;

import com.jjjwelectronics.card.MagneticStripeFailureException;

import com.thelocalmarketplace.hardware.external.CardIssuer;

import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

import java.io.IOException;

import java.math.BigDecimal;

import java.util.HashSet;

import java.util.Set;

public class CardHandler implements CardReaderListener {
    private Funds fundController = null;

    public CardHandler(Funds fundController) {
        if (this.fundController == null) {
            throw new IllegalArgumentException("The argument cannot be null");
        }
        this.fundController = fundController;
    }

    /**
	 * Processes a credit card payment via swipe.
	 *
	 * @param card         The credit card to be used for payment.
	 * @param amountCharged The amount to be charged to the credit card.
	 * @param cardIssuer   The card issuer responsible for authorizing the transaction.
	 * @throws IOException          If an I/O error occurs.
	 * @throws EmptyDevice          If the checkout station device is empty.
	 * @throws OverloadedDevice     If the checkout station device is overloaded.
	 */
	public int payWithCreditViaSwipe(Card card, double amountCharged, CardIssuer cardIssuer) throws IOException {
		try {
			if (stationSoftware.getStationBlock()) {
				System.out.println("Blocked. Please add your item to the bagging area.");
				return -1;
			}

			CardData data = cardReader.swipe(card);

			long holdNumber = cardIssuer.authorizeHold(data.getNumber(), amountCharged);
			if (holdNumber == -1) {
				// HOLD FAILED
				System.out.println("The hold on the card failed. Please try again.");
				return -1;
			}
			boolean transaction = cardIssuer.postTransaction(data.getNumber(), holdNumber, amountCharged);
			if (!transaction) {
				// TRANSACTION FAILED
				cardIssuer.releaseHold(data.getNumber(), holdNumber); // Remove the hold.
				System.out.println("The transaction failed. Please try again.");
				return -1;
			}
			stationSoftware.removeTotalOrderPrice((long) amountCharged); // Update the total amount due to the customer
			amountSpent = BigDecimal.valueOf(amountCharged);
			changeRemaining = BigDecimal.ZERO;
			// Receipt printing is handled inside the demo
			return 1;
		} catch (MagneticStripeFailureException msfe) {
			System.out.println("Card Swipe failed, please try again!");
			return -1;
		}
	}

    public boolean approvePurchase(String cardNumber, double amount) {
        for (CardIssuer bank : fundController.checkoutStationSoftware.getBanks()) {
            long holdNumber = bank.authorizeHold(cardNumber, amount);
            if (holdNumber != -1) 
                return bank.postTransaction(cardNumber, holdNumber, amount);
        }
        return false;
    }

    @Override
    public void aCardHasBeenInserted() {}

    @Override
    public void theCardHasBeenRemoved() {}

    @Override
    public void aCardHasBeenTapped() {}

    @Override
    public void aCardHasBeenSwiped() {}

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
            fundController.checkoutStationSoftware.removeTotalOrderPrice(totalOrderPrice);
            
        } else {

        }
        
    }

    @Override
    public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {}

    @Override
    public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {}

    @Override
    public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {}

    @Override
    public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {}
}
