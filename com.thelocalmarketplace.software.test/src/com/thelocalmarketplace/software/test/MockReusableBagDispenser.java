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

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.bag.AbstractReusableBagDispenser;
import com.jjjwelectronics.bag.IReusableBagDispenser;
import com.jjjwelectronics.bag.ReusableBag;

public class MockReusableBagDispenser extends AbstractReusableBagDispenser implements IReusableBagDispenser {
    private int quantityRemaining;
    private int capacity;

    public MockReusableBagDispenser(int quantityRemaining, int capacity) {
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
        if (bags.length + quantityRemaining > capacity) {
            throw new OverloadedDevice("You have tried to stuff the dispenser with too many bags");
        }
        quantityRemaining += bags.length;
    }
    
    @Override
    public ReusableBag dispense() throws EmptyDevice {
    	if (quantityRemaining == 0) {
            throw new EmptyDevice("Dispenser is empty");
        }
        quantityRemaining--;
        return new ReusableBag();
    }
}