package com.thelocalmarketplace.software;

import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AttendantPageGUI extends JFrame{
	 public AttendantPageGUI() {
	        setTitle("Attendant Page");
	        setSize(800, 600);
	        setDefaultCloseOperation(EXIT_ON_CLOSE);
	        setLocationRelativeTo(null);

	        JPanel mainPanel = new JPanel(new GridLayout(2, 1));

	        // Button panel for managing customer checkout stations
	        JPanel stationPanel = new JPanel(new GridLayout(2, 2));
	        JButton checkout1Button = new JButton("Checkout Station 1");
	        JButton checkout2Button = new JButton("Checkout Station 2");
	        JButton checkout3Button = new JButton("Checkout Station 3");
	        JButton shutdownButton = new JButton("Shutdown Station");
	        
	        stationPanel.add(checkout1Button);
	        stationPanel.add(checkout2Button);
	        stationPanel.add(checkout3Button);
	        stationPanel.add(shutdownButton);

	        // Adding the station panel to the main panel
	        mainPanel.add(stationPanel, BorderLayout.CENTER);

	        add(mainPanel);
	 }
}
