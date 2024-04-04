package com.thelocalmarketplace.software.test;

import javax.swing.JFrame;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.thelocalmarketplace.software.communication.CustomerStation;
import com.thelocalmarketplace.software.communication.MembershipNumberInput;

public class MembershipNumberInputTest {
	private MembershipNumberInput dialog;
	
	@Before
	public void setUp() {
		CustomerStation customerStation = new CustomerStation(1);
	    dialog = new MembershipNumberInput(customerStation);
		dialog.frameInit();
	}
	
	@Test
	public void testMembershipNumberDialogAppears() {
		assertNotNull(dialog.getMembershipNumberField());
	}
	
	@Test
	public void testValidMembershipNumber() {
		
	}
}

