package com.thelocalmarketplace.software.oldCode;

import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

public class mockAttendant {

   
    private Order order;
	private AbstractElectronicScale scale; 
	private double weight_of_bag;
	private AddOwnBag instance;
	
	SelfCheckoutStationSoftware order_testt;
	double test_order_Weight = order_testt.getTotalOrderWeightInGrams();
	
/** In the constructor pass in order scale and the bag weight and we create an instance of the addownbag class
 * @param order2
 * @param scale
 * @param weight_of_bag
 */
	public mockAttendant(double test_order_Weight, AbstractElectronicScale scale, double weight_of_bag) {
    	this.test_order_Weight = test_order_Weight;
    	this.scale = scale;
    	this.weight_of_bag = weight_of_bag;
    	
    	instance = new AddOwnBag(order_testt,scale);
        
    }

    
	/** method that prints a message to notify to the attendant to some to station */
    public void notifyAttendant() {
        System.out.println("Attendant come here");
        
    }

    /** here the attendant approves the situation and set station block to false so the uesr can continue
     *  then call the the instance of add bag weight and print the message*/
    public void Attendant_approves() {
        // here the bag is approved
    	WeightDiscrepancy.setStationBlock(false);
    	instance.addbagweight(order_testt,scale,weight_of_bag);
    	instance.print_mess();	
        
    }
    
    /** here the attendant fixes the problem, then sets the station to false and call the message 
     * so that now the user can continue 
     */
    public void Attendant_corrected_problem() {
        // here the bag is approved
    	WeightDiscrepancy.setStationBlock(false);
    	instance.addbagweight(order_testt,scale,weight_of_bag);
    	instance.print_mess();
    }
}