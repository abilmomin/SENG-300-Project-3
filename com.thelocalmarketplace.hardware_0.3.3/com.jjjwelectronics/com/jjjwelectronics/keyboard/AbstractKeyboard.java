package com.jjjwelectronics.keyboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jjjwelectronics.AbstractDevice;
import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;

import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import powerutility.PowerGrid;

/**
 * Abstract base class for keyboards.
 * 
 * @author JJJW Electronics LLP
 *
 */
public abstract class AbstractKeyboard extends AbstractDevice<KeyboardListener> implements IKeyboard {
	protected Map<String, Key> keys;
	
	protected AbstractKeyboard(List<String> keys) {
		if(keys == null)
			throw new NullPointerSimulationException("keys");

		this.keys = new HashMap<String, Key>();

		for(String label : keys) {
			Key key = new Key();
			key.register(new SynchronizeKeyAndKeyboardState(label));
			this.keys.put(label, key);
		}
	}

	protected class SynchronizeKeyAndKeyboardState implements KeyListener {
		private String label;

		public SynchronizeKeyAndKeyboardState(String label) {
			this.label = label;
		}

		@Override
		public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
			// To avoid infinite loops, we need to directly set the state
			if(AbstractKeyboard.this.isDisabled())
				((Key)device).disableWithoutEvents();
		}

		@Override
		public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
			// To avoid infinite loops, we need to directly set the state
			if(!AbstractKeyboard.this.isDisabled())
				((Key)device).enableWithoutEvents();
		}

		@Override
		public void aKeyHasBeenPressed(Key k) {
			AbstractKeyboard.this.notifyKeyPressed(label);
		}

		@Override
		public void aKeyHasBeenReleased(Key k) {
			AbstractKeyboard.this.notifyKeyReleased(label);
		}

		@Override
		public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
			((Key)device).turnOffWithoutEvents();
		}

		@Override
		public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
			((Key)device).turnOnWithoutEvents();
		}
	}

	@Override
	public Key getKey(String label) {
		return keys.get(label);
	}

	@Override
	public Map<String, Key> keys() {
		return keys;
	}

	@Override
	public synchronized void turnOn() {
		super.turnOn();

		for(Key key : keys.values())
			key.turnOnWithoutEvents();
	}

	@Override
	public synchronized void turnOff() {
		super.turnOff();

		for(Key key : keys.values())
			key.turnOffWithoutEvents();
	}

	@Override
	public synchronized void plugIn(PowerGrid grid) {
		super.plugIn(grid);

		for(Key key : keys.values())
			key.plugInSpecial(grid);
	}

	@Override
	public synchronized void unplug() {
		super.unplug();

		for(Key key : keys.values())
			key.unplugSpecial();
	}

	@Override
	public synchronized void disable() {
		super.disable();

		for(Key key : keys.values())
			key.disable(); // disable() has to be called so that other listeners can be notified
	}

	@Override
	public synchronized void enable() {
		super.enable();

		for(Key key : keys.values())
			key.enable(); // enable() has to be called so that other listeners can be notified
	}

	protected void notifyKeyPressed(String label) {
		for(KeyboardListener listener : listeners())
			listener.aKeyHasBeenPressed(label);
	}

	protected void notifyKeyReleased(String label) {
		for(KeyboardListener listener : listeners())
			listener.aKeyHasBeenReleased(label);
	}
}