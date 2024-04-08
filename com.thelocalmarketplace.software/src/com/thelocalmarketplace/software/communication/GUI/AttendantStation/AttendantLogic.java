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

package com.thelocalmarketplace.software.communication.GUI.AttendantStation;

import java.math.BigDecimal;

import java.util.Currency;
import java.util.List;
import java.util.Map;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.printer.IReceiptPrinter;
import com.jjjwelectronics.printer.ReceiptPrinterBronze;

import com.tdc.CashOverloadException;
import com.tdc.banknote.Banknote;
import com.tdc.banknote.BanknoteStorageUnit;
import com.tdc.banknote.IBanknoteDispenser;
import com.tdc.coin.Coin;
import com.tdc.coin.CoinStorageUnit;
import com.tdc.coin.ICoinDispenser;

import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.ISelfCheckoutStation;

import ca.ucalgary.seng300.simulation.SimulationException;

import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.CustomerStation;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware.StartSession;
import com.thelocalmarketplace.software.funds.Receipt;

/**
 * The AttendantLogic class contains methods that represent the logic and actions
 * performed by an attendant on the self-checkout station.
 */
public class AttendantLogic {
	
	/**
     * Empties the coin storage unit and coin dispensers of the self-checkout station.
     * 
     * @param cSoftware 
     * 			The self-checkout station software instance.
     */
	public void emptyCoinStorage(SelfCheckoutStationSoftware cSoftware) {
		ISelfCheckoutStation cS = cSoftware.getStationHardware();
		CoinStorageUnit storage = cS.getCoinStorage();
		storage.unload();
		
		Map<BigDecimal, ICoinDispenser> coinDispensersMap = cS.getCoinDispensers();
		for( BigDecimal coin: coinDispensersMap.keySet()) {
			ICoinDispenser dispenser = coinDispensersMap.get(coin);
			dispenser.unload();
		}
	}
	
	/**
     * Empties the banknote storage unit and banknote dispensers of the self-checkout station.
     * 
     * @param cSoftware 
     * 			The self-checkout station software instance.
     */
	public void emptyBanknoteStorage(SelfCheckoutStationSoftware cSoftware) {
		ISelfCheckoutStation cS = cSoftware.getStationHardware();
		BanknoteStorageUnit storage = cS.getBanknoteStorage();
		storage.unload();
		
		Map<BigDecimal, IBanknoteDispenser> banknoteDispensersMap = cS.getBanknoteDispensers();
		for( BigDecimal banknote: banknoteDispensersMap.keySet()) {
			IBanknoteDispenser dispenser = banknoteDispensersMap.get(banknote);
			dispenser.unload();
		}
	}
	
	 /**
     * Refills the coin dispensers of the self-checkout station with coins.
     * 
     * @param cSoftware 
     * 			The self-checkout station software instance.
     * @throws SimulationException 
     * 			If an error occurs during simulation.
     * @throws CashOverloadException 
     * 			If the coin dispensers are overloaded.
     */
	public void refillCoinDispensers(SelfCheckoutStationSoftware cSoftware) throws SimulationException, CashOverloadException {
		ISelfCheckoutStation cS = cSoftware.getStationHardware();
		Currency currency = Currency.getInstance("CAD");
		List<BigDecimal> denominations = cS.getCoinDenominations();
		Map<BigDecimal, ICoinDispenser> dispensers = cS.getCoinDispensers();
		for (BigDecimal denomination : denominations) {
			ICoinDispenser dispenser = dispensers.get(denomination);
			while (dispenser.hasSpace())
				dispenser.load(new Coin(currency, denomination));
		}
	}
	
	 /**
     * Refills the banknote dispensers of the self-checkout station with banknotes.
     * 
     * @param cSoftware 
     * 			The self-checkout station software instance.
     * @throws SimulationException 
     * 			If an error occurs during simulation.
     * @throws CashOverloadException 
     * 			If the banknote dispensers are overloaded.
     */
	public void refillBanknoteDispensers(SelfCheckoutStationSoftware cSoftware) throws SimulationException, CashOverloadException {
		ISelfCheckoutStation cS = cSoftware.getStationHardware();
		AbstractSelfCheckoutStation aCS = (AbstractSelfCheckoutStation) cS;
		Currency currency = Currency.getInstance("CAD");
		BigDecimal[] denominations = cS.getBanknoteDenominations();
		Map<BigDecimal, IBanknoteDispenser> dispensers = aCS.getBanknoteDispensers();

		for (BigDecimal denomination : denominations) {
			IBanknoteDispenser dispenser = dispensers.get(denomination);
			while (dispenser.size() < dispenser.getCapacity())
				dispenser.load(new Banknote(currency, denomination));
		}
	}
	
	/**
	 * Refills the printer ink of the self-checkout station with the maximum ink capacity.
	 * 
	 * @param cSoftware 
	 * 			The self-checkout station software instance.
	 * @throws OverloadedDevice 
	 * 			If an error occurs due to printer overload.
	 */
	public void refillPrinterInk(SelfCheckoutStationSoftware cSoftware) throws OverloadedDevice {
		Receipt receipt = cSoftware.getReceipt();
		ISelfCheckoutStation cS = cSoftware.getStationHardware();
		IReceiptPrinter printer = cS.getPrinter();
		printer.addInk(ReceiptPrinterBronze.MAXIMUM_INK);
		receipt.notifyInkAdded(printer);
	}

	/**
	 * Refills the printer paper of the self-checkout station with the maximum paper capacity.
	 * 
	 * @param cSoftware 
	 * 			The self-checkout station software instance.
	 * @throws OverloadedDevice 
	 * 			If an error occurs due to printer overload.
	 */
	public void refillPrinterPaper(SelfCheckoutStationSoftware cSoftware) throws OverloadedDevice {
		Receipt receipt = cSoftware.getReceipt();
		ISelfCheckoutStation cS = cSoftware.getStationHardware();
		IReceiptPrinter printer = cS.getPrinter();
		printer.addPaper(ReceiptPrinterBronze.MAXIMUM_PAPER);
		receipt.notifyPaperAdded(printer);
	}
	
	/**
	 * Refills the printer paper of the self-checkout station when paper is low.
	 * 
	 * @param cSoftware 
	 * 			The self-checkout station software instance.
	 * @throws OverloadedDevice
	 * 			If an error occurs due to printer overload.
	 */
	public void refillPrinterPaperWhenLow(SelfCheckoutStationSoftware cSoftware) throws OverloadedDevice {
		Receipt receipt = cSoftware.getReceipt();
		ISelfCheckoutStation cS = cSoftware.getStationHardware();
		IReceiptPrinter printer = cS.getPrinter();

		try {
			printer.addPaper(ReceiptPrinterBronze.MAXIMUM_PAPER - printer.paperRemaining());
		} catch (UnsupportedOperationException e) {
			for (int i = 0; i < ReceiptPrinterBronze.MAXIMUM_PAPER; i++) {
				try {
					printer.print('c');
				} catch (EmptyDevice x) { }
			}
			printer.addPaper(ReceiptPrinterBronze.MAXIMUM_PAPER);
		}
		receipt.notifyPaperAdded(printer);
	}
	
	/**
	 * Refills the printer ink of the self-checkout station when ink is low.
	 * 
	 * @param cSoftware 
	 * 			The self-checkout station software instance.
	 * @throws OverloadedDevice 
	 * 			If an error occurs due to printer overload.
	 */
	public void refillPrinterInkWhenLow(SelfCheckoutStationSoftware cSoftware) throws OverloadedDevice {
		Receipt receipt = cSoftware.getReceipt();
		ISelfCheckoutStation cS = cSoftware.getStationHardware();
		IReceiptPrinter printer = cS.getPrinter();

		try {
			printer.addInk(ReceiptPrinterBronze.MAXIMUM_INK - printer.inkRemaining());
		} catch (UnsupportedOperationException e) {
			for (int i = 0; i < ReceiptPrinterBronze.MAXIMUM_INK; i++) {
				try {
					printer.print('c');
				} catch (EmptyDevice x) { }
			}
			printer.addInk(ReceiptPrinterBronze.MAXIMUM_INK);
		}
		receipt.notifyInkAdded(printer);
	}
	
	/**
     * Enables the specified self-checkout station.
     * 
     * @param selectedStation 
     * 			The index of the selected station.
     * @param customerStation 
     * 			Array of customer stations.
     * @param stationSoftwareInstances 
     * 			Array of self-checkout station software instances.
     * @param checkoutStation 
     * 			The abstract self-checkout station instance.
     * @param startSessions 
     * 			Array of start session instances.
     */
	public void EnableStation(int selectedStation,CustomerStation[] customerStation, SelfCheckoutStationSoftware[] stationSoftwareInstances,AbstractSelfCheckoutStation checkoutStation, StartSession[] startSessions )  {
		if (stationSoftwareInstances[selectedStation].getStationBlock()== true) {
			stationSoftwareInstances[selectedStation].setStationUnblock();	
		}		
		startSessions[selectedStation].enableMouseListener();
	}
	
	/**
     * Disables the specified self-checkout station.
     * 
     * @param selectedStation 
     * 			The index of the selected station.
     * @param customerStation 
     * 			Array of customer stations.
     * @param stationSoftwareInstances 
     * 			Array of self-checkout station software instances.
     * @param checkoutStation 
     * 			The abstract self-checkout station instance.
     * @param startSessions 
     * 			Array of start session instances.
     * @return True if the station is successfully disabled, otherwise false.
     */
	public boolean DisableStation(int selectedStation,CustomerStation[] customerStation, SelfCheckoutStationSoftware[] stationSoftwareInstances,AbstractSelfCheckoutStation checkoutStation, StartSession[] startSessions) {
        if (stationSoftwareInstances[selectedStation].getStationBlock() == false) {
        	if (stationSoftwareInstances[selectedStation].getStationActive() == false) {
        		stationSoftwareInstances[selectedStation].setStationBlock();
        		startSessions[selectedStation].disableMouseListener();
        		startSessions[selectedStation].sessionPopUp("Out of order");
        	}
        	return true;
        }
        else {
        	return false;		
        }
	}
}