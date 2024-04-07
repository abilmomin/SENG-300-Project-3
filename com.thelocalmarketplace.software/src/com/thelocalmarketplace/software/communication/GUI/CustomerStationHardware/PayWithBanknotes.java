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

package com.thelocalmarketplace.software.communication.GUI.CustomerStationHardware;

import javax.swing.*;

import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.banknote.Banknote;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public class PayWithBanknotes extends JFrame {

    private JLabel totalLabel;
    private BigDecimal totalCount = BigDecimal.ZERO;
    private SelfCheckoutStationSoftware software;

    public PayWithBanknotes(SelfCheckoutStationSoftware software) {
    	this.software = software;
    	
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
        BigDecimal[] banknoteDenominations = software.station.getBanknoteDenominations();

        // Add banknote buttons
        for (BigDecimal denomination : banknoteDenominations) {
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
            	setVisible(false);
            }
        });

        // Button panel for finish button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(finishButton);

     // Add banknote panel and total panel to a separate panel with FlowLayout
        JPanel banknoteAndTotalPanel = new JPanel(new BorderLayout());
        banknoteAndTotalPanel.add(banknotePanel, BorderLayout.CENTER);
        banknoteAndTotalPanel.add(totalPanel, BorderLayout.SOUTH);

        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(banknoteAndTotalPanel, BorderLayout.CENTER);
        mainPanel.add(finishButton, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);

        // Set frame visible
        setVisible(false);
    }

    private JButton createBanknoteButton(BigDecimal denomination) {
        JButton button = new JButton("$" + (denomination));
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                totalCount = totalCount.add(denomination);
                updateTotalLabel();
                Banknote banknote = new Banknote(Currency.getInstance("CAD"), denomination);
                try {
					software.station.getBanknoteInput().receive(banknote);
				} catch (DisabledException | CashOverloadException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        return button;
    }

    private void updateTotalLabel() {
        totalLabel.setText("Total Amount: $" + totalCount);
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(PayWithBanknotes::new);
//    }
}
