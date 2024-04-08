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

import javax.swing.*;

import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class SettingsPanel extends JPanel {
    private JComboBox<String> languageDropdown;
    private JComboBox<String> accessibilityDropdown;

    private String[] languages = {"English"};
    private String[] accessibilitySettings = {"Default"};
    
    private StatusOfStation status;

    public SettingsPanel(SelfCheckoutStationSoftware software) {
    	if(software != null) 
    		this.status = new StatusOfStation(software);
        setLayout(new FlowLayout(FlowLayout.LEFT));

        // Language settings
        JPanel languagePanel = new JPanel(new BorderLayout());
        languageDropdown = new JComboBox<>(languages);
        languagePanel.add(new JLabel("Language: "), BorderLayout.NORTH);
        languagePanel.add(languageDropdown, BorderLayout.CENTER);

        // Accessibility settings
        JPanel accessibilityPanel = new JPanel(new BorderLayout());
        accessibilityDropdown = new JComboBox<>(accessibilitySettings);
        accessibilityPanel.add(new JLabel("Accessibility: "), BorderLayout.NORTH);
        accessibilityPanel.add(accessibilityDropdown, BorderLayout.CENTER);

        add(languagePanel);
        add(accessibilityPanel);

        if(software != null) {
	        // Button to open StatusOfStation pop-up
	        JButton statusButton = new JButton("Status of Station");
	        statusButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	status.setVisible(true);
	            }
	        });
	        add(statusButton);
        }
        
        setVisible(true);
    }

    public void updateStatus() {
    	status.updateLabels();
    }
}
