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
	
	public AddtoBagging(String item) {

	    setTitle("Add to Bag");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setSize(600, 500);
	    setLocationRelativeTo(null);

	    // Main panel with BoxLayout for vertical alignment
	    JPanel mainPanel = new JPanel();
	    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

	    // Label for the large text display
	    JLabel bigTextLabel = new JLabel("Item: " + item);
	    bigTextLabel.setFont(new Font("Arial", Font.BOLD, 32));
	    bigTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

	    // Label for the small text display
	    JLabel smallTextLabel = new JLabel("Please add item to bagging area.");
	    smallTextLabel.setFont(new Font("Arial", Font.PLAIN, 12)); 
	    smallTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 

	    // Button
	    JButton button = new JButton("Place item");
	    button.setAlignmentX(Component.CENTER_ALIGNMENT);
	    button.setFont(new Font("Arial", Font.PLAIN, 16));
	    button.setPreferredSize(new Dimension(100, 30));

	    
	    button.addActionListener(e -> {
	    	// CODE TO ADD ITEM TO ORDER GOES HERE
	    	dispose();
	    });
	    
	   
	    mainPanel.add(Box.createVerticalGlue());
	    mainPanel.add(bigTextLabel);
	    mainPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Small space between big text and small text
	    mainPanel.add(smallTextLabel);
	    mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
	    mainPanel.add(button);
	    mainPanel.add(Box.createVerticalGlue());


	    add(mainPanel);
	    setVisible(true);
	}
}
