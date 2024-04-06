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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.*;

import com.jjjwelectronics.printer.IReceiptPrinter;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.funds.Funds;
import com.thelocalmarketplace.software.funds.Receipt;
import com.thelocalmarketplace.software.funds.ReceiptHandler;

public class ReceiptHandlerTest {

    private ReceiptHandler handler;
    private Funds funds;
    private SelfCheckoutStationSoftware station;
    private IReceiptPrinter printer;
    private Receipt receipt;
    private mockReceiptObserver observer;
    private mockPredictError pError;

    @Before
    public void setUp() {
        this.station = new SelfCheckoutStationSoftware(new SelfCheckoutStationBronze());
        this.funds = new Funds(this.station);
        this.printer = this.station.getStationHardware().getPrinter();
        this.receipt = new Receipt(printer, funds);
        this.handler = new ReceiptHandler(this.receipt);
        this.observer = new mockReceiptObserver();
        this.pError = new mockPredictError();

        this.receipt.register(observer, pError);
    }

    @Test
    public void testOutOfPaper() {
        this.handler.thePrinterIsOutOfPaper();
        assertTrue(pError.noPaperCalled);

    }

    @Test
    public void testOutOfInk() {
        this.handler.thePrinterIsOutOfInk();
        assertTrue(pError.noInkCalled);

    }

    @Test
    public void testLowInk() {
        this.handler.thePrinterHasLowInk();;
        assertTrue(pError.lowInkCalled);

    }

    @Test
    public void testLowPaper() {
        this.handler.thePrinterHasLowPaper();;
        assertTrue(pError.lowPaperCalled);

    }



    @Test
    public void testPaperHasBeenAdded() {
        this.handler.paperHasBeenAddedToThePrinter();
        assertTrue(observer.paperAddedCalled);
    }

    @Test
    public void testInkHasBeenAdded() {
        this.handler.inkHasBeenAddedToThePrinter();
        assertTrue(observer.inkAddedCalled);

    }



}
