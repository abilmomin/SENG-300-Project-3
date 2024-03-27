package com.jjjwelectronics.bag;

import java.math.BigInteger;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;

/**
 * Represents a single, reusable bag for carrying groceries.
 * 
 * @author JJJW Electronics LLP
 */
public class ReusableBag extends Item {
	private static final Mass idealMass = new Mass(BigInteger.valueOf(5_000_000));

	/**
	 * Default constructor.
	 */
	public ReusableBag() {
		super(idealMass);
	}
}
