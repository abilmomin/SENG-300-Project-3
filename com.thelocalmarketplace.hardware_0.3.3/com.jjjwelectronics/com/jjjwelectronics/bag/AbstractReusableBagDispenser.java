package com.jjjwelectronics.bag;

import java.util.ArrayList;
import java.util.Arrays;

import com.jjjwelectronics.AbstractDevice;
import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.OverloadedDevice;

import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import powerutility.NoPowerException;

/**
 * Abstract base class for automatic, reusable bag dispensers.
 * 
 * @author JJJW Electronics LLP
 */
public abstract class AbstractReusableBagDispenser extends AbstractDevice<ReusableBagDispenserListener>
	implements IReusableBagDispenser {
	protected ArrayList<ReusableBag> bags = new ArrayList<ReusableBag>();
	protected int capacity;

	@Override
	public int getCapacity() {
		return capacity;
	}

	@Override
	public void load(ReusableBag... bags) throws OverloadedDevice {
		if(!isPoweredUp())
			throw new NoPowerException();

		if(bags == null)
			throw new NullPointerSimulationException();

		if(this.bags.size() + bags.length > capacity)
			throw new OverloadedDevice("You have tried to stuff the dispenser with too many bags");

		this.bags.addAll(Arrays.asList(bags));

		notifyBagsLoaded(bags.length);
	}

	@Override
	public ReusableBag[] unload() {
		if(!isPoweredUp())
			throw new NoPowerException();

		ReusableBag[] array = bags.toArray(new ReusableBag[bags.size()]);
		bags.clear();
		notifyOutOfBags();

		return array;
	}

	@Override
	public ReusableBag dispense() throws EmptyDevice {
		if(!isPoweredUp())
			throw new NoPowerException();

		if(bags.isEmpty())
			throw new EmptyDevice("Out of bags");

		ReusableBag bag = bags.remove(bags.size() - 1);
		notifyBagDispensed();

		if(bags.isEmpty())
			notifyOutOfBags();

		return bag;
	}

	protected void notifyBagsLoaded(int count) {
		for(ReusableBagDispenserListener listener : listeners())
			listener.bagsHaveBeenLoadedIntoTheDispenser(count);
	}

	protected void notifyBagDispensed() {
		for(ReusableBagDispenserListener listener : listeners())
			listener.aBagHasBeenDispensedByTheDispenser();
	}

	protected void notifyOutOfBags() {
		for(ReusableBagDispenserListener listener : listeners())
			listener.theDispenserIsOutOfBags();
	}

}