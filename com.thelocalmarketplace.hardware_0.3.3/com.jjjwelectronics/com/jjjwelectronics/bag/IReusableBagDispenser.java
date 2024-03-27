package com.jjjwelectronics.bag;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.OverloadedDevice;

/**
 * Abstract base type for automatic, reusable bag dispensers.
 * 
 * @author JJJW Electronics LLP
 */
public interface IReusableBagDispenser extends IDevice<ReusableBagDispenserListener> {
	/**
	 * Obtains the maximum capacity of the dispenser.
	 * 
	 * @return The maximum capacity.
	 */
	int getCapacity();

	/**
	 * Obtains the remaining quantity of bags in this device. Not all models support
	 * this, and some provide only an approximation.
	 * 
	 * @return The remaining quantity of bags in the device.
	 */
	int getQuantityRemaining();

	/**
	 * Adds the indicated bags to the dispenser. Causes a "bags loaded" event to be
	 * announced. Requires power.
	 * 
	 * @param bags
	 *            The bags to be added.
	 * @throws OverloadedDevice
	 *             if the new total number of bags would exceed the capacity of the
	 *             dispenser.
	 */
	void load(ReusableBag... bags) throws OverloadedDevice;

	/**
	 * Removes all the bags from the dispenser. Causes an "out of bags" event to be
	 * announced. Requires power.
	 * 
	 * @return The bags formerly in the dispenser.
	 */
	ReusableBag[] unload();

	/**
	 * Dispenses one bag to the customer. Causes a "bag dispensed" event to be
	 * announced. May cause an "out of bags" event to also be announced, if the
	 * dispensed bag is the last one in the dispenser. Requires power.
	 * 
	 * @return The dispensed bag.
	 * @throws EmptyDevice
	 *             if the dispenser contained no bags when this method was called.
	 */
	ReusableBag dispense() throws EmptyDevice;
}
