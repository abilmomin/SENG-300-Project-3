package com.thelocalmarketplace.software;

import com.thelocalmarketplace.hardware.AttendantStation;

public class AttendantStationSoftware {
	private AttendantStation attendantStation;
	
	public AttendantStationSoftware (AttendantStation attendantStation) {
		this.attendantStation = attendantStation;
	}
	
	// monitor stations to see if banknote, coins, paper, ink levels are low/high
	// 		Attendant should be signaled if any of these have low levels while a customer is paying
	//		read the case descriptions there's too many to type here
	// enable/disable station
	// be notified when a customer needs assistance, clear the request once theyve been assisted
	// approve bulky item request 
	// approve no bagging item request
	// approve a weight disc 
}