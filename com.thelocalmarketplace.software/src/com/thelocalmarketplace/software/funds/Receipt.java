/**

 SENG 300 - ITERATION 3
 GROUP GOLD {8}
 
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

import java.math.BigDecimal;

import java.util.ArrayList;

import java.util.HashSet;


import java.util.Set;

import com.jjjwelectronics.EmptyDevice;

import com.jjjwelectronics.Item;

import com.jjjwelectronics.OverloadedDevice;

import com.jjjwelectronics.printer.IReceiptPrinter;

import com.jjjwelectronics.scanner.BarcodedItem;

import com.thelocalmarketplace.hardware.BarcodedProduct;

import com.thelocalmarketplace.hardware.PLUCodedItem;

import com.thelocalmarketplace.hardware.PLUCodedProduct;

import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

public class Receipt {

    public IReceiptPrinter receiptPrinter;
    protected final SelfCheckoutStationSoftware checkoutStationSoftware;
    private Funds funds;
    protected Set<ReceiptObserver> observers = new HashSet<>();
    private ArrayList<Item> order;

    /**
     * Constructs a new Receipt instance.
     *
     * @param printer The receipt printer.
     * @param funds The Funds facade.
     * @param checkoutStation The checkout station software.
     */
    public Receipt (IReceiptPrinter printer, Funds funds) {
        this.funds = funds;
        this.receiptPrinter = printer;
        this.checkoutStationSoftware = funds.checkoutStationSoftware;

        ReceiptHandler rh = new ReceiptHandler(this);
        receiptPrinter.register(rh);

        this.order = checkoutStationSoftware.getOrder();
    }

    /**
     * Prints receipt with details of each item, total cost, payment, and change due.
     *
     * @return The printed receipt as a String.
     * @throws EmptyDevice If receipt printer is out of paper or ink.
     * @throws OverloadedDevice If receipt printer is overloaded with commands.
     */
    public String printReceipt() throws EmptyDevice, OverloadedDevice {
        ArrayList<String> receiptItems = compileReceiptItems();
        printItems(receiptItems);
        this.receiptPrinter.cutPaper();
        notifyReceiptPrinted(order);
        return this.receiptPrinter.removeReceipt();
    }

    private ArrayList<String> compileReceiptItems() {
        ArrayList<String> receiptItems = new ArrayList<>();

        for (Item item : order) {
            receiptItems.add(formatItemDescription(item));
        }

        BigDecimal purchaseValue = new BigDecimal(String.valueOf(checkoutStationSoftware.getTotalOrderPrice()));
        BigDecimal amountPaid = this.funds.getTotalPaid();
        BigDecimal changeDue = this.funds.getMoneyLeft().multiply(new BigDecimal("-1"));

        receiptItems.add("Total: $" + String.format("%.2f", purchaseValue));
        receiptItems.add("Paid: $" + String.format("%.2f", amountPaid));
        receiptItems.add("Change: $" + String.format("%.2f", changeDue));

        return receiptItems;
    }

    private String formatItemDescription(Item item) {
        String productDescription;
        long price;

        if (item instanceof BarcodedItem) {
            BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(((BarcodedItem) item).getBarcode());
            productDescription = product.getDescription();
            price = product.getPrice();
        } else if (item instanceof PLUCodedItem) {
            PLUCodedProduct product = ProductDatabases.PLU_PRODUCT_DATABASE.get(((PLUCodedItem) item).getPLUCode());
            productDescription = product.getDescription();
            price = product.getPrice();
        } else {
            throw new IllegalArgumentException("Unsupported product type.");
        }

        return productDescription + " $" + String.format("%.2f", (float)price);
    }

    private void printItems(ArrayList<String> receiptItems) throws EmptyDevice, OverloadedDevice {
        for (String item : receiptItems) {
            this.receiptPrinter.print('\n');
            for (char ch : item.toCharArray()) {
                this.receiptPrinter.print(ch);
            }
        }
    }

    public void register(ReceiptObserver listener) {
        observers.add(listener);
    }

    public void deregister(ReceiptObserver listener) {
        observers.remove(listener);
    }

    public void notifyReceiptPrinted(ArrayList<Item> order) {
        for(ReceiptObserver observer : observers)
            observer.receiptPrinted(order);
    }

    public void notifyInkEmpty(IReceiptPrinter printer) {
    	for(ReceiptObserver observer : observers)
            observer.noInkError(printer);
    }

    public void notifyPaperEmpty(IReceiptPrinter printer) {
    	for(ReceiptObserver observer : observers)
            observer.noPaperError(printer);
    }

    public void notifyInkLow(IReceiptPrinter printer) {
    	for(ReceiptObserver observer : observers)
            observer.lowInkError(printer);
    }

    public void notifyPaperLow(IReceiptPrinter printer) {
    	for(ReceiptObserver observer : observers)
            observer.lowPaperError(printer);
    }


    public void notifyInkAdded(IReceiptPrinter printer) {
        for(ReceiptObserver observer : observers)
            observer.inkAdded(printer);
    }

    public void notifyPaperAdded(IReceiptPrinter printer) {
        for(ReceiptObserver observer : observers)
            observer.paperAdded(printer);
    }
}
