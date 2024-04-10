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

import static org.junit.Assert.assertEquals;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.junit.Before;
import org.junit.Test;
import org.junit.Test.None;

import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantPageGUI;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.CustomerStation;

import powerutility.PowerGrid;

public class CardPaymentTest {
	private CustomerStation customerStation;
	private SelfCheckoutStationGold station;
    private SelfCheckoutStationSoftware stationSoftwareInstance;
    private AbstractElectronicScale scale;
    private AttendantPageGUI attendantGUI;

    private JButton findButton(JPanel container, String labelName) {
    	for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                if(((JButton) comp).getText().contains(labelName))
                	return (JButton) comp;
            }
        }	
    	return null;
    }

    private JRadioButton findRadioButton(JPanel container, String labelName) {
    	for (Component comp : container.getComponents()) {
            if (comp instanceof JRadioButton) {
                if(((JRadioButton) comp).getText().contains(labelName))
                	return (JRadioButton) comp;
            }
        }	
    	return null;
    }
    
	@Before
	public void setUp() {
		station = new SelfCheckoutStationGold();
        PowerGrid.engageUninterruptiblePowerSource();
        station.plugIn(PowerGrid.instance());
        station.turnOn();
		
        stationSoftwareInstance = new SelfCheckoutStationSoftware(station);
        stationSoftwareInstance.setStationActive(true);
        scale = (AbstractElectronicScale) stationSoftwareInstance.station.getBaggingArea();
        attendantGUI = new AttendantPageGUI();
        customerStation = new CustomerStation(1, stationSoftwareInstance, scale, attendantGUI);
	}
	
	@Test(expected = None.class)
	public void testCreatePinPanel() {
		customerStation.getPaymentWindow().creditWindow.createPinPanel(null);
		customerStation.getPaymentWindow().debitWindow.createPinPanel(null);
        customerStation.getDefaultCloseOperation(); // just tests the constructor. close right away.
	}
	
	@Test
	public void testDebitPinPanel() {
		JButton addItem = (JButton) findButton(customerStation.menuPanel, "Add Item");
		if(addItem != null) addItem.doClick();
		
		JButton pay = (JButton) findButton(customerStation.payButtonPanel, "Pay");
		if(pay != null) pay.doClick();
		
		JButton debit = (JButton) findButton(customerStation.getPaymentWindow().buttonPanel, "Debit");
		if(debit != null) debit.doClick();
		
		JRadioButton swipe = (JRadioButton) findRadioButton(customerStation.getPaymentWindow().debitWindow.radioPanel, "Swipe"); 
		if(swipe != null) swipe.doClick();
		JRadioButton tap = (JRadioButton) findRadioButton(customerStation.getPaymentWindow().debitWindow.radioPanel, "Tap"); 
		if(tap != null) tap.doClick();
		JRadioButton insertCard = (JRadioButton) findRadioButton(customerStation.getPaymentWindow().debitWindow.radioPanel, "Insert Card"); 
		if(insertCard != null) insertCard.doClick();

		String[] buttonLabels = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
		for (int i = 0; i < buttonLabels.length; i++) {
			JButton keypad = (JButton) findButton(customerStation.getPaymentWindow().debitWindow.keypadPanel, buttonLabels[i]);
			if(keypad != null) keypad.doClick();
		}
		
		JButton enter = (JButton) findButton(customerStation.getPaymentWindow().debitWindow.keypadPanel, "Enter");
		if(enter != null) enter.doClick();
		
		assertEquals(customerStation.getPaymentWindow().debitWindow.pinInput, "123456789");
	}
	
	@Test
	public void testCreditPinPanel() {
		JButton addItem = (JButton) findButton(customerStation.menuPanel, "Add Item");
		if(addItem != null) addItem.doClick();
		
		JButton pay = (JButton) findButton(customerStation.payButtonPanel, "Pay");
		if(pay != null) pay.doClick();
		
		JButton debit = (JButton) findButton(customerStation.getPaymentWindow().buttonPanel, "Debit");
		if(debit != null) debit.doClick();
		
		JRadioButton swipe = (JRadioButton) findRadioButton(customerStation.getPaymentWindow().debitWindow.radioPanel, "Swipe"); 
		if(swipe != null) swipe.doClick();
		JRadioButton tap = (JRadioButton) findRadioButton(customerStation.getPaymentWindow().debitWindow.radioPanel, "Tap"); 
		if(tap != null) tap.doClick();
		JRadioButton insertCard = (JRadioButton) findRadioButton(customerStation.getPaymentWindow().creditWindow.radioPanel, "Insert Card"); 
		if(insertCard != null) insertCard.doClick();

		String[] buttonLabels = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
		for (int i = 0; i < buttonLabels.length; i++) {
			JButton keypad = (JButton) findButton(customerStation.getPaymentWindow().creditWindow.keypadPanel, buttonLabels[i]);
			if(keypad != null) keypad.doClick();
		}
		
		JButton enter = (JButton) findButton(customerStation.getPaymentWindow().creditWindow.keypadPanel, "Enter");
		if(enter != null) enter.doClick();
		
		assertEquals(customerStation.getPaymentWindow().creditWindow.pinInput, "123456789");
	}
}
