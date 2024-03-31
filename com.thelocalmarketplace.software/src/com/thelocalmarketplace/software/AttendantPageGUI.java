package com.thelocalmarketplace.software;

import javax.swing.*;

import com.thelocalmarketplace.software.communication.CustomerStation;

import java.awt.*;
import java.awt.event.*;

public class AttendantPageGUI extends JFrame {
    private int selectedStation = -1; // Variable to store the selected station number
    private JButton[] stationButtons; // Array to hold the station buttons
    private CustomerStation[] customerGUIs;
    public AttendantPageGUI() {

        // Setup
        setTitle("Attendant Page");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new GridLayout(5, 1));

        // Label for stations
        JLabel stationsLabel = new JLabel("Checkout Stations: ");

        // Button panel for managing customer checkout stations
        JPanel stationPanel = new JPanel(new GridLayout(2, 2));
        stationButtons = new JButton[4]; // Initialize the station buttons array
        customerGUIs = new CustomerStation[4];
        // Create station buttons
        for (int i = 0; i < 4; i++) {
            JButton checkoutButton = new JButton("Checkout Station " + (i + 1));
            checkoutButton.addActionListener(new StationButtonListener(i));
            stationPanel.add(checkoutButton);
            stationButtons[i] = checkoutButton; // Add button to the array
        }

        // Label for station controls
        JLabel stationControlLabel = new JLabel("Checkout Station Controls: ");

        // Button panel for station controls
        JPanel stationControlPanel = new JPanel(new FlowLayout());
        JButton startStation = new JButton("Start Station");
        JButton blockStation = new JButton("Block Station");
        JButton unblockStation = new JButton("Unblock Station");
        JButton closeStation = new JButton("Close Station");

        startStation.addActionListener(new StartStationButtonListener());
        closeStation.addActionListener(new CloseStationButtonListener());
        stationControlPanel.add(startStation);
        stationControlPanel.add(blockStation);
        stationControlPanel.add(unblockStation);
        stationControlPanel.add(closeStation);

        // Adding the panels to the main panel
        mainPanel.add(stationsLabel);
        mainPanel.add(stationPanel);
        mainPanel.add(stationControlLabel);
        mainPanel.add(stationControlPanel);
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
                // Open the Customer GUI page for the selected station
//                CustomerStation customerGUI = new CustomerStation(selectedStation + 1); // Adjusted index for station number
//                customerGUI.setVisible(true);
            	  customerGUIs[selectedStation] = new CustomerStation(selectedStation + 1); // Adjusted index for station number
                  customerGUIs[selectedStation].setVisible(true);
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
}
