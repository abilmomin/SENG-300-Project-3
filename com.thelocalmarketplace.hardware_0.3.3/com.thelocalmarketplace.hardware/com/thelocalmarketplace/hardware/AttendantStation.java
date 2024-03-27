package com.thelocalmarketplace.hardware;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jjjwelectronics.keyboard.USKeyboardQWERTY;
import com.jjjwelectronics.screen.TouchScreenBronze;

import powerutility.PowerGrid;

/**
 * Simulates the station used by the attendant.
 * <p>
 * A supervisor station possesses:
 * <ul>
 * <li>one touch screen; and,</li>
 * <li>one QWERTY keyboard.</li>
 * </ul>
 * <p>
 * All other functionality of the supervisor station must be performed in
 * software. A given self-checkout station can be supervised by at most one
 * supervision station.
 */
public class AttendantStation {
	/**
	 * Represents a touch screen display on which there is a graphical user
	 * interface.
	 */
	public final TouchScreenBronze screen;
	/**
	 * Represents a physical keyboard.
	 */
	public final USKeyboardQWERTY keyboard;

	private final ArrayList<ISelfCheckoutStation> supervisedStations;

	/**
	 * Creates a supervisor station.
	 */
	public AttendantStation() {
		screen = new TouchScreenBronze();
		supervisedStations = new ArrayList<ISelfCheckoutStation>();
		keyboard = new USKeyboardQWERTY();
	}

	/**
	 * Accesses the list of supervised self-checkout stations.
	 * 
	 * @return An immutable list of the self-checkout stations supervised by this
	 *             supervisor station.
	 */
	public synchronized List<ISelfCheckoutStation> supervisedStations() {
		return Collections.unmodifiableList(supervisedStations);
	}

	/**
	 * Obtains the number of self-checkout stations supervised by this supervision
	 * station.
	 * 
	 * @return The count, which will always be non-negative.
	 */
	public synchronized int supervisedStationCount() {
		return supervisedStations.size();
	}

	/**
	 * Adds a self-checkout station to the ones supervised by this supervision
	 * station.
	 * 
	 * @param station
	 *            The self-checkout station to be added to the supervision of this
	 *            supervision station.
	 * @throws IllegalArgumentException
	 *             If station is null.
	 * @throws IllegalStateException
	 *             If station is already supervised.
	 */
	public synchronized void add(ISelfCheckoutStation station) {
		if(station == null)
			throw new IllegalArgumentException("station cannot be null");
		if(station.isSupervised())
			throw new IllegalStateException("station is already supervised but cannot be");

		station.setSupervisor(this);
		supervisedStations.add(station);
	}

	/**
	 * Removes the indicated station from the ones supervised by this supervision
	 * station.
	 * 
	 * @param station
	 *            The station to be removed from supervision.
	 * @return true, if the indicated station was successfully removed from
	 *             supervision; otherwise, false.
	 */
	public synchronized boolean remove(SelfCheckoutStationBronze station) {
		boolean result = supervisedStations.remove(station);

		if(result) {
			station.setSupervisor(null);
		}

		return result;
	}

	/**
	 * Plugs in all the devices in the station.
	 * 
	 * @param grid
	 *            The power grid to plug into. Cannot be null.
	 */
	public void plugIn(PowerGrid grid) {
		keyboard.plugIn(grid);
		screen.plugIn(grid);
	}

	/**
	 * Unplugs all the devices in the station.
	 */
	public void unplug() {
		keyboard.unplug();
		screen.unplug();
	}

	/**
	 * Turns on all the devices in the station.
	 */
	public void turnOn() {
		keyboard.turnOn();
		screen.turnOn();
	}

	/**
	 * Turns off all the devices in the station.
	 */
	public void turnOff() {
		keyboard.turnOff();
		screen.turnOff();
	}
}
