package com.thelocalmarketplace.software.test;

import java.util.ArrayList;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.printer.IReceiptPrinter;
import com.thelocalmarketplace.software.funds.ReceiptObserver;

public class mockReceiptObserver implements ReceiptObserver{
	boolean receiptPrintedCalled = false;
    boolean inkAddedCalled = false;
    boolean paperAddedCalled = false;
    boolean noInkCalled =  false;
    boolean noPaperCalled =  false;
    boolean lowInkCalled =  false;
    boolean lowPaperCalled =  false;
    


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
    
    @Override
    public void noInkError(IReceiptPrinter printer) {
        noInkCalled = true;
    }
    
    @Override
    public void noPaperError(IReceiptPrinter printer) {
        noPaperCalled = true;
    }
    
    @Override
    public void lowInkError(IReceiptPrinter printer) {
        lowInkCalled = true;
    }
    
    @Override
    public void lowPaperError(IReceiptPrinter printer) {
        lowPaperCalled = true;
    }
}


