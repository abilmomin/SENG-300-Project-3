/**

Name                      UCID

Yotam Rojnov             30173949
Duncan McKay             30177857
Mahfuz Alam              30142265
Luis Trigueros Granillo  30167989
Lilia Skumatova          30187339
Abdelrahman Abbas        30110374
Talaal Irtija            30169780
Alejandro Cardona        30178941
Alexandre Duteau         30192082
Grace Johnson            30149693
Abil Momin               30154771
Tara Ghasemi M. Rad      30171212
Izabella Mawani          30179738
Binish Khalid            30061367
Fatima Khalid            30140757
Lucas Kasdorf            30173922
Emily Garcia-Volk        30140791
Yuinikoru Futamata       30173228
Joseph Tandyo            30182561
Syed Haider              30143096
Nami Marwah              30178528

 */


package com.thelocalmarketplace.software.funds;

import java.math.BigDecimal;

/**
 * Permits objects to listen to one or more Funds when registered with them.
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

	/**
	 * Signals an event in which the station is blocked at payment state.
	 *
	 * @param fundsFacade The facade in which the event occurred.
	 * @param blockedStatus Whether the station is blocked.
	 */
	default public void fundsStationBlocked(Funds fundsFacade, boolean blockedStatus) {}
}
