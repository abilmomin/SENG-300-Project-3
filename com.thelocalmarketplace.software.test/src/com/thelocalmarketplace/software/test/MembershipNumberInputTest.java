package com.thelocalmarketplace.software.test;

import javax.swing.JFrame;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.thelocalmarketplace.software.MembershipCard;

public class MembershipNumberInputTest {
	private MembershipCard membershipCard;
	
	@Before
	public void setUp() {
		membershipCard = new MembershipCard();
	}
	
	@Test
	public void isMembershipValid() {
		assertTrue(membershipCard.isMembershipNumberValid("1234567890"));
	}
	
	@Test
	public void isMembershipInvalid() {
		assertFalse(membershipCard.isMembershipNumberValid("12345"));
		assertFalse(membershipCard.isMembershipNumberValid("abc"));
	}
}

