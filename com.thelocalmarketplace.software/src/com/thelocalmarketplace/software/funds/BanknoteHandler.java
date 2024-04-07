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
import java.math.RoundingMode;
import java.util.Currency;

import com.jjjwelectronics.EmptyDevice;

import com.jjjwelectronics.OverloadedDevice;

import com.tdc.CashOverloadException;

import com.tdc.DisabledException;

import com.tdc.IComponent;

import com.tdc.IComponentObserver;

import com.tdc.NoCashAvailableException;

import com.tdc.banknote.Banknote;

import com.tdc.banknote.BanknoteDispenserObserver;
import com.tdc.banknote.BanknoteStorageUnit;
import com.tdc.banknote.BanknoteStorageUnitObserver;
import com.tdc.banknote.BanknoteValidator;

import com.tdc.banknote.BanknoteValidatorObserver;

import com.tdc.banknote.IBanknoteDispenser;

/**
 * This class acts as the facade and controller for all payments handled with banknotes.
 */
public class BanknoteHandler implements BanknoteValidatorObserver, BanknoteDispenserObserver, BanknoteStorageUnitObserver {
	
	private Funds fundController = null;
	
	/**
	 * Constructs a BanknoteHandler with the specified fund controller.
	 * 
	 * @param fundController The fund controller to associate with this handler.
	 */
	public BanknoteHandler(Funds fundController) {
		this.fundController = fundController;
	}

	/**
	 * Handles the event when a valid banknote is accepted.
	 * 
	 * @param validator The banknote validator.
	 * @param currency The currency of the accepted banknote.
	 * @param denomination The denomination of the accepted banknote.
	 */
	@Override
	public void goodBanknote(BanknoteValidator validator, Currency currency, BigDecimal denomination) {
		this.fundController.notifyFundsAdded(denomination);
		this.fundController.addToTotalPaid(denomination);
//		this.fundController.totalPaid = this.fundController.totalPaid.add(denomination);
		BigDecimal amountDue = new BigDecimal(this.fundController.checkoutStationSoftware.getTotalOrderPrice()).subtract(this.fundController.totalPaid);
        amountDue = amountDue.setScale(1, RoundingMode.CEILING);

		if (amountDue.compareTo(BigDecimal.ZERO) <= 0) {
        	this.fundController.checkoutStationSoftware.setOrderTotalPrice(0);

			amountDue = amountDue.abs();
			
			boolean missed = false;
			try {
				missed = this.fundController.dispenseAccurateChange(amountDue);
			} catch (DisabledException | CashOverloadException | NoCashAvailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (missed) {				
				this.fundController.notifyPaidFunds(amountDue);
			}
		}
		else {
        	this.fundController.checkoutStationSoftware.removeTotalOrderPrice(denomination.doubleValue());
        }
	}

	/**
	 * Handles the event when an invalid banknote is detected.
	 * 
	 * @param validator The banknote validator.
	 */
	@Override
	public void badBanknote(BanknoteValidator validator) {
		this.fundController.notifyInvalidFunds(PaymentKind.Kind.CASH);
	}
	

	/**
	 * Handles the event when a banknote is added to the dispenser.
	 * 
	 * @param dispenser The banknote dispenser.
	 * @param banknote The banknote added to the dispenser.
	 */
	@Override
	public void banknoteAdded(IBanknoteDispenser dispenser, Banknote banknote) {
	}

	/**
	 * Handles the event when a banknote is removed from the dispenser.
	 * 
	 * @param dispenser The banknote dispenser.
	 * @param banknote The banknote removed from the dispenser.
	 */
	@Override
	public void banknoteRemoved(IBanknoteDispenser dispenser, Banknote banknote) {
		this.fundController.banknotesAvailable.put(banknote.getDenomination(), (int)this.fundController.banknotesAvailable.get(banknote.getDenomination()) - 1);
		if (dispenser.size() < 5)
        	this.fundController.notifyBanknotesLow(dispenser);
	}

	/**
	 * Handles the event when banknotes are loaded into the dispenser.
	 * 
	 * @param dispenser The banknote dispenser.
	 * @param banknotes The banknotes loaded into the dispenser.
	 */
	@Override
	public void banknotesLoaded(IBanknoteDispenser dispenser, Banknote... banknotes) {
		for (Banknote b : banknotes) {
			this.fundController.banknotesAvailable.put(b.getDenomination(), (int)this.fundController.banknotesAvailable.get(b.getDenomination()) + 1);
		}
	}

	/**
	 * Handles the event when banknotes are unloaded from the dispenser.
	 * 
	 * @param dispenser The banknote dispenser.
	 * @param banknotes The banknotes unloaded from the dispenser.
	 * @throws NullPointerException If attempting to unload banknotes not available in the dispenser.
	 */
	@Override
	public void banknotesUnloaded(IBanknoteDispenser dispenser, Banknote... banknotes) {
		for (Banknote b : banknotes) {
			if((int)this.fundController.banknotesAvailable.get(b.getDenomination()) > 0) {
				this.fundController.banknotesAvailable.put(b.getDenomination(), (int)this.fundController.banknotesAvailable.get(b.getDenomination()) - 1);
			}
			else {
				throw new NullPointerException();
			}
		}
		if (dispenser.size() < 5)
        	this.fundController.notifyBanknotesLow(dispenser);
	}

	// These methods are not useful for this class and thus left unimplemented with default JavaDoc comments.
	
	@Override
	public void enabled(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
	}

	@Override
	public void disabled(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
	}

	@Override
	public void turnedOn(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
	}

	@Override
	public void turnedOff(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void moneyFull(IBanknoteDispenser dispenser) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Handles the event when the banknote dispenser becomes empty.
	 * 
	 * @param dispenser The banknote dispenser.
	 */
	@Override
	public void banknotesEmpty(IBanknoteDispenser dispenser) {
		if (dispenser.size() < 5)
        	this.fundController.notifyBanknotesLow(dispenser);
	}

	@Override
	public void banknotesFull(BanknoteStorageUnit unit) {
		// TODO Auto-generated method stub
		this.fundController.notifyBanknotesHigh(unit);
	}

	@Override
	public void banknoteAdded(BanknoteStorageUnit unit) {
		// TODO Auto-generated method stub
		if (unit.getCapacity() - unit.getBanknoteCount() < 5)
        	this.fundController.notifyBanknotesHigh(unit);
	}

	@Override
	public void banknotesLoaded(BanknoteStorageUnit unit) {
		// TODO Auto-generated method stub
		if (unit.getCapacity() - unit.getBanknoteCount() < 5)
        	this.fundController.notifyBanknotesHigh(unit);
	}

	@Override
	public void banknotesUnloaded(BanknoteStorageUnit unit) {
		// TODO Auto-generated method stub
		
	}
}
