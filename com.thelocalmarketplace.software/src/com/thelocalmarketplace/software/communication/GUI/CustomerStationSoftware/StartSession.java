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

package com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware;

import javax.swing.*;
import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantPageGUI;
import java.awt.*;
import java.awt.event.*;


/**
 * This class represents the start session window of the self-checkout station.
 */
@SuppressWarnings("serial")
public class StartSession extends JFrame {
    private final int SCREEN_WIDTH = 900;
    private final int SCREEN_HEIGHT = 700;
    private int selectedStation;
    private CustomerStation customerStation;
    private AttendantPageGUI attendantPageGUI;
    private SelfCheckoutStationSoftware stationSoftwareInstance;
    private boolean mouseListenerEnabled = true;

    /**
     * Starts a customer session with the specified station number.
     * 
     * @param stationNumber 
     * 				The number of the self-checkout station.
     * @param stationSoftwareInstance 
     * 				The instance of the self-checkout station software.
     * @param scale 
     * 				The electronic scale associated with the station.
     */
    public void startCustomerSession(int stationNumber, SelfCheckoutStationSoftware stationSoftwareInstance, AbstractElectronicScale scale) {
        if (customerStation == null) {
            this.customerStation = new CustomerStation(stationNumber,stationSoftwareInstance,scale, attendantPageGUI);
            this.customerStation.setVisible(true);
            // Update the reference in AttendantPageGUI
            if (attendantPageGUI != null) {
                attendantPageGUI.updateCustomerStation(stationNumber, customerStation);
            }
        }
    }
    
    /**
     * Sets the attendant page GUI for the start session.
     * 
     * @param attendantPageGUI 
     * 			The attendant page GUI to be set.
     */
    public void setAttendantPageGUI(AttendantPageGUI attendantPageGUI) {
        this.attendantPageGUI = attendantPageGUI;
    }

    /**
     * Constructs a StartSession object.
     * 
     * @param stationNumber 
     * 				The number of the station.
     * @param stationSoftwareInstance 
     * 				The instance of the self checkout station software.
     * @param scale 
     * 				The electronic scale used in the station.
     */
    public StartSession(int stationNumber, SelfCheckoutStationSoftware stationSoftwareInstance, AbstractElectronicScale scale) {
    	this.stationSoftwareInstance = stationSoftwareInstance;
    	this.stationSoftwareInstance.setStationActive(false);
    	this.selectedStation = stationNumber;
    	
        setTitle("Welcome to the Market - Station " + selectedStation);
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Content panel for the main content
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        contentPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (mouseListenerEnabled) {
                    stationSoftwareInstance.setStationActive(true);
                    // Once the session is started, dispose the current frame and open the CustomerStation GUI
                    StartSession.this.dispose(); // Close StartSession window
                    startCustomerSession(stationNumber, stationSoftwareInstance, scale);
                }
            }
        });

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome to the Self-Checkout Station!");
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Set the font size of the welcome label
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Start session label
        JLabel startSessionLabel = new JLabel("Touch Anywhere to Start");
        startSessionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // set the font size of the start session label
        startSessionLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        // Add components to the panel with constraints
        contentPanel.add(Box.createVerticalStrut(50));
        contentPanel.add(welcomeLabel, gbc);
        contentPanel.add(Box.createVerticalStrut(50));
        contentPanel.add(startSessionLabel, gbc);

        add(contentPanel, BorderLayout.CENTER);
        SettingsPanel settingsPanel = new SettingsPanel(null);
        settingsPanel.setVisible(true);
        add(settingsPanel, BorderLayout.NORTH);

        setVisible(true);
    }
    
    /**
     * Displays a pop-up message.
     * 
     * @param message 
     * 				The message to be displayed.
     */
    public void sessionPopUp(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    /**
     * Enables the mouse listener for user interaction.
     */
    public void enableMouseListener() {
        mouseListenerEnabled = true;
    }

    /**
     * Disables the mouse listener.
     */
    public void disableMouseListener() {
        mouseListenerEnabled = false;
    }
}