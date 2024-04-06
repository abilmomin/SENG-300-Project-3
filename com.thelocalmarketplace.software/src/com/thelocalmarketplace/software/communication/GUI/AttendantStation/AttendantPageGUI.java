package com.thelocalmarketplace.software.communication.GUI.AttendantStation;

import javax.swing.*;

import com.jjjwelectronics.OverloadedDevice;
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

import ca.ucalgary.seng300.simulation.SimulationException;
import powerutility.PowerGrid;

import java.awt.*;
import java.awt.event.*;

public class AttendantPageGUI extends JFrame {
    // CONSTANTS FOR THE GUI
    private static final int NUM_STATIONS = 4;
    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 700;

    // Button panel for managing customer checkout stations
    private final JPanel stationStartPanel = new JPanel(new GridLayout(2, 2));

    // Button panel for station controls
    private final JPanel stationControlPanel = new JPanel(new FlowLayout());
    private final JButton enableStation = new JButton("Enable Station");
    private final JButton disableStation = new JButton("Disable Station");
    private final JButton clearStationSignal = new JButton("Clear Station Signal");

    // Button panel for station controls
    private final JPanel stationServicesPanel = new JPanel(new FlowLayout());
    private final JButton startStation = new JButton("Turn On Station");
    private final JButton closeStation = new JButton("Turn Off Station");
    private final JButton refillCoins = new JButton("Refill Coins");
    private final JButton refillBanknotes = new JButton("Refill Banknotes");
    private final JButton refillReceiptPaper = new JButton("Refill Receipt Paper");
    private final JButton emptyCoins = new JButton("Empty Coins");
    private final JButton emptyBanknotes = new JButton("Empty Banknotes");
    private final JButton refillReceiptInk = new JButton("Refill Receipt Ink");
    private final JButton addItemByText = new JButton("Add Item by Text Search");

    // Panel for customer services
    private final JPanel customerServicesPanel = new JPanel(new FlowLayout());
    private int selectedStation = -1; // Variable to store the selected station number
    private final JButton[] stationStartButtons = new JButton[NUM_STATIONS]; // Array to hold the station buttons
    private final StartSession[] startSessions = new StartSession[NUM_STATIONS]; // Array to hold StartSession instances
    private final boolean[] stationEnabled = new boolean[NUM_STATIONS]; // Array to keep track of station status
    private final CustomerStation[] customerStation = new CustomerStation[NUM_STATIONS];
    private final SelfCheckoutStationSoftware[] stationSoftwareInstances = new SelfCheckoutStationSoftware[NUM_STATIONS];

    // Variables for station hardware
    private AbstractElectronicScale scale;
    private AbstractSelfCheckoutStation checkoutStation;
    private final boolean[] stationAssistanceRequested = new boolean[NUM_STATIONS]; // Track assistance requests

    public AttendantPageGUI() {
        // Initialize the GUI
        setupGUI();
        setupStationStartPanel();
        createStationControls();
        createStationServices();
        createCustomerServices();

        // Adding the panels to the main panel
        // Variables for the GUI
        JPanel mainPanel = new JPanel(new GridLayout(9, 1));
        // Label for stations
        JLabel stationsLabel = new JLabel("Checkout Stations: ");
        mainPanel.add(stationsLabel);
        mainPanel.add(stationStartPanel);
        // Label for station controls
        JLabel stationControlLabel = new JLabel("Checkout Station Controls: ");
        mainPanel.add(stationControlLabel);
        mainPanel.add(stationControlPanel);
        // Label for station controls
        JLabel stationServicesLabel = new JLabel("Hardware Services: ");
        mainPanel.add(stationServicesLabel);
        mainPanel.add(stationServicesPanel);
        // Label for customer services
        JLabel customerServicesLabel = new JLabel("Customer Services: ");
        mainPanel.add(customerServicesLabel);
        mainPanel.add(customerServicesPanel);

        add(mainPanel);
        setVisible(true);
    }

    private void setupGUI() {
        setTitle("Attendant Page");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
    }

    private void setupStationStartPanel() {
        // Create station buttons
        for (int i = 0; i < NUM_STATIONS; i++) {
            JButton checkoutButton = getjButton(i);
            checkoutButton.addActionListener(new StationButtonListener(i));
            stationStartPanel.add(checkoutButton);
            stationStartButtons[i] = checkoutButton; // Add button to the array
            stationEnabled[i] = true; // Initialize station as enabled
        }
    }

    private void createStationControls() {
        enableStation.addActionListener(new EnableStationButtonListener());
        disableStation.addActionListener(new DisableStationButtonListener());
        clearStationSignal.addActionListener(new ClearStationSignalButtonListener());

        stationControlPanel.add(enableStation);
        stationControlPanel.add(disableStation);
        stationControlPanel.add(clearStationSignal);
    }

    private void createStationServices() {
        startStation.addActionListener(new StartStationButtonListener());
        closeStation.addActionListener(new CloseStationButtonListener());

        refillCoins.addActionListener(new refillCoinServiceButtonListener());
        refillBanknotes.addActionListener(new refillBanknotesServiceButtonListener());
        refillReceiptPaper.addActionListener(new refillReceiptPaperServiceButtonListener());
        refillReceiptInk.addActionListener(new refillReceiptInkServiceButtonListener());
        emptyCoins.addActionListener(new emptyCoinServiceButtonListener());
        emptyBanknotes.addActionListener(new emptyBanknotesServiceButtonListener());

        stationServicesPanel.add(startStation);
        stationServicesPanel.add(closeStation);

        stationServicesPanel.add(refillCoins);
        stationServicesPanel.add(refillBanknotes);
        stationServicesPanel.add(refillReceiptPaper);
        stationServicesPanel.add(refillReceiptInk);
        stationServicesPanel.add(emptyCoins);
        stationServicesPanel.add(emptyBanknotes);

    }

    private void createCustomerServices() {
        addItemByText.addActionListener(new customerServiceButtonListener());
        customerServicesPanel.add(addItemByText);
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
                Products product = new Products(stationSoftwareInstances[selectedStation]);
                //product.addItemByTextSearch(searchText, null);
            }
        }
    }

    // Method to get the station button with the station number
    private static JButton getjButton(int i) {
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
        return checkoutButton;
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


                        checkoutStation.plugIn(PowerGrid.instance());
                        checkoutStation.turnOn();

                        SwingUtilities.invokeLater(() -> {
                            stationSoftwareInstances[selectedStation] = new SelfCheckoutStationSoftware(checkoutStation);
                            stationSoftwareInstances[selectedStation].setStationUnblock();

                            if (stationEnabled[selectedStation]) {
                                if (startSessions[selectedStation] == null) {
                                    startSessions[selectedStation] = new StartSession(selectedStation + 1, stationSoftwareInstances[selectedStation],scale);
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
            if (selectedStation != -1) {
                if (customerStation[selectedStation] != null) { // Check if a station is selected and GUI is created
                    customerStation[selectedStation].dispose(); // Close the Customer GUI page for the selected station
                    customerStation[selectedStation] = null; // Reset the reference
                    startSessions[selectedStation] = null; // Reset the reference
                } else if(startSessions[selectedStation] != null) {
                    startSessions[selectedStation].dispose(); // Close the StartSession page for the selected station
                    startSessions[selectedStation] = null; // Reset the reference
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
                            new Thread(() -> waitForSessionCompletion(selectedStation)).start();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a station first.");
            }
        }
    }

    // Method to wait for session completion
    private void waitForSessionCompletion(int stationNumber) {
        try {
            while (stationSoftwareInstances[stationNumber].getStationActive()) {
                // Sleep for a certain period before checking again
                Thread.sleep(5000); // Sleep for 5 seconds
            }
            // Once the session is complete, disable the station
            SwingUtilities.invokeLater(() -> {
                stationSoftwareInstances[selectedStation].setStationBlock();
                customerStation[selectedStation].freezeGUI();
            });
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    public void bagdiscpreancydectected(SelfCheckoutStationSoftware instance) {
    	// Attendant approves discrepancy 
    	JOptionPane.showMessageDialog(this, "Bags Too Heavy, Inspect.");
    	instance.setStationUnblock();
    	JOptionPane.showMessageDialog(this, "Customer may now continue.");
    }
    
    public void weightDiscpreancydNotify(SelfCheckoutStationSoftware instance) {
    	// Attendant approves discrepancy 
    	JOptionPane.showMessageDialog(this, "Weight Discrepancy Detected.");
    	instance.setStationUnblock();
    }

    // Method to highlight the selected station button
    private void highlightSelectedStation() {
        for (int i = 0; i < stationStartButtons.length; i++) {
            if (i == selectedStation && !stationAssistanceRequested[i]) {
                stationStartButtons[i].setBackground(Color.YELLOW); // Highlight selected station
                stationStartButtons[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            } else if (!stationAssistanceRequested[i]) { // Check if the station has not requested assistance
                stationStartButtons[i].setBackground(null); // Reset background color only if no assistance is requested
                stationStartButtons[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            }
            // If stationAssistanceRequested[i] is true, do nothing to preserve the red color
        }
    }

    public void setStationAssistanceRequested(int stationNumber, boolean requested) {
        if (stationNumber >= 0 && stationNumber < stationStartButtons.length) {
            stationAssistanceRequested[stationNumber] = requested;
            if (requested) {
                stationStartButtons[stationNumber].setBackground(Color.RED); // Mark as needing assistance
            } else { // Reset color if not selected
                stationStartButtons[stationNumber].setBackground(null);
            } // If it is the selected station, it should remain yellow, handled by highlightSelectedStation
        }
    }


    public static void main(final String[] args) {
        SwingUtilities.invokeLater(AttendantPageGUI::new);
    }
     
}
