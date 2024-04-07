package com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware;

import java.awt.*;

import javax.swing.*;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.scale.IElectronicScale;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedItem;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;


// GENERAL LAYOUT
// Box layout is used to arrange items vertically or horizontally
// Glue is like a spring << add on before and after box components to squish them to the middle

public class AddtoBagging extends JFrame {
	
	public AddtoBagging(Product product, SelfCheckoutStationSoftware stationSoftwareInstance) {

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

	    // Button
	    JButton button = new JButton("Place item");
	    button.setAlignmentX(Component.CENTER_ALIGNMENT);
	    button.setFont(new Font("Arial", Font.PLAIN, 16));
	    button.setPreferredSize(new Dimension(100, 30));

	    
	    button.addActionListener(e -> {
	    	if (product instanceof BarcodedProduct) {
	    		BarcodedProduct barcodedProduct = (BarcodedProduct) product;
	    		
	    		stationSoftwareInstance.getProductHandler().addItemViaBarcodeScan(barcodedProduct.getBarcode());
	    		
	            double productWeight = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcodedProduct.getBarcode()).getExpectedWeight();
	            Mass mass = new Mass(productWeight);
	            BarcodedItem barcodedItem = new BarcodedItem(barcodedProduct.getBarcode(), mass);
	            
	        	IElectronicScale baggingArea = stationSoftwareInstance.getStationHardware().getBaggingArea();
	        	baggingArea.addAnItem(barcodedItem);
	    		
	    	} else {
	    		PLUCodedProduct pluCodedProduct = (PLUCodedProduct) product;
	    		
	    		PLUCodedItem pluItem = new PLUCodedItem(pluCodedProduct.getPLUCode(), new Mass(1.0));
	    		
	    		stationSoftwareInstance.getProductHandler().addItemByPLUCode(pluItem);
	    		
	        	IElectronicScale baggingArea = stationSoftwareInstance.getStationHardware().getBaggingArea();
	        	baggingArea.addAnItem(pluItem);
	    	}
	    	dispose();
	    });
	    
	   
	    mainPanel.add(Box.createVerticalGlue());
	    mainPanel.add(bigTextLabel);
	    mainPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Small space between big text and small text
	    mainPanel.add(smallTextLabel);
	    mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
	    mainPanel.add(button);
	    mainPanel.add(Box.createVerticalGlue());


	    add(mainPanel);
	    setVisible(true);
	}
}
