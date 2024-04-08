package com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware;

import javax.swing.*;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedItem;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantPageGUI;
import java.awt.*;

public class RemoveItemRequest extends JFrame {

    public RemoveItemRequest(Item item, SelfCheckoutStationSoftware stationSoftwareInstance, AttendantPageGUI attendantGUI) {
        setTitle("Item Removed");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        JLabel headerLabel = new JLabel("Scale Overload");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        String itemName = "";
        
        if (item instanceof BarcodedItem) {
        	Barcode barcode = ((BarcodedItem) item).getBarcode();
        	BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
        	itemName = product.getDescription();
        } else if (item instanceof PLUCodedItem) {
        	PriceLookUpCode pluCode = ((PLUCodedItem) item).getPLUCode();
        	PLUCodedProduct product = ProductDatabases.PLU_PRODUCT_DATABASE.get(pluCode);
        	itemName = product.getDescription();
        }

        JLabel itemLabel = new JLabel(itemName);
        itemLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        itemLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel instructionLabel = new JLabel("Please remove item from the bagging area to continue checkout.");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton removeButton = new JButton("Remove from Bagging Area");
        removeButton.setFont(new Font("Arial", Font.BOLD, 16));
        removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeButton.setBackground(Color.RED);
        removeButton.setForeground(Color.WHITE);
        removeButton.setOpaque(true);
        removeButton.setBorderPainted(false);

        removeButton.addActionListener(e -> {
        	stationSoftwareInstance.getStationHardware().getBaggingArea().removeAnItem(item);

            dispose();
        });
       
        add(Box.createVerticalStrut(20));
        add(headerLabel);
        add(Box.createVerticalStrut(10)); 
        add(itemLabel);
        add(Box.createVerticalStrut(10)); 
        add(instructionLabel);
        add(Box.createVerticalStrut(20)); 
        add(removeButton);
        add(Box.createVerticalStrut(20)); 

        pack(); 
        setLocationRelativeTo(null); 
        setVisible(true);
    }

//    public static void main(final String[] args) {
//        SwingUtilities.invokeLater(RemoveItemRequest::new);
//    }
}
