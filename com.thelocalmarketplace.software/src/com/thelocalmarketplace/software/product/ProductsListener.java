package com.thelocalmarketplace.software.product;

import com.thelocalmarketplace.hardware.Product;

/**
 * 
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
}
