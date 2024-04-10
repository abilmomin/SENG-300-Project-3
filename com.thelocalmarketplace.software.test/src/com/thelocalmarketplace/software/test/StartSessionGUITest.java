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

import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.StartSession;

import powerutility.PowerGrid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class StartSessionGUITest {
    private SelfCheckoutStationSoftware station;
	private SelfCheckoutStationGold checkoutStation;
    private StartSession session;

    @Before
    public void setUp() {
        // Set up SelfCheckoutStation
		this.checkoutStation = new SelfCheckoutStationGold();
        PowerGrid.engageUninterruptiblePowerSource();
		this.checkoutStation.plugIn(PowerGrid.instance());
		this.checkoutStation.turnOn();

		this.station = new SelfCheckoutStationSoftware(checkoutStation);
        station.setStationActive(true);
        session = new StartSession(1, station, (AbstractElectronicScale) station.getStationHardware().getBaggingArea());
    }

    @Test
    public void testConstructor() {
        assertNotNull(session.getContentPane());
        assertEquals("Welcome to the Market - Station 1", session.getTitle());
        assertEquals(900, session.getWidth());
        assertEquals(700, session.getHeight());
        assertEquals(JFrame.EXIT_ON_CLOSE, session.getDefaultCloseOperation());
        assertEquals(2, session.getContentPane().getComponentCount());
    }

    @Test
    public void testStartSessionButtonClick() {
        JPanel startSessionPanel = findPanelByText("Content Panel");
        simulateMouseClick(startSessionPanel);
    }

    private JPanel findPanelByText(String text) {
        for (int i = 0; i < session.getContentPane().getComponentCount(); i++) {
            if (session.getContentPane().getComponent(i) instanceof JPanel) {
                JPanel panel = (JPanel) session.getContentPane().getComponent(i);
                if (panel.getName().equals(text)) {
                    return panel;
                }
            }
        }
        return null;
    }

    // Method to simulate mouse click
    private void simulateMouseClick(Component component) {
        Point center = new Point(component.getWidth() / 2, component.getHeight() / 2);
        SwingUtilities.convertPointToScreen(center, component);
        MouseEvent event = new MouseEvent(component, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, center.x, center.y, 1, false);
        for (MouseListener listener : component.getMouseListeners()) {
            listener.mouseClicked(event);
        }
    }
}