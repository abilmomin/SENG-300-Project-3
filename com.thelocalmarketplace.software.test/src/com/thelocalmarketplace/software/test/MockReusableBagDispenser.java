package com.thelocalmarketplace.software.test;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.bag.AbstractReusableBagDispenser;
import com.jjjwelectronics.bag.IReusableBagDispenser;
import com.jjjwelectronics.bag.ReusableBag;

public class MockReusableBagDispenser extends AbstractReusableBagDispenser implements IReusableBagDispenser {
    private int quantityRemaining;
    private int capacity;


    public MockReusableBagDispenser(int quantityRemaining, int capacity) {
       // super(); 
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
    @Override
    public void load(ReusableBag... bags) throws OverloadedDevice {
        // Implement the load method to handle loading bags
        // You can add your logic here to handle the load method in the mock
        // For example, you can throw OverloadedDevice if the number of bags exceeds the capacity
        if (bags.length + quantityRemaining > capacity) {
            throw new OverloadedDevice("You have tried to stuff the dispenser with too many bags");
        }
        quantityRemaining += bags.length;
    }
    
    //@Override
    //public ReusableBag[] unload() {
        // Implementation of unload method
       // return null;
   // }
    
    @Override
    public ReusableBag dispense() throws EmptyDevice {
        // Implementation of dispense method
    	if (quantityRemaining == 0) {
            throw new EmptyDevice("Dispenser is empty");
        }
        quantityRemaining--;
        return new ReusableBag();
    }

}
