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

import java.util.ArrayList;

import java.util.HashSet;

import java.util.Map;

import java.util.Set;

import com.jjjwelectronics.EmptyDevice;

import com.jjjwelectronics.IDevice;

import com.jjjwelectronics.IDeviceListener;

import com.jjjwelectronics.Item;

import com.jjjwelectronics.OverloadedDevice;

import com.jjjwelectronics.printer.IReceiptPrinter;

import com.jjjwelectronics.printer.ReceiptPrinterBronze;

import com.jjjwelectronics.printer.ReceiptPrinterGold;

import com.jjjwelectronics.printer.ReceiptPrinterListener;

import com.jjjwelectronics.printer.ReceiptPrinterSilver;

import com.jjjwelectronics.scanner.BarcodedItem;

import com.tdc.coin.ICoinDispenser;

import com.thelocalmarketplace.hardware.BarcodedProduct;

import com.thelocalmarketplace.hardware.PLUCodedItem;

import com.thelocalmarketplace.hardware.PLUCodedProduct;

import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.PredictError;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

import powerutility.PowerGrid;

/**
 * Receipt class which handles the logic of the receipt.
 */
public class Receipt {

    private IReceiptPrinter receiptPrinter;
    protected final SelfCheckoutStationSoftware checkoutStationSoftware;
    private Funds funds;
    protected Set<ReceiptObserver> observers = new HashSet<>();
    protected Set<PredictError> errorObservers = new HashSet<>();
    private ArrayList<Item> order;
    
    /**
     * Constructor for the receipt class.
     *
     * @param printer the receipt printer.
     * @param funds The Funds facade.
     * @param checkoutStation The checkout station software.
     */
    public Receipt (IReceiptPrinter printer, Funds funds, SelfCheckoutStationSoftware checkoutStation) {
        this.receiptPrinter = checkoutStation.getStationHardware().getPrinter();
//        if  (printer == null)
//            throw new NullPointerException("No argument may be null.");
//        if (printer instanceof ReceiptPrinterBronze)
//            this.receiptPrinter = (ReceiptPrinterBronze) printer;
//        else if (printer instanceof ReceiptPrinterSilver)
//            this.receiptPrinter = (ReceiptPrinterSilver) printer;
//        else if (printer instanceof ReceiptPrinterGold)
//            this.receiptPrinter = (ReceiptPrinterGold) printer;
//
//        receiptPrinter.plugIn(PowerGrid.instance());
//        receiptPrinter.turnOn();
        
        ReceiptHandler rh = new ReceiptHandler(receiptPrinter);
        checkoutStation.station.getPrinter().register(rh);

        this.funds = funds;
        this.checkoutStationSoftware = checkoutStation;
        this.order = checkoutStationSoftware.getOrder();

        this.checkoutStationSoftware.getStationHardware().plugIn(PowerGrid.instance());
        this.checkoutStationSoftware.getStationHardware().turnOn();
    }

    public String printReceipt() throws EmptyDevice, OverloadedDevice {

        ArrayList<String> receiptItems = new ArrayList<String>();

        for (int i = 0; i < order.size(); i++) {
            String productDescription;
            Item item = order.get(i);

            if (item instanceof BarcodedItem) { // Gets the product description and the price of a barcoded product
                BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(((BarcodedItem) item).getBarcode());
                productDescription = product.getDescription();
                long price = product.getPrice();
                receiptItems.add(productDescription + " $" + String.format("%.2f", (float)price));
            }

            else if (item instanceof PLUCodedItem) { // Gets the product description and the price of a product inputted
                // through price-lookup (PLU)
                PLUCodedProduct product = ProductDatabases.PLU_PRODUCT_DATABASE.get(((PLUCodedItem) item).getPLUCode());
                productDescription = product.getDescription();
                long price = product.getPrice();
                receiptItems.add(productDescription + " $" + String.format("%.2f", (float)price));
            }
            else {
                throw new NullPointerException("This product is not a supported product, can not be registered for a price");
            }
        }

        BigDecimal purchaseValue = new BigDecimal(String.valueOf(checkoutStationSoftware.getTotalOrderPrice()));
        BigDecimal amountPaid = this.funds.getTotalPaid();
        BigDecimal changeDue = this.funds.getMoneyLeft();


        receiptItems.add("Total: $" + String.format("%.2f", purchaseValue));
        receiptItems.add("Paid: $" + String.format("%.2f", amountPaid));
        receiptItems.add("Change: $" + String.format("%.2f", changeDue));

        for (int i = 0; i < receiptItems.size(); i++) {
            this.receiptPrinter.print('\n');

            for (int j = 0; j < receiptItems.get(i).length(); j++) {
                this.receiptPrinter.print(receiptItems.get(i).charAt(j));
            }
        }

        this.receiptPrinter.cutPaper();
        notifyReceiptPrinted(order);
        return this.receiptPrinter.removeReceipt();
    }
    
    public void register(ReceiptObserver listener, PredictError error) {
		observers.add(listener);
		errorObservers.add(error);
	}

    public void deregister(ReceiptObserver listener, PredictError error) {
		observers.remove(listener);
		errorObservers.remove(error);
	}

    protected void notifyReceiptPrinted(ArrayList<Item> order) {
		for(ReceiptObserver observer : observers)
			observer.receiptPrinted(order);
	}
    
    protected void notifyInkLow(IReceiptPrinter printer) {
		for (PredictError observer : errorObservers)
			observer.lowInkError(printer);
	}
	
	protected void notifyPaperLow(IReceiptPrinter printer) {
		for (PredictError observer : errorObservers)
			observer.lowPaperError(printer);
	}
    
   
}
