package com.thelocalmarketplace.software.test;

import java.util.ArrayList;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.printer.IReceiptPrinter;
import com.thelocalmarketplace.software.PredictError;
import com.thelocalmarketplace.software.funds.ReceiptObserver;

public class mockPredictError implements PredictError {
    boolean receiptPrintedCalled = false;
    boolean inkLowCalled = false;
    boolean paperLowCalled = false;

    @Override
    public void lowInkError(IReceiptPrinter printer) {
        inkLowCalled = true;
    }

    @Override
    public void lowPaperError(IReceiptPrinter printer) {
        paperLowCalled = true;
    }

}

