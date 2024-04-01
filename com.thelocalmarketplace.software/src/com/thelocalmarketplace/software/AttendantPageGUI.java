package com.thelocalmarketplace.software;

import javax.swing.*;

import com.thelocalmarketplace.software.communication.CustomerStation;

import java.awt.*;
import java.awt.event.*;

public class AttendantPageGUI extends JFrame {
    private int selectedStation = -1; // Variable to store the selected station number
    private JButton[] stationButtons; // Array to hold the station buttons
    private CustomerStation[] customerGUIs;
    private boolean[] stationEnabled; // Array to keep track of station status

    public AttendantPageGUI() {
        // Setup
        setTitle("Attendant Page");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new GridLayout(7, 1));

        // Label for stations
        JLabel stationsLabel = new JLabel("Checkout Stations: ");

        // Button panel for managing customer checkout stations
        JPanel stationPanel = new JPanel(new GridLayout(2, 2));
        stationButtons = new JButton[4]; // Initialize the station buttons array
        customerGUIs = new CustomerStation[4];
        stationEnabled = new boolean[4]; // Initialize the station status array
        // Create station buttons
        for (int i = 0; i < 4; i++) {
            JButton checkoutButton = new JButton("Checkout Station " + (i + 1));
            checkoutButton.addActionListener(new StationButtonListener(i));
            stationPanel.add(checkoutButton);
            stationButtons[i] = checkoutButton; // Add button to the array
            stationEnabled[i] = true; // Initialize station as enabled
        }

        // Label for station controls
        JLabel stationControlLabel = new JLabel("Checkout Station Controls: ");

        // Button panel for station controls
        JPanel stationControlPanel = new JPanel(new FlowLayout());
        JButton startStation = new JButton("Start Station");
        JButton blockStation = new JButton("Block Station");
        JButton unblockStation = new JButton("Unblock Station");
        JButton closeStation = new JButton("Close Station");
        JButton enableStation = new JButton("Enable Station");
        JButton disableStation = new JButton("Disable Station");

        startStation.addActionListener(new StartStationButtonListener());
        closeStation.addActionListener(new CloseStationButtonListener());
        blockStation.addActionListener(new BlockStationButtonListener());
        unblockStation.addActionListener(new UnblockStationButtonListener());
        enableStation.addActionListener(new EnableStationButtonListener());
        disableStation.addActionListener(new DisableStationButtonListener());

        stationControlPanel.add(startStation);
        stationControlPanel.add(closeStation);
        stationControlPanel.add(blockStation);
        stationControlPanel.add(unblockStation);
        stationControlPanel.add(enableStation);
        stationControlPanel.add(disableStation);

        // Label for station controls
        JLabel stationServicesLabel = new JLabel("Other Services: ");

        // Button panel for station controls
        JPanel stationServicesPanel = new JPanel(new FlowLayout());
        JButton addItembyText = new JButton("Add Item by Text Search");
        JButton refillCoins = new JButton("Refill Coins");
        JButton refillBanknotes = new JButton("Refill Banknotes");
        JButton refillReciptPaper = new JButton("Refill Receipt Paper");
        JButton emptyCoins = new JButton("Empty Coins");
        JButton emptyBanknotes = new JButton("Empty Banknotes");
        JButton refillReceiptInk = new JButton("Refill Receipt Ink");

        stationServicesPanel.add(addItembyText);
        stationServicesPanel.add(refillCoins);
        stationServicesPanel.add(refillBanknotes);
        stationServicesPanel.add(refillReciptPaper);
        stationServicesPanel.add(refillReceiptInk);
        stationServicesPanel.add(emptyCoins);
        stationServicesPanel.add(emptyBanknotes);

        // Adding the panels to the main panel
        mainPanel.add(stationsLabel);
        mainPanel.add(stationPanel);
        mainPanel.add(stationControlLabel);
        mainPanel.add(stationControlPanel);
        mainPanel.add(stationServicesLabel);
        mainPanel.add(stationServicesPanel);

        add(mainPanel);
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
            highlightSelectedStation(); // Highlight the selected station
        }
    }

    // Action listener for start station button
    private class StartStationButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedStation != -1) { // Check if a station is selected
                if (stationEnabled[selectedStation]) { // Check if station is enabled
                    customerGUIs[selectedStation] = new CustomerStation(selectedStation + 1); // Adjusted index for station number
                    customerGUIs[selectedStation].setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Selected station is disabled. Please enable it.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a station first.");
            }
        }
    }

    // Action listener for close station button
    private class CloseStationButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedStation != -1 && customerGUIs[selectedStation] != null) { // Check if a station is selected and GUI is created
                customerGUIs[selectedStation].dispose(); // Close the Customer GUI page for the selected station
            } else {
                JOptionPane.showMessageDialog(null, "Please select a station with an active Customer Station GUI.");
            }
        }
    }

    // Action listener for block station button
    private class BlockStationButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedStation != -1) { // Check if a station is selected
                stationEnabled[selectedStation] = false; // Disable the selected station
                if (customerGUIs[selectedStation] != null) { // Check if GUI is created for the selected station
                    customerGUIs[selectedStation].freezeGUI(); // Freeze the GUI
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a station first.");
            }
        }
    }

    // Action listener for unblock station button
    private class UnblockStationButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedStation != -1) { // Check if a station is selected
                stationEnabled[selectedStation] = true; // Enable the selected station
                if (customerGUIs[selectedStation] != null) { // Check if GUI is created for the selected station
                    customerGUIs[selectedStation].unfreezeGUI(); // Unfreeze the GUI
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a station first.");
            }
        }
    }

    // Action listener for enable station button
    private class EnableStationButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedStation != -1) { // Check if a station is selected
                stationEnabled[selectedStation] = true; // Enable the selected station
                if (customerGUIs[selectedStation] != null) { // Check if GUI is created for the selected station
                    customerGUIs[selectedStation].unfreezeGUI(); // Unfreeze the GUI
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
                stationEnabled[selectedStation] = false; // Disable the selected station
                if (customerGUIs[selectedStation] != null) { // Check if GUI is created for the selected station
                    customerGUIs[selectedStation].freezeGUI(); // Freeze the GUI
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a station first.");
            }
        }
    }

    public static void notifyAssistanceRequired(int stationNumber) {
        JOptionPane.showMessageDialog(null, "Station " + (stationNumber + 1) + " requires assistance.");
    }
    
    // Method to highlight the selected station button
    private void highlightSelectedStation() {
        for (int i = 0; i < stationButtons.length; i++) {
            if (i == selectedStation) {
                stationButtons[i].setBackground(Color.YELLOW); // Change background color for selected station
            } else {
                stationButtons[i].setBackground(null); // Reset background color for other stations
            }
        }
    }
    
    public void bagdiscpreancydectected() {
    	// Attendant approves discrepancy 
    	SelfCheckoutStationSoftware checkout = new SelfCheckoutStationSoftware(null);
    	checkout.setStationBlock(false);
   
    }
     
}

