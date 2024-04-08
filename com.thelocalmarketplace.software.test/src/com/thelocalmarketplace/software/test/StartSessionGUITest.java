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

package com.thelocalmarketplace.software.test;

import org.junit.Before;
import org.junit.Test;

import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.StartSession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.swing.JButton;
import javax.swing.JFrame;

public class StartSessionGUITest {
	StartSession session;

    @Before
    public void setUp() {
        session = new StartSession(1, null, null);
    }

    @Test
    public void testConstructor() {
        assertNotNull(session.getContentPane());
        assertEquals("Welcome to the Market", session.getTitle());
        assertEquals(900, session.getWidth());
        assertEquals(700, session.getHeight());
        assertEquals(JFrame.EXIT_ON_CLOSE, session.getDefaultCloseOperation());
        assertEquals(1, session.getContentPane().getComponentCount());
    }

    @Test
    public void testStartSessionButtonClick() {
        JButton startSessionButton = findButtonByText("Start Session");
        startSessionButton.doClick();
    }
    
    private JButton findButtonByText(String text) {
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