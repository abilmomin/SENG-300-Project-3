package com.thelocalmarketplace.software;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

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
import com.thelocalmarketplace.software.funds.Receipt;
import powerutility.PowerGrid;

public class ALogic {
	
	public void emptyCoinStorage(SelfCheckoutStationSoftware cSoftware) {
		ISelfCheckoutStation cS = cSoftware.getStationHardware();
		CoinStorageUnit storage = cS.getCoinStorage();
		storage.unload();
	}
	
	public void emptyBanknoteStorage(SelfCheckoutStationSoftware cSoftware) {
		ISelfCheckoutStation cS = cSoftware.getStationHardware();
		BanknoteStorageUnit storage = cS.getBanknoteStorage();
		storage.unload();
	}
	
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
	
	public void refillBanknoteDispensers(SelfCheckoutStationSoftware cSoftware) throws SimulationException, CashOverloadException {
		ISelfCheckoutStation cS = cSoftware.getStationHardware();
		AbstractSelfCheckoutStation aCS = (AbstractSelfCheckoutStation) cS;
		Currency currency = Currency.getInstance("CAD");
		BigDecimal[] denominations = cS.getBanknoteDenominations();
		Map<BigDecimal, IBanknoteDispenser> dispensers = aCS.getBanknoteDispensers();
		aCS.plugIn(PowerGrid.instance());
		aCS.turnOn();
		for (BigDecimal denomination : denominations) {
			IBanknoteDispenser dispenser = dispensers.get(denomination);
			while (dispenser.size() < dispenser.getCapacity())
				dispenser.load(new Banknote(currency, denomination));
		}
	}

	// ONLY USE ONCE INK IS EMPTY
	public void refillPrinterInk(SelfCheckoutStationSoftware cSoftware, Receipt receipt) throws OverloadedDevice {
		ISelfCheckoutStation cS = cSoftware.getStationHardware();
		IReceiptPrinter printer = cS.getPrinter();
		printer.addInk(ReceiptPrinterBronze.MAXIMUM_INK);
		receipt.notifyInkAdded(printer);

	}

	// ONLY USE ONCE PAPER IS EMPTY
	public void refillPrinterPaper(SelfCheckoutStationSoftware cSoftware, Receipt receipt) throws OverloadedDevice {
		ISelfCheckoutStation cS = cSoftware.getStationHardware();
		IReceiptPrinter printer = cS.getPrinter();
		printer.addPaper(ReceiptPrinterBronze.MAXIMUM_PAPER);
		receipt.notifyPaperAdded(printer);

	}

	public void refillPrinterPaperWhenLow(SelfCheckoutStationSoftware cSoftware, Receipt receipt) throws OverloadedDevice {
		ISelfCheckoutStation cS = cSoftware.getStationHardware();
		IReceiptPrinter printer = cS.getPrinter();


		for (int i = 0; i < ReceiptPrinterBronze.MAXIMUM_PAPER; i++) {
			try {
				printer.print('c');
			} catch (EmptyDevice e) {
				// TODO Auto-generated catch block

			}
		}


		printer.addPaper(ReceiptPrinterBronze.MAXIMUM_PAPER);
		receipt.notifyPaperAdded(printer);

	}

	public void refillPrinterInkWhenLow(SelfCheckoutStationSoftware cSoftware, Receipt receipt) throws OverloadedDevice {
		ISelfCheckoutStation cS = cSoftware.getStationHardware();
		IReceiptPrinter printer = cS.getPrinter();


		for (int i = 0; i < ReceiptPrinterBronze.MAXIMUM_INK; i++) {
			try {
				printer.print('c');
			} catch (EmptyDevice e) {
				// TODO Auto-generated catch block

			}
		}


		printer.addInk(ReceiptPrinterBronze.MAXIMUM_INK);
		receipt.notifyInkAdded(printer);

	}



}