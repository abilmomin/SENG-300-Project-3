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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchProductByText extends JFrame {
    private JTextField searchField;
    private JTextField filterField;
    private JList<Product> searchResults;
    private DefaultListModel<Product> listModel;
    private CustomKeyboard keyboard;

    public SearchProductByText() {
        setTitle("Product Search");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Components
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(400, 30));
        searchField.getDocument().addDocumentListener(new FilterDocumentListener());

        filterField = new JTextField();
        filterField.setPreferredSize(new Dimension(400, 30));

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
        contentPane.add(keyboard, BorderLayout.SOUTH);

        setContentPane(contentPane);

        // Sample products
        List<Product> products = new ArrayList<>();
        products.add(new Product("Apple", "apple.png"));
        products.add(new Product("Banana", "banana.png"));
        products.add(new Product("Carrot", "carrots.png"));
        products.add(new Product("Asparagus", "asparagus.png"));

        for (Product product : products) {
            listModel.addElement(product);
        }
    }

    public static void main(String[] args) {
    	//for testing, delete main when integrating all of gui together
        SwingUtilities.invokeLater(() -> {
            SearchProductByText app = new SearchProductByText();
            app.setVisible(true);
        });
    }

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

    class Product {
        private String name;
        private ImageIcon image;

        public Product(String name, String imageName) {
            this.name = name;
            this.image = resizeImage("./images/" + imageName, 50, 50); // Adjust size as needed
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
