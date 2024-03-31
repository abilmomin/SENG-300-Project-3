package com.thelocalmarketplace.software.communication;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PayWithCoins extends JFrame {

    private JLabel coinTotalLabel;
    private int totalCount = 0;

    public PayWithCoins() {
        setTitle("Coin Counter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel();
        JLabel headerLabel = new JLabel("Insert Coin", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(headerLabel);

        // Coin options panel
        JPanel coinPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        coinPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Coin denominations
        int[] coinDenominations = {1, 5, 10, 25, 50, 100};

        // Add coin buttons
        for (int denomination : coinDenominations) {
            JButton coinButton = createCoinButton(denomination);
            coinPanel.add(coinButton);
        }

        // Coin total label
        coinTotalLabel = new JLabel("Total Value of Coins Added: $0.00", SwingConstants.CENTER);
        coinTotalLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Total panel
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        totalPanel.add(coinTotalLabel);

        // Finish button
        JButton finishButton = new JButton("Finish");
        finishButton.addActionListener(new ActionListener() {
        	//this should replaced with a return to customer homepage instead later
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(PayWithCoins.this, "Total inserted: $" + (totalCount / 100.0));
            }
        });

        // Add coin panel and total panel to a separate panel with FlowLayout
        JPanel coinAndTotalPanel = new JPanel(new BorderLayout());
        coinAndTotalPanel.add(coinPanel, BorderLayout.CENTER);
        coinAndTotalPanel.add(totalPanel, BorderLayout.SOUTH);

        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(coinAndTotalPanel, BorderLayout.CENTER);
        mainPanel.add(finishButton, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);

        // Set frame visible
        setVisible(true);
    }

    private JButton createCoinButton(int denomination) {
        JButton button = new JButton("$" + (denomination / 100.0));
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                totalCount += denomination;
                updateTotalLabel();
            }
        });
        return button;
    }

    private void updateTotalLabel() {
        double totalAmount = totalCount / 100.0;
        coinTotalLabel.setText(String.format("Total Value of Coins Added: $%.2f", totalAmount));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PayWithCoins::new);
    }
}
