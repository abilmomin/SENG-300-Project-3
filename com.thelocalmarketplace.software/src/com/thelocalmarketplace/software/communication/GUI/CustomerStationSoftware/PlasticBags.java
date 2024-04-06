package com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PlasticBags extends JFrame {
	private JPanel mainPanel;
	private JPanel centerPanel;
	private JPanel keypadPanel;
	private JPanel screenPanel;
	private JPanel headerPanel;
	private JPanel footerPanel;
    private JTextField screenTextField;
    
    public PlasticBags() {
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setSize(600, 500);
	    setLocationRelativeTo(null);
	    
	    JPanel mainPanel = new JPanel();
	    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
	    
	    JLabel topText = new JLabel("How many bags did you use?");
	    topText.setFont(new Font("Arial", Font.BOLD, 24));
	    topText.setAlignmentX(Component.CENTER_ALIGNMENT);
	    
	    
	    keypadPanel = createKeypadPanel();
	    screenPanel = createScreenPanel();
	    
	    JButton button = new JButton("Return to Checkout");
	    button.setAlignmentX(Component.CENTER_ALIGNMENT);
	    button.setFont(new Font("Arial", Font.BOLD, 16));
	    button.setForeground(new Color(199, 100, 2));
	    button.setBackground(new Color(245, 228, 211));
	    button.setPreferredSize(new Dimension(100, 30));
	    
	    mainPanel.add(Box.createVerticalGlue());
	    mainPanel.add(topText);
	    mainPanel.add(screenPanel);
	    mainPanel.add(keypadPanel);
	    mainPanel.add(button);
	    mainPanel.add(Box.createVerticalGlue());
	    
	    add(mainPanel);
	    setVisible(true);
    }
    

    public JPanel createKeypadPanel() {
    	JPanel keypadPanel = new JPanel();
    	keypadPanel.setLayout(new GridLayout(4,4,10,10));
    	keypadPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
    	
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
        	String currentText = screenTextField.getText();
        	if (!currentText.isEmpty()) {
                 // Remove the last character from the text
                 String newText = currentText.substring(0, currentText.length() - 1);
                 screenTextField.setText(newText);
        	}
        });
    	
    	/*
    	enter.addActionListener(e -> {
        	AddtoBagging popup  = new AddtoBagging(this);
        	popup.setVisible(true);
        });
    	*/
    	return keypadPanel;
    }
    
    
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
    
    public JPanel createScreenPanel() {
    	JPanel screenPanel = new JPanel();
        screenTextField = new JTextField(); // Initialize the screen text field
        screenTextField.setEditable(false); // Make it read-only
        screenTextField.setBackground(Color.WHITE);
        screenTextField.setHorizontalAlignment(JTextField.CENTER);
        screenPanel.add(screenTextField);
        screenPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        screenTextField.setPreferredSize(new Dimension(550, 50));
        
        return screenPanel;
    }
    
    public static void main(String[] args ) {
		PlasticBags popup = new PlasticBags();	// USING ARBITRARY TEST VALUE ATM
	}
}
