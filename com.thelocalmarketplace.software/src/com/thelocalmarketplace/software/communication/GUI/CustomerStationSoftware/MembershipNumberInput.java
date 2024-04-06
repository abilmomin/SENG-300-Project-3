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

import com.thelocalmarketplace.software.MembershipCard;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.CustomerStation;

import java.awt.*;

public class MembershipNumberInput extends JDialog {
	private static final long serialVersionUID = 1997230330248440038L;
	private JTextField membershipNumberField;
	private JPanel membershipPanel;
	private CustomerStation parent;

	
	
	public MembershipNumberInput(Frame parent) {
		super(parent, "Membership Number", true);
		this.parent = (CustomerStation) parent;
		setLocationRelativeTo(parent);
		setVisible(true);
	}
	
	public void frameInit() {
		membershipPanel = new JPanel(new BorderLayout(5,5));
		membershipNumberField = new JTextField(20);
		membershipPanel.add(membershipNumberField, BorderLayout.NORTH);
		
		JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Membership Number:"));
        inputPanel.add(membershipNumberField);
        membershipPanel.add(inputPanel, BorderLayout.NORTH);
        
		addVirtualNumberKeyboard();
		addOptionsPanel();
		
		setLayout(new BorderLayout());
		add(membershipPanel, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getParent());
		
		
	
	}

	private void addOptionsPanel() {
		Object[] options = {"Enter", "Scan Card", "swipe Card", "Cancel"};

        int choice = JOptionPane.showOptionDialog(
            this,
            membershipPanel,
            "Membership Number",
            JOptionPane.NO_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]
        );
        switch (choice) {
        case 0:
        	String membershipNumber = membershipNumberField.getText();
            checkMembershipNumber(membershipNumber);
            dispose(); // Close the dialog
            break;
        case 1:
        	scanCard();
        	dispose(); // Close the dialog
            break;
        case 2:
        	swipeCard();
        	dispose(); // Close the dialog
            break;
        default:
            membershipNumber = null; // Reset the number
            dispose(); // Close the dialog
            break;
        }
    }
    
    private void swipeCard() {
		
		
	}

	private void scanCard() {
		
		
	}
	public void checkMembershipNumber(String membershipNumber) {
		MembershipCard membershipCard = new MembershipCard();
        if (membershipNumber != null && !membershipNumber.isEmpty()) {
            if (membershipCard.isMembershipNumberValid(membershipNumber)) {
                JOptionPane.showMessageDialog(this, "Membership number accepted.");
            } else {
                showOptions();
            }
        } else {
            JOptionPane.showMessageDialog(this, "No membership number entered.");
        }
    }

	private void showOptions() {
		String options[] = new String[] {"Try again", "Become a member", "continue"};
    	int response = JOptionPane.showOptionDialog(
                this,
                "Invalid membership number.",
                "Membership Error",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );
    	
    	switch (response) {
    		case 0:
    			membershipNumberField.setText("");
    			parent.askForMembershipNumber();
                break;
    		case 1:
    			createAccount();
    			dispose();
    			break;
    		case 2: 
    			JOptionPane.showMessageDialog(this, "Continuing without a membership account.");
			dispose();
    			break;
    	}
    }
	

	private void createAccount() {
        String memberName = JOptionPane.showInputDialog(this, "Please enter your name:");
        if (memberName != null && !memberName.trim().isEmpty()) {
            MembershipCard newCard = new MembershipCard();
            newCard.createNewMember(memberName);
            JOptionPane.showMessageDialog(this, "Account created successfully!\nName: " + memberName + "\nMembership Number: " + newCard.getMembershipNumber());
        } else {
            JOptionPane.showMessageDialog(this, "You must enter a name to create a membership account.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

	private void addVirtualNumberKeyboard() {
		JPanel keyboardPanel = new JPanel();
    	keyboardPanel.setLayout(new GridLayout(4,3,5,5));
    	for (int i = 1; i<=9; i++) {
    		JButton numberButtons = new JButton(String.valueOf(i));
    		numberButtons.addActionListener(e->membershipNumberField.setText(membershipNumberField.getText() + e.getActionCommand()));
    		keyboardPanel.add(numberButtons);
    	}
    	JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> membershipNumberField.setText(""));
        keyboardPanel.add(clearButton);
        
        JButton zeroButton = new JButton("0");
        zeroButton.addActionListener(e -> membershipNumberField.setText(membershipNumberField.getText() + e.getActionCommand()));
        keyboardPanel.add(zeroButton);
        
        JButton backButton = new JButton("back"); // Placeholder to fill the grid
        backButton.addActionListener(e -> {
        	String currentNumber = membershipNumberField.getText();
        	if (!currentNumber.isEmpty()) {
        		String newNumber = currentNumber.substring(0, currentNumber.length()-1);
        		membershipNumberField.setText(newNumber);
        	}
        });
        keyboardPanel.add(backButton);
        
        membershipPanel.add(keyboardPanel, BorderLayout.SOUTH);
	}
	
	public JTextField getMembershipNumberField() {
		return membershipNumberField;
	}
	

	
}