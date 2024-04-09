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

package com.thelocalmarketplace.software.product;

import com.thelocalmarketplace.hardware.Product;

/**
 * This interface defines the listeners for events related to product management within the software.
 */
public interface ProductsListener {
	/**
	 * Signals an event in which one or more products have been removed from the
	 * facade.
	 * 
	 * @param productFacade
	 *            The facade in which the event occurred.
	 * @param product
	 *            The product involved.
	 */
	default public void productRemoved(Products productFacade, Product product) {}

	/**
	 * Signals an event in which one product has been added from the facade.
	 * 
	 * @param productFacade
	 *            The facade in which the event occurred.
	 * @param product
	 *            The product involved.
	 */
	default public void productAdded(Products productFacade, Product product) {}
	
	/**
	 * Signals an event in which one product must be added to the bagging area.
	 * 
	 * @param productFacade
	 *            The facade in which the event occurred.
	 * @param product
	 *            The product involved.
	 */
	default public void productToBaggingArea(Products productFacade, Product product) {}
	
	/**
	 * Signals an event in which one or more reusable bags have been purchased.
	 * 
	 * @param productFacade
	 *            The facade in which the event occurred.
	 * @param totalCost
	 * 			  The total cost of all the bags purchased.
	 */
	default public void bagsPurchased(Products productFacade, long totalCost) {}
}
