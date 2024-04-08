package com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware;


import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.scale.AbstractElectronicScale;
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


// GENERAL LAYOUT
// Box layout is used to arrange items vertically or horizontally
// Glue is like a spring << add on before and after box components to squish them to the middle

public class RemoveFromBagging extends JFrame {
    SelfCheckoutStationSoftware stationSoftwareInstance;
    AttendantPageGUI attendantGUI;
    BaggingArea baggingArea;
    Item item;

    public RemoveFromBagging(Item item, SelfCheckoutStationSoftware stationSoftwareInstance, AttendantPageGUI attendantGUI, BaggingArea baggingArea) {
        this.stationSoftwareInstance = stationSoftwareInstance;
        this.attendantGUI = attendantGUI;
        this.baggingArea = baggingArea;
        this.item = item;

        setTitle("Remove From Bag");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        // Main panel with BoxLayout for vertical alignment
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        String itemText = "Item";
        if (item instanceof BarcodedItem) {
            Barcode barcode = ((BarcodedItem) item).getBarcode();
            BarcodedProduct bProduct = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
            itemText = bProduct.getDescription();
        } else {
            PriceLookUpCode pluCode = ((PLUCodedItem) item).getPLUCode();
            PLUCodedProduct pProduct = ProductDatabases.PLU_PRODUCT_DATABASE.get(pluCode);
            itemText = pProduct.getDescription();
        }

        // Label for the large text display
        JLabel bigTextLabel = new JLabel("Item: " + itemText);
        bigTextLabel.setFont(new Font("Arial", Font.BOLD, 32));
        bigTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Label for the small text display
        JLabel smallTextLabel = new JLabel("Please remove item from the bagging area.");
        smallTextLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        smallTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button
        JButton button = new JButton("Remove item from the bagging area");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setPreferredSize(new Dimension(100, 30));

        String finalItemText = itemText;
        button.addActionListener(e -> {
            baggingArea.removeProduct(finalItemText);
            IElectronicScale scale = stationSoftwareInstance.getStationHardware().getBaggingArea();

            boolean itemRemovedFromBaggingArea = baggingArea.itemToRemove(finalItemText);

            if(itemRemovedFromBaggingArea) {
                scale.removeAnItem(item);
            }

            dispose();
        });

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(bigTextLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(smallTextLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(button);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);
        setVisible(true);
    }
}
