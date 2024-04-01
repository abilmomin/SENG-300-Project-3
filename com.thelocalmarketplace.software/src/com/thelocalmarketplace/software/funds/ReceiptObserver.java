package com.thelocalmarketplace.software.funds;

import java.util.ArrayList;

import com.jjjwelectronics.Item;

public interface ReceiptObserver {
	
	default public void receiptPrinted(ArrayList<Item> Order) {}
}
