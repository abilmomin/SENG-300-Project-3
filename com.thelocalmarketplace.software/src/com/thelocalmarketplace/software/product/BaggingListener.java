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

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.bag.ReusableBagDispenserListener;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

public class BaggingListener implements ReusableBagDispenserListener {
	// In order to access the hardware of the SelfCheckoutStation, use software.getHARDWARE_YOU_WANNA_GET()
	
	private SelfCheckoutStationSoftware software;
	private ProductHandler handler;
	
	public BaggingListener(SelfCheckoutStationSoftware software, ProductHandler handler) {
		this.software = software;
		this.handler = handler;	
	}

	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aBagHasBeenDispensedByTheDispenser() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void theDispenserIsOutOfBags() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bagsHaveBeenLoadedIntoTheDispenser(int count) {
		// TODO Auto-generated method stub
		
	}

}
