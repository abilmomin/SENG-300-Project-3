package com.thelocalmarketplace.software.test;

import javax.swing.JFrame;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.thelocalmarketplace.software.communication.MembershipNumberInput;

public class MembershipNumberInputTest {
	private MembershipNumberInput dialog;
	private JFrame parent;
	
	@Before
	public void setUp() {
		parent = new JFrame();
		dialog = new MembershipNumberInput(parent);
		dialog.frameInit();
	}
	
	@Test
	public void testMembershipNumberDialogAppears() {
		assertNotNull(dialog.getMembershipNumberField());
	}
}

