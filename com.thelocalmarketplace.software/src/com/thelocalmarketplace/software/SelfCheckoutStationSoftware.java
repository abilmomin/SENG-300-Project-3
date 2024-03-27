/**
 * Yotam Rojnov (UCID: 30173949)
 * Duncan McKay (UCID: 30177857)
 * Mahfuz Alam (UCID:30142265)
 * Luis Trigueros Granillo (UCID: 30167989)
 * Lilia Skumatova (UCID: 30187339)
 * Abdelrahman Abbas (UCID: 30110374)
 * Talaal Irtija (UCID: 30169780)
 * Alejandro Cardona (UCID: 30178941)
 * Alexandre Duteau (UCID: 30192082)
 * Grace Johnson (UCID: 30149693)
 * Abil Momin (UCID: 30154771)
 * Tara Ghasemi M. Rad (UCID: 30171212)
 * Izabella Mawani (UCID: 30179738)
 * Binish Khalid (UCID: 30061367)
 * Fatima Khalid (UCID: 30140757)
 * Lucas Kasdorf (UCID: 30173922)
 * Emily Garcia-Volk (UCID: 30140791)
 * Yuinikoru Futamata (UCID: 30173228)
 * Joseph Tandyo (UCID: 30182561)
 * Syed Haider (UCID: 30143096)
 * Nami Marwah (UCID: 30178528)
 */

package com.thelocalmarketplace.software;

import ca.ucalgary.seng300.simulation.InvalidStateSimulationException;
import static com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation.resetConfigurationToDefaults;
import java.util.Scanner;

/**
 * This class is for the software required for the self-checkout station session
 */
public class SelfCheckoutStationSoftware {

	/**
	 * Boolean variable that is used to track whether user interaction is blocked
	 */
	private static boolean blocked = false;

	/**
	 * Boolean variable to track if a current session is active or not. 
	 */
	private static boolean active = false;

	/**
	 * Set function to change the blocked variable value.
	 * @param value The new value for station block status
	 */
	public static void setStationBlock(boolean value) {
		blocked = value;
	}

	/**
	 * Get function to get the blocked station status.
	 */
	public static boolean getStationBlock() {
		return blocked;
	}
	
	/**
	 * Set function to change the active variable value.
	 */
	public static void setStationActive(boolean value) {
		active = value;
	}

	/**
	 * Get function to get the blocked station status.
	 */
	public static boolean getStationActive() {
		return active;
	}
	
	/**
	 * Function to start a session for self-checkout machine
	 * @param scanner The scanner used to obtain user input.
	 * @throws InvalidStateSimulationException If a session is already active.
	 */
	public void startSession(Scanner scanner) {
		if (active) {
			throw new InvalidStateSimulationException("Session already started.");
		}
		
		resetConfigurationToDefaults(); // Reset all self-checkout station configurations to default.
		

		// Prompt the user to touch anywhere to start and wait for an input.
		System.out.println("Welcome to The Local Marketplace. Touch anywhere to start.");
		
		// assume the user gives some kind of input.
		scanner.nextLine();

		setStationActive(true); // Set the current session to active.

	}
}