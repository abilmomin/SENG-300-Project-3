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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantPageGUI;

import ca.ucalgary.seng300.simulation.InvalidArgumentSimulationException;


/**
 * Represents a graphical user interface for requesting the removal of an item from the bagging area.
 */
@SuppressWarnings("serial")
public class RemoveItemRequest extends JFrame {
	
	
	/**
     * Initializes a new instance of the RemoveItemRequest class.
     * 
     * @param itemName              
     * 						The name of the item to be removed.
     * 
     * @param stationSoftwareInstance 
     * 						The instance of the self-checkout station software.
     * 
     * @param attendantGUI          
     * 						The GUI instance for the attendant page.
     */
    public RemoveItemRequest(String itemName, SelfCheckoutStationSoftware stationSoftwareInstance, AttendantPageGUI attendantGUI) {
        setTitle("Item Removed");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        JLabel headerLabel = new JLabel("Scale Overload");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

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
        	try {
        		stationSoftwareInstance.getGUI().handleRemoveItem();
            	stationSoftwareInstance.getGUI().canMakePopUp = true;
        	} catch (InvalidArgumentSimulationException error) {
        		error.printStackTrace();
        	}
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
}