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

    @Before
    public void setUp() {
        this.station = new SelfCheckoutStationSoftware(new SelfCheckoutStationBronze());
        this.handler = new ReceiptHandler(new Receipt(this.station.getStationHardware().getPrinter(), funds, station));
        this.funds = new Funds(this.station);
        this.printer = this.station.getStationHardware().getPrinter();
        this.receipt = new Receipt(printer, funds, station);
    }

    @Test
    public void testOutOfPaper() {
        mockReceiptObserver observer = new mockReceiptObserver();
        mockPredictError pError = new mockPredictError();

        // Act
        this.handler.thePrinterIsOutOfPaper();
        assertTrue(pError.noPaperCalled);
        assertTrue(observer.paperAddedCalled);


    }

    @Test
    public void testOutOfInk() {

        mockReceiptObserver observer = new mockReceiptObserver();
        mockPredictError pError = new mockPredictError();

        // Act
        this.handler.thePrinterIsOutOfInk();
        assertTrue(pError.noInkCalled);
        assertTrue(observer.inkAddedCalled);


    }

    @Test
    public void testPaperHasBeenAdded() {
        mockReceiptObserver observer = new mockReceiptObserver();

        this.handler.paperHasBeenAddedToThePrinter();
        assertTrue(observer.paperAddedCalled);
    }

    @Test
    public void testInkHasBeenAdded() {

        mockReceiptObserver observer = new mockReceiptObserver();

        this.handler.inkHasBeenAddedToThePrinter();
        assertTrue(observer.inkAddedCalled);

    }




}
