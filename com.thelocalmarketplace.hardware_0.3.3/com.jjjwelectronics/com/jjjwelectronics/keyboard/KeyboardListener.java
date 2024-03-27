package com.jjjwelectronics.keyboard;

import com.jjjwelectronics.IDeviceListener;

/**
 * Listens for events emanating from the keyboard. Individual keys cause their
 * own events; the keyboard listens for these and translates them into its own
 * events.
 * 
 * @author JJJW Electronics LLP
 */
public interface KeyboardListener extends IDeviceListener {
	/**
	 * Announces that a key has been pressed (and potentially held).
	 * 
	 * @param label
	 *            The label of the key generating the event.
	 */
	public void aKeyHasBeenPressed(String label);

	/**
	 * Announces that a key has been released.
	 * 
	 * @param label
	 *            The label of the key generating the event.
	 */
	public void aKeyHasBeenReleased(String label);
}
