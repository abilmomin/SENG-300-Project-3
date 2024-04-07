package com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware;
import javax.swing.*;

import com.jjjwelectronics.printer.IReceiptPrinter;
import com.tdc.banknote.IBanknoteDispenser;
import com.tdc.coin.ICoinDispenser;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatusOfStation extends JFrame {

    private SelfCheckoutStationSoftware software;
    private JLabel inkLabel;
    private JLabel paperLabel;
    private JPanel coinDenominationPanel;
    private JPanel banknoteDenominationPanel;
    private Map<BigDecimal, JLabel> coinDenominationLabels;
    private Map<BigDecimal, JLabel> banknoteDenominationLabels;

    public StatusOfStation(SelfCheckoutStationSoftware software) {
        this.software = software;
        this.coinDenominationLabels = new HashMap<>();
        this.banknoteDenominationLabels = new HashMap<>();

        setTitle("Status of Station");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Set to dispose on close
        setSize(300, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(0, 1)); // Vertical layout
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding to the main panel

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
        JPanel coinPanel = new JPanel(new BorderLayout()); // Use BorderLayout for tabbed style
        coinPanel.add(coinLabel, BorderLayout.NORTH);
        coinPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add padding

        coinDenominationPanel = new JPanel(new GridLayout(0, 1)); // Vertical layout for stacked denominations
        coinPanel.add(coinDenominationPanel, BorderLayout.CENTER); // Add stacked denominations
        mainPanel.add(coinPanel);

        // Banknote denominations
        JLabel banknoteLabel = new JLabel("Banknote Denominations:");
        JPanel banknotePanel = new JPanel(new BorderLayout()); // Use BorderLayout for tabbed style
        banknotePanel.add(banknoteLabel, BorderLayout.NORTH);
        banknotePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add padding

        banknoteDenominationPanel = new JPanel(new GridLayout(0, 1)); // Vertical layout for stacked denominations
        banknotePanel.add(banknoteDenominationPanel, BorderLayout.CENTER); // Add stacked denominations
        mainPanel.add(banknotePanel);

        add(mainPanel);
        
        // Initial update
        updateLabels();
        setVisible(false);
    }

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
