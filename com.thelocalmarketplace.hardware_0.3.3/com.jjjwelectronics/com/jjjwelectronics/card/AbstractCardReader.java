package com.jjjwelectronics.card;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import com.jjjwelectronics.AbstractDevice;
import com.jjjwelectronics.card.Card.CardData;

import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import powerutility.NoPowerException;

/**
 * Abstract base class for card readers.
 * 
 * @author JJJW Electronics LLP
 */
public abstract class AbstractCardReader extends AbstractDevice<CardReaderListener> implements ICardReader {
	protected boolean cardIsInserted = false;
	protected static final ThreadLocalRandom random = ThreadLocalRandom.current();
	protected double probabilityOfTapFailure = 0.05;
	protected double probabilityOfInsertFailure = 0.05;
	protected double probabilityOfSwipeFailure = 0.5;

	@Override
	public synchronized CardData tap(Card card) throws IOException {
		if(!isPoweredUp())
			throw new NoPowerException();
	
		if(card.isTapEnabled) {
			notifyCardTapped();
	
			if(random.nextDouble(0.0, 1.0) > probabilityOfTapFailure) {
				CardData data = card.tap();
	
				notifyCardDataRead(data);
	
				return data;
			}
			else
				throw new ChipFailureException();
		}
	
		// else ignore
	
		return null;
	}

	@Override
	public synchronized CardData swipe(Card card) throws IOException {
		if(!isPoweredUp())
			throw new NoPowerException();
	
		notifyCardSwiped();
	
		if(random.nextDouble(0.0, 1.0) > probabilityOfSwipeFailure) {
			CardData data = card.swipe();
	
			notifyCardDataRead(data);
	
			return data;
		}
	
		throw new MagneticStripeFailureException();
	}

	@Override
	public synchronized CardData insert(Card card, String pin) throws IOException {
		if(!isPoweredUp())
			throw new NoPowerException();
	
		if(cardIsInserted)
			throw new IllegalStateException("There is already a card in the slot");
	
		cardIsInserted = true;
	
		notifyCardInserted();
	
		if(card.hasChip && random.nextDouble(0.0, 1.0) > probabilityOfInsertFailure) {
			CardData data = card.insert(pin);
	
			notifyCardDataRead(data);
	
			return data;
		}
	
		throw new ChipFailureException();
	}

	@Override
	public void remove() {
		if(!isPoweredUp())
			throw new NoPowerException();
	
		if(!cardIsInserted)
			throw new NullPointerSimulationException();
	
		cardIsInserted = false;
		notifyCardRemoved();
	}

	protected void notifyCardTapped() {
		for(CardReaderListener l : listeners())
			l.aCardHasBeenTapped();
	}

	protected void notifyCardInserted() {
		for(CardReaderListener l : listeners())
			l.aCardHasBeenInserted();
	}

	protected void notifyCardSwiped() {
		for(CardReaderListener l : listeners())
			l.aCardHasBeenSwiped();
	}

	protected void notifyCardDataRead(CardData data) {
		for(CardReaderListener l : listeners())
			l.theDataFromACardHasBeenRead(data);
	}

	protected void notifyCardRemoved() {
		for(CardReaderListener l : listeners())
			l.theCardHasBeenRemoved();
	}
}