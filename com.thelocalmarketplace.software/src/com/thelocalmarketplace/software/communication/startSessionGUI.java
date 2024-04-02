package com.thelocalmarketplace.software.communication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class startSessionGUI extends JFrame {

    public startSessionGUI() {
        // Frame initialization
        setTitle("Welcome to the Market");
        setSize(300, 200); // Set the size of the window
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel to hold components
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome to the Market!");
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Start session button
        JButton startSessionButton = new JButton("Start Session");
        startSessionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startSessionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // need to be changess
                JOptionPane.showMessageDialog(startSessionGUI.this, "Change this");
            }
        });

        // Adding components to the panel
        panel.add(Box.createRigidArea(new Dimension(0, 50))); // Spacer
        panel.add(welcomeLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        panel.add(startSessionButton);

        // Adding the panel to the frame
        add(panel);

        // Make the GUI visible
        setVisible(true);
    }

    public static void main(String[] args) {
        // Ensure the GUI is created on the Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new startSessionGUI();
            }
        });
    }
}
