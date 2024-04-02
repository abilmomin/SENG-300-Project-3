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
package com.thelocalmarketplace.software.communication;

import javax.swing.*;
import java.awt.*;

public class BaggingArea extends JFrame {
    JFrame baggingAreaFrame;
    JPanel baggingAreaPanel;
    JButton returnToCheckoutButton, removeItemButton;
    JList<Product> productList;
    JScrollPane scrollPane;
    DefaultListModel<Product> listModel;

    // Constructor for the BaggingArea class
    public BaggingArea() {
        baggingAreaFrame = new JFrame("Bagging Area Simulation");
        baggingAreaPanel = new JPanel();
        baggingAreaPanel.setLayout(new BoxLayout(baggingAreaPanel, BoxLayout.Y_AXIS));

        initializeProducts();

        addWidgets();
    }

    // Initialize the products with corresponding images
    private void initializeProducts() {
        listModel = new DefaultListModel<>();
        listModel.addElement(new Product("Asparagus", "./images/asparagus.png"));
        listModel.addElement(new Product("Banana", "./images/banana.png"));
        listModel.addElement(new Product("Apples", "./images/apple.png"));
        listModel.addElement(new Product("Carrots", "./images/carrots.png"));
    }

    // Add widgets to the frame
    private void addWidgets() {
        productList = new JList<>(listModel);
        productList.setCellRenderer(new ProductRenderer());
        scrollPane = new JScrollPane(productList);
        baggingAreaPanel.add(scrollPane);

        removeItemButton = createStyledButton("Remove Item");
        removeItemButton.addActionListener(e -> {
            if (!productList.isSelectionEmpty()) {
                listModel.removeElement(productList.getSelectedValue());
            }
        });
        baggingAreaPanel.add(removeItemButton);

        returnToCheckoutButton = createStyledButton("Return to Checkout");
        returnToCheckoutButton.addActionListener(e -> baggingAreaFrame.dispose());
        baggingAreaPanel.add(returnToCheckoutButton);

        baggingAreaFrame.add(baggingAreaPanel);
        baggingAreaFrame.setSize(600, 400); // Increased size
        baggingAreaFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        baggingAreaFrame.setLocationRelativeTo(null);
        baggingAreaFrame.setVisible(true);
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
        String imagePath;

        public Product(String name, String imagePath) {
            this.name = name;
            this.imagePath = imagePath;
        }

        public String getName() {
            return name;
        }

        public String getImagePath() {
            return imagePath;
        }
    }

    // Custom cell renderer to display products with images
    static class ProductRenderer extends JLabel implements ListCellRenderer<Product> {
        @Override
        public Component getListCellRendererComponent(JList<? extends Product> list, Product product, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            setText(product.getName());

            // Load the image
            ImageIcon icon = createImageIcon(product.getImagePath(), product.getName());
            if (icon != null) {
                // Scale the icon if necessary
                Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                setIcon(new ImageIcon(img));
            } else {
                System.out.println("Image not found: " + product.getImagePath());
            }

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

        // Helper method to load images
        protected static ImageIcon createImageIcon(String path, String description) {
            java.net.URL imgURL = BaggingArea.class.getResource(path);
            if (imgURL != null) {
                return new ImageIcon(imgURL, description);
            } else {
                System.err.println("Couldn't find file: " + path);
                return null;
            }
        }
    }
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(BaggingArea::new);
    }
}
