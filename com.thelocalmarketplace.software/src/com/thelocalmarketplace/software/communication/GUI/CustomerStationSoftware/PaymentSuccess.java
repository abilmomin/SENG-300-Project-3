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

import java.awt.Component;
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
import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantPageGUI;
import com.thelocalmarketplace.software.funds.Receipt;


/**
 * The PaymentSuccess class represents a JFrame for displaying a payment success message.
 * It suppresses the serial warning as it does not use serialization.
 */
@SuppressWarnings("serial")
public class PaymentSuccess extends JFrame {
	@SuppressWarnings("unused")
	private SelectPayment paymentWindow;
	
	/**
	 * Constructs a new PaymentSuccess dialog window.
	 * 
	 * @param change 
	 * 				The amount of change returned.
	 * @param stationSoftware 
	 * 				The SelfCheckoutStationSoftware instance managing the transaction.
	 * @param attendantGUI 
	 * 				The AttendantPageGUI instance.
	 */
	public PaymentSuccess(double change, SelfCheckoutStationSoftware stationSoftware, AttendantPageGUI attendantGUI) {
		paymentWindow = new SelectPayment(stationSoftware);
		setTitle("Thank you!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900, 700);
		setLocationRelativeTo(null);

		JPanel mainPanel = new JPanel();

		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		JLabel bigTextLabel = new JLabel("Payment successful!");
		bigTextLabel.setFont(new Font("Arial", Font.BOLD, 24));
		bigTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		double amountDue =  stationSoftware.getFunds().getTotalPaid().doubleValue();
		JLabel mediumTextLabel = new JLabel("Amount Paid: $" + amountDue);
		mediumTextLabel.setFont(new Font("Arial", Font.PLAIN, 15));
		mediumTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel smallTextLabel = new JLabel("Change Returned: $" + change);
		smallTextLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		smallTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel receiptQuestion = new JLabel("Would you like a receipt?");
		receiptQuestion.setFont(new Font("Arial", Font.BOLD, 18));
		receiptQuestion.setAlignmentX(Component.CENTER_ALIGNMENT);

		AbstractElectronicScale scale = (AbstractElectronicScale) stationSoftware.getStationHardware().getBaggingArea();
		
		JCheckBox ck1 = new JCheckBox("Yes");
		ck1.setAlignmentX(Component.CENTER_ALIGNMENT);
		ck1.addActionListener(new ActionListener() {
			 /**
	         * Invoked when the "Yes" checkbox is selected.
	         * Prints the receipt and continues the transaction.
	         * 
	         * @param e 
	         * 			The action event.
	         */
			@Override
			public void actionPerformed(ActionEvent e) {
				Receipt rec = new Receipt(stationSoftware.getStationHardware().getPrinter(), stationSoftware.getFunds()); 
				try {
					JOptionPane.showMessageDialog(PaymentSuccess.this, rec.printReceipt());
					stationSoftware.resetOrder();
					dispose();
					JOptionPane.showMessageDialog(PaymentSuccess.this, "Thank you for shopping with us! We hope to see you again!");
					new StartSession(attendantGUI.getCurrentStationNumber(), stationSoftware, scale);
				} catch (HeadlessException | EmptyDevice | OverloadedDevice e1) {
					e1.printStackTrace();
				}
			}
		});
		
		JCheckBox ck2 = new JCheckBox("No");
		ck2.setAlignmentX(Component.CENTER_ALIGNMENT);
		ck2.addActionListener(new ActionListener() {

			/**
	         * Invoked when the "No" checkbox is selected.
	         * Closes the dialog and starts a new session.
	         * 
	         * @param e 
	         * 			The action event.
	         */
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(PaymentSuccess.this, "Thank you for shopping with us! We hope to see you again!");
				dispose();
				new StartSession(attendantGUI.getCurrentStationNumber(), stationSoftware, scale);
			}
		});

		mainPanel.add(Box.createVerticalGlue());
		mainPanel.add(bigTextLabel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		mainPanel.add(mediumTextLabel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		mainPanel.add(smallTextLabel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		mainPanel.add(receiptQuestion);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		mainPanel.add(ck1);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		mainPanel.add(ck2);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		add(mainPanel);

		setVisible(true);
	}
}