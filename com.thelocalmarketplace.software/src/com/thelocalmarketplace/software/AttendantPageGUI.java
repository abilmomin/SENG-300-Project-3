package com.thelocalmarketplace.software;

import javax.swing.*;

import com.thelocalmarketplace.software.communication.CustomerStation;

import java.awt.*;
import java.awt.event.*;

public class AttendantPageGUI extends JFrame{
	 private int selectedStation = -1; // Variable to store the selected station number

	 public AttendantPageGUI() {
		 
		  //setup
	        setTitle("Attendant Page");
	        setSize(800, 600);
	        setDefaultCloseOperation(EXIT_ON_CLOSE);
	        setLocationRelativeTo(null);
	        
	        //main panel
	        JPanel mainPanel = new JPanel(new GridLayout(5, 1));

	        //label for stations
	        JLabel stationsLabel = new JLabel("Checkout Stations: ");
	        // Button panel for managing customer checkout stations
	        JPanel stationPanel = new JPanel(new GridLayout(2, 2));
	        JButton checkout1Button = new JButton("Checkout Station 1");
	        JButton checkout2Button = new JButton("Checkout Station 2");
	        JButton checkout3Button = new JButton("Checkout Station 3");
	        JButton checkout4Button = new JButton("Checkout Station 4");

	        // Add action listeners to station buttons
	        checkout1Button.addActionListener(new StationButtonListener(1));
	        checkout2Button.addActionListener(new StationButtonListener(2));
	        checkout3Button.addActionListener(new StationButtonListener(3));
	        checkout4Button.addActionListener(new StationButtonListener(4));
	        
	        stationPanel.add(checkout1Button);
	        stationPanel.add(checkout2Button);
	        stationPanel.add(checkout3Button);
	        stationPanel.add(checkout4Button);

	        //label for station controls
	        JLabel stationControlLabel = new JLabel("Checkout Station Controls: ");
	        // Button panel for station controls 
	        JPanel stationControlPanel = new JPanel(new FlowLayout());
	        JButton startStation = new JButton("Start Station");
	        JButton blockStation = new JButton("Block Station");
	        JButton unblockStation = new JButton("Unblock Station");
	        JButton closeStation = new JButton("Close Station");
	        
	        startStation.addActionListener(new StartStationButtonListener());
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
	        }
	    }

	    // Action listener for start station button
	    private class StartStationButtonListener implements ActionListener {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            if (selectedStation != -1) { // Check if a station is selected
	                // Open the Customer GUI page for the selected station
	                CustomerStation customerGUI = new CustomerStation(selectedStation);
	                customerGUI.setVisible(true);
	            } else {
	                JOptionPane.showMessageDialog(null, "Please select a station first.");
	            }
	        }
	    }
}
