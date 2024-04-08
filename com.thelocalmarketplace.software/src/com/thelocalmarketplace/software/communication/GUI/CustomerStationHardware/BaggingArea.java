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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

@SuppressWarnings("serial")
public class BaggingArea extends JFrame {
    public JFrame baggingAreaFrame;
    JPanel baggingAreaPanel;
    JButton returnToCheckoutButton;
    JList<Product> productList;
    JScrollPane scrollPane;
    DefaultListModel<Product> listModel;

    /**
     * Simple constructor for BaggingArea. Initializes a new JFrame.
     */
    public BaggingArea() {
        baggingAreaFrame = new JFrame("Bagging Area Simulation");
        baggingAreaPanel = new JPanel();
        baggingAreaPanel.setLayout(new BoxLayout(baggingAreaPanel, BoxLayout.Y_AXIS));
        setSize(600, 400);

        listModel = new DefaultListModel<>();

        addWidgets();
    }

    /**
     * Add a product onto the listModel.
     */
    public void addProduct(String name) {
        listModel.addElement(new Product(name));
    }

    /**
     * Remove a product from the listModel.
     * @param name of the product to be removed.
     */
    public void removeProduct(String name) {
        for (int i = 0; i < listModel.size(); i++) {
            if (listModel.get(i).getName().equals(name)) {
                listModel.remove(i);
                break;
            }
        }
    }

    /**
     * Checks if a product exists in the listModel.
     * @param name of the product to be checked.
     * @return true if the product exists, false otherwise.
     */
    public boolean itemToRemove(String name) {
        for (int i = 0; i < listModel.size(); i++) {
            if (listModel.get(i).getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private void addWidgets() {
        productList = new JList<>(listModel);
        productList.setCellRenderer(new ProductRenderer());
        scrollPane = new JScrollPane(productList);
        baggingAreaPanel.add(scrollPane);

        returnToCheckoutButton = createStyledButton("Return to Checkout");
        returnToCheckoutButton.addActionListener(e -> baggingAreaFrame.dispose());
        baggingAreaPanel.add(returnToCheckoutButton);

        baggingAreaFrame.add(baggingAreaPanel);
        baggingAreaFrame.setSize(600, 400);
        baggingAreaFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        baggingAreaFrame.setLocationRelativeTo(null);
        baggingAreaFrame.setVisible(false);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 50));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(Color.LIGHT_GRAY);
        return button;
    }

    static class Product {
        String name;

        public Product(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    static class ProductRenderer extends JLabel implements ListCellRenderer<Product> {
        @Override
        public Component getListCellRendererComponent(JList<? extends Product> list, Product product, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            setText(product.getName());

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setOpaque(true);
            return this;
        }
    }
}
