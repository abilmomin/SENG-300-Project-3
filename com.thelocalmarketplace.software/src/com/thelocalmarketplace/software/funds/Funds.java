package com.thelocalmarketplace.software.funds;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.OverloadedDevice;
import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.NoCashAvailableException;
import com.tdc.banknote.Banknote;
import com.tdc.banknote.IBanknoteDispenser;
import com.tdc.coin.Coin;
import com.tdc.coin.ICoinDispenser;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

public class Funds {
	protected BigDecimal coinPaid = BigDecimal.ZERO;
	protected BigDecimal banknotePaid = BigDecimal.ZERO;
	protected Map<BigDecimal, Number> coinsAvailable;
	protected Map<BigDecimal, Number> banknotesAvailable;
	protected int storedCredit = 0;
	protected final SelfCheckoutStationSoftware checkoutStationSoftware;
	protected Set<FundsObserver> observers = new HashSet<>();

	/**
	 * Basic constructor.
	 * 
	 * @param vendingMachine
	 *            The device facade that will be used to implement all low-level
	 *            functions.
	 */
	public Funds(SelfCheckoutStationSoftware checkoutStation) {
		if(checkoutStation == null)
			throw new IllegalArgumentException("The argument cannot be null");

		this.checkoutStationSoftware = checkoutStation;
		
		// register the coin payment handler to track coin available and that were entered into the checkout station
		PayWithCoinHandler pch = new PayWithCoinHandler(this);
		checkoutStation.station.getCoinValidator().attach(pch);
		Map<BigDecimal, ICoinDispenser> coinDispensersMap = this.checkoutStationSoftware.getStationHardware().getCoinDispensers();
		for( BigDecimal coin: coinDispensersMap.keySet()) {
			ICoinDispenser dispenser = coinDispensersMap.get(coin);
			dispenser.attach(pch);
		}
		// register the banknote payment handler to track banknotes available and that were entered into the checkout station
		PayWithBanknoteHandler pbh = new PayWithBanknoteHandler(this);
		checkoutStation.station.getBanknoteValidator().attach(pbh);
		Map<BigDecimal, IBanknoteDispenser> banknoteDispensersMap = this.checkoutStationSoftware.getStationHardware().getBanknoteDispensers();
		for( BigDecimal coin: banknoteDispensersMap.keySet()) {
			IBanknoteDispenser dispenser = banknoteDispensersMap.get(coin);
			dispenser.attach(pbh);
		}
		
		//add registeration of credit, debit, and crypto here wehn ready
	}

	/**
	 * Registers the given listener with this facade so that the listener will be
	 * notified of events emanating from here.
	 * 
	 * @param listener
	 *            The listener to be registered. No effect if it is already
	 *            registered. Cannot be null.
	 */
	public void register(FundsObserver listener) {
		observers.add(listener);
	}

	/**
	 * De-registers the given listener from this facade so that the listener will no
	 * longer be notified of events emanating from here.
	 * 
	 * @param listener
	 *            The listener to be de-registered. No effect if it is not already
	 *            registered or null.
	 */
	public void deregister(FundsObserver listener) {
		observers.remove(listener);
	}
	
	/**
	 * Returns the total amount paid by the customer in both coins and banknotes.
	 * 
	 * @return The total amount paid.
	 */
	protected BigDecimal totalPaid() {
		return new BigDecimal(coinPaid.add(banknotePaid).intValue());
	}
	
	
	/**
	 * Notifies observers that the payment funds are invalid for the specified payment kind.
	 * 
	 * @param kind The kind of payment for which the funds are invalid.
	 */
	protected void notifyInvalidFunds(PaymentKind.Kind kind) {
		for(FundsObserver observer : observers)
			observer.fundsInvalid(this, kind);
	}
	
	/**
	 * Notifies observers that the payment funds have been paid in full, along with the change returned.
	 * 
	 * @param changeReturned The amount of change returned to the customer.
	 */
	protected void notifyPaidFunds(BigDecimal changeReturned) {
		for(FundsObserver observer : observers)
			observer.fundsPaidInFull(this, changeReturned);
	}
	
