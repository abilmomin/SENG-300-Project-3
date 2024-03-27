package com.jjjwelectronics.keyboard;

import java.util.Map;

import com.jjjwelectronics.IDevice;

/**
 * Abstract base type for keyboards.
 * 
 * @author JJJW Electronics LLP
 */
public interface IKeyboard extends IDevice<KeyboardListener> {
	/**
	 * Obtains the key with the indicated label on this keyboard. Does not require
	 * power.
	 * 
	 * @param label
	 *            The label of the key of interest.
	 * @return The key corresponding to the indicated label, or null if nonesuch
	 *             exists.
	 */
	Key getKey(String label);

	/**
	 * Obtains all the keys on the keyboard, indexed by their labels. Does not
	 * require power.
	 * 
	 * @return A map from key labels to key objects.
	 */
	Map<String, Key> keys();

	/**
	 * Disables the keyboard as well as its individual keys. Attempts to enable
	 * individual keys on a disabled keyboard (or vice versa) are blocked to
	 * maintain the invariant that the enabled/disabled state of the keyboard will
	 * always be the same as those of its keys. Requires power.
	 */
	void disable();

	/**
	 * Enables the keyboard as well as its individual keys. Attempts to disable
	 * individual keys on an enabled keyboard (or vice versa) are blocked to
	 * maintain the invariant that the enabled/disabled state of the keyboard will
	 * always be the same as those of its keys. Requires power.
	 */
	void enable();
}