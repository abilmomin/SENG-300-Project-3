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

package com.thelocalmarketplace.software.test;

import com.thelocalmarketplace.hardware.Product;

import com.thelocalmarketplace.software.product.Products;
import com.thelocalmarketplace.software.product.ProductsListener;

public class ProductsListenerStub implements ProductsListener{
	private boolean productAddedCalled = false;
    private Product lastAddedProduct = null;

    private boolean productRemovedCalled = false;
    private Product lastRemovedProduct = null;
    
    private boolean productToBaggingAreaCalled = false;
    private Product lastBaggingAreaProduct = null;

    private boolean bagsPurchasedCalled = false;
    private long lastBagsPurchasedTotalCost = 0;

    @Override
    public void productAdded(Products productFacade, Product product) {
        this.productAddedCalled = true;
        this.lastAddedProduct = product;
    }

    @Override
    public void productRemoved(Products productFacade, Product product) {
        this.productRemovedCalled = true;
        this.lastRemovedProduct = product;
    }
    @Override
    public void productToBaggingArea(Products productFacade, Product product) {
        this.productToBaggingAreaCalled = true;
        this.lastBaggingAreaProduct = product;
    }

    @Override
    public void bagsPurchased(Products productFacade, long totalCost) {
        this.bagsPurchasedCalled = true;
        this.lastBagsPurchasedTotalCost = totalCost;
    }

    
    public boolean isProductAddedCalled() {
        return productAddedCalled;
    }

    public Product getLastAddedProduct() {
        return lastAddedProduct;
    }

    public boolean isProductRemovedCalled() {
        return productRemovedCalled;
    }

    public Product getLastRemovedProduct() {
        return lastRemovedProduct;
    }
    public boolean isProductToBaggingAreaCalled() {
        return productToBaggingAreaCalled;
    }

    public Product getLastBaggingAreaProduct() {
        return lastBaggingAreaProduct;
    }

    public boolean isBagsPurchasedCalled() {
        return bagsPurchasedCalled;
    }

    public long getLastBagsPurchasedTotalCost() {
        return lastBagsPurchasedTotalCost;
    }
    
}
