package com.thelocalmarketplace.software.test;


import org.junit.Before;
import org.junit.Test;

import com.thelocalmarketplace.software.communication.startSessionGUI;

import javax.swing.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StartSessionGUITest {


    @Before
    public void setUp() {
        startSessionGUI session = new startSessionGUI();
    }

    @Test
    public void testConstructor() {
    	startSessionGUI session = new startSessionGUI();
        assertNotNull(session.getContentPane());
        assertEquals("Welcome to the Market", session.getTitle());
        assertEquals(300, session.getWidth());
        assertEquals(200, session.getHeight());
        assertEquals(JFrame.EXIT_ON_CLOSE, session.getDefaultCloseOperation());
        assertEquals(1, session.getContentPane().getComponentCount());
    }

    @Test
    public void testStartSessionButtonClick() {
        JButton startSessionButton = findButtonByText("Start Session");
        startSessionButton.doClick();
        // You might add more assertions here if necessary
    }
    
    
    // Utility method for testing the button
    private JButton findButtonByText(String text) {
    	startSessionGUI session = new startSessionGUI();
        for (int i = 0; i < session.getContentPane().getComponentCount(); i++) {
            if (session.getContentPane().getComponent(i) instanceof JButton) {
                JButton button = (JButton) session.getContentPane().getComponent(i);
                if (button.getText().equals(text)) {
                    return button;
                }
            }
        }
        return null;
    }
}
