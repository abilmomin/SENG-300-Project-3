package com.thelocalmarketplace.software;

import javax.swing.*;

import com.jjjwelectronics.OverloadedDevice;
import com.tdc.CashOverloadException;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.PLUCodedItem;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.software.communication.CustomerStation;
import com.thelocalmarketplace.software.communication.StartSession;
import com.thelocalmarketplace.software.product.Products;

import ca.ucalgary.seng300.simulation.SimulationException;
import powerutility.PowerGrid;

import java.awt.*;
import java.awt.event.*;

public class AttendantPageGUI extends JFrame {
    private int selectedStation = -1; // Variable to store the selected station number
    private JButton[] stationButtons; // Array to hold the station buttons
    private StartSession[] startSessions; // Array to hold StartSession instances
    private boolean[] stationEnabled; // Array to keep track of station status
    private CustomerStation[] customerStation;
    private SelfCheckoutStationSoftware[] stationSoftwareInstances;
    private  AbstractSelfCheckoutStation checkoutStation;
    public AttendantPageGUI() {
        // Setup
        setTitle("Attendant Page");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Initialize the stationSoftwareInstances array here
        stationSoftwareInstances = new SelfCheckoutStationSoftware[4];

        // Main panel
        JPanel mainPanel = new JPanel(new GridLayout(9, 1));

        // Label for stations
        JLabel stationsLabel = new JLabel("Checkout Stations: ");

        // Button panel for managing customer checkout stations
        JPanel stationPanel = new JPanel(new GridLayout(2, 2));
        stationButtons = new JButton[4]; // Initialize the station buttons array
        startSessions = new StartSession[4]; // Initialize the StartSession instances array
        stationEnabled = new boolean[4]; // Initialize the station status array
        customerStation = new CustomerStation[4];
     // Create station buttons
        for (int i = 0; i < 4; i++) {
            JButton checkoutButton = new JButton("Checkout Station " + (i + 1));
            String stationTypeLabel = "";
            switch (i) {
                case 0:
                    stationTypeLabel = " (Gold)";
                    break;
                case 1:
                    stationTypeLabel = " (Silver)";
                    break;
                case 2:
                case 3:
                    stationTypeLabel = " (Bronze)";
                    break;
            }
            checkoutButton.setText(checkoutButton.getText() + stationTypeLabel);
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
        JButton closeStation = new JButton("Close Station");
        JButton enableStation = new JButton("Enable Station");
        JButton disableStation = new JButton("Disable Station");

        startStation.addActionListener(new StartStationButtonListener());
        closeStation.addActionListener(new CloseStationButtonListener());
        enableStation.addActionListener(new EnableStationButtonListener());
        disableStation.addActionListener(new DisableStationButtonListener());

        stationControlPanel.add(startStation);
        stationControlPanel.add(closeStation);
        stationControlPanel.add(enableStation);
        stationControlPanel.add(disableStation);

        // Label for station controls
        JLabel stationServicesLabel = new JLabel("Hardware Services: ");

        // Button panel for station controls
        JPanel stationServicesPanel = new JPanel(new GridLayout(2,1));
       
        JButton refillCoins = new JButton("Refill Coins");
        JButton refillBanknotes = new JButton("Refill Banknotes");
        JButton refillReciptPaper = new JButton("Refill Receipt Paper");
        JButton emptyCoins = new JButton("Empty Coins");
        JButton emptyBanknotes = new JButton("Empty Banknotes");
        JButton refillReceiptInk = new JButton("Refill Receipt Ink");
        
        // Add action listeners to buttons
        refillCoins.addActionListener(new ActionListener() {
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
        });

        refillBanknotes.addActionListener(new ActionListener() {
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
        });

        refillReciptPaper.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ALogic logic = new ALogic();
                try {
					logic.refillPrinterPaper(stationSoftwareInstances[selectedStation]);
				} catch (OverloadedDevice e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });

        emptyCoins.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ALogic logic = new ALogic();
                logic.emptyCoinStorage(stationSoftwareInstances[selectedStation]);
            }
        });

        emptyBanknotes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ALogic logic = new ALogic();
                logic.emptyBanknoteStorage(stationSoftwareInstances[selectedStation]);
            }
        });

        refillReceiptInk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ALogic logic = new ALogic();
                try {
					logic.refillPrinterInk(stationSoftwareInstances[selectedStation]);
				} catch (OverloadedDevice e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        
        
        
        
        stationServicesPanel.add(refillCoins);
        stationServicesPanel.add(refillBanknotes);
        stationServicesPanel.add(refillReciptPaper);
        stationServicesPanel.add(refillReceiptInk);
        stationServicesPanel.add(emptyCoins);
        stationServicesPanel.add(emptyBanknotes);
        
        JLabel customerServicesLabel = new JLabel("Customer Services: ");
        JPanel customerServicesPanel = new JPanel(new FlowLayout());
        JButton addItembyText = new JButton("Add Item by Text Search");
        
        addItembyText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a pop-up dialog with a search bar
                String searchText = JOptionPane.showInputDialog(null, "Enter search text:");

                // Call the method in Products class to add item by text search
                if (searchText != null && !searchText.isEmpty()) {
                	Products product = new Products( stationSoftwareInstances[selectedStation]);
                	//product.addItemByTextSearch(searchText, null);
                }
            }
        });

        customerServicesPanel.add(addItembyText);


        // Adding the panels to the main panel
        mainPanel.add(stationsLabel);
        mainPanel.add(stationPanel);
        mainPanel.add(stationControlLabel);
        mainPanel.add(stationControlPanel);
        mainPanel.add(stationServicesLabel);
        mainPanel.add(stationServicesPanel);
        mainPanel.add(customerServicesLabel);
        mainPanel.add(customerServicesPanel);

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
                // Use a separate thread to initialize SelfCheckoutStationBronze
                new Thread(() -> {
                    try { 
                        if (selectedStation == 0) {
                            checkoutStation = new SelfCheckoutStationGold();
                            System.out.println("SelfCheckoutStationGold initialized");
                        } else if (selectedStation == 1) {
                            checkoutStation = new SelfCheckoutStationSilver();
                            System.out.println("SelfCheckoutStationSilver initialized");
                        } else {
                            checkoutStation = new SelfCheckoutStationBronze();
                            System.out.println("SelfCheckoutStationBronze initialized");
                        }

                        checkoutStation.plugIn(PowerGrid.instance());
                        checkoutStation.turnOn();

                        SwingUtilities.invokeLater(() -> {
                            stationSoftwareInstances[selectedStation] = new SelfCheckoutStationSoftware(checkoutStation);
                            stationSoftwareInstances[selectedStation].setStationUnblock();

                            if (stationEnabled[selectedStation]) {
                                if (startSessions[selectedStation] == null) {
                                    startSessions[selectedStation] = new StartSession(selectedStation + 1);
                                    startSessions[selectedStation].setVisible(true);
                                    startSessions[selectedStation].setAttendantPageGUI(AttendantPageGUI.this); // Pass the reference to AttendantPageGUI
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

    public void updateCustomerStation(int stationNumber, CustomerStation customerStation) {
        this.customerStation[stationNumber - 1] = customerStation;
    }


    // Action listener for close station button
    private class CloseStationButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedStation != -1 && customerStation[selectedStation] != null) { // Check if a station is selected and GUI is created
            	customerStation[selectedStation].dispose(); // Close the Customer GUI page for the selected station
            } else {
                JOptionPane.showMessageDialog(null, "Please select a station with an active Customer Station GUI.");
            }
        }
    }


    // Action listener for enable station button
    private class EnableStationButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedStation != -1) { // Check if a station is selected
                stationEnabled[selectedStation] = true; // Enable the selected station
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
                stationEnabled[selectedStation] = false; // Disable the selected station
                if (customerStation[selectedStation] != null && stationSoftwareInstances[selectedStation].getStationBlock()== false) { // Check if GUI is created for the selected station
                	stationSoftwareInstances[selectedStation].setStationBlock();
                	customerStation[selectedStation].freezeGUI(); // Freeze the GUI
                	customerStation[selectedStation].customerPopUp("Out of Order.");

                	
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
    	checkout.setStationBlock();
   
    }
     
}
