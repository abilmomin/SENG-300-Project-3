package com.thelocalmarketplace.software.test;

import java.util.ArrayList;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.printer.IReceiptPrinter;
import com.thelocalmarketplace.software.PredictError;
import com.thelocalmarketplace.software.funds.ReceiptObserver;

public class mockReceiptObserver implements ReceiptObserver{
    boolean receiptPrintedCalled = false;
    boolean inkAddedCalled = false;
    boolean paperAddedCalled = false;


    @Override
    public void receiptPrinted(ArrayList<Item> order) {
        receiptPrintedCalled = true;
    }

    @Override
    public void inkAdded(IReceiptPrinter printer) {
        inkAddedCalled = true;
    }

    @Override
    public void paperAdded(IReceiptPrinter printer) {
        paperAddedCalled = true;
    }
}