	/**
	 * Notifies observers that the payment funds have been added, along with the amount added.
	 * 
	 * @param amount The amount of funds added by the customer.
	 */
	protected void notifyFundsAdded(BigDecimal amount) {
		for(FundsObserver observer : observers)
			observer.fundsAdded(this, amount);
	}
	
	/**
	 * Notifies observers that the payment funds have removed, along with the amount removed.
	 * 
	 * @param amount The amount of funds removed by the customer.
	 */
	protected void notifyFundsRemoved(BigDecimal amount) {
		for(FundsObserver observer : observers)
			observer.fundsRemoved(this, amount);
	}
	
	/**
	 * Dispenses the correct amount of change to the customer and gives them the
	 * choice to print a receipt.
	 *
	 * Implements change dispensing logic using available coin denominations.
	 *
	 * @param changeValue The amount of change to be dispensed.
	 * @return true if correct change is dispensed, false otherwise.
	 * @throws DisabledException        If the coin slot is disabled.
	 * @throws CashOverloadException    If the cash storage is overloaded.
	 * @throws NoCashAvailableException If no cash is available for dispensing
	 *                                  change.
	 * @throws OverloadedDevice
	 * @throws EmptyDevice
	 */
	public boolean dispenseAccurateChange(BigDecimal changeValue)
			throws DisabledException, CashOverloadException, NoCashAvailableException, EmptyDevice, OverloadedDevice {
		AbstractSelfCheckoutStation station = (AbstractSelfCheckoutStation) checkoutStationSoftware.getStationHardware();
		
		BigDecimal amountDispensed = new BigDecimal("0.0");
		BigDecimal remainingAmount = changeValue;
		List<BigDecimal> coinDenominations = station.getCoinDenominations();
		Collections.sort(coinDenominations);
		Collections.reverse(coinDenominations);
		List<BigDecimal> bankNoteDenominations = Arrays.stream(station.getBanknoteDenominations())
				.collect(Collectors.toList());
		Collections.sort(bankNoteDenominations);
		Collections.reverse(bankNoteDenominations);

		// This approach aims to find the optimal combination of denominations to minimize the
		// number of banknotes and coins used while considering the limited availability of
		// each denomination.
		while (remainingAmount.compareTo(BigDecimal.ZERO) > 0) {
			// If neither banknotes nor coins can be used, break the loop
			BigDecimal lowestCoin = coinDenominations.get(coinDenominations.size() - 1);
			BigDecimal lowestBankNote = bankNoteDenominations.get(bankNoteDenominations.size() - 1);
			BigDecimal lowestVal = lowestCoin.min(lowestBankNote);
			if (remainingAmount.compareTo(lowestVal) < 0 && remainingAmount.compareTo(BigDecimal.ZERO) > 0) {
				station.getCoinDispensers().get(lowestVal).emit();
				amountDispensed = changeValue;
				remainingAmount = BigDecimal.ZERO;
				break;
			}
			
			boolean dispensed = false;
			// Try using banknotes first
			for (BigDecimal bankNote : bankNoteDenominations) {
				if (remainingAmount.compareTo(bankNote) >= 0 && (int)banknotesAvailable.get(bankNote) > 0) {
					station.getBanknoteDispensers().get(bankNote).emit();
					amountDispensed = amountDispensed.add(bankNote);
					remainingAmount = remainingAmount.subtract(bankNote);
					dispensed = true;
					break;
				}
			}

			// If no banknotes are available or insufficient, try using coins
			if (!dispensed) {
				for (BigDecimal coin : coinDenominations) {
					if (remainingAmount.compareTo(coin) >= 0 && (int)coinsAvailable.get(coin) > 0) {
						station.getCoinDispensers().get(coin).emit();
						amountDispensed = amountDispensed.add(coin);
						remainingAmount = remainingAmount.subtract(coin);
						dispensed = true;
						break;
					}
				}
			}
			if(!dispensed)
				break;
		}

		return remainingAmount.compareTo(BigDecimal.ZERO) == 0;
	}
	
	
}