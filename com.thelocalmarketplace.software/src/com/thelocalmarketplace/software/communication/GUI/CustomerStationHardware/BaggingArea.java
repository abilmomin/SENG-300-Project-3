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
// WORK IN PROGRESS
package com.thelocalmarketplace.software.communication.GUI.CustomerStationHardware;

import javax.swing.*;
import java.awt.*;
import javax.swing.*;
import java.awt.*;

public class BaggingArea extends JFrame {
    public JFrame baggingAreaFrame;
    JPanel baggingAreaPanel;
    JButton returnToCheckoutButton;
    JList<Product> productList;
    JScrollPane scrollPane;
    DefaultListModel<Product> listModel;

    // Constructor for the BaggingArea class
    public BaggingArea() {
        baggingAreaFrame = new JFrame("Bagging Area Simulation");
        baggingAreaPanel = new JPanel();
        baggingAreaPanel.setLayout(new BoxLayout(baggingAreaPanel, BoxLayout.Y_AXIS));
        setSize(600, 400);  // Set the size of the window

        listModel = new DefaultListModel<>();

        addWidgets();
    }

    public void addProduct(String name) {
        System.out.println("Adding product: " + name);
        listModel.addElement(new Product(name));
    }

    public void removeProduct(String name) {
        System.out.println("Removing product: " + name);
        for (int i = 0; i < listModel.size(); i++) {

            System.out.println("THIS IS THE NAME 1: " + listModel.get(i).getName());
            System.out.println("THIS IS THE NAME 2: " + name);
            if (listModel.get(i).getName().equals(name)) {
                System.out.println("ACTUALLY REMOVING");
                listModel.remove(i);
                break;
            }
        }
    }

    public boolean itemToRemove(String name) {
        for (int i = 0; i < listModel.size(); i++) {
            if (listModel.get(i).getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    // Add widgets to the frame
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

    // Method to create styled buttons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 50));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(Color.LIGHT_GRAY);
        return button;
    }

    // Product class to hold item information
    static class Product {
        String name;

        public Product(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    // Custom cell renderer to display products
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
