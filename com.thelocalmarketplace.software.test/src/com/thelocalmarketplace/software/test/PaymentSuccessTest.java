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

import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantPageGUI;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.CustomerStation;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.PaymentSuccess;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.SelectPayment;
import org.junit.Before;
import org.junit.Test;
import powerutility.PowerGrid;

import static org.junit.Assert.assertTrue;


public class PaymentSuccessTest {
    private CustomerStation customerStation;
    private SelfCheckoutStationSoftware stationSoftwareInstance;
    private AbstractElectronicScale scale;
    private AttendantPageGUI attendantGUI;
    private SelfCheckoutStationGold station;
    private SelectPayment paymentWindow;
    private PaymentSuccess paymentSuccess;
    

    @Before
    public void setUp() {
        // Initialize the objects
        station = new SelfCheckoutStationGold();
        PowerGrid.engageUninterruptiblePowerSource();
        station.plugIn(PowerGrid.instance());
        station.turnOn();

        stationSoftwareInstance = new SelfCheckoutStationSoftware(station);
        stationSoftwareInstance.setStationActive(true);
        scale = (AbstractElectronicScale) stationSoftwareInstance.station.getBaggingArea();
        attendantGUI = new AttendantPageGUI();
        customerStation = new CustomerStation(1, stationSoftwareInstance, scale, attendantGUI);

        paymentWindow = new SelectPayment(stationSoftwareInstance);

    }

    @Test
    public void testPaymentSuccess() {
        customerStation = new CustomerStation(1, stationSoftwareInstance, scale, attendantGUI);
        paymentWindow = new SelectPayment(stationSoftwareInstance);

        try{
            PaymentSuccess paymentSuccess = new PaymentSuccess(0.0, stationSoftwareInstance, attendantGUI);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void paymentSuccessLabelTest(){
        customerStation = new CustomerStation(1, stationSoftwareInstance, scale, attendantGUI);
        paymentWindow = new SelectPayment(stationSoftwareInstance);

        PaymentSuccess paymentSuccess = new PaymentSuccess(0.0, stationSoftwareInstance, attendantGUI);
        paymentSuccess.setVisible(true);

        assertTrue(paymentSuccess.getTitle().equals("Thank you!"));
    }
}
