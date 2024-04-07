package com.thelocalmarketplace.software.communication.GUI.AttendantStation;

import ca.ucalgary.seng300.simulation.SimulationException;

import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.printer.ReceiptPrinterBronze;
import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.jjjwelectronics.scale.ElectronicScaleBronze;
import com.jjjwelectronics.scale.ElectronicScaleGold;
import com.jjjwelectronics.scale.ElectronicScaleSilver;
import com.tdc.CashOverloadException;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.hardware.external.CardIssuer;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.CustomerStation;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.StartSession;
import com.thelocalmarketplace.software.product.Products;
import powerutility.PowerGrid;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Currency;

public class AttendantListeners {
    private AttendantPageGUI gui;
    private int selectedStation;
    private SelfCheckoutStationSoftware[] stationSoftwareInstances;
    private CustomerStation[] customerStation;
    private StartSession[] startSessions;
    private boolean[] stationEnabled;
    private ALogic logic;

    private AbstractSelfCheckoutStation checkoutStation;
    private AbstractElectronicScale scale;

    public AttendantListeners(AttendantPageGUI gui, int selectedStation, SelfCheckoutStationSoftware[] stationSoftwareInstances, CustomerStation[] customerStation, StartSession[] startSessions, boolean[] stationEnabled) {
        this.gui = gui;
        this.selectedStation = selectedStation;
        this.stationSoftwareInstances = stationSoftwareInstances;
        this.customerStation = customerStation;
        this.startSessions = startSessions;
        this.stationEnabled = stationEnabled;
        this.logic = new ALogic();
    }

    private class refillCoinServiceButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                logic.refillCoinDispensers(stationSoftwareInstances[selectedStation]);
            } catch (SimulationException | CashOverloadException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    private class refillBanknotesServiceButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                logic.refillBanknoteDispensers(stationSoftwareInstances[selectedStation]);
            } catch (SimulationException | CashOverloadException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    private class refillReceiptPaperServiceButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
	            System.out.println("Refilling Receipt Paper"); // temporary
	            logic.refillPrinterPaper(stationSoftwareInstances[selectedStation]);
            } catch (OverloadedDevice e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }
    private class emptyCoinServiceButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            logic.emptyCoinStorage(stationSoftwareInstances[selectedStation]);
        }
    }

    private class emptyBanknotesServiceButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            logic.emptyBanknoteStorage(stationSoftwareInstances[selectedStation]);
        }
    }

    private class refillReceiptInkServiceButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
                try {
                    System.out.println("Refilling Receipt Ink"); // temporary
                    logic.refillPrinterInk(stationSoftwareInstances[selectedStation]);
                } catch (OverloadedDevice e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
        }
    }

    private class customerServiceButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Create a pop-up dialog with a search bar
            String searchText = JOptionPane.showInputDialog(null, "Enter search text:");

            // Call the method in Products class to add item by text search
            if (searchText != null && !searchText.isEmpty()) {
                Products product = stationSoftwareInstances[selectedStation].getProductHandler();
                product.addItemByTextSearch(searchText);
                
            }
        }
    }
    // Action listener for station buttons
    private class StationButtonListener implements ActionListener {
        private int stationNumber;

        public StationButtonListener(int stationNumber) {
            this.stationNumber = stationNumber;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            selectedStation = stationNumber; // Store the selected station number
            gui.highlightSelectedStation(stationNumber); // Highlight the selected station
        }
    }


    // Action listener for start station button
    private class StartStationButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedStation != -1 && (startSessions[selectedStation] == null || customerStation[selectedStation] == null)) { // Check if a station is selected
                // Use a separate thread to initialize SelfCheckoutStationBronze
                new Thread(() -> {
                    try {
                    	BigDecimal[] coinDenominations = { new BigDecimal("0.25"), new BigDecimal("0.10"), new BigDecimal("0.50"),
                    			new BigDecimal("1.0") };
                    	BigDecimal[] banknoteDenominations = { new BigDecimal("5.0"), new BigDecimal("10.0"), new BigDecimal("20.0"),
                				new BigDecimal("50.0"), new BigDecimal("100.0") };
                    	PowerGrid.engageUninterruptiblePowerSource();
                    	
                		SelfCheckoutStationGold.configureCurrency(Currency.getInstance("CAD"));
                		SelfCheckoutStationSilver.configureCurrency(Currency.getInstance("CAD"));
                		SelfCheckoutStationBronze.configureCurrency(Currency.getInstance("CAD"));

                    	SelfCheckoutStationGold.configureCoinDenominations(coinDenominations);
                    	SelfCheckoutStationSilver.configureCoinDenominations(coinDenominations);
                    	SelfCheckoutStationBronze.configureCoinDenominations(coinDenominations);
                    	
                    	SelfCheckoutStationGold.configureBanknoteDenominations(banknoteDenominations);
                		SelfCheckoutStationSilver.configureBanknoteDenominations(banknoteDenominations);
                		SelfCheckoutStationBronze.configureBanknoteDenominations(banknoteDenominations);

                        if (selectedStation == 0) {
                            checkoutStation = new SelfCheckoutStationGold();
                            scale = new ElectronicScaleGold();
                
                        } else if (selectedStation == 1) {
                            checkoutStation = new SelfCheckoutStationSilver();
                        
                            scale = new ElectronicScaleSilver();
                        } else {
                            checkoutStation = new SelfCheckoutStationBronze();
                            scale = new ElectronicScaleBronze();
                      
                        }

                        PowerGrid.engageUninterruptiblePowerSource();
                        checkoutStation.plugIn(PowerGrid.instance());
                        checkoutStation.turnOn();
                        PowerGrid test = PowerGrid.instance();
                        test.engageUninterruptiblePowerSource();
                        scale.plugIn(test);
                       
                        scale.turnOn();

                        Card creditCard = new Card("credit", "99988877", "User", "111", "1234", true, true);
                        Card debitCard = new Card("debit", "11122233", "User", "222", "1234", true, true);

                        CardIssuer cardIssuer = new CardIssuer("TD", 100);

                        Calendar expiryDate = Calendar.getInstance();
                        expiryDate.set(2027, Calendar.DECEMBER, 1);

                        cardIssuer.addCardData("99988877", "User", expiryDate, "111", 1000000);
                        cardIssuer.addCardData("11122233", "User", expiryDate, "222", 1000000);

                        SwingUtilities.invokeLater(() -> {
                            stationSoftwareInstances[selectedStation] = new SelfCheckoutStationSoftware(checkoutStation);
                            stationSoftwareInstances[selectedStation].setStationUnblock();
                            stationSoftwareInstances[selectedStation].addPaymentCard(creditCard, "credit");
                            stationSoftwareInstances[selectedStation].addPaymentCard(debitCard, "debit");
                            stationSoftwareInstances[selectedStation].addBank(cardIssuer);

                            try {
                                stationSoftwareInstances[selectedStation].getStationHardware().getPrinter().addInk(ReceiptPrinterBronze.MAXIMUM_INK);
                                stationSoftwareInstances[selectedStation].getStationHardware().getPrinter().addPaper(ReceiptPrinterBronze.MAXIMUM_PAPER);
                            } catch (OverloadedDevice ex) {
                                throw new RuntimeException(ex);
                            }

                            if (stationEnabled[selectedStation]) {
                                if (startSessions[selectedStation] == null) {
                                    startSessions[selectedStation] = new StartSession(selectedStation + 1, stationSoftwareInstances[selectedStation],scale);
                                    startSessions[selectedStation].setVisible(true);
                                    startSessions[selectedStation].setAttendantPageGUI(gui); // Pass the reference to AttendantPageGUI
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Selected station is disabled. Please enable it.");
                            }
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error initializing station."));
                    }
                }).start();
            } else {
                JOptionPane.showMessageDialog(null, "Please select a station first.");
            }
        }
    }

    // Action listener for close station button
    private class CloseStationButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedStation != -1) {
                if (customerStation[selectedStation] != null) { // Check if a station is selected and GUI is created
                    customerStation[selectedStation].dispose(); // Close the Customer GUI page for the selected station
                    customerStation[selectedStation] = null; // Reset the reference
                    startSessions[selectedStation] = null; // Reset the reference
                    gui.setStationAssistanceRequested(selectedStation,false); // Update the station status in the GUI
                } else if(startSessions[selectedStation] != null) {
                    startSessions[selectedStation].dispose(); // Close the StartSession page for the selected station
                    startSessions[selectedStation] = null; // Reset the reference
                    gui.setStationAssistanceRequested(selectedStation,false); // Update the station status in the GUI
                }
                else {
                    JOptionPane.showMessageDialog(null, "Please select a station with an active Customer Station GUI.");
                }
            }
        }
    }

    private class ClearStationSignalButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedStation != -1) { // Check if a station is selected
                if (customerStation[selectedStation] != null && customerStation[selectedStation].getSignal()) { // Check if GUI is created for the selected station
                    customerStation[selectedStation].clearSignal();
                }
                else {
                    JOptionPane.showMessageDialog(null, "Please select a station with an active Customer Station GUI that required assistance.");
                }
            }
        }
    }

    // Action listener for enable station button
    private class EnableStationButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
        
            if (selectedStation != -1) { // Check if a station is selected
            	
                logic.EnableStation(selectedStation, customerStation, stationSoftwareInstances, checkoutStation,startSessions); { // Check if GUI is created for the selected station    
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a station first.");
            }
        }
    }

    
 // Action listener for disable station button
    private class DisableStationButtonListener implements ActionListener {
        @Override 
        public void actionPerformed(ActionEvent e) {
       
            if (selectedStation != -1) { // Check if a station is selected
            	
                if (logic.DisableStation(selectedStation, customerStation, stationSoftwareInstances, checkoutStation,startSessions)==true) { // Check if GUI is created for the selected station       
                	
                        boolean notactive_notnull = true; }
                
                     else {
                        // If station is active, prompt user and disable after session completion
                        new Thread(() -> gui.waitForSessionCompletion(selectedStation)).start();
                    }
                }
             else {
                JOptionPane.showMessageDialog(null, "Please select a station first.");
            }
        }
    }


    
  
    
    // get action listeners
    public ActionListener getRefillCoinServiceButtonListener() {
        return new refillCoinServiceButtonListener();
    }
    public ActionListener getRefillBanknotesServiceButtonListener() {
        return new refillBanknotesServiceButtonListener();
    }
    public ActionListener getRefillReceiptPaperServiceButtonListener() {
        return new refillReceiptPaperServiceButtonListener();
    }
    public ActionListener getEmptyCoinServiceButtonListener() {
        return new emptyCoinServiceButtonListener();
    }
    public ActionListener getEmptyBanknotesServiceButtonListener() {
        return new refillReceiptInkServiceButtonListener();
    }
    public ActionListener getCustomerServiceButtonListener() {
        return new customerServiceButtonListener();
    }

    public ActionListener getStationButtonListener(int stationNumber) {
        return new StationButtonListener(stationNumber);
    }
    public ActionListener getRefillReceiptInkServiceButtonListener() {
        return new refillReceiptInkServiceButtonListener();
    }

    public ActionListener getStartStationButtonListener() {
        return new StartStationButtonListener();
    }
    public ActionListener getCloseStationButtonListener() {
        return new CloseStationButtonListener();
    }
    public ActionListener getClearStationSignalButtonListener() {
        return new ClearStationSignalButtonListener();
    }
    public ActionListener getDisableStationButtonListener() {
        return new DisableStationButtonListener();
    }

    public ActionListener getEnableStationButtonListener() {
        return new EnableStationButtonListener();
    }

}