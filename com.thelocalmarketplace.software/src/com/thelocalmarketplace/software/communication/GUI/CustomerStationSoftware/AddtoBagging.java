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
import com.jjjwelectronics.Mass;
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

@SuppressWarnings("serial")
public class AddtoBagging extends JFrame {
	SelfCheckoutStationSoftware stationSoftwareInstance;
	AttendantPageGUI attendantGUI;
	BaggingArea baggingArea;
	
	public AddtoBagging(Product product, SelfCheckoutStationSoftware stationSoftwareInstance, AttendantPageGUI attendantGUI, BaggingArea baggingArea) {
		this.stationSoftwareInstance = stationSoftwareInstance;
		this.attendantGUI = attendantGUI;
		this.baggingArea = baggingArea;

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
	    JButton button = new JButton("Place Item in Bagging Area");
	    button.setAlignmentX(Component.CENTER_ALIGNMENT);
	    button.setFont(new Font("Arial", Font.PLAIN, 16));
	    button.setPreferredSize(new Dimension(100, 30));
	    
	    JButton doNotBagBtn = new JButton("Don't Bag Item");
	    doNotBagBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
	    doNotBagBtn.setFont(new Font("Arial", Font.PLAIN, 16));
	    doNotBagBtn.setPreferredSize(new Dimension(100, 30));

        button.addActionListener(e -> {
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
            dispose();
        });
	    
	    doNotBagBtn.addActionListener(e -> {
	    	dontBagItem();
	    	dispose();
	    });
	    
	    mainPanel.add(Box.createVerticalGlue());
	    mainPanel.add(bigTextLabel);
	    mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
	    mainPanel.add(smallTextLabel);
	    mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
	    mainPanel.add(button);
	    mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
	    mainPanel.add(doNotBagBtn);
	    mainPanel.add(Box.createVerticalGlue());

	    add(mainPanel);
	    setVisible(true);
	}
	
    private void dontBagItem() {
		// TODO Auto-generated method stub
    	ArrayList<Item> orderList = stationSoftwareInstance.getOrder();
    	if (!orderList.isEmpty()) {
    		int lastIndex = orderList.size() - 1;

            // Extract the last item from the list
            Item lastItem = orderList.get(lastIndex);
            double massInGramsDouble = lastItem.getMass().inGrams().doubleValue();
            
            stationSoftwareInstance.getProductHandler().handleBulkyItem(massInGramsDouble,this.attendantGUI);
    	} else {
    	    // Handle the case when the list is empty
    		JOptionPane.showMessageDialog(this, "Scan Item First");
    	}
	}
}
