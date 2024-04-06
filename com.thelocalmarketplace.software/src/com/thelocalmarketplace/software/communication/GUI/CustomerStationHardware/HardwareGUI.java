package com.thelocalmarketplace.software.communication.GUI.CustomerStationHardware;


import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantLoginPage;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.CustomerStation;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.StartSession;

import javax.swing.*;
import java.awt.*;

public class HardwareGUI extends JFrame {
    // CONSTANTS FOR THE GUI
    private static final int NUM_STATIONS = 4;
    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 700;

    // Button panel for managing customer checkout stations
    private final JPanel hardwareCustomerPanel = new JPanel(new FlowLayout());

    // Button panel for station controls
    private final JPanel hardwareAttendantPanel = new JPanel(new FlowLayout());
    private final JButton viewBaggingArea = new JButton("View Bagging Area"); // open bottom menu with button add or remove
    private final JButton handheldBarcodeScan = new JButton("Handheld Barcode Scan");
    private final JButton barcodeScan = new JButton("Barcode Scan");
    private final JButton payWithCash = new JButton("Pay With Cash"); // open bottom menu with denominations
    private final JButton payWithBanknote = new JButton("Pay With Banknote"); // open bottom menu with denominations
    private final JButton payWithDebitCredit = new JButton("Pay With Debit/Credit");

    // Button panel for station controls
    private final JButton refillCoins = new JButton("Refill Coins");
    private final JButton refillBanknotes = new JButton("Refill Banknotes");
    private final JButton refillReceiptPaper = new JButton("Refill Receipt Paper");
    private final JButton emptyCoins = new JButton("Empty Coins");
    private final JButton emptyBanknotes = new JButton("Empty Banknotes");
    private final JButton refillReceiptInk = new JButton("Refill Receipt Ink");

    public HardwareGUI() {
        // Initialize the GUI
        setupGUI();
        setupHardwareCustomerPanel();
        setupHardwareAttendantPanel();

        // Adding the panels to the main panel
        // Variables for the GUI
        JPanel mainPanel = new JPanel(new GridLayout(9, 1));
        // Label for stations
        JLabel hardwareCustomerLabel = new JLabel("Hardware for customer: ");
        mainPanel.add(hardwareCustomerLabel);
        mainPanel.add(hardwareCustomerPanel);
        // Label for station controls
        JLabel hardwareAttendantLabel = new JLabel("Hardware for attendant: ");
        mainPanel.add(hardwareAttendantLabel);
        mainPanel.add(hardwareAttendantPanel);

        add(mainPanel);
        setVisible(true);
    }

    private void setupGUI() {
        setTitle("Hardware Simulation Page");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
    }

    private void setupHardwareCustomerPanel() {
        hardwareCustomerPanel.add(viewBaggingArea);
        hardwareCustomerPanel.add(handheldBarcodeScan);
        hardwareCustomerPanel.add(barcodeScan);
        hardwareCustomerPanel.add(payWithCash);
        hardwareCustomerPanel.add(payWithBanknote);
        hardwareCustomerPanel.add(payWithDebitCredit);
    }

    private void setupHardwareAttendantPanel() {
        hardwareAttendantPanel.add(refillCoins);
        hardwareAttendantPanel.add(refillBanknotes);
        hardwareAttendantPanel.add(refillReceiptPaper);
        hardwareAttendantPanel.add(emptyCoins);
        hardwareAttendantPanel.add(emptyBanknotes);
        hardwareAttendantPanel.add(refillReceiptInk);
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(HardwareGUI::new);
    }
}