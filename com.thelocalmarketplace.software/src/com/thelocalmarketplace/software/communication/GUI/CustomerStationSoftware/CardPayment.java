package com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware;

import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class CardPayment extends JFrame {

    private int paymentType = 0;

    public CardPayment(SelfCheckoutStationSoftware software, String typeOfCard) {
        setTitle("Select Card Payment Method");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JRadioButton swipeButton = new JRadioButton("Swipe");
        swipeButton.addActionListener(e -> {
            paymentType = 1;
        });
        JRadioButton tapButton = new JRadioButton("Tap");
        tapButton.addActionListener(e -> {
            paymentType = 2;
        });
        JRadioButton insertButton = new JRadioButton("Insert Card");
        insertButton.addActionListener(e -> {
            paymentType = 3;
        });

        ButtonGroup group = new ButtonGroup();
        group.add(swipeButton);
        group.add(tapButton);
        group.add(insertButton);

        JPanel radioPanel = new JPanel(new GridLayout(0, 1));
        radioPanel.add(swipeButton);
        radioPanel.add(tapButton);
        radioPanel.add(insertButton);

        JButton finishPaymentButton = new JButton("Finish Payment");
        finishPaymentButton.addActionListener(e -> {
            try {

                switch (paymentType) {
                    case 1 -> software.getStationHardware().getCardReader().swipe(software.getCard(typeOfCard));
                    case 2 -> software.getStationHardware().getCardReader().tap(software.getCard(typeOfCard));
                    // case 3 -> software.getStationHardware().getCardReader().insert(software.getCard(typeOfCard), );
                    default -> JOptionPane.showMessageDialog(this, "Please select a payment method.", "Payment", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
//            dispose();
        });

        add(radioPanel);
        add(finishPaymentButton);

        setVisible(false);
    }
}
