package com.thelocalmarketplace.software;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AttendantPageGUI extends JFrame{

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
}
