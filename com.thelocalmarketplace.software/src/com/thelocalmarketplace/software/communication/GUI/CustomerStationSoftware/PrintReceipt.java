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

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.printer.IReceiptPrinter;
import com.jjjwelectronics.printer.ReceiptPrinterBronze;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.funds.Funds;
import com.thelocalmarketplace.software.funds.Receipt;

import powerutility.PowerGrid;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class PrintReceipt extends JFrame{


    public PrintReceipt(IReceiptPrinter printer, Funds funds) {
        JFrame printerFrame = new JFrame("Receipt Printer Option");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);

        Container printerPane = printerFrame.getContentPane();
        printerPane.setLayout(new BorderLayout());
        JLabel receiptQuestion = new JLabel("Would you like a receipt?");
        JCheckBox ck1 = new JCheckBox("Yes");
        ck1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Receipt rec = new Receipt(printer, funds);
                try {
                    JOptionPane.showMessageDialog(PrintReceipt.this, rec.printReceipt());
                    System.exit(EXIT_ON_CLOSE);
                } catch (HeadlessException | EmptyDevice | OverloadedDevice e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

        });
        JCheckBox ck2 = new JCheckBox("No");
        ck2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(PrintReceipt.this, "Thank you for shopping with us! We hope to see you again!");
                System.exit(EXIT_ON_CLOSE);

            }

        });
        printerPane.add(receiptQuestion, "North");
        printerPane.add(ck1, "West");
        printerPane.add(ck2, "East");

        printerFrame.setVisible(true);



    }



}
