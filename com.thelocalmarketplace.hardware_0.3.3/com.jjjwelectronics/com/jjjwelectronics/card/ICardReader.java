package com.jjjwelectronics.card;

import java.io.IOException;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.card.Card.CardData;

import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import ca.ucalgary.seng300.simulation.SimulationException;

/**
 * Abstract base type of card readers.
 * 
 * @author JJJW Electronics LLP
 */
public interface ICardReader extends IDevice<CardReaderListener> {
	/**
	 * Tap the card. Requires power.
	 * 
	 * @param card
	 *            The card to tap.
	 * @return The card's (possibly corrupted) data, or null if the card is not tap
	 *             enabled.
	 * @throws IOException
	 *             If the tap failed (lack of failure does not mean that the data is
	 *             not corrupted).
	 */
	CardData tap(Card card) throws IOException;

	/**
	 * Swipe the card. Requires power.
	 * 
	 * @param card
	 *            The card to swipe.
	 * @return The card data.
	 * @throws IOException
	 *             If the swipe failed.
	 */
	CardData swipe(Card card) throws IOException;

	/**
	 * Insert the card. Requires power.
	 * 
	 * @param card
	 *            The card to insert.
	 * @param pin
	 *            The customer's PIN.
	 * @return The card data.
	 * @throws SimulationException
	 *             If there is already a card in the slot.
	 * @throws IOException
	 *             The insertion failed.
	 */
	CardData insert(Card card, String pin) throws IOException;

	/**
	 * Remove the card from the slot. Requires power.
	 * 
	 * @throws NullPointerSimulationException
	 *             if no card is present.
	 */
	void remove();
}
