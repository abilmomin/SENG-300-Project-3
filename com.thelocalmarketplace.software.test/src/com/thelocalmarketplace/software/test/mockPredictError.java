package com.thelocalmarketplace.software.test;

import java.util.ArrayList;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.printer.IReceiptPrinter;
import com.thelocalmarketplace.software.PredictError;
import com.thelocalmarketplace.software.funds.ReceiptObserver;

public class mockPredictError implements PredictError {
    boolean noInkCalled = false;
    boolean noPaperCalled = false;


    @Override
    public void noInkError(IReceiptPrinter printer) {
        noInkCalled = true;
    }

    @Override
    public void noPaperError(IReceiptPrinter printer) {
        noPaperCalled = true;
    }



}


