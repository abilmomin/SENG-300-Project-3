/**

Name                      UCID

Yotam Rojnov             30173949
Duncan McKay             30177857
Mahfuz Alam              30142265
Luis Trigueros Granillo  30167989
Lilia Skumatova          30187339
Abdelrahman Abbas        30110374
Talaal Irtija            30169780
Alejandro Cardona        30178941
Alexandre Duteau         30192082
Grace Johnson            30149693
Abil Momin               30154771
Tara Ghasemi M. Rad      30171212
Izabella Mawani          30179738
Binish Khalid            30061367
Fatima Khalid            30140757
Lucas Kasdorf            30173922
Emily Garcia-Volk        30140791
Yuinikoru Futamata       30173228
Joseph Tandyo            30182561
Syed Haider              30143096
Nami Marwah              30178528

 */

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
     * Constructor for CardHandler class.
     * 
     * @param fundController 
     * 				The funds controller class.
     */
    public CardHandler(Funds fundController) {
        if (fundController == null) {
            throw new IllegalArgumentException("The argument cannot be null");
        }
        this.fundController = fundController;
    }

    /**
     * Function for approving a purchase using card and card issuer information.
     * 
     * @param cardNumber 
     * 				The user's card number.
     * @param amount 
     * 				Total amount to be charged.
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
     * Implemented method that uses the user's card information to process transaction.
     * 
     * @param data 
     * 				The data of the user's card.
     */
    @Override
    public void theDataFromACardHasBeenRead(CardData data) {
        PaymentKind.Kind cardType = PaymentKind.getCardType(data.getType());

        if (fundController.checkoutStationSoftware.getStationBlock()) {
            fundController.notifyFundsStationBlocked();
            return;
        }
        double amountDue = fundController.getMoneyLeft().doubleValue();
        boolean purchaseStatus = approvePurchase(data.getNumber(), amountDue);

        if (purchaseStatus) {
            fundController.addToTotalPaid(BigDecimal.valueOf(amountDue));
            fundController.notifyFundsAdded(BigDecimal.valueOf(amountDue));
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
