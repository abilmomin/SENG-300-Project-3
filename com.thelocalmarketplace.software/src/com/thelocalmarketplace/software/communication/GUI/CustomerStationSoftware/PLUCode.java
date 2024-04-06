package com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;


public class PLUCode extends JFrame {
    private JTextArea cartTextArea;
    private JLabel totalPriceLabel;
    private JTextField screenTextField;
     

    public PLUCode(int selectedStation) {
        setTitle("Self-Checkout Station " + selectedStation);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Menu panel
        JPanel menuPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Buttons
        // the following nulls inside the buttons should be replaced by the corresponding callback functions 
        //associated with the popups
        JButton useOwnBagsBtn = createButton("Use Own Bags", null);
        JButton scanBarcodeBtn = createButton("Scan Barcode", null);
        JButton enterPLUBtn = createButton("Enter PLU Code", null);
        JButton searchProductBtn = createButton("Search Product", null);
        JButton removeItemBtn = createButton("Remove Item", null);
        JButton doNotBagBtn = createButton("Do Not Bag", null);
        JButton viewBaggingAreaBtn = createButton("View Bagging Area", null);
   
        // Add buttons to menu panel
        menuPanel.add(useOwnBagsBtn);
        menuPanel.add(scanBarcodeBtn);
        menuPanel.add(enterPLUBtn);
        menuPanel.add(searchProductBtn);
        menuPanel.add(removeItemBtn);
        menuPanel.add(doNotBagBtn);
        menuPanel.add(viewBaggingAreaBtn);
        
        //PLU panel
        JPanel PLUPanel = new JPanel(new BorderLayout());
        
        // Keypad panel
        JPanel keypadPanel = new JPanel(new GridLayout(4, 4,10,10));
        keypadPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        ActionListener addNum = e -> {
            JButton button = (JButton) e.getSource();
            String digit = button.getText();
            String current = screenTextField.getText(); // Append the digit to the screen
            String newText = current + digit;
            
            Font currentFont = screenTextField.getFont();
            Font newFont = new Font(currentFont.getName(), currentFont.getStyle(), 24); // Adjust the font size as needed
            screenTextField.setFont(newFont);
            
            screenTextField.setText(newText);
        };
        
        // Buttons 
        JButton button1 = createButton2("1", addNum);
        JButton button2 = createButton2("2", addNum);
        JButton button3 = createButton2("3", addNum);
        JButton button4 = createButton2("4", addNum);
        JButton button5 = createButton2("5", addNum);
        JButton button6 = createButton2("6", addNum);
        JButton button7 = createButton2("7", addNum);
        JButton button8 = createButton2("8", addNum);
        JButton button9 = createButton2("9", addNum);
        JButton enter = createButton("Enter", null);
        JButton button0 = createButton2("0", addNum);
        
        JButton delete = new JButton("Delete");
        //JButton delete = createButton("Delete", );
        
        delete.addActionListener(e -> {
        	String currentText = screenTextField.getText();
        	if (!currentText.isEmpty()) {
                 // Remove the last character from the text
                 String newText = currentText.substring(0, currentText.length() - 1);
                 screenTextField.setText(newText);
        	}
        });
        
        enter.addActionListener(e -> {
        	AddtoBagging popup  = new AddtoBagging("bruh");
        	popup.setVisible(true);
        });
        
        enter.setBackground(new Color(214, 255, 217));
        enter.setForeground(new Color(42, 120, 48));
        delete.setBackground(new Color(227, 188, 188));
        delete.setForeground(Color.RED);
        
        // Add buttons to PLU panel
        keypadPanel.add(button1);
        keypadPanel.add(button2);
        keypadPanel.add(button3);
        keypadPanel.add(button4);
        keypadPanel.add(button5);
        keypadPanel.add(button6);
        keypadPanel.add(button7);
        keypadPanel.add(button8);
        keypadPanel.add(button9);
        keypadPanel.add(enter);
        keypadPanel.add(button0);
        keypadPanel.add(delete);
        
        
        // Screen panel
        JPanel screenPanel = new JPanel(new BorderLayout());
        screenTextField = new JTextField(); // Initialize the screen text field
        screenTextField.setEditable(false); // Make it read-only
        screenTextField.setBackground(Color.WHITE);
        screenTextField.setHorizontalAlignment(JTextField.CENTER);
        screenPanel.add(screenTextField, BorderLayout.CENTER);
        screenPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        screenTextField.setPreferredSize(new Dimension(100, 50));

        
        
        PLUPanel.add(keypadPanel);
        PLUPanel.add(screenPanel, BorderLayout.NORTH);
        
        // Add panels to main panel
        mainPanel.add(menuPanel, BorderLayout.CENTER);
        mainPanel.add(PLUPanel, BorderLayout.EAST);

        // Add main panel to frame
        add(mainPanel);

        // Set frame visible
        setVisible(true);
    }

	private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton("<html><center>" + text + "</center></html>");
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        if (actionListener != null) {
            button.addActionListener(actionListener);
        }
        return button;
    }
	
	private JButton createButton2(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        if (actionListener != null) {
            button.addActionListener(actionListener);
        }
        return button;
    }
	public static void main(String[] args ) {
		PLUCode pluCode = new PLUCode(1);	// USING ARBITRARY TEST VALUE ATM
	}
}
