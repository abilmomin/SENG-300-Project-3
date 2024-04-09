/**

 SENG 300 - ITERATION 3
 GROUP GOLD {8}

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
import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.IComponent;
import com.tdc.IComponentObserver;
import com.tdc.NoCashAvailableException;
import com.tdc.coin.Coin;
import com.tdc.coin.CoinDispenserObserver;
import com.tdc.coin.CoinStorageUnit;
import com.tdc.coin.CoinStorageUnitObserver;
import com.tdc.coin.CoinValidator;
import com.tdc.coin.CoinValidatorObserver;
import com.tdc.coin.ICoinDispenser;


/**
 * Handles coin payment events, implementing observer interfaces for CoinValidator and CoinDispenser.
 */
public class CoinHandler implements CoinValidatorObserver, CoinDispenserObserver, CoinStorageUnitObserver {
    
    private Funds fundController = null;
    
    /**
     * Constructs a CoinHandler with a given fund controller.
     * 
     * @param fundController 
     * 				The fund controller to associate with this handler.
     */
    public CoinHandler(Funds fundController) {
        this.fundController = fundController;
    }
    
    /**
     * Handles the event when a valid coin is detected by the CoinValidator.
     * 
     * @param validator 
     * 				The CoinValidator detecting the coin.
     * @param value 
     * 				The value of the detected coin.
     */
    @Override 
    public void validCoinDetected(CoinValidator validator, BigDecimal value)  {
        this.fundController.addToTotalPaid(value);
        this.fundController.notifyFundsAdded(value);

        BigDecimal amountDue = new BigDecimal(this.fundController.checkoutStationSoftware.getTotalOrderPrice()).subtract(this.fundController.getTotalPaid());
        amountDue = amountDue.setScale(2, RoundingMode.CEILING);
        
        if (amountDue.compareTo(BigDecimal.ZERO) <= 0) {
            amountDue = amountDue.abs();
            
            boolean validChange = false;
            try {
            	validChange = this.fundController.dispenseAccurateChange(amountDue);
            } catch (DisabledException | CashOverloadException  | NoCashAvailableException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            while (!validChange) {   
                this.fundController.notifyNoValidChange(amountDue);
                try {
                	validChange = this.fundController.dispenseAccurateChange(amountDue);
				} catch (CashOverloadException | NoCashAvailableException | DisabledException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            this.fundController.notifyPaidFunds(amountDue);
        }
    }

    /**
     * Handles the event when an invalid coin is detected by the CoinValidator.
     * 
     * @param validator 
     * 				The CoinValidator detecting the invalid coin.
     */
    @Override
    public void invalidCoinDetected(CoinValidator validator) {
        this.fundController.notifyInvalidFunds(PaymentKind.Kind.CASH);
    }
    
    /**
     * Adds a coin to the list of available coins when a coin is added to the dispenser.
     * 
     * @param dispenser 
     * 				The coin dispenser where the coin is added.
     * @param coin 
     * 				The coin added to the dispenser.
     */
    @Override
	public void coinAdded(ICoinDispenser dispenser, Coin coin) {
        this.fundController.coinsAvailable.put(coin.getValue(), (int)this.fundController.coinsAvailable.get(coin.getValue()) + 1);
        this.fundController.notifyFundsStored(coin.getValue());
    }

    /**
     * Removes a coin from the list of available coins when a coin is removed from the dispenser.
     * 
     * @param dispenser 
     * 				The coin dispenser where the coin is removed.
     * @param coin 
     * 				The coin removed from the dispenser.
     * @throws NullPointerException 
     * 				If the coin being removed is not available.
     */
    @Override
    public void coinRemoved(ICoinDispenser dispenser, Coin coin) {
        if((int)this.fundController.coinsAvailable.get(coin.getValue()) > 0) {
            this.fundController.coinsAvailable.put(coin.getValue(), (int)this.fundController.coinsAvailable.get(coin.getValue()) - 1);
            this.fundController.notifyFundsRemoved(coin.getValue());
        }
        else {
            throw new NullPointerException();
        }
        if (dispenser.size() < 5)
        	this.fundController.notifyCoinsLow(dispenser);
    }
    
    /**
     * Loads coins into the dispenser and updates the list of available coins.
     * 
     * @param dispenser 
     * 				The coin dispenser where coins are loaded.
     * @param coins 
     * 				The coins loaded into the dispenser.
     */
    @Override
    public void coinsLoaded(ICoinDispenser dispenser, Coin... coins) {
        for (Coin c : coins) {
            this.fundController.coinsAvailable.put(c.getValue(), (int)this.fundController.coinsAvailable.get(c.getValue()) + 1);
            this.fundController.notifyFundsStored(c.getValue());
        }
    }

    /**
     * Unloads coins from the dispenser and updates the list of available coins.
     * 
     * @param dispenser 
     * 				The coin dispenser where coins are unloaded.
     * @param coins 
     * 				The coins unloaded from the dispenser.
     * @throws NullPointerException 
     * 				If any unloaded coin is not available.
     */
    @Override
    public void coinsUnloaded(ICoinDispenser dispenser, Coin... coins) {
        for (Coin c : coins) {
            if((int)this.fundController.coinsAvailable.get(c.getValue()) > 0) {
                this.fundController.coinsAvailable.put(c.getValue(), (int)this.fundController.coinsAvailable.get(c.getValue()) - 1);
                this.fundController.notifyFundsRemoved(c.getValue());
            } else {
                throw new NullPointerException();
            }
        }
        if (dispenser.size() < 5)
        	this.fundController.notifyCoinsLow(dispenser);
    }
    
    /**
     * Notifies the fund controller when the coin dispenser is empty.
     */
    @Override
    public void coinsEmpty(ICoinDispenser dispenser) {
        this.fundController.notifyCoinsLow(dispenser);
    }

	/**
	 * Notifies the fund controller when the coin storage unit is full.
	 */
    @Override
	public void coinsFull(CoinStorageUnit unit) {
		// TODO Auto-generated method stub
        this.fundController.notifyCoinsHigh(unit);
	}

    /**
     * Notifies the fund controller when a coin is added to the coin storage unit and it is nearly full.
     */
	@Override
	public void coinAdded(CoinStorageUnit unit) {
		// TODO Auto-generated method stub
		if (unit.getCapacity() - unit.getCoinCount() < 5)
        	this.fundController.notifyCoinsHigh(unit);
	}

	/**
	 * Notifies the fund controller when the coin storage unit is loaded and nearly full.
	 */
	@Override
	public void coinsLoaded(CoinStorageUnit unit) {
		// TODO Auto-generated method stub
		if (unit.getCapacity() - unit.getCoinCount() < 5)
        	this.fundController.notifyCoinsHigh(unit);
	}
	
	/**
	 * Called when a component is enabled.
	 * This method is invoked when a component is enabled.
	 * 
	 * @param component 
	 * 				The component that has been enabled.
	 */
    @Override
    public void enabled(IComponent<? extends IComponentObserver> component) {
        // TODO Auto-generated method stub
    }
    
    
    /**
     * Called when a component is disabled.
     * This method is invoked when a component is disabled.
     * 
     * @param component 
     * 				The component that has been disabled.
     */
    @Override
    public void disabled(IComponent<? extends IComponentObserver> component) {
        // TODO Auto-generated method stub
    }
    
    
    /**
     * Called when a component is turned on.
     * This method is invoked when a component is turned on.
     * 
     * @param component 
     * 				The component that has been turned on.
     */
    @Override
    public void turnedOn(IComponent<? extends IComponentObserver> component) {
        // TODO Auto-generated method stub
    }
    
    
    /**
     * Called when a component is turned off.
     * This method is invoked when a component is turned off.
     * 
     * @param component 
     * 				The component that has been turned off.
     */
    @Override
    public void turnedOff(IComponent<? extends IComponentObserver> component) {
        // TODO Auto-generated method stub    
    }

    
    /**
     * This method is not utilized in the current implementation.
     * 
     * @param dispenser 
     * 				The coin dispenser.
     */
    @Override
    public void coinsFull(ICoinDispenser dispenser) {
        // TODO Auto-generated method stub
    }
    
    
    /**
     * This method is not utilized in the current implementation.
     * 
     * @param unit The coin storage unit.
     */
	@Override
	public void coinsUnloaded(CoinStorageUnit unit) {
		// TODO Auto-generated method stub
	}


}
