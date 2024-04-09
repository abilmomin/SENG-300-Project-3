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

package com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.jjjwelectronics.Mass;
import com.thelocalmarketplace.hardware.PLUCodedItem;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantPageGUI;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationHardware.BaggingArea;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * This class represents a graphical user interface for searching products by text input.
*/
@SuppressWarnings("serial")
public class SearchProductByText extends JFrame {
    private JTextField searchField;
    private JTextField filterField;
    private JButton submitButton;
    private JButton returnButton;
    private JList<Product> searchResults;
    private DefaultListModel<Product> listModel;
    private CustomKeyboard keyboard;
    BaggingArea baggingArea;

    /**
     * Constructs a SearchProductByText instance.
     * 
     * @param software 
     * 			The instance of SelfCheckoutStationSoftware used for product handling.
     * @param attendantGUI 
     * 			The AttendantPageGUI instance.
     * @param baggingArea 
     * 			The BaggingArea instance representing the area where scanned items are placed.
     */
    public SearchProductByText(SelfCheckoutStationSoftware software, AttendantPageGUI attendantGUI, BaggingArea baggingArea) {
        setTitle("Product Search");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.baggingArea = baggingArea;

        // Components
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(400, 30));
        searchField.getDocument().addDocumentListener(new FilterDocumentListener());

        filterField = new JTextField();
        filterField.setPreferredSize(new Dimension(400, 30));
        
        submitButton = new JButton("Add Item");
        returnButton = new JButton("Return to checkout");
        
        // Create custom keyboard
        keyboard = new CustomKeyboard(searchField);

        listModel = new DefaultListModel<>();
        searchResults = new JList<>(listModel);
        searchResults.setCellRenderer(new ProductListCellRenderer());

        JScrollPane scrollPane = new JScrollPane(searchResults);

        JPanel contentPane = new JPanel();
        
        contentPane.setLayout(new BorderLayout());
        contentPane.add(searchField, BorderLayout.NORTH);
        contentPane.add(filterField, BorderLayout.CENTER);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel submitPanel = new JPanel(new FlowLayout());
        submitButton.setPreferredSize(new Dimension(120, 30));
        returnButton.setPreferredSize(new Dimension(180, 30));
        submitPanel.add(submitButton);
        submitPanel.add(returnButton);

        bottomPanel.add(keyboard, BorderLayout.NORTH);
        bottomPanel.add(submitPanel, BorderLayout.CENTER);

        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(contentPane);
        
        submitButton.addActionListener(e -> {
            Product selectedProduct = searchResults.getSelectedValue();
            if (selectedProduct != null) {
                String productName = selectedProduct.getName();
                
                com.thelocalmarketplace.hardware.Product product = software.getProductHandler().findProductByTextSearch(productName);
                PLUCodedItem pluItem = new PLUCodedItem(((PLUCodedProduct) product).getPLUCode(), new Mass(1.0));

                software.getProductHandler().addItemByPLUCode(pluItem);
                
                new AddtoBagging(product, software, attendantGUI, new BaggingArea());
                baggingArea.addProduct(productName);

            } else {
                JOptionPane.showMessageDialog(SearchProductByText.this, "Please select a product first.");
            }
            
            this.setVisible(false);
        });

        returnButton.addActionListener(e -> {
        	dispose();
        });

        // Sample products
        List<Product> products = new ArrayList<>();
        products.add(new Product("Apple", "apple.png"));
        products.add(new Product("Banana", "banana.png"));
        products.add(new Product("Carrot", "carrots.png"));
        products.add(new Product("Asparagus", "asparagus.png"));

        for (Product product : products) {
            listModel.addElement(product);
        }
        setVisible(false);
    }

    /**
     * This class implements a DocumentListener to update the search results based on the text entered in the search field.
     */
    class FilterDocumentListener implements DocumentListener {
        public void insertUpdate(DocumentEvent e) {
            updateFilter();
        }

        public void removeUpdate(DocumentEvent e) {
            updateFilter();
        }

        public void changedUpdate(DocumentEvent e) {
            updateFilter();
        }

        private void updateFilter() {
            String filterText = searchField.getText().toLowerCase();
            DefaultListModel<Product> filteredModel = new DefaultListModel<>();
            for (int i = 0; i < listModel.getSize(); i++) {
                Product product = listModel.getElementAt(i);
                if (product.getName().toLowerCase().startsWith(filterText)) {
                    filteredModel.addElement(product);
                }
            }
            searchResults.setModel(filteredModel);
        }
    }

    /**
     * This class implements a custom virtual keyboard for a JTextField, providing functionality for entering text using buttons.
     */
    class CustomKeyboard extends JPanel {
        private JTextField textField;

        public CustomKeyboard(JTextField textField) {
            this.textField = textField;
            setLayout(new GridLayout(4, 1));

            // Define different-sized keys
            String[] keys1 = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
            String[] keys2 = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "Clear"};
            String[] keys3 = {"A", "S", "D", "F", "G", "H", "J", "K", "L", "Back"};
            String[] keys4 = {"Z", "X", "C", "V", "B", "N", "M", "Space"};

            addKeys(keys1);
            addKeys(keys2);
            addKeys(keys3);
            addKeys(keys4);
        }

        private void addKeys(String[] keys) {
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            for (String key : keys) {
                JButton button = new JButton(key);
                button.addActionListener(new KeyActionListener(key));
                rowPanel.add(button);
            }
            add(rowPanel);
        }

        class KeyActionListener implements ActionListener {
            private String key;

            public KeyActionListener(String key) {
                this.key = key;
            }

            public void actionPerformed(ActionEvent e) {
                switch (key) {
                    case "Clear":
                        textField.setText("");
                        break;
                    case "Back":
                        String text = textField.getText();
                        if (!text.isEmpty()) {
                            textField.setText(text.substring(0, text.length() - 1));
                        }
                        break;
                    case "Space":
                        textField.setText(textField.getText() + " ");
                        break;
                    default:
                        textField.setText(textField.getText() + key);
                        break;
                }
            }
        }
    }

    /**
     * Represents a product with a name and an image.
     */
    class Product {
        private String name;
        private ImageIcon image;

        public Product(String name, String imageName) {
            this.name = name;
            this.image = resizeImage("../../images/" + imageName, 50, 50); // Adjust size as needed
        }

        public String getName() {
            return name;
        }

        public ImageIcon getImage() {
            return image;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Custom renderer for displaying products in a list.
     */
    class ProductListCellRenderer extends JLabel implements ListCellRenderer<Product> {
        public Component getListCellRendererComponent(JList<? extends Product> list, Product value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            setOpaque(true);
            setText(value.getName());
            setIcon(value.getImage());
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            return this;
        }
    }

    /**
     * Resizes an image located at the specified path to the given width and height.
     * 
     * @param path   
     * 			The path to the image file.
     * @param width  
     * 			The desired width of the resized image.
     * @param height 
     * 			The desired height of the resized image.
     * @return An ImageIcon object representing the resized image, or null if the image file is not found.
     */
    private ImageIcon resizeImage(String path, int width, int height) {
        URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            ImageIcon originalIcon = new ImageIcon(imgURL);
            Image img = originalIcon.getImage();
            Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImg);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
