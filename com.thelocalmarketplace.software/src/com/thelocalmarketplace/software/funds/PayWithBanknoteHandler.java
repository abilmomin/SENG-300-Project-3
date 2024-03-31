package com.thelocalmarketplace.software.funds;

import java.math.BigDecimal;
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
import com.tdc.banknote.BanknoteValidator;
import com.tdc.banknote.BanknoteValidatorObserver;
import com.tdc.banknote.IBanknoteDispenser;
import com.tdc.coin.Coin;

public class PayWithBanknoteHandler implements BanknoteValidatorObserver, BanknoteDispenserObserver {
	
	private Funds fundController = null;
	
	/**
	 * Constructs a PayWithBanknoteHandler with the specified fund controller.
	 * 
	 * @param fundController The fund controller to associate with this handler.
	 */
	public PayWithBanknoteHandler(Funds fundController) {
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
		this.fundController.banknotePaid.add(denomination);
		BigDecimal amountDue = new BigDecimal(this.fundController.checkoutStationSoftware.getTotalOrderPrice()).subtract(this.fundController.totalPaid());
		if (amountDue.compareTo(BigDecimal.ZERO) <= 0) {
			amountDue = amountDue.abs();
			
			boolean missed = false;
			try {
				missed = this.fundController.dispenseAccurateChange(amountDue);
			} catch (DisabledException | CashOverloadException | NoCashAvailableException | EmptyDevice
					| OverloadedDevice e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (missed) {				
				this.fundController.notifyPaidFunds(amountDue);
			}
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
		this.fundController.banknotesAvailable.put(banknote.getDenomination(), (int)this.fundController.banknotesAvailable.get(banknote.getDenomination()) - 1);
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
			this.fundController.banknotesAvailable.put(b.getDenomination(), (int)this.fundController.banknotesAvailable.get(b.getDenomination()) - 1);
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
	}
}