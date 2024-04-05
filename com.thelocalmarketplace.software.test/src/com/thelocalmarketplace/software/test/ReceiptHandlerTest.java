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

import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.funds.Funds;
import com.thelocalmarketplace.software.funds.Receipt;
import com.thelocalmarketplace.software.funds.ReceiptHandler;

public class ReceiptHandlerTest {

    private ReceiptHandler handler;
    private Funds funds;
    private SelfCheckoutStationSoftware station;

    @Before
    public void setUp() {
        this.handler = new ReceiptHandler(new Receipt(this.station.getStationHardware().getPrinter(), funds, station));
    }

    @Test
    public void testOutOfPaper() {

    }

    @Test
    public void testOutOfInk() {

    }

    @Test
    public void testLowInk() {

    }

    @Test
    public void testLowPaper() {

    }



}
