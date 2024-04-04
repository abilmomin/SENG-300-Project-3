package com.thelocalmarketplace.software.communication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartSession extends JFrame {
    private int selectedStation; // Variable to hold the selected station number
    private CustomerStation customerStation; // Reference to the CustomerStation GUI

    // Method to start a customer session (this might be called based on some user interaction within StartSession)
    public void startCustomerSession(int stationNumber) {
        if (customerStation == null) {
            customerStation = new CustomerStation(stationNumber);
            customerStation.setVisible(true);
        }
    }

    // Methods to control the CustomerStation GUI
    public void freezeCustomerGUI() {
        if (customerStation != null) {
            customerStation.freezeGUI();
        }
    }

    public void unfreezeCustomerGUI() {
        if (customerStation != null) {
            customerStation.unfreezeGUI();
        }
    }
    public StartSession(int stationNumber) {
        this.selectedStation = stationNumber; // Set the selected station number

        // Frame initialization
        setTitle("Welcome to the Market - Station " + selectedStation);
        setSize(900, 700); // Set the size of the window
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main panel with GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER; // End row after this component
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch component horizontally
        gbc.anchor = GridBagConstraints.CENTER; // Center component

        // Make the panel clickable
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Implement session start logic here
                JOptionPane.showMessageDialog(StartSession.this, "Session Started!");
                // Once the session is started, dispose the current frame and open the CustomerStation GUI
                StartSession.this.dispose(); // Close StartSession window
                new CustomerStation(stationNumber); // Create and display a new instance of CustomerStation (using station 1 for this example)
            }
        });

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome!");
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Set the font size of the welcome label
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Start session label
        JLabel startSessionLabel = new JLabel("Click anywhere to start a session");
        startSessionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // set the font size of the start session label
        startSessionLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        // Add components to the panel with constraints
        panel.add(Box.createVerticalStrut(50)); // Vertical spacer
        panel.add(welcomeLabel, gbc);
        panel.add(Box.createVerticalStrut(50)); // Vertical spacer
        panel.add(startSessionLabel, gbc);

        // Adding the panel to the frame
        add(panel);

        // Make the GUI visible
        setVisible(true);
    }

    public static void main(String[] args) {
        // Ensure the GUI is created on the Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new StartSession(1);
            }
        });
    }
}