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

import java.util.ArrayList;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.printer.IReceiptPrinter;

/**
 * Observer for the receipt.
 */
public interface ReceiptObserver {

	/**
	 * Default method for a receipt has been printed.
	 * 
	 * @param order The list of items included in the printed receipt.
	 */
	default public void receiptPrinted(ArrayList<Item> Order) {}

	/**
	 * Default method for ink has been added.
	 * 
	 * @param printer The printer to which ink was added.
	 */
	default public void inkAdded(IReceiptPrinter printer) {}
	
	/**
	 * Default method for paper added to receipt printer.
	 * 
	 * @param printer The printer to which paper was added.
	 */
	default public void paperAdded(IReceiptPrinter printer) {}
	
	/**
	 * Default method to indicate receipt printer has run out of ink.
	 * 
	 * @param printer The receipt printer that has no ink.
	 */
	default public void noInkError(IReceiptPrinter printer) {}

	/**
	 * Default method to indicate receipt printer has no paper.
	 *  
	 * @param printer The receipt printer that has no paper.
	 */
	default public void noPaperError(IReceiptPrinter printer) {}

	/**
	 * Default method indicating the receipt printer is low on ink.
	 * 
	 * @param printer The receipt printer that is low on ink.
	 */
	default public void lowInkError(IReceiptPrinter printer) {}

	/**
	 * Default method indicating the receipt printer is low on paper.
	 * 
	 * @param printer The receipt printer that is low on paper.
	 */
	default public void lowPaperError(IReceiptPrinter printer) {}
}
