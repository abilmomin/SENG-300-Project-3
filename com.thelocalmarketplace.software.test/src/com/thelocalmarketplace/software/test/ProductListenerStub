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
