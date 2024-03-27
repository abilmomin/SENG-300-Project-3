package com.jjjwelectronics.screen;

import javax.swing.JFrame;

import com.jjjwelectronics.IDevice;

/**
 * Abstract base type for touch screens.
 * 
 * @author JJJW Electronics LLP
 *
 */
public interface ITouchScreen extends IDevice<TouchScreenListener> {
	/**
	 * Gets the {@link JFrame} object that should be in fullscreen mode for the
	 * touch screen. The frame will be invisible until it is explicitly made
	 * visible, by calling {@link #setVisible(boolean)} with a {@code true}
	 * argument.
	 * 
	 * @return The frame on which the graphical user interface can be deployed.
	 */
	JFrame getFrame();

	/**
	 * Allows the visibility of the {@link JFrame} to be changed. Requires power.
	 * 
	 * @param visibility
	 *            True if the frame should be made visible; otherwise, false.
	 */
	void setVisible(boolean visibility);
}