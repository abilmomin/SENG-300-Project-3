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