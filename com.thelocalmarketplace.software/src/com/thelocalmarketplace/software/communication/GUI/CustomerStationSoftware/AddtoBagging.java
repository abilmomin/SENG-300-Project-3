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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import com.jjjwelectronics.Item;
import com.jjjwelectronics.scale.IElectronicScale;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedItem;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantPageGUI;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationHardware.BaggingArea;


/**
 * Represents a user interface for adding items to the bagging area in a self-checkout system.
 * This class provides functionality for displaying item information and options for adding items to the bagging area.
 */
@SuppressWarnings("serial")
public class AddtoBagging extends JFrame {
    SelfCheckoutStationSoftware stationSoftwareInstance;
    AttendantPageGUI attendantGUI;
    BaggingArea baggingArea;
    
    /**
     * Constructor that creates a panel for adding items to the bagging area.
     * 
     * @param product 
     * 			The product to be added to the bagging area.
     * @param stationSoftwareInstance 
     * 			The SelfCheckoutStationSoftware instance associated with the hardware.
     * @param attendantGUI 
     * 			The AttendantPageGUI instance for the attendant user interface.
     * @param baggingArea 
     * 			The BaggingArea instance for displaying the bagging area interface.
     */
    public AddtoBagging(Product product, SelfCheckoutStationSoftware stationSoftwareInstance, AttendantPageGUI attendantGUI, BaggingArea baggingArea) {
        this.stationSoftwareInstance = stationSoftwareInstance;
        this.attendantGUI = attendantGUI;
        this.baggingArea = baggingArea;

        initializeUI(product);
        setVisible(true);
    }

    /**
     * Initializes the user interface components for adding items to the bagging area.
     * 
     * @param product 
     * 			The product to be added to the bagging area.
     */
    private void initializeUI(Product product) {
        setTitle("Add to Bag");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        // Main panel with BoxLayout for vertical alignment
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        String item = "Item";
        if (product instanceof BarcodedProduct) {
            BarcodedProduct bProduct = (BarcodedProduct) product;
            item = bProduct.getDescription();
        } else {
            PLUCodedProduct pProduct = (PLUCodedProduct) product;
            item = pProduct.getDescription();
        }

        // Label for the large text display
        JLabel bigTextLabel = new JLabel("Item: " + item);
        bigTextLabel.setFont(new Font("Arial", Font.BOLD, 32));
        bigTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Label for the small text display
        JLabel smallTextLabel = new JLabel("Please add item to bagging area.");
        smallTextLabel.setFont(new Font("Arial", Font.PLAIN, 12)); 
        smallTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 

        // Button panel
        JPanel buttonPanel = createButtonPanel(product);

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(bigTextLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(smallTextLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);
    }

    /**
     * Creates and configures the button panel for adding items to the bagging area.
     * 
     * @param product 
     * 			The product to be added to the bagging area.
     * @return The configured button panel.
     */
    private JPanel createButtonPanel(Product product) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JButton placeItemButton = new JButton("Place Item in Bagging Area");
        placeItemButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        placeItemButton.setFont(new Font("Arial", Font.PLAIN, 16));
        placeItemButton.setPreferredSize(new Dimension(100, 30));
        placeItemButton.addActionListener(e -> {
            placeItemInBaggingArea(product);
            dispose();
        });

        JButton dontBagItemButton = new JButton("Don't Bag Item");
        dontBagItemButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        dontBagItemButton.setFont(new Font("Arial", Font.PLAIN, 16));
        dontBagItemButton.setPreferredSize(new Dimension(100, 30));
        dontBagItemButton.addActionListener(e -> {
            dontBagItem();
            dispose();
        });

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(placeItemButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(dontBagItemButton);
        buttonPanel.add(Box.createVerticalGlue());

        return buttonPanel;
    }

    /**
     * Handles the scenario when the item is placed in the bagging area.
     * 
     * @param product 
     * 		The product to be placed in the bagging area.
     */
    private void placeItemInBaggingArea(Product product) {
        if (product instanceof BarcodedProduct) {
            ArrayList<Item> order = stationSoftwareInstance.getOrder();
            if (order.size() > 0) {
                BarcodedItem itemToAdd = (BarcodedItem) order.get(order.size() - 1);
                IElectronicScale baggingAreaScale = stationSoftwareInstance.getStationHardware().getBaggingArea();
                baggingAreaScale.addAnItem(itemToAdd);
                Barcode barcode = itemToAdd.getBarcode();
                BarcodedProduct barcodedProduct = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
                baggingArea.addProduct(barcodedProduct.getDescription());
            }
        } else {
            ArrayList<Item> order = stationSoftwareInstance.getOrder();
            if (order.size() > 0) {
                PLUCodedItem itemToAdd = (PLUCodedItem) order.get(order.size() - 1);
                IElectronicScale baggingAreaScale = stationSoftwareInstance.getStationHardware().getBaggingArea();
                baggingAreaScale.addAnItem(itemToAdd);
                PriceLookUpCode pluCode = itemToAdd.getPLUCode();
                PLUCodedProduct pluCodedProduct = ProductDatabases.PLU_PRODUCT_DATABASE.get(pluCode);
                baggingArea.addProduct(pluCodedProduct.getDescription());
            }
        }
    }

	 /**
     * Handles the scenario when the item is not added to the bagging area.
     */
    private void dontBagItem() {
    	ArrayList<Item> orderList = stationSoftwareInstance.getOrder();
    	if (!orderList.isEmpty()) {
    		int lastIndex = orderList.size() - 1;

            // Extract the last item from the list
            Item lastItem = orderList.get(lastIndex);
            double massInGramsDouble = lastItem.getMass().inGrams().doubleValue();
            
            stationSoftwareInstance.getProductHandler().handleBulkyItem(massInGramsDouble,this.attendantGUI);
    	} else {
    	  
    		JOptionPane.showMessageDialog(this, "Scan Item First");
    	}
	}
}
