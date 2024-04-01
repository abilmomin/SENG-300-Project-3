package com.thelocalmarketplace.software.communication;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.*;


// GENERAL LAYOUT
// Box layout is used to arrange items vertically or horizontally
// Glue is like a spring << add on before and after box components to squish them to the middle

public class AddtoBagging extends JFrame {
	private PLUCode pluCode;
	
	public AddtoBagging(PLUCode pluCode) {
		this.pluCode = pluCode;
		
	    setTitle("Add to Bag");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setSize(600, 500);
	    setLocationRelativeTo(null);

	    // Main panel with BoxLayout for vertical alignment
	    JPanel mainPanel = new JPanel();
	    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

	    // Label for the large text display
	    JLabel bigTextLabel = new JLabel("BIG TEXT");
	    bigTextLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set font for big text
	    bigTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align the label

	    // Label for the small text display
	    JLabel smallTextLabel = new JLabel("small text");
	    smallTextLabel.setFont(new Font("Arial", Font.PLAIN, 12)); // Set font for small text
	    smallTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align the label

	    // Button
	    JButton button = new JButton("TEXT HERE");
	    button.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align the button
 
	    button.addActionListener(e -> {
	    	dispose();
	    	
	    });
	    // Add some vertical glue before and after the components to center them in the window
	    mainPanel.add(Box.createVerticalGlue());
	    mainPanel.add(bigTextLabel);
	    mainPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Small space between big text and small text
	    mainPanel.add(smallTextLabel);
	    mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between small text and button
	    mainPanel.add(button);
	   mainPanel.add(Box.createVerticalGlue());

	    // Add the main panel to the frame
	    add(mainPanel);

	    // Set frame visibility
	    setVisible(true);
	}

	// NEEDS TO BE START FROM PLUCode
}
