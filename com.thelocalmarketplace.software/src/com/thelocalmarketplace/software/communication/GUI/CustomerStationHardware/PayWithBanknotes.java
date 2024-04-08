/**

 SENG 300 - ITERATION 3
 GROUP GOLD {8}

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

import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.banknote.Banknote;

import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.math.BigDecimal;

import java.util.Currency;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


/**
 * Represents a user interface for paying with banknotes in a self-checkout system.
 * This class provides functionality for selecting banknotes and displaying the total amount.
 */
@SuppressWarnings("serial")
public class PayWithBanknotes extends JFrame {

    private JLabel totalLabel;
    private BigDecimal totalCount = BigDecimal.ZERO;
    private SelfCheckoutStationSoftware software;

    /**
     * Constructor that creates a panel containing banknote information and options.
     * @param software The SelfCheckoutStationSoftware to which the hardware is attached.
     */
    public PayWithBanknotes(SelfCheckoutStationSoftware software) {
    	this.software = software;
    	
        setTitle("Banknote Counter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

     
        JPanel mainPanel = new JPanel(new BorderLayout());

        
        JPanel headerPanel = new JPanel();
        JLabel headerLabel = new JLabel("Push Banknote", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(headerLabel);

        
        JPanel banknotePanel = new JPanel();
        banknotePanel.setLayout(new GridLayout(0, 1, 10, 5));
        banknotePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        
        BigDecimal[] banknoteDenominations = software.station.getBanknoteDenominations();

        // Add banknote buttons
        for (BigDecimal denomination : banknoteDenominations) {
            JButton banknoteButton = createBanknoteButton(denomination);
            banknotePanel.add(banknoteButton);
        }

        totalLabel = new JLabel("Total Amount: $0", SwingConstants.CENTER);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        
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

        add(mainPanel);

        setVisible(false);
    }
    
    
    /**
     * Creates a JButton representing a banknote denomination.
     * @param denomination The denomination of the banknote.
     * @return A styled JButton representing the banknote denomination.
     */
    private JButton createBanknoteButton(BigDecimal denomination) {
        JButton button = new JButton("$" + (denomination));
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                	if(software.station.getBanknoteInput().hasDanglingBanknotes())
                		software.station.getBanknoteInput().removeDanglingBanknote();
                	
	                Banknote banknote = new Banknote(Currency.getInstance("CAD"), denomination);
					software.station.getBanknoteInput().receive(banknote);
					
					totalCount = totalCount.add(denomination);
	                updateTotalLabel();
				} catch (DisabledException | CashOverloadException e1) {
					e1.printStackTrace();
				}
            }
        });
        return button;
    }
    
    
    /**
     * Updates the total label with the current total amount of banknotes.
     */
    private void updateTotalLabel() {
        totalLabel.setText("Total Amount: $" + totalCount);
    }
}
