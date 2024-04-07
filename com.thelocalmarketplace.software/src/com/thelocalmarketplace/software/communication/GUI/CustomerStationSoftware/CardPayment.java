package com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware;

import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.awt.event.ActionListener;

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

            JPanel pinPanel = createPinPanel();
            pinPanel.setVisible(true);
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
                    case 1 -> software.getStationHardware().getCardReader().swipe(software.getCard(typeOfCard));
                    case 2 -> software.getStationHardware().getCardReader().tap(software.getCard(typeOfCard));
                    case 3 -> software.getStationHardware().getCardReader().insert(software.getCard(typeOfCard), pinInput);
                    default -> JOptionPane.showMessageDialog(this, "Please select a payment method.", "Payment", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
//            dispose();
        });

        add(radioPanel);
        add(finishPaymentButton);

        setVisible(false);
    }

    public JPanel createPinPanel() {
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
