package com.jjjwelectronics.keyboard;

import com.jjjwelectronics.IDeviceListener;

/**
 * Listens for events emanating from a key on a keyboard.
 * 
 * @author JJJW Electronics LLP
 */
public interface KeyListener extends IDeviceListener {
	/**
	 * Announces that the indicated key has been pressed.
	 * 
	 * @param k
	 *            The key where the event occurred.
	 */
	public void aKeyHasBeenPressed(Key k);

	/**
	 * Announces that the indicated key has been released.
	 * 
	 * @param k
	 *            The key where the event occurred.
	 */
	public void aKeyHasBeenReleased(Key k);
}
