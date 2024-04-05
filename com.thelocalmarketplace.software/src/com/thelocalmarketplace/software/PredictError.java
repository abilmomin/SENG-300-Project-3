package com.thelocalmarketplace.software;

import com.jjjwelectronics.printer.IReceiptPrinter;
import com.tdc.banknote.BanknoteStorageUnit;
import com.tdc.banknote.IBanknoteDispenser;
import com.tdc.coin.CoinStorageUnit;
import com.tdc.coin.ICoinDispenser;

public interface PredictError {


	default public void noInkError(IReceiptPrinter printer) {}

	default public void noPaperError(IReceiptPrinter printer) {}
	default public void lowCoinsError(ICoinDispenser dispenser) {}
	
	default public void lowBanknotesError(IBanknoteDispenser dispenser) {}
	
	default public void highCoinsError(CoinStorageUnit storage) {}
	
	default public void highBanknotesError(BanknoteStorageUnit storage) {}
	
}

