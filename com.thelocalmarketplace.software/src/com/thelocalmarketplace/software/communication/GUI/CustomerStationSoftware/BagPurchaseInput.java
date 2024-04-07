package com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BagPurchaseInput extends JDialog {
    private int numOfBags = 0;

    public BagPurchaseInput(Frame owner) {
        super(owner, "Purchase Bags", true);
        initializeUI();
    }

    private void initializeUI() {
        setSize(300, 300);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        JTextField inputField = new JTextField();
        inputField.setEditable(false);
        inputField.setHorizontalAlignment(JTextField.CENTER);
        add(inputField, BorderLayout.NORTH);

        JPanel keypadPanel = new JPanel(new GridLayout(4, 3));
        ActionListener numPadActionListener = e -> inputField.setText(inputField.getText() + e.getActionCommand());

        for (int i = 1; i <= 9; i++) {
            JButton button = new JButton(Integer.toString(i));
            button.addActionListener(numPadActionListener);
            keypadPanel.add(button);
        }

        keypadPanel.add(new JLabel(""));
        JButton zeroButton = new JButton("0");
        zeroButton.addActionListener(numPadActionListener);
        keypadPanel.add(zeroButton);
        keypadPanel.add(new JLabel(""));

        add(keypadPanel, BorderLayout.CENTER);

        JPanel confirmPanel = new JPanel(new FlowLayout());
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            String input = inputField.getText();
            if (!input.isEmpty()) {
                try {
                    numOfBags = Integer.parseInt(input);
                    dispose(); // Close the dialog
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid number. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        confirmPanel.add(okButton);
        confirmPanel.add(cancelButton);

        add(confirmPanel, BorderLayout.SOUTH);
    }

    public int getNumOfBags() {
        return numOfBags;
    }
}
