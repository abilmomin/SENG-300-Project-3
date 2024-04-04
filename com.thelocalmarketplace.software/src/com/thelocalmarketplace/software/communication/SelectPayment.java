package com.thelocalmarketplace.software.communication;

import javax.swing.*;
import java.awt.*;

public class SelectPayment extends JFrame {

    public SelectPayment() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(0, 1));

        JLabel billTotalLabel = new JLabel("Bill Total: $30", SwingConstants.CENTER);
        JLabel totalPaidLabel = new JLabel("Total Paid: $0", SwingConstants.CENTER);
        JLabel amountOwingLabel = new JLabel("Amount Owing: $30", SwingConstants.CENTER);

        billTotalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalPaidLabel.setFont(new Font("Arial", Font.BOLD, 16));
        amountOwingLabel.setFont(new Font("Arial", Font.BOLD, 16));

        panel.add(billTotalLabel);
        panel.add(totalPaidLabel);
        panel.add(amountOwingLabel);

        // Buttons for different payment methods
        JButton debitButton = new JButton("Debit");
        JButton creditButton = new JButton("Credit");
        JButton coinButton = new JButton("Coin");
        JButton cashButton = new JButton("Cash");
        JButton cryptoButton = new JButton("Crypto");
        JButton addMembershipButton = new JButton("Add membership");
        JButton returnToCheckoutButton = new JButton("Return to checkout");

        debitButton.addActionListener(e -> new CardPayment());
        creditButton.addActionListener(e -> new CardPayment());
        coinButton.addActionListener(e -> new PayWithCoins());


        
        panel.add(debitButton);
        panel.add(creditButton);
        panel.add(coinButton);
        panel.add(cashButton);
        panel.add(cryptoButton);
        panel.add(addMembershipButton);
        panel.add(returnToCheckoutButton);

        add(panel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Select Payment Method");
        pack();
        setVisible(true);
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(SelectPayment::new);
    }
}





