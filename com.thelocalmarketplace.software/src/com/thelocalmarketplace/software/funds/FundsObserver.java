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
import com.tdc.banknote.BanknoteStorageUnit;
import com.tdc.banknote.IBanknoteDispenser;
import com.tdc.coin.CoinStorageUnit;
import com.tdc.coin.ICoinDispenser;


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
	public void fundsAdded(Funds fundsFacade, BigDecimal funds);

	/**
	 * Signals an event in which funds have been removed from the facade, no longer
	 * available for use.
	 * 
	 * @param fundsFacade
	 *            The facade in which the event occurred.
	 * @param funds
	 *            The quantity of funds involved.
	 */
	public void fundsRemoved(Funds fundsFacade, BigDecimal funds);

	/**
	 * Signals an event in which funds have been removed from the facade, but stored
	 * internally, no longer available for use.
	 * 
	 * @param fundsFacade
	 *            The facade in which the event occurred.
	 * @param funds
	 *            The quantity of funds involved.
	 */
	public void fundsStored(Funds fundsFacade, BigDecimal funds);
	
    /**
     * Signals an event in which funds are invalid for a payment kind.
     * 
     * @param fundsFacade 
     * 				The facade in which the event occurred.
     * @param kind 
     * 				The kind of payment for which the funds are invalid.
     */
    public void fundsInvalid(Funds fundsFacade, PaymentKind.Kind kind);
    
    /**
     * Signals an event in which funds have been paid in full, with change returned.
     * 
     * @param fundsFacade 
     * 				The facade in which the event occurred.
     * @param changeReturned 
     * 				The amount of change returned after paying in full.
     */
    public void fundsPaidInFull(Funds fundsFacade, BigDecimal changeReturned);

	/**
	 * Signals an event in which the station is blocked at payment state.
	 *
	 * @param fundsFacade 
	 * 				The facade in which the event occurred.
	 */
	public void fundsStationBlocked(Funds fundsFacade);
	
	/**
	 * Signals an event when there's a low coins error in the coin dispenser.
	 *
	 * @param dispenser 
	 * 				The coin dispenser where the error occurred.
	 */	
	default public void lowCoinsError(ICoinDispenser dispenser) {}
	
	/**
	 * Signals an event when there's a low banknotes error in the banknote dispenser.
	 *
	 * @param dispenser 
	 * 				The banknote dispenser where the error occurred.
	 */
	default public void lowBanknotesError(IBanknoteDispenser dispenser) {}
	
	/**
	 * Signals an event when there's a high coins error in the coin storage unit.
	 *
	 * @param storage 
	 * 				The coin storage unit where the error occurred.
	 */
	default public void highCoinsError(CoinStorageUnit storage) {}
	
	/**
	 * Signals an event when there's a high banknotes error in the banknote storage unit.
	 *
	 * @param storage 
	 * 				The banknote storage unit where the error occurred.
	 */
	default public void highBanknotesError(BanknoteStorageUnit storage) {}
	
	/**
	 * Signals an event when no valid change can be provided.
	 *
	 * @param fundsFacade 
	 * 				The facade where the event occurred.
	 * @param changeDue    
	 * 				The amount of change due that couldn't be provided.
	 */
	default public void noValidChange(Funds fundsFacade, BigDecimal changeDue) {}
}
