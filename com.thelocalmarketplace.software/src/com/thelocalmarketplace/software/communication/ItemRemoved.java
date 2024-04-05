package com.thelocalmarketplace.software.communication;

import javax.swing.*;
import java.awt.*;

public class ItemRemoved extends JFrame {

    public ItemRemoved() {
        setTitle("Item Removed");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        JLabel headerLabel = new JLabel("Item Removed from Cart");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel itemLabel = new JLabel("Sugar Brown 1kg");
        itemLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        itemLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel instructionLabel = new JLabel("Remove this item from the bagging area to continue checkout.");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton removeButton = new JButton("Remove from Bagging Area");
        removeButton.setFont(new Font("Arial", Font.BOLD, 16));
        removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeButton.setBackground(new Color(70, 130, 180));
        removeButton.setForeground(Color.WHITE);
        removeButton.setOpaque(true);
        removeButton.setBorderPainted(false);

       
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

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(ItemRemoved::new);
    }
}
