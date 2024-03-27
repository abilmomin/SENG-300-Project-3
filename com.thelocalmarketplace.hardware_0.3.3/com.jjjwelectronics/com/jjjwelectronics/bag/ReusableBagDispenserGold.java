package com.jjjwelectronics.bag;

import ca.ucalgary.seng300.simulation.InvalidArgumentSimulationException;

/**
 * Represents the reusable-bag dispenser.
 * <p>
 * As our premium model, Gold supports an exact sensor to count the quantity of
 * bags remaining in the device.
 * 
 * @author JJJW Electronics LLP
 */
public class ReusableBagDispenserGold extends AbstractReusableBagDispenser {
	/**
	 * Basic constructor permitting the capacity to be set.
	 * 
	 * @param capacity
	 *            The maximum number of bags that the dispenser can contain.
	 * @throws InvalidArgumentSimulationException
	 *             if capacity &le;0.
	 */
	public ReusableBagDispenserGold(int capacity) {
		super();
		if(capacity <= 0)
			throw new InvalidArgumentSimulationException("The capacity must be a positive integer.");

		this.capacity = capacity;
	}

	@Override
	public int getQuantityRemaining() {
		return bags.size();
	}
}
