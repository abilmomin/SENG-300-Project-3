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

package com.thelocalmarketplace.software.communication.GUI.AttendantStation;

import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.CustomerStation;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.StartSession;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
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
    public final boolean[] stationAssistanceRequested = new boolean[NUM_STATIONS]; // Track assistance requests

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
            String buttonText = getjButtonText(i);
            JButton checkoutButton = new JButton(buttonText);
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
    private static String getjButtonText(int i) {
        String checkoutButtonText = "Checkout Station " + (i + 1);
        String stationTypeLabel = switch (i) {
            case 0 -> " (Gold)";
            case 1 -> " (Silver)";
            case 2, 3 -> " (Bronze)";
            default -> "";
        };
        return checkoutButtonText + stationTypeLabel;
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
    
    public int getCurrentStationNumber() {
    	return this.selectedStation + 1;
    }
    
    public void bagdiscpreancydectected(SelfCheckoutStationSoftware instance, CustomerStation customerStation2) {
        // Display a message dialog to prompt the user
        int option = JOptionPane.showConfirmDialog(this, "Bags Too Heavy, Inspect.", "Bag Discrepancy Detected", JOptionPane.OK_CANCEL_OPTION);
        
        // Check if the user clicked OK
        if (option == JOptionPane.OK_OPTION) {
            // User clicked OK, proceed with unblocking the station
            
            // Unblocking the station
            instance.setStationUnblock();
            customerStation2.customerPopUp("You may now continue");
        } else {
        	JOptionPane.getRootFrame().dispose();
        }
    }


    public boolean bulkItemRequest(String message) {
        JOptionPane.showMessageDialog(this, message);
        return true;
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
            String buttonText = getjButtonText(i);

            if (i == selectedStation) {
                stationStartButtons[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
            } else {
                stationStartButtons[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            }

            // If stationAssistanceRequested[i] is true, additional handling may be needed to reflect assistance requested state
            if (stationAssistanceRequested[i]) {
                stationStartButtons[i].setText("(Assistance Requested) " + buttonText);
                stationStartButtons[i].setBackground(Color.ORANGE); // Mark as needing assistance
            } else {
                stationStartButtons[i].setBackground(null); // Reset background color if no assistance is requested
            }
        }
    }
    
    // Method to revert the highlighting of station buttons
    void revertHighlight() {
        for (int i = 0; i < stationStartButtons.length; i++) {
            String buttonText = getjButtonText(i);
            stationStartButtons[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Reset border
            if (stationAssistanceRequested[i]) {
                stationStartButtons[i].setText("(Assistance Requested) " + buttonText);
                stationStartButtons[i].setBackground(Color.ORANGE); // Mark as needing assistance
            } else {
                // Remove the additional text if assistance is not requested
                String originalText = buttonText.replace("(Assistance Requested) ", "");
                stationStartButtons[i].setText(originalText);
                stationStartButtons[i].setBackground(null); // Reset background color if no assistance is requested
            }
        }
    }

    public void setStationAssistanceRequested(int stationNumber, boolean requested) {
        if (stationNumber >= 0 && stationNumber < stationStartButtons.length) {
            stationAssistanceRequested[stationNumber] = requested;
            stationSoftwareInstances[selectedStation].setStationBlock();
            highlightSelectedStation(stationNumber);
        }
    } 

    public void warnForChange(BigDecimal change) {
        JDialog dialog = new JDialog(this, "Amount Due", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("Change Due: $" + change + " at station " + selectedStation+1);
        JButton refillCoinsButton = new JButton("Refill Coins");
        refillCoinsButton.addActionListener(e -> {
                stationSoftwareInstances[selectedStation].setStationUnblock();
                setStationAssistanceRequested(selectedStation, false);
                revertHighlight();
                System.out.println("HELP");
                attendantListeners.getRefillCoinServiceButtonListener().actionPerformed(e);
                dialog.dispose();
        });
        
        JButton refillBanknotessButton = new JButton("Refill Banknotes");
        refillBanknotessButton.addActionListener(e -> {
                stationSoftwareInstances[selectedStation].setStationUnblock();
                setStationAssistanceRequested(selectedStation, false);
                revertHighlight();
                System.out.println("HELP");
                attendantListeners.getRefillBanknotesServiceButtonListener().actionPerformed(e);	
                dialog.dispose();
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(label);
        panel.add(refillCoinsButton);
        panel.add(refillBanknotessButton);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(AttendantPageGUI::new);
    }
     
}
