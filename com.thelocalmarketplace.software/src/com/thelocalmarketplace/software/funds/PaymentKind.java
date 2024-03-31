package com.thelocalmarketplace.software.funds;

public class PaymentKind{
	/**
	 * Enumeration of possible payment methods
	 */
	public static enum Kind {
		NONE, // Default
		CREDIT,
		DEBIT,
		CASH,
		CRYPTO
	}

	public static Kind getCardType(String cardType) {
		if (cardType.equalsIgnoreCase("debit")) {
			return Kind.DEBIT;
		} else if (cardType.equalsIgnoreCase("credit")) {
			return Kind.CREDIT;
		} else {
			return Kind.NONE;
		}
	}
}