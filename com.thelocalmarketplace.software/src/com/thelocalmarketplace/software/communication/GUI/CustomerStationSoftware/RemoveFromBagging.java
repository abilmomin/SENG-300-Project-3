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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.jjjwelectronics.Item;
import com.jjjwelectronics.scale.IElectronicScale;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedItem;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantPageGUI;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationHardware.BaggingArea;


@SuppressWarnings("serial")
public class RemoveFromBagging extends JFrame {
    SelfCheckoutStationSoftware stationSoftwareInstance;
    AttendantPageGUI attendantGUI;
    BaggingArea baggingArea;
    Item item;

    /**
     * Initializes a new instance of the RemoveFromBagging class.
     * 
     * @param item                 
     * 				The item to be removed from the bagging area.
     * @param stationSoftwareInstance 
     * 				The instance of the self-checkout station software.
     * @param attendantGUI         
     * 				The GUI instance for the attendant page.
     * @param baggingArea          
     * 				The bagging area from which the item needs to be removed.
     */
    public RemoveFromBagging(Item item, SelfCheckoutStationSoftware stationSoftwareInstance, AttendantPageGUI attendantGUI, 
    						BaggingArea baggingArea) {
        this.stationSoftwareInstance = stationSoftwareInstance;
        this.attendantGUI = attendantGUI;
        this.baggingArea = baggingArea;
        this.item = item;

        setTitle("Remove From Bag");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
    }

    /**
     * Builds and displays the graphical user interface for removing an item from the bagging area.
     */
    public void buildUI() {
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
            IElectronicScale scale = stationSoftwareInstance.getStationHardware().getBaggingArea();

            boolean itemRemovedFromBaggingArea = baggingArea.itemToRemove(finalItemText);
            if(itemRemovedFromBaggingArea) {
                scale.removeAnItem(item);
            }
            baggingArea.removeProduct(finalItemText);
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