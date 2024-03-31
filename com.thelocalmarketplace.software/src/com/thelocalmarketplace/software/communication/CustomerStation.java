package com.thelocalmarketplace.software.communication;
import javax.swing.*;

import com.thelocalmarketplace.software.MembershipCard;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class CustomerStation extends JFrame {

    private JTextArea cartTextArea;
    private JLabel totalPriceLabel;
    JTextField membershipNumberField = new JTextField();
    JPanel membershipPanel = new JPanel(new BorderLayout(5, 5));
    
    public CustomerStation(int selectedStation) {
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
        JButton enterMembershipNumber = createButton("Enter Membership Number", e->askForMembershipNumber());

        // Add buttons to menu panel
        menuPanel.add(useOwnBagsBtn);
        menuPanel.add(scanBarcodeBtn);
        menuPanel.add(enterPLUBtn);
        menuPanel.add(searchProductBtn);
        menuPanel.add(removeItemBtn);
        menuPanel.add(doNotBagBtn);
        menuPanel.add(viewBaggingAreaBtn);
        menuPanel.add(enterMembershipNumber);
        // Cart panel
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBorder(BorderFactory.createTitledBorder("Cart"));

        // Cart contents
        cartTextArea = new JTextArea(20, 30);
        cartTextArea.setEditable(false);
        JScrollPane cartScrollPane = new JScrollPane(cartTextArea);

        // Total price label
        totalPriceLabel = new JLabel("Total Price: $0.00");
        totalPriceLabel.setFont(totalPriceLabel.getFont().deriveFont(Font.BOLD, 16));
        JPanel totalPricePanel = new JPanel(new BorderLayout());
        totalPricePanel.add(totalPriceLabel, BorderLayout.WEST);

        // Pay button
        JButton payButton = new JButton("Pay");
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform payment action here
                JOptionPane.showMessageDialog(CustomerStation.this, "Payment processed successfully!");
                // Clear cart after payment
                cartTextArea.setText("");
                totalPriceLabel.setText("Total Price: $0.00");
            }
        });
        payButton.setFont(payButton.getFont().deriveFont(Font.BOLD, 16));
        JPanel payButtonPanel = new JPanel(new BorderLayout());
        payButtonPanel.add(payButton, BorderLayout.CENTER);

        // Add components to cart panel
        cartPanel.add(cartScrollPane, BorderLayout.CENTER);
        cartPanel.add(totalPricePanel, BorderLayout.NORTH);
        cartPanel.add(payButtonPanel, BorderLayout.SOUTH);

        // Add panels to main panel
        mainPanel.add(menuPanel, BorderLayout.CENTER);
        mainPanel.add(cartPanel, BorderLayout.EAST);

        // Add main panel to frame
        add(mainPanel);

        // Add sample products to the cart
        addProductToCart("Apple", 1.99);
        addProductToCart("Banana", 0.99);

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

    private void addProductToCart(String productName, double price) {
        cartTextArea.append(productName + " - $" + price + "\n");
        double currentTotal = Double.parseDouble(totalPriceLabel.getText().replace("Total Price: $", ""));
        currentTotal += price;
        totalPriceLabel.setText("Total Price: $" + String.format("%.2f", currentTotal));
    }

    
    public void askForMembershipNumber() {

        JPanel labelPanel = new JPanel(new GridLayout(0, 1, 2, 2));
        labelPanel.add(new JLabel("Enter your membership number, or use your card:", SwingConstants.RIGHT));
        membershipPanel.add(labelPanel, BorderLayout.WEST);

        JPanel controlsPanel = new JPanel(new GridLayout(0, 1, 2, 2));
        
        controlsPanel.add(membershipNumberField);
        membershipPanel.add(controlsPanel, BorderLayout.CENTER);
        
        showVirtualNumberKeyboard();
        showChoicesOfEnteringNumber();
    }
    
    public void showVirtualNumberKeyboard() {
    	JPanel keyboardPanel = new JPanel();
    	keyboardPanel.setLayout(new GridLayout(4,3,5,5));
    	for (int i = 1; i<=9; i++) {
    		JButton numberButtons = new JButton(String.valueOf(i));
    		numberButtons.addActionListener(e->membershipNumberField.setText(membershipNumberField.getText() + e.getActionCommand()));
    		keyboardPanel.add(numberButtons);
    	}
    	JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> membershipNumberField.setText(""));
        keyboardPanel.add(clearButton);
        
        JButton zeroButton = new JButton("0");
        zeroButton.addActionListener(e -> membershipNumberField.setText(membershipNumberField.getText() + e.getActionCommand()));
        keyboardPanel.add(zeroButton);
        
        JButton backButton = new JButton("back"); // Placeholder to fill the grid
        backButton.addActionListener(e -> {
        	String currentNumber = membershipNumberField.getText();
        	if (!currentNumber.isEmpty()) {
        		String newNumber = currentNumber.substring(0, currentNumber.length()-1);
        		membershipNumberField.setText(newNumber);
        	}
        });
        keyboardPanel.add(backButton);
        
        membershipPanel.add(keyboardPanel, BorderLayout.SOUTH);
        
    }
    public void showChoicesOfEnteringNumber() {
        Object[] options = {"Enter", "Scan Card", "swipe Card", "Cancel"};

        int choice = JOptionPane.showOptionDialog(
            this,
            membershipPanel,
            "Membership Number",
            JOptionPane.NO_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]
        );
        switch (choice) {
        case 0:
        	String membershipNumber = membershipNumberField.getText();
            checkMembershipNumber(membershipNumber);
        case 1:
        	scanCard();
        case 2:
        	swipeCard();
        }
    }
    
    public void checkMembershipNumber(String membershipNumber) {
    	MembershipCard membershipCard = new MembershipCard();
    	if (membershipNumber != null && !membershipNumber.isEmpty()) {
            if (membershipCard.isMembershipNumberValid(membershipNumber)) {
                JOptionPane.showMessageDialog(this, "Membership number accepted.");
            } else {
                showOptions();
            }
        } else if (membershipNumber != null) {
            JOptionPane.showMessageDialog(this, "No membership number entered.");
        }
    }
    
    public void showOptions() {
    	String options[] = new String[] {"Try again", "Become a member", "continue"};
    	int response = JOptionPane.showOptionDialog(
                this,
                "Invalid membership number.",
                "Membership Error",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );
    	
    	switch (response) {
    		case 0:
    			membershipNumberField.setText("");
    			askForMembershipNumber();
                break;
    		case 1:
    			createAccount();
    			break;
    		case 2: 
    			JOptionPane.showMessageDialog(this, "Continuing without a membership account.");
    			break;
    	}
    }
    
    public void createAccount() {
		MembershipCard membershipCard = new MembershipCard();
		String memberName = JOptionPane.showInputDialog(this, "Please enter your name:");
		if (memberName != null && !memberName.trim().isEmpty()) {
			Random random = new Random();
			long randomNumber = (long) (random.nextDouble() * 9_000_000_000L) + 1_000_000_000L;
			String membershipNumber = Long.toString(randomNumber);
			membershipCard.createNewMember(membershipNumber, memberName);
		    JOptionPane.showMessageDialog(this, "Account created successfully!\nName: " + memberName + "\nMembership Number: " + membershipNumber);
	    } else {
	        JOptionPane.showMessageDialog(this, "You must enter a name to create a membership account.", "Error", JOptionPane.ERROR_MESSAGE);
	    }
		
    }
    
    public void scanCard() {
    	
    }
    
    public void swipeCard() {
    	
    }
    
}
