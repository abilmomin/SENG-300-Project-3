package com.thelocalmarketplace.software.test;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.jjjwelectronics.card.Card;
import com.thelocalmarketplace.software.MembershipCard;

public class MembershipNumberInputTest {
	private MembershipCard membershipCard;
	
	@Before
	public void setUp() {
		membershipCard = new MembershipCard();
	}
	
	@Test
    public void createsSuccessfulMembershipCard() {
        String cardType = "Membership";
        String number = "1234567890";
        String cardholder = "Samantha";
        String cvv = "101";
        String pin = "1234";
        boolean isTapEnabled = true;
        boolean hasChip = true;

        membershipCard.createMembershipCard(cardType, number, cardholder, cvv, pin, isTapEnabled, hasChip);
        Card card = membershipCard.getMemberShipCard();

        assertNotNull(card);
        assertEquals(cardType, card.kind);
        assertEquals(number, card.number);
        assertEquals(cardholder, card.cardholder);
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
	
	@Test
    public void assignsValidMembershipNumber() {
        String memberName = "Samantha";
        String membershipNumber = membershipCard.createNewMember(memberName);

        assertEquals(memberName, membershipCard.getMemberName());
        assertEquals("Membership number should be 10 digits long", 10, membershipNumber.length());
        assertTrue("Membership number should contain only digits", membershipNumber.matches("\\d{10}"));
        assertNotNull(membershipNumber);
    }
	
	@Test
	public void returnsCorrectMembershipNumber() {
		String expectedMembershipNumber = membershipCard.createNewMember("Test");
		String membershipNumber = membershipCard.getMembershipNumber();
		assertNotNull(membershipNumber);
		assertEquals(membershipNumber, expectedMembershipNumber);
		
	}
	
	
}

