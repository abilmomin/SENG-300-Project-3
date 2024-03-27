package com.jjjwelectronics.bag;

import com.jjjwelectronics.IDeviceListener;

/**
 * Listens for events emanating from the reusable-bag dispenser.
 * 
 * @author JJJW Electronics LLP
 */
public interface ReusableBagDispenserListener extends IDeviceListener {
	/**
	 * Announces that a reusable bag has been dispensed.
	 */
	public void aBagHasBeenDispensedByTheDispenser();

	/**
	 * Announces that a dispenser has run out of bags to dispense.
	 */
	public void theDispenserIsOutOfBags();

	/**
	 * Announces that bags have been loaded into the dispenser.
	 * 
	 * @param count
	 *            The number of bags loaded into the dispenser.
	 */
	public void bagsHaveBeenLoadedIntoTheDispenser(int count);

}
