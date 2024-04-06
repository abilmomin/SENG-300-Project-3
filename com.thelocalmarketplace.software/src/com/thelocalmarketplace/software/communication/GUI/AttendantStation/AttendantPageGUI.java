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

    private AttendantListeners attendantListeners;
    public AttendantPageGUI() {
        this.attendantListeners = new AttendantListeners(this, selectedStation, stationSoftwareInstances, customerStation, startSessions, stationEnabled);
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
            checkoutButton.addActionListener(attendantListeners.getStationButtonListener(i));
            stationStartPanel.add(checkoutButton);
            stationStartButtons[i] = checkoutButton; // Add button to the array
            stationEnabled[i] = true; // Initialize station as enabled
        }
    }

    private void createStationControls() {
        enableStation.addActionListener(attendantListeners.getEnableStationButtonListener());
        disableStation.addActionListener(attendantListeners.getDisableStationButtonListener());
        clearStationSignal.addActionListener(attendantListeners.getClearStationSignalButtonListener());

        stationControlPanel.add(enableStation);
        stationControlPanel.add(disableStation);
        stationControlPanel.add(clearStationSignal);
    }

    private void createStationServices() {
        startStation.addActionListener(attendantListeners.getStartStationButtonListener());
        closeStation.addActionListener(attendantListeners.getCloseStationButtonListener());

        refillCoins.addActionListener(attendantListeners.getRefillCoinServiceButtonListener());
        refillBanknotes.addActionListener(attendantListeners.getRefillBanknotesServiceButtonListener());
        refillReceiptPaper.addActionListener(attendantListeners.getRefillReceiptPaperServiceButtonListener());
        refillReceiptInk.addActionListener(attendantListeners.getRefillReceiptInkServiceButtonListener());
        emptyCoins.addActionListener(attendantListeners.getEmptyCoinServiceButtonListener());
        emptyBanknotes.addActionListener(attendantListeners.getEmptyBanknotesServiceButtonListener());

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
        addItemByText.addActionListener(attendantListeners.getCustomerServiceButtonListener());
        customerServicesPanel.add(addItemByText);
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

    public void updateCustomerStation(int stationNumber, CustomerStation customerStation) {
        this.customerStation[stationNumber - 1] = customerStation;
    }

    // Method to wait for session completion
    void waitForSessionCompletion(int stationNumber) {
        try {
            while (stationSoftwareInstances[stationNumber].getStationActive()) {
                // Sleep for a certain period before checking again
                Thread.sleep(5000); // Sleep for 5 seconds
            }
            // Once the session is complete, disable the station
            SwingUtilities.invokeLater(() -> {
                stationSoftwareInstances[selectedStation].setStationBlock();
                
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
    void highlightSelectedStation(int selectedStation) {
        this.selectedStation = selectedStation;
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
