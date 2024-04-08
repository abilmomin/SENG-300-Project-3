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

import com.jjjwelectronics.card.BlockedCardException;
import com.jjjwelectronics.card.InvalidPINException;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import ca.ucalgary.seng300.simulation.NullPointerSimulationException;

import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class CardPayment extends JFrame {

    private int paymentType = 0;
    private JTextField pinTextField;
    private String pinInput;

    public CardPayment(SelfCheckoutStationSoftware software, String typeOfCard) {
        setTitle("Select Card Payment Method");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JRadioButton swipeButton = new JRadioButton("Swipe");
        swipeButton.addActionListener(e -> {
            paymentType = 1;
        });
        JRadioButton tapButton = new JRadioButton("Tap");
        tapButton.addActionListener(e -> {
            paymentType = 2;
        });
        JRadioButton insertButton = new JRadioButton("Insert Card");
        insertButton.addActionListener(e -> {
            paymentType = 3;

            JFrame pinFrame = new JFrame("Pin Panel");
            pinFrame.setLocationRelativeTo(null);
            pinFrame.setSize(380, 350);

            JPanel pinPanel = new JPanel();
            pinPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            pinTextField = new JTextField(); // Initialize the screen text field
            pinTextField.setEditable(false); // Make it read-only
            pinTextField.setBackground(Color.WHITE);
            pinTextField.setHorizontalAlignment(JTextField.CENTER);
            pinPanel.add(pinTextField, BorderLayout.CENTER);
            pinTextField.setPreferredSize(new Dimension(300, 75));

            JPanel keypadPanel = createPinPanel(pinFrame);
            pinPanel.add(keypadPanel);
            pinFrame.add(pinPanel);
            pinFrame.setVisible(true);
        });

        ButtonGroup group = new ButtonGroup();
        group.add(swipeButton);
        group.add(tapButton);
        group.add(insertButton);

        JPanel radioPanel = new JPanel(new GridLayout(0, 1));
        radioPanel.add(swipeButton);
        radioPanel.add(tapButton);
        radioPanel.add(insertButton);

        JButton finishPaymentButton = new JButton("Finish Payment");
        finishPaymentButton.addActionListener(e -> {
            try {
                switch (paymentType) {
                    case 1 :
                    	software.getStationHardware().getCardReader().swipe(software.getCard(typeOfCard));
                    	dispose(); 
                    	break;
                    case 2 :
                    	software.getStationHardware().getCardReader().tap(software.getCard(typeOfCard));
                    	dispose();
                    	break;
                    case 3: 
                    	software.getStationHardware().getCardReader().insert(software.getCard(typeOfCard), pinInput);
                    	dispose();
                    	break;
                    default :
                    	JOptionPane.showMessageDialog(this, "Please select a payment method.", "Payment", JOptionPane.INFORMATION_MESSAGE);
                    	dispose();
                    	break;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InvalidPINException ipe) {
                software.getGUI().customerPopUp("Invalid PIN Entered");
                software.getStationHardware().getCardReader().remove();
            } catch (BlockedCardException bce) {
                software.getGUI().customerPopUp("The Card is Blocked");
                try {
                    software.getStationHardware().getCardReader().remove();
                } catch (NullPointerSimulationException npse) { // Unable to remove card

                }
            }
        });

        add(radioPanel);
        add(finishPaymentButton);

        setVisible(false);
    }

    public JPanel createPinPanel(JFrame pinPanel) {
    	JPanel keypadPanel = new JPanel();
    	keypadPanel.setLayout(new GridLayout(4,4,10,10));
    	keypadPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    	
    	for (int i = 1; i <= 9; i++) {
            JButton button = new JButton(Integer.toString(i));
            button.setFont(new Font("Arial", Font.PLAIN, 16));
            keypadPanel.add(button);
            button.addActionListener(addNum);
        }
    	
    	JButton enter = new JButton("Enter");
    	enter.setFont(new Font("Arial", Font.PLAIN, 16));
    	enter.setBackground(new Color(214, 255, 217));
        enter.setForeground(new Color(42, 120, 48));
    	keypadPanel.add(enter);
    	
    	JButton button0 = new JButton("0");
    	button0.setFont(new Font("Arial", Font.PLAIN, 16));
    	keypadPanel.add(button0);
    	button0.addActionListener(addNum);
    	
    	JButton delete = new JButton("Delete");
    	delete.setFont(new Font("Arial", Font.PLAIN, 16));
        delete.setBackground(new Color(227, 188, 188));
        delete.setForeground(Color.RED);
    	keypadPanel.add(delete);
    	
    	delete.addActionListener(e -> {
        	String currentText = pinTextField.getText();
        	if (!currentText.isEmpty()) {
                 // Remove the last character from the text
                 String newText = currentText.substring(0, currentText.length() - 1);
                 pinTextField.setText(newText);
        	}
        });
    	
    	enter.addActionListener(e -> {
    	    pinInput = pinTextField.getText();
            pinPanel.dispose();
    	});
    	
    	return keypadPanel;
    }
   
    ActionListener addNum = e -> {
        JButton button = (JButton) e.getSource();
        String digit = button.getText();
        String current = pinTextField.getText(); // Append the digit to the screen
        String newText = current + digit;
        
        Font currentFont = pinTextField.getFont();
        Font newFont = new Font(currentFont.getName(), currentFont.getStyle(), 24); // Adjust the font size as needed
        pinTextField.setFont(newFont);
        
        pinTextField.setText(newText);
    };
}
