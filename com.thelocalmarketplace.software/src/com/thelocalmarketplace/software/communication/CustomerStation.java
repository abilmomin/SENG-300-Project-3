package com.thelocalmarketplace.software.communication;

import javax.swing.*;

import com.thelocalmarketplace.software.AttendantPageGUI;
import com.thelocalmarketplace.software.MembershipCard;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class CustomerStation extends JFrame {

    private JTextArea cartTextArea;
    private JLabel totalPriceLabel;
    private JPanel menuPanel;
    private JPanel payButtonPanel;

    public CustomerStation(int selectedStation) {
        setTitle("Self-Checkout Station " + selectedStation);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Menu panel
        menuPanel = new JPanel(new GridLayout(3, 3, 10, 10));
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
        //Signal for attendant button 
        JButton helpButton = new JButton("Help");
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyAttendant(selectedStation);
            }
        });
        
        // Add buttons to menu panel
        menuPanel.add(useOwnBagsBtn);
        menuPanel.add(scanBarcodeBtn);
        menuPanel.add(enterPLUBtn);
        menuPanel.add(searchProductBtn);
        menuPanel.add(removeItemBtn);
        menuPanel.add(doNotBagBtn);
        menuPanel.add(viewBaggingAreaBtn);
        menuPanel.add(helpButton);
        
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
        payButtonPanel = new JPanel(new BorderLayout());
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
    private void notifyAttendant(int selectedStation) {
        // Notify the AttendantPageGUI that assistance is required at this station
        AttendantPageGUI.notifyAssistanceRequired(selectedStation-1);
    }
    
    private void addProductToCart(String productName, double price) {
        cartTextArea.append(productName + " - $" + price + "\n");
        double currentTotal = Double.parseDouble(totalPriceLabel.getText().replace("Total Price: $", ""));
        currentTotal += price;
        totalPriceLabel.setText("Total Price: $" + String.format("%.2f", currentTotal));
    }

    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton("<html><center>" + text + "</center></html>");
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        if (actionListener != null) {
            button.addActionListener(actionListener);
        }
        return button;
    }

    public void askForMembershipNumber() {
    	MembershipNumberInput dialog = new MembershipNumberInput(this);
        dialog.frameInit();
    }

   
    public void freezeGUI() {
        for (Component component : getContentPane().getComponents()) {
            component.setEnabled(false);
        }
        // Disable buttons in the menuPanel
        for (Component component : menuPanel.getComponents()) {
            if (component instanceof JButton) {
                component.setEnabled(false);
            }
        }
        // Disable pay button
        Component[] cartComponents = payButtonPanel.getComponents();
        for (Component component : cartComponents) {
            if (component instanceof JButton && ((JButton) component).getText().equals("Pay")) {
                component.setEnabled(false);
            }
        }
        JOptionPane.showMessageDialog(null, "Out of order.");
    }

    public void unfreezeGUI() {
        for (Component component : getContentPane().getComponents()) {
            component.setEnabled(true);
        }
        // Enable buttons in the menuPanel
        if (menuPanel != null) {
            for (Component component : menuPanel.getComponents()) {
                if (component instanceof JButton) {
                    component.setEnabled(true);
                }
            }
            // Enable pay button
            Component[] cartComponents = payButtonPanel.getComponents();
            for (Component component : cartComponents) {
                if (component instanceof JButton && ((JButton) component).getText().equals("Pay")) {
                    component.setEnabled(true);
                }
            }
        }
    } 

}
