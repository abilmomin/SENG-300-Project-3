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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
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
