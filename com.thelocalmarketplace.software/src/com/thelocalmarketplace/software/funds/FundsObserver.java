package com.thelocalmarketplace.software.funds;

import java.math.BigDecimal;

/**
 * Permits objects to listen to one or more {@link Funds}s when registered
 * with them.
 * 
 * @author Robert J. Walker
 */
public interface FundsObserver {
	/**
	 * Signals an event in which funds have been added to the facade.
	 * 
	 * @param fundsFacade
	 *            The facade in which the event occurred.
	 * @param funds
	 *            The quantity of funds involved.
	 */
	default public void fundsAdded(Funds fundsFacade, BigDecimal funds) {}

	/**
	 * Signals an event in which funds have been removed from the facade, no longer
	 * available for use.
	 * 
	 * @param fundsFacade
	 *            The facade in which the event occurred.
	 * @param funds
	 *            The quantity of funds involved.
	 */
	default public void fundsRemoved(Funds fundsFacade, BigDecimal funds) {}

	/**
	 * Signals an event in which funds have been removed from the facade, but stored
	 * internally, no longer available for use.
	 * 
	 * @param fundsFacade
	 *            The facade in which the event occurred.
	 * @param funds
	 *            The quantity of funds involved.
	 */
	default public void fundsStored(Funds fundsFacade, BigDecimal funds) {}
	
    /**
     * Signals an event in which funds are invalid for a payment kind.
     * 
     * @param fundsFacade The facade in which the event occurred.
     * @param kind The kind of payment for which the funds are invalid.
     */
    default public void fundsInvalid(Funds fundsFacade, PaymentKind.Kind kind) {}
    
    /**
     * Signals an event in which funds have been paid in full, with change returned.
     * 
     * @param fundsFacade The facade in which the event occurred.
     * @param changeReturned The amount of change returned after paying in full.
     */
    default public void fundsPaidInFull(Funds fundsFacade, BigDecimal changeReturned) {}
}
