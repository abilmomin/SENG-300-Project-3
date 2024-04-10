package com.thelocalmarketplace.software.test;

import static org.junit.Assert.assertTrue;

import javax.swing.*;
import java.awt.*;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantPageGUI;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.CustomerStation;

import powerutility.PowerGrid;

public class CustomerStationTest {

	 private CustomerStation customerStation;
	    private SelfCheckoutStationSoftware stationSoftwareInstance;
	    private AbstractElectronicScale scale;
	    private AttendantPageGUI attendantGUI;
	    private SelfCheckoutStationGold station;

	    public JButton findButton(JPanel container, CharSequence labelName) {
	    	for (Component comp : container.getComponents()) {
	    		System.out.println(comp);
	            if (comp instanceof JButton) {
	                System.out.println(((JButton) comp).getText());
	                if(((JButton) comp).getText().contains(labelName)) return (JButton) comp;
	            }
	        }	
	    	return null;
	    }
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
	    }

	    @Test
	    public void testAddBarCodeProductToCart() {
	        customerStation = new CustomerStation(1, stationSoftwareInstance, scale, attendantGUI);
	        JButton addItem = (JButton) findButton(customerStation.menuPanel, "Add Item");
	        if(addItem != null) addItem.doClick();
	        
	        JButton addBarcode = (JButton) findButton(customerStation.addItemBtnPanel, "Scan Barcode");
	        if(addBarcode != null) addBarcode.doClick();
	        
	        // Check if the product was added to the cart
	        assertTrue(stationSoftwareInstance.getOrder().size() == 1);
	        customerStation.getDefaultCloseOperation();
	    }

	    @Test
	    public void testAddPLUProductToCart() {
	        customerStation = new CustomerStation(1, stationSoftwareInstance, scale, attendantGUI);
	    	JButton addItem = (JButton) findButton(customerStation.menuPanel, "Add Item");
	        if(addItem != null) addItem.doClick();
	        
	        JButton addPLU = (JButton) findButton(customerStation.addItemBtnPanel, "Enter PLU Code");
	        if(addPLU != null) addPLU.doClick();
	        
	        JButton PLU1 = (JButton) findButton(customerStation.keypadPanel, "1");
	        if(PLU1 != null) PLU1.doClick();
	        JButton PLU2 = (JButton) findButton(customerStation.keypadPanel, "2");
	        if(PLU2 != null) PLU2.doClick();
	        JButton PLU3 = (JButton) findButton(customerStation.keypadPanel, "3");
	        if(PLU3 != null) PLU3.doClick();
	        JButton PLU4 = (JButton) findButton(customerStation.keypadPanel, "4");
	        if(PLU4 != null) PLU4.doClick();
	        JButton PLUEnter = (JButton) findButton(customerStation.keypadPanel, "Enter");
	        if(PLUEnter != null) PLUEnter.doClick();
	        
	        // Check if the product was added to the cart
	        assertTrue(stationSoftwareInstance.getOrder().size() == 1);
	        customerStation.getDefaultCloseOperation();
	    }
	    
	    @Test
	    public void testRemoveBarcodedProductFromCart() {
	        customerStation = new CustomerStation(1, stationSoftwareInstance, scale, attendantGUI);
	    	JButton addItem = (JButton) findButton(customerStation.menuPanel, "Add Item");
	    	
	        if(addItem != null) addItem.doClick();
	        
	        JButton addBarcode = (JButton) findButton(customerStation.addItemBtnPanel, "Scan Barcode");
	        if(addBarcode != null) addBarcode.doClick();
	        
	    	JButton bagItem = (JButton) findButton(customerStation.addToBagPopUp.buttonPanel, "Place Item in Bagging Area");
	        if(bagItem != null) bagItem.doClick();
	        
	        customerStation.cartItemButtons.get(0).doClick();
	        
	        JButton removeItem = (JButton) findButton(customerStation.menuPanel, "Remove Item");
	        if(removeItem != null) removeItem.doClick();
	        
	        // Check if the product was added to the cart
	        assertTrue(stationSoftwareInstance.getOrder().size() == 0);
	        customerStation.getDefaultCloseOperation();
	    }
	    
	    @Test
	    public void testRemovePLUProductFromCart() {
	        customerStation = new CustomerStation(1, stationSoftwareInstance, scale, attendantGUI);
	    	JButton addItem = (JButton) findButton(customerStation.menuPanel, "Add Item");
	        if(addItem != null) addItem.doClick();
	        
	        JButton addPLU = (JButton) findButton(customerStation.addItemBtnPanel, "Enter PLU Code");
	        if(addPLU != null) addPLU.doClick();
	        
	        JButton PLU1 = (JButton) findButton(customerStation.keypadPanel, "1");
	        if(PLU1 != null) PLU1.doClick();
	        JButton PLU2 = (JButton) findButton(customerStation.keypadPanel, "2");
	        if(PLU2 != null) PLU2.doClick();
	        JButton PLU3 = (JButton) findButton(customerStation.keypadPanel, "3");
	        if(PLU3 != null) PLU3.doClick();
	        JButton PLU4 = (JButton) findButton(customerStation.keypadPanel, "4");
	        if(PLU4 != null) PLU4.doClick();
	        JButton PLUEnter = (JButton) findButton(customerStation.keypadPanel, "Enter");
	        if(PLUEnter != null) PLUEnter.doClick();
	        
	    	JButton bagItem = (JButton) findButton(customerStation.addToBagPopUp.buttonPanel, "Place Item in Bagging Area");
	        if(bagItem != null) bagItem.doClick();
	        
	        customerStation.cartItemButtons.get(0).doClick();
	        
	        JButton removeItem = (JButton) findButton(customerStation.menuPanel, "Remove Item");
	        if(removeItem != null) removeItem.doClick();
	        
	        // Check if the product was added to the cart
	        assertTrue(stationSoftwareInstance.getOrder().size() == 0);
	        customerStation.getDefaultCloseOperation();
	    }
	    
	    @Test
	    public void testPurchaseBags() throws Exception {
	        customerStation = new CustomerStation(1, stationSoftwareInstance, scale, attendantGUI);
	        JButton addBag = (JButton) findButton(customerStation.menuPanel, "Purchase Bags");
	        if(addBag != null) addBag.doClick();

	        Thread.sleep(2000);
	        System.out.println(customerStation.bagPurchasePanel.keypadPanel);
            JButton add3Bags = (JButton) findButton(customerStation.bagPurchasePanel.keypadPanel, "3");
            if(add3Bags != null) add3Bags.doClick();
            
            JButton bagsOk = (JButton) findButton(customerStation.bagPurchasePanel.confirmPanel, "OK");
	        if(bagsOk != null) bagsOk.doClick();

	        System.out.println(stationSoftwareInstance.getOrder().size());
	        // Check if the product was added to the cart
	        assertTrue(stationSoftwareInstance.getOrder().size() == 3);
	        customerStation.getDefaultCloseOperation();
	    }

	    @Test
	    public void testAddItemVisually() {
	    	customerStation = new CustomerStation(1, stationSoftwareInstance, scale, attendantGUI);
	        JButton addItem = (JButton) findButton(customerStation.menuPanel, "Add Item");
	        if(addItem != null) addItem.doClick();
	        
	        JButton addVisual = (JButton) findButton(customerStation.addItemBtnPanel, "Search Product");
	        if(addVisual != null) addVisual.doClick();
	        
	        customerStation.searchProductByText.searchResults.setSelectedIndex(0);
	        customerStation.searchProductByText.submitButton.doClick();
	        
	        // Check if the product was added to the cart
	        assertTrue(stationSoftwareInstance.getOrder().size() == 1);
	        customerStation.getDefaultCloseOperation();
	    }
	    
}
