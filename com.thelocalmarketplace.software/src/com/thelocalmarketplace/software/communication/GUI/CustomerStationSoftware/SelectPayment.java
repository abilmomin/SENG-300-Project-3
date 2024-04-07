package com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware;

import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationHardware.PayWithCoins;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationHardware.PayWithBanknotes;

import javax.swing.*;
import java.awt.*;

public class SelectPayment extends JFrame {
	
	private SelfCheckoutStationSoftware software;
	private double totalPaid = 0;
	private double totalPrice = 0;
	private JLabel totalPaidValueLabel;
	private JLabel amountOwingValueLabel;
	private JLabel billTotalValueLabel;
	private CardPayment debitWindow;
	private CardPayment creditWindow;
	private PayWithBanknotes banknoteWindow;
	private PayWithCoins coinWindow;

    public SelectPayment(SelfCheckoutStationSoftware software) {
    	this.software = software;
    	this.debitWindow = new CardPayment(software, "debit");
    	this.creditWindow = new CardPayment(software, "crebit");
    	this.banknoteWindow = new PayWithBanknotes(software);
    	this.coinWindow = new PayWithCoins(software);
    	
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel labelsPanel = new JPanel(new GridLayout(3, 1));
        JLabel billTotalLabel = new JLabel("Bill Total");
        JLabel totalPaidLabel = new JLabel("Total Paid");
        JLabel amountOwingLabel = new JLabel("Amount Owing");

        billTotalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalPaidLabel.setFont(new Font("Arial", Font.BOLD, 18));
        amountOwingLabel.setFont(new Font("Arial", Font.BOLD, 18));

        labelsPanel.add(billTotalLabel);
        labelsPanel.add(totalPaidLabel);
        labelsPanel.add(amountOwingLabel);

        JPanel valuesPanel = new JPanel(new GridLayout(3, 1));
        totalPrice = software.getTotalOrderPrice();
        billTotalValueLabel = new JLabel("$" + String.format("%.2f", totalPrice));
        totalPaidValueLabel = new JLabel("$" + String.format("%.2f", totalPaid));
        amountOwingValueLabel = new JLabel("$" + String.format("%.2f", (totalPrice - totalPaid)));

        billTotalValueLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalPaidValueLabel.setFont(new Font("Arial", Font.BOLD, 18));
        amountOwingValueLabel.setFont(new Font("Arial", Font.BOLD, 18));

        valuesPanel.add(billTotalValueLabel);
        valuesPanel.add(totalPaidValueLabel);
        valuesPanel.add(amountOwingValueLabel);

        topPanel.add(labelsPanel, BorderLayout.WEST);
        topPanel.add(valuesPanel, BorderLayout.EAST);

        // Buttons panel for payment methods
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JButton debitButton = createColoredButton("Debit", new Color(173, 216, 230));
        JButton creditButton = createColoredButton("Credit", new Color(255, 182, 193));
        JButton coinButton = createColoredButton("Coin", new Color(255, 255, 224));
        JButton cashButton = createColoredButton("Cash", new Color(240, 230, 140));

        buttonPanel.add(debitButton);
        buttonPanel.add(creditButton);
        buttonPanel.add(coinButton);
        buttonPanel.add(cashButton);

        // Bottom panel for membership and return options
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        JButton addMembershipButton = createColoredButton("Add Membership", new Color(0, 128, 128));
        JButton returnToCheckoutButton = createColoredButton("Return to Checkout", new Color(255, 127, 80));

        debitButton.addActionListener(e -> debitWindow.setVisible(true));
        creditButton.addActionListener(e -> creditWindow.setVisible(true));        
        cashButton.addActionListener(e -> banknoteWindow.setVisible(true));
        coinButton.addActionListener(e -> coinWindow.setVisible(true));
        
        bottomPanel.add(addMembershipButton);
        bottomPanel.add(returnToCheckoutButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Select Payment Method");
        pack();
        setLocationRelativeTo(null);
        setVisible(false);
    }
    
	// Method to update totalPaidValueLabel
    public void updateTotalPaidValueLabel(double addedFunds) {
    	totalPaid += addedFunds;
    	updatePanelsAndVariables();
    }
    
    // Method to make the panel visible
    public void showPanel() {
    	totalPrice = software.getTotalOrderPrice();
        billTotalValueLabel.setText("$" + String.format("%.2f", totalPrice)); // Update text of existing label
    	updatePanelsAndVariables();
        setVisible(true);
    }

    // Method to close the panel
    public void closePanel() {
        setVisible(false);
    }
    
    private void updatePanelsAndVariables() {
        totalPaidValueLabel.setText("$" + String.format("%.2f", totalPaid)); // Update text of existing label
        amountOwingValueLabel.setText("$" + String.format("%.2f", (totalPrice - totalPaid))); // Update text of existing label
    }

    private JButton createColoredButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setOpaque(true);
        button.setBorderPainted(false); 
        button.setFont(new Font("Arial", Font.BOLD, 16));
        return button;
    }

//    public static void main(final String[] args) {
//        SwingUtilities.invokeLater(SelectPayment::new);
//    }
}
