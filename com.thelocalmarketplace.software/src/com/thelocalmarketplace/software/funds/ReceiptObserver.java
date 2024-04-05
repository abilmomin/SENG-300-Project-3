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

import java.util.ArrayList;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.printer.IReceiptPrinter;

/**
 * Observer for the receipt.
 */
public interface ReceiptObserver {

	/**
	 * Default method for a receipt has been printed.
	 */
	default public void receiptPrinted(ArrayList<Item> Order) {}

	default public void inkAdded(IReceiptPrinter printer) {}
	
	default public void paperAdded(IReceiptPrinter printer) {}
}
