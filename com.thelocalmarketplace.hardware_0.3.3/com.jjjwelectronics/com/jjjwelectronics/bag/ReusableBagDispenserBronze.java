package com.jjjwelectronics.bag;

import java.util.Random;

import ca.ucalgary.seng300.simulation.InvalidArgumentSimulationException;

/**
 * Represents the reusable-bag dispenser.
 * <p>
 * As our most economical model, Bronze does not support counting the quantity
 * of bags remaining in the device.
 * 
 * @author JJJW Electronics LLP
 */
public class ReusableBagDispenserBronze extends AbstractReusableBagDispenser {
	private static final Random random = new Random();

	/**
	 * Basic constructor permitting the capacity to be set.
	 * 
	 * @param capacity
	 *            The maximum number of bags that the dispenser can contain.
	 * @throws InvalidArgumentSimulationException
	 *             if capacity &le;0.
	 */
	public ReusableBagDispenserBronze(int capacity) {
		super();
		if(capacity <= 0)
			throw new InvalidArgumentSimulationException("The capacity must be a positive integer.");

		this.capacity = capacity;
	}

	@Override
	public int getQuantityRemaining() {
		throw new UnsupportedOperationException();
	}
}
