package com.thelocalmarketplace.software.test;

import com.jjjwelectronics.bag.AbstractReusableBagDispenser;
import com.jjjwelectronics.bag.IReusableBagDispenser;

public class mockReusableBagDispenser extends AbstractReusableBagDispenser implements IReusableBagDispenser {
    private int quantityRemaining;
    private int capacity;


    public mockReusableBagDispenser(int quantityRemaining, int capacity) {
        super(); 
    	this.quantityRemaining = quantityRemaining;
        this.capacity = capacity;
    }

    
    @Override
    public int getQuantityRemaining() {
        return quantityRemaining;
    }

    @Override
    public int getCapacity() {
    	return capacity;
    }
    
}