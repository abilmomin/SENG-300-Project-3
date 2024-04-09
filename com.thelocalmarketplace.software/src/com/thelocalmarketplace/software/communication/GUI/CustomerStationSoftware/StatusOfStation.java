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

package com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware;

import javax.swing.*;
import com.jjjwelectronics.printer.IReceiptPrinter;
import com.tdc.banknote.IBanknoteDispenser;
import com.tdc.coin.ICoinDispenser;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Provides a GUI representation of the status of the self-checkout station.
 */
@SuppressWarnings("serial")
public class StatusOfStation extends JFrame {

    private SelfCheckoutStationSoftware software;
    private JLabel inkLabel;
    private JLabel paperLabel;
    private JPanel coinDenominationPanel;
    private JPanel banknoteDenominationPanel;
    private Map<BigDecimal, JLabel> coinDenominationLabels;
    private Map<BigDecimal, JLabel> banknoteDenominationLabels;

    /**
     * Constructor.
     * 
     * @param software
     * 				The instance of the self-checkout station software.
     */
    public StatusOfStation(SelfCheckoutStationSoftware software) {
        this.software = software;
        this.coinDenominationLabels = new HashMap<>();
        this.banknoteDenominationLabels = new HashMap<>();

        setTitle("Status of Station");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(0, 1));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Ink remaining
        inkLabel = new JLabel();
        JPanel inkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inkPanel.add(inkLabel);
        mainPanel.add(inkPanel);

        // Paper remaining
        paperLabel = new JLabel();
        JPanel paperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        paperPanel.add(paperLabel);
        mainPanel.add(paperPanel);

        // Coin denominations
        JLabel coinLabel = new JLabel("Coin Denominations:");
        JPanel coinPanel = new JPanel(new BorderLayout());
        coinPanel.add(coinLabel, BorderLayout.NORTH);
        coinPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        coinDenominationPanel = new JPanel(new GridLayout(0, 1));
        coinPanel.add(coinDenominationPanel, BorderLayout.CENTER);
        mainPanel.add(coinPanel);

        // Banknote denominations
        JLabel banknoteLabel = new JLabel("Banknote Denominations:");
        JPanel banknotePanel = new JPanel(new BorderLayout());
        banknotePanel.add(banknoteLabel, BorderLayout.NORTH);
        banknotePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        banknoteDenominationPanel = new JPanel(new GridLayout(0, 1));
        banknotePanel.add(banknoteDenominationPanel, BorderLayout.CENTER);
        mainPanel.add(banknotePanel);

        add(mainPanel);
        
        // Initial update
        updateLabels();
        setVisible(false);
    }

    /**
     * Updates the displayed labels for ink and paper levels, coin and banknote denominations.
     */
    public void updateLabels() {
        IReceiptPrinter printer = software.station.getPrinter();

        String inkRemaining;
        try {
            inkRemaining = printer.inkRemaining() + " characters";
        } catch (UnsupportedOperationException e) {
            inkRemaining = "N/A";
        }
        inkLabel.setText("Ink Remaining: " + inkRemaining);

        String paperRemaining;
        try {
            paperRemaining = printer.paperRemaining() + " characters";
        } catch (UnsupportedOperationException e) {
            paperRemaining = "N/A";
        }
        paperLabel.setText("Paper Remaining: " + paperRemaining);

        // Update coin denominations
        coinDenominationPanel.removeAll();
        List<BigDecimal> coinDenominations = software.station.getCoinDenominations();
        Map<BigDecimal, ICoinDispenser> coinDispensersMap = software.getStationHardware().getCoinDispensers();
        for (BigDecimal denomination : coinDenominations) {
            ICoinDispenser dispenser = coinDispensersMap.get(denomination);
            JLabel coinDenominationLabel = coinDenominationLabels.get(denomination);
            if (coinDenominationLabel == null) {
                coinDenominationLabel = new JLabel();
                coinDenominationLabels.put(denomination, coinDenominationLabel);
            }
            coinDenominationLabel.setText(denomination.doubleValue() + " dollars: " + dispenser.size());
            coinDenominationPanel.add(coinDenominationLabel);
        }

        // Update banknote denominations
        banknoteDenominationPanel.removeAll();
        BigDecimal[] banknoteDenominations = software.station.getBanknoteDenominations();
        Map<BigDecimal, IBanknoteDispenser> banknoteDispensersMap = software.getStationHardware().getBanknoteDispensers();
        for (BigDecimal denomination : banknoteDenominations) {
            IBanknoteDispenser dispenser = banknoteDispensersMap.get(denomination);
            JLabel banknoteDenominationLabel = banknoteDenominationLabels.get(denomination);
            if (banknoteDenominationLabel == null) {
                banknoteDenominationLabel = new JLabel();
                banknoteDenominationLabels.put(denomination, banknoteDenominationLabel);
            }
            banknoteDenominationLabel.setText(denomination.doubleValue() + " dollars: " + dispenser.size());
            banknoteDenominationPanel.add(banknoteDenominationLabel);
        }

        // Refresh UI
        revalidate();
        repaint();
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
