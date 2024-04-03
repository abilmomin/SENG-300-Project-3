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

package com.thelocalmarketplace.software;

import javax.swing.*; 
import java.awt.*;
import java.awt.event.*;

public class AttendantStationSoftware extends JFrame{
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
	
	private JTextField idField;
    private JPasswordField passwordField;

    public AttendantStationSoftware() { 
    	setTitle("Attendant Station Login");
	    setSize(900, 700);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    setLocationRelativeTo(null);
	    
	    //Main Panel
	    JPanel mainPanel = new JPanel(new BorderLayout());
	
	    JPanel loginPanel = new JPanel(new GridLayout(7, 1));
	    JLabel idLabel = new JLabel("Attendant ID Number: ");
	    JLabel passwordLabel = new JLabel("Password: ");
	    JLabel spacerLabel = new JLabel(" ");
	    idField = new JTextField();
	    passwordField = new JPasswordField();
	    JButton loginButton = new JButton("Login");
	
	    loginButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            String id = idField.getText();
	            String password = new String(passwordField.getPassword());
	            // Check if id and password are correct
	            if (isValidLogin(id, password)) {
	                openAttendantPage();
	            } else {
	                JOptionPane.showMessageDialog(AttendantStationSoftware.this,
	                        "Invalid ID or password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
	                idField.setText("");
	                passwordField.setText("");
	            }
	        }
	    });

	    loginPanel.add(idLabel);
	    loginPanel.add(idField);
	    loginPanel.add(passwordLabel);
	    loginPanel.add(passwordField);
	    loginPanel.add(spacerLabel);
	    loginPanel.add(loginButton);
	    
	    
	    JPanel centerPanel = new JPanel(new GridBagLayout());
	    centerPanel.add(loginPanel);
	
	    mainPanel.add(centerPanel, BorderLayout.CENTER);
	    add(mainPanel);
	    
	    setVisible(true);
	}
	
	private boolean isValidLogin(String id, String password) {
	    return id.equals("1234") && password.equals("1234");
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
        SwingUtilities.invokeLater(AttendantStationSoftware::new); 
    }
}
