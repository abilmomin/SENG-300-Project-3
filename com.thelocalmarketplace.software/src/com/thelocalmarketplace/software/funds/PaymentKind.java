package com.thelocalmarketplace.software.funds;

public class PaymentKind{
	/**
	 * Enumeration of possible payment methods
	 */
	public static enum Kind {
		NONE, // Default
		CREDIT,
		DEBIT,
		CASH
	}
}