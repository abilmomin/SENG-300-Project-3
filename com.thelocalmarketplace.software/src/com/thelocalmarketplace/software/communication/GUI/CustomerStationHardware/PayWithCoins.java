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
import com.tdc.coin.Coin;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


/**
 * Represents a user interface for paying with coins in a self-checkout system.
 * This class provides functionality for selecting coins and displaying the total value.
 */
@SuppressWarnings("serial")
public class PayWithCoins extends JFrame {

    private final JLabel coinTotalLabel;
    private BigDecimal totalCount = BigDecimal.ZERO;
    private final SelfCheckoutStationSoftware software;
    
    /**
     * Constructor that creates a panel containing coin information and options.
     * 
     * @param software 
     * 			The SelfCheckoutStationSoftware to which the hardware is attached.
     */
    public PayWithCoins(SelfCheckoutStationSoftware software) {
    	this.software = software;
        setTitle("Coin Counter");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        JPanel headerPanel = new JPanel();
        JLabel headerLabel = new JLabel("Insert Coin", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(headerLabel);
        
        JPanel coinPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        coinPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        List<BigDecimal> coinDenominations = software.station.getCoinDenominations();

        // Add coin buttons
        for (BigDecimal denomination : coinDenominations) {
            JButton coinButton = createCoinButton(denomination);
            coinPanel.add(coinButton);
        }
        
        coinTotalLabel = new JLabel("Total Value of Coins Added: $0.00", SwingConstants.CENTER);
        coinTotalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        totalPanel.add(coinTotalLabel);
        
        JButton finishButton = new JButton("Finish");
        finishButton.addActionListener(new ActionListener() {
        	
            @Override
            public void actionPerformed(ActionEvent e) {
            	setVisible(false);
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
       
        add(mainPanel);
        
        setVisible(false);
    }
    
    /**
     * Creates a JButton representing a coin denomination.
     * 
     * @param denomination 
     * 			The denomination of the coin.
     * @return A styled JButton representing the coin denomination.
     */
    private JButton createCoinButton(BigDecimal denomination) {
        JButton button = new JButton("$" + (denomination));
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                	Coin coin = new Coin(Currency.getInstance("CAD"), denomination);
					software.station.getCoinSlot().receive(coin);
					
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
     * Updates the total label with the current total value of coins.
     */
    private void updateTotalLabel() {
        BigDecimal totalAmount = totalCount;
        coinTotalLabel.setText(String.format("Total Value of Coins Added: $%.2f", totalAmount));
    }
    
    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            dispose(); // Dispose only this instance
        } else {
            super.processWindowEvent(e);
        }
    }
}
