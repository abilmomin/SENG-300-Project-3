/**

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

package com.thelocalmarketplace.software.communication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PayWithBanknotes extends JFrame {

    private JLabel totalLabel;
    private int totalAmount;

    public PayWithBanknotes() {
        setTitle("Banknote Counter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel();
        JLabel headerLabel = new JLabel("Push Banknote", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(headerLabel);

        // Banknote options panel
        JPanel banknotePanel = new JPanel();
        banknotePanel.setLayout(new GridLayout(0, 1, 10, 5)); // Increased vertical padding
        banknotePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Banknote denominations
        int[] banknoteDenominations = {1, 5, 10, 20, 50, 100};

        // Add banknote buttons
        for (int denomination : banknoteDenominations) {
            JButton banknoteButton = createBanknoteButton(denomination);
            banknotePanel.add(banknoteButton);
        }

        // Total label
        totalLabel = new JLabel("Total Amount: $0", SwingConstants.CENTER);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
     // Total panel
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        totalPanel.add(totalLabel);

        // Finish button
        JButton finishButton = new JButton("Finish");
        finishButton.setFont(new Font("Arial", Font.BOLD, 24));
        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(PayWithBanknotes.this, "Total Amount: $" + totalAmount);
            }
        });

        // Button panel for finish button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(finishButton);

     // Add coin panel and total panel to a separate panel with FlowLayout
        JPanel coinAndTotalPanel = new JPanel(new BorderLayout());
        coinAndTotalPanel.add(banknotePanel, BorderLayout.CENTER);
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

    private JButton createBanknoteButton(int denomination) {
        JButton button = new JButton("$" + denomination);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setPreferredSize(new Dimension(100, 50)); // Set preferred size
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                totalAmount += denomination;
                updateTotalLabel();
            }
        });
        return button;
    }

    private void updateTotalLabel() {
        totalLabel.setText("Total Amount: $" + totalAmount);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PayWithBanknotes::new);
    }
}
