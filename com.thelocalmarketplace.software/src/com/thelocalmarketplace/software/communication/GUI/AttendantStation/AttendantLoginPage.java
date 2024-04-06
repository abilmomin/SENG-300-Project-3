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

package com.thelocalmarketplace.software.communication.GUI.AttendantStation;

import javax.swing.*;

import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AttendantLoginPage extends JFrame{
//	private AttendantStation attendantStation;
//	
//	public AttendantStationSoftware (AttendantStation attendantStation) {
//		this.attendantStation = attendantStation;
//	}
//	
	
	// monitor stations to see if banknote, coins, paper, ink levels are low/high
	// 		Attendant should be signaled if any of these have low levels while a customer is paying
	//		read the case descriptions there's too many to type here
	// enable/disable station
	// be notified when a customer needs assistance, clear the request once theyve been assisted
	// approve bulky item request 
	// approve no bagging item request
	// approve a weight disc


	private static final String USERNAME = "1234";
	private static final String PASSWORD = "1234";
	private static final int WINDOW_WIDTH = 900;
	private static final int WINDOW_HEIGHT = 700;


	JPanel loginPanel = new JPanel(new GridLayout(7, 1));
	JLabel idLabel = new JLabel("Attendant ID Number: ");
	JLabel passwordLabel = new JLabel("Password: ");

	private static final long serialVersionUID = 1L; 
	private JTextField idField;
    private JPasswordField passwordField;
    private static ArrayList<Barcode> barcodes;
    private static ArrayList<PriceLookUpCode> pluCodes;

    public AttendantLoginPage() {
		// Initialize the GUI
		setUpGUI();
		setupLoginPanel();

		//Main Panel
		JPanel mainPanel = new JPanel(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER; // End row after this component
		gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch component horizontally
		gbc.anchor = GridBagConstraints.CENTER; // Center component

		mainPanel.add(loginPanel, gbc);
		add(mainPanel);
		setVisible(true);

		// Make PLU products
		PriceLookUpCode pluCode1 = new PriceLookUpCode("1111");
		PLUCodedProduct apple = new PLUCodedProduct(pluCode1, "Apple", 2L);
		PriceLookUpCode pluCode2 = new PriceLookUpCode("2222");
		PLUCodedProduct banana = new PLUCodedProduct(pluCode2, "Banana", 1L);
		PriceLookUpCode pluCode3 = new PriceLookUpCode("3333");
		PLUCodedProduct carrot = new PLUCodedProduct(pluCode3, "Carrot", 1L);
		PriceLookUpCode pluCode4 = new PriceLookUpCode("4444");
		PLUCodedProduct asparagus = new PLUCodedProduct(pluCode4, "Asparagus", 3L);

		// Make barcoded products
		Numeral[] barcode1 = {Numeral.five, Numeral.five, Numeral.five, Numeral.five};
		BarcodedProduct cereal = new BarcodedProduct(new Barcode(barcode1), "Cereal", 9L, 500);
		Numeral[] barcode2 = {Numeral.six, Numeral.six, Numeral.six, Numeral.six};
		BarcodedProduct soup = new BarcodedProduct(new Barcode(barcode2), "Soup", 4L, 500);
		Numeral[] barcode3 = {Numeral.seven, Numeral.seven, Numeral.seven, Numeral.seven};
		BarcodedProduct frozenPizza = new BarcodedProduct(new Barcode(barcode3), "Frozen pizza", 12L, 400);
		Numeral[] barcode4 = {Numeral.eight, Numeral.eight, Numeral.eight, Numeral.eight};
		BarcodedProduct gum = new BarcodedProduct(new Barcode(barcode4), "Gum", 2L, 50);

		// Populate the product databases
		ProductDatabases.PLU_PRODUCT_DATABASE.put(pluCode1, apple);
		ProductDatabases.PLU_PRODUCT_DATABASE.put(pluCode2, banana);
		ProductDatabases.PLU_PRODUCT_DATABASE.put(pluCode3, carrot);
		ProductDatabases.PLU_PRODUCT_DATABASE.put(pluCode4, asparagus);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(new Barcode(barcode1), cereal);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(new Barcode(barcode2), soup);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(new Barcode(barcode3), frozenPizza);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(new Barcode(barcode4), gum);
	}

	public static ArrayList<Barcode> getBarcodes() {
		return barcodes;
	}

	public static ArrayList<PriceLookUpCode> getPLUCodes() {
		return pluCodes;
	}

	private void setUpGUI() {
		setTitle("Attendant Station Login");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	private void setupLoginPanel() {
		idField = new JTextField();
		passwordField = new JPasswordField();
		JButton loginButton = new JButton("Login");


		loginPanel.add(idLabel);
		loginPanel.add(idField);
		loginPanel.add(passwordLabel);
		loginPanel.add(passwordField);
		loginPanel.add(loginButton);

		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = idField.getText();
				String password = new String(passwordField.getPassword());
				// Check if id and password are correct
				if (isValidLogin(id, password)) {
					openAttendantPage();
				} else {
					JOptionPane.showMessageDialog(AttendantLoginPage.this,
							"Invalid ID or password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
					idField.setText("");
					passwordField.setText("");
				}
			}
		});
	}
	
	private boolean isValidLogin(String id, String password) {
	    return id.equals(USERNAME) && password.equals(PASSWORD);
	}

	private void openAttendantPage() {
	    // Open the attendant page here
	    EventQueue.invokeLater(new Runnable() {
	        public void run() {
	            new AttendantPageGUI().setVisible(true);
	        }
	    });
	    // Close the login frame
	    dispose();
	}
	
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(AttendantLoginPage::new); 
    }
}
