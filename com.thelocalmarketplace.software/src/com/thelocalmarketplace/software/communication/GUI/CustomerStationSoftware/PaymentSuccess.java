package com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.printer.IReceiptPrinter;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.funds.Funds;
import com.thelocalmarketplace.software.funds.Receipt;

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



		JLabel receiptQuestion = new JLabel("Would you like a receipt?");
		JCheckBox ck1 = new JCheckBox("Yes");
		ck1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Receipt rec = new Receipt(stationSoftware.getStationHardware().getPrinter(), stationSoftware.getFunds());
				try {
					JOptionPane.showMessageDialog(PaymentSuccess.this, rec.printReceipt());
					System.exit(EXIT_ON_CLOSE);
				} catch (HeadlessException | EmptyDevice | OverloadedDevice e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});
		JCheckBox ck2 = new JCheckBox("No");
		ck2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(PaymentSuccess.this, "Thank you for shopping with us! We hope to see you again!");
				System.exit(EXIT_ON_CLOSE);

			}

		});


//	    JLabel bye = new JLabel("Have a great day!");
//	    bye.setAlignmentX(Component.CENTER_ALIGNMENT);
//	    bye.setFont(new Font("Arial", Font.BOLD, 32));

		mainPanel.add(Box.createVerticalGlue());
		mainPanel.add(bigTextLabel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Small space between big text and small text
		mainPanel.add(mediumTextLabel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		mainPanel.add(smallTextLabel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		mainPanel.add(receiptQuestion);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Small space between big text and small text
		mainPanel.add(ck1);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		mainPanel.add(ck2);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
//	    mainPanel.add(bye);
//	    mainPanel.add(Box.createVerticalGlue());

		add(mainPanel);

		setVisible(true);
	}

	 public static void main(String[] args) {
	 	PaymentSuccess success = new PaymentSuccess(12, new SelfCheckoutStationSoftware(new SelfCheckoutStationGold()));
	 }
}
