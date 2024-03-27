package com.jjjwelectronics.screen;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import powerutility.NoPowerException;

/**
 * A device for display and customer input. It displays a {@link JFrame} object
 * to which the graphical user interface may be deployed. Touch screens announce
 * no events. To listen to events there, one has to listen to events from
 * individual graphical objects.
 * 
 * @author JJJW Electronics LLP
 */
public final class TouchScreenBronze extends AbstractTouchScreen {
	private JFrame frame;
	private volatile boolean ready = false;

	/**
	 * Creates a touch screen. The frame herein will initially be invisible.
	 */
	public TouchScreenBronze() {
		super();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame = createGUI();
				ready = true;
			}
		});
		while(!ready)
			;
	}

	private JFrame createGUI() {
		JFrame frame = new JFrame();

		frame.setUndecorated(true);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				super.windowOpened(e);
				ready = true;
				frame.setVisible(false);
			}
		});

		return frame;
	}

	@Override
	public JFrame getFrame() {
		return frame;
	}

	@Override
	public void setVisible(boolean visibility) {
		if(visibility && !isPoweredUp())
			throw new NoPowerException();

		frame.setVisible(visibility);
	}
}
