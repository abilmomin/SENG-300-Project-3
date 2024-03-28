/**
 * Yotam Rojnov (UCID: 30173949)
 * Duncan McKay (UCID: 30177857)
 * Mahfuz Alam (UCID:30142265)
 * Luis Trigueros Granillo (UCID: 30167989)
 * Lilia Skumatova (UCID: 30187339)
 * Abdelrahman Abbas (UCID: 30110374)
 * Talaal Irtija (UCID: 30169780)
 * Alejandro Cardona (UCID: 30178941)
 * Alexandre Duteau (UCID: 30192082)
 * Grace Johnson (UCID: 30149693)
 * Abil Momin (UCID: 30154771)
 * Tara Ghasemi M. Rad (UCID: 30171212)
 * Izabella Mawani (UCID: 30179738)
 * Binish Khalid (UCID: 30061367)
 * Fatima Khalid (UCID: 30140757)
 * Lucas Kasdorf (UCID: 30173922)
 * Emily Garcia-Volk (UCID: 30140791)
 * Yuinikoru Futamata (UCID: 30173228)
 * Joseph Tandyo (UCID: 30182561)
 * Syed Haider (UCID: 30143096)
 * Nami Marwah (UCID: 30178528)
 */

package com.thelocalmarketplace.software;


import java.math.BigDecimal;
import java.util.ArrayList;


import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Item;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.printer.IReceiptPrinter;
import com.jjjwelectronics.printer.ReceiptPrinterBronze;
import com.jjjwelectronics.printer.ReceiptPrinterGold;
import com.jjjwelectronics.printer.ReceiptPrinterListener;
import com.jjjwelectronics.printer.ReceiptPrinterSilver;
import com.jjjwelectronics.scanner.BarcodedItem;

import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedItem;
import com.thelocalmarketplace.hardware.PLUCodedProduct;

import com.thelocalmarketplace.hardware.external.ProductDatabases;

import powerutility.PowerGrid;

public class Receipt implements ReceiptPrinterListener{

    private IReceiptPrinter receiptPrinter;
    private PaymentHandler handler;
    private Order order;

    public Receipt (IReceiptPrinter printer, PaymentHandler handler, Order order) {
        if  (printer == null)
            throw new NullPointerException("No argument may be null.");
        if (printer instanceof ReceiptPrinterBronze)
            this.receiptPrinter = (ReceiptPrinterBronze) printer;
        else if (printer instanceof ReceiptPrinterSilver)
            this.receiptPrinter = (ReceiptPrinterSilver) printer;
        else if (printer instanceof ReceiptPrinterGold)
            this.receiptPrinter = (ReceiptPrinterGold) printer;

        receiptPrinter.plugIn(PowerGrid.instance());
        receiptPrinter.turnOn();

        this.handler = handler;
        this.order = order;
    }

    public String printReceipt(Order order) throws EmptyDevice, OverloadedDevice {

        ArrayList<String> receiptItems = new ArrayList<String>();

        for (int i = 0; i < order.getOrder().size(); i++) {
            String productDescription;
            Item item = order.getOrder().get(i);

            if (item instanceof BarcodedItem) { // Gets the product description and the price of a barcoded product
                BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(((BarcodedItem) item).getBarcode());
                productDescription = product.getDescription();
                long price = product.getPrice();
                receiptItems.add(productDescription + " $" + String.format("%.2f", (float)price));
            }

            else if (item instanceof PLUCodedItem) { // Gets the product description and the price of a product inputted
                // through price-lookup (PLU)
                PLUCodedProduct product = ProductDatabases.PLU_PRODUCT_DATABASE.get(((PLUCodedItem) item).getPLUCode());
                productDescription = product.getDescription();
                long price = product.getPrice();
                receiptItems.add(productDescription + " $" + String.format("%.2f", (float)price));
            }
            else {
                throw new NullPointerException("This product is not a supported product, can not be registered for a price");
            }
        }

        BigDecimal purchaseValue = new BigDecimal(String.valueOf(order.getTotalPrice()));
        BigDecimal amountPaid = this.handler.amountSpent;
        BigDecimal changeDue = this.handler.changeRemaining;


        receiptItems.add("Total: $" + String.format("%.2f", purchaseValue));
        receiptItems.add("Paid: $" + String.format("%.2f", amountPaid));
        receiptItems.add("Change: $" + String.format("%.2f", changeDue));

        for (int i = 0; i < receiptItems.size(); i++) {
            this.receiptPrinter.print('\n');

            for (int j = 0; j < receiptItems.get(i).length(); j++) {
                this.receiptPrinter.print(receiptItems.get(i).charAt(j));
            }
        }

        this.receiptPrinter.cutPaper();
        return this.receiptPrinter.removeReceipt();
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
    public void thePrinterIsOutOfPaper() {
        // TODO Auto-generated method stub

    }

    @Override
    public void thePrinterIsOutOfInk() {
        try {
            this.receiptPrinter.addInk(ReceiptPrinterBronze.MAXIMUM_INK);
        } catch (OverloadedDevice e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void thePrinterHasLowInk(){
        try {
            this.receiptPrinter.addInk(ReceiptPrinterBronze.MAXIMUM_INK - this.receiptPrinter.inkRemaining());
        } catch (OverloadedDevice e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void thePrinterHasLowPaper() {
        // TODO Auto-generated method stub

    }

    @Override
    public void paperHasBeenAddedToThePrinter() {
        // TODO Auto-generated method stub

    }

    @Override
    public void inkHasBeenAddedToThePrinter() {
        // TODO Auto-generated method stub

    }
}
