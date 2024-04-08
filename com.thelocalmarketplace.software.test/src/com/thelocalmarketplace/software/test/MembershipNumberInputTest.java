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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

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