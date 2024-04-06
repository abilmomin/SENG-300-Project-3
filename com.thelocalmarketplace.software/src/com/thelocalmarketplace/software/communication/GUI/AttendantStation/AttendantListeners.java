package com.thelocalmarketplace.software.communication.GUI.AttendantStation;

import ca.ucalgary.seng300.simulation.SimulationException;
import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.jjjwelectronics.scale.ElectronicScaleBronze;
import com.jjjwelectronics.scale.ElectronicScaleGold;
import com.jjjwelectronics.scale.ElectronicScaleSilver;
import com.tdc.CashOverloadException;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.software.ALogic;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.CustomerStation;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.StartSession;
import com.thelocalmarketplace.software.product.Products;
import powerutility.PowerGrid;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AttendantListeners {
    private AttendantPageGUI gui;
    private int selectedStation;
    private SelfCheckoutStationSoftware[] stationSoftwareInstances;
    private CustomerStation[] customerStation;
    private StartSession[] startSessions;
    private boolean[] stationEnabled;

    private AbstractSelfCheckoutStation checkoutStation;
    private AbstractElectronicScale scale;

    public AttendantListeners(AttendantPageGUI gui, int selectedStation, SelfCheckoutStationSoftware[] stationSoftwareInstances, CustomerStation[] customerStation, StartSession[] startSessions, boolean[] stationEnabled) {
        this.gui = gui;
        this.selectedStation = selectedStation;
        this.stationSoftwareInstances = stationSoftwareInstances;
        this.customerStation = customerStation;
        this.startSessions = startSessions;
        this.stationEnabled = stationEnabled;
    }

    private class refillCoinServiceButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ALogic logic = new ALogic();
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
            ALogic logic = new ALogic();
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
            ALogic logic = new ALogic();
//            try {
            System.out.println("Refilling Receipt Paper"); // temporary
            //logic.refillPrinterPaper(stationSoftwareInstances[selectedStation]);
//            } catch (OverloadedDevice e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();
//            }
        }
    }
    private class emptyCoinServiceButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ALogic logic = new ALogic();
            logic.emptyCoinStorage(stationSoftwareInstances[selectedStation]);
        }
    }

    private class emptyBanknotesServiceButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ALogic logic = new ALogic();
            logic.emptyBanknoteStorage(stationSoftwareInstances[selectedStation]);
        }
    }

    private class refillReceiptInkServiceButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ALogic logic = new ALogic();
//                try {
//                    logic.refillPrinterInk(stationSoftwareInstances[selectedStation]);
//                } catch (OverloadedDevice e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
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
                if (product.addItemByTextSearch(searchText) == true) {
                	customerStation[selectedStation].customerPopUp("Add item to bagging area.");
                }
                
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
                        if (selectedStation == 0) {
                            checkoutStation = new SelfCheckoutStationGold();
                            scale = new ElectronicScaleGold();
                            System.out.println("SelfCheckoutStationGold initialized");
                        } else if (selectedStation == 1) {
                            checkoutStation = new SelfCheckoutStationSilver();
                            System.out.println("SelfCheckoutStationSilver initialized");
                            scale = new ElectronicScaleSilver();
                        } else {
                            checkoutStation = new SelfCheckoutStationBronze();
                            scale = new ElectronicScaleBronze();
                            System.out.println("SelfCheckoutStationBronze initialized");
                        }

                        PowerGrid.engageUninterruptiblePowerSource();
                        checkoutStation.plugIn(PowerGrid.instance());
                        checkoutStation.turnOn();

                        SwingUtilities.invokeLater(() -> {
                            stationSoftwareInstances[selectedStation] = new SelfCheckoutStationSoftware(checkoutStation);
                            stationSoftwareInstances[selectedStation].setStationUnblock();

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

                if (customerStation[selectedStation] != null && stationSoftwareInstances[selectedStation].getStationBlock()== true) { // Check if GUI is created for the selected station
                    stationSoftwareInstances[selectedStation].setStationUnblock();
                    customerStation[selectedStation].unfreezeGUI(); // Unfreeze the GUI
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
                if (customerStation[selectedStation] != null && stationSoftwareInstances[selectedStation].getStationBlock() == false) { // Check if GUI is created for the selected station
                    if (stationSoftwareInstances[selectedStation].getStationActive() == false) {
                        // If station is not active, disable it immediately
                        stationSoftwareInstances[selectedStation].setStationBlock();
                        customerStation[selectedStation].freezeGUI();
                    } else {
                        // If station is active, prompt user and disable after session completion
                        new Thread(() -> gui.waitForSessionCompletion(selectedStation)).start();
                    }
                }
            } else {
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
