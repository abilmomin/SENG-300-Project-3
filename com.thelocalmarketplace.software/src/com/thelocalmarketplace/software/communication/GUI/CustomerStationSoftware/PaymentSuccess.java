package com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

public class PaymentSuccess extends JFrame {

	public PaymentSuccess(double change, SelfCheckoutStationSoftware stationSoftware) {
	    setTitle("Thank you!");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setSize(600, 500);
	    setLocationRelativeTo(null);

	    JPanel mainPanel = new JPanel();
	    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

	    JLabel bigTextLabel = new JLabel("Payment successful");
	    bigTextLabel.setFont(new Font("Arial", Font.PLAIN, 16));
	    bigTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel mediumTextLabel = new JLabel("Amount Due: $" + stationSoftware.getTotalOrderPrice());
	    mediumTextLabel.setFont(new Font("Arial", Font.PLAIN, 15));
	    mediumTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    
	    JLabel smallTextLabel = new JLabel("Change Returned: $" + change);
	    smallTextLabel.setFont(new Font("Arial", Font.PLAIN, 14)); 
	    smallTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
	    
	    JLabel bye = new JLabel("Have a great day!");
	    bye.setAlignmentX(Component.CENTER_ALIGNMENT);
	    bye.setFont(new Font("Arial", Font.BOLD, 32));
	
	    mainPanel.add(Box.createVerticalGlue());
	    mainPanel.add(bigTextLabel);
	    mainPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Small space between big text and small text
		mainPanel.add(mediumTextLabel);
	    mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
	    mainPanel.add(smallTextLabel);
	    mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
	    mainPanel.add(bye);
	    mainPanel.add(Box.createVerticalGlue());
	    
	    add(mainPanel);
	    setVisible(true);
	}
	
	// public static void main(String[] args) {
	// 	PaymentSuccess success = new PaymentSuccess(12);
	// }
}
