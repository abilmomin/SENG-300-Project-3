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


package com.thelocalmarketplace.software;

import java.util.Random;
import com.jjjwelectronics.card.Card;

/**
 * The MembershipCard class manages membership operations, including member registration, 
 * validation, and issuance of membership cards.
 */
public class MembershipCard {

	private String membershipNumber;
	private String memberName;
	private Card membershipCard;
	
	/**
	 * Allows a customer to become a member by
	 * creating a new membership number for the customer.
	 * 
	 * @param memberName
	 * 			The name of the customer as a string
	 * @return The membership number for the customer as a string
	 */
	public String createNewMember(String memberName) {
		Random random = new Random();
        long randomNumber = (long) (random.nextDouble() * 9_000_000_000L) + 1_000_000_000L;
        String membershipNumber = Long.toString(randomNumber);
		this.memberName = memberName;
		this.membershipNumber = membershipNumber;
		return membershipNumber;
	}
	
	/**
	 * Get the membership number.
	 * 
	 * @return The membership number as a string.
	 */
	public String getMembershipNumber() {
		return membershipNumber;
	}
	
	/**
	 * Check if the number that has been input is valid
	 * 
	 * @param membershipNumber
	 * 			The membership number that has been input
	 * @return
	 * 			A boolean value. True if it matches (valid), False otherwise
	 */
	public boolean isMembershipNumberValid(String membershipNumber) {
		String num = "\\d{10}";
		return membershipNumber.matches(num);
	}
	
	/**
	 * Get the name of the member.
	 * 
	 * @return The name of the member as a string.
	 */
	public String getMemberName() {
		return memberName;
	}
	
	/**
	 * Create a membership card for a customer.
	 * 
	 * @param cardType
	 * 			The type of card as a string.
	 * @param number
	 * 			The number on the card as a string.
	 * @param cardholder
	 * 			The name of the card holder as a string.
	 * @param cvv
	 * 			The cvv of the card as a string.
	 * @param pin
	 * 			The card pin as a string.
	 * @param isTapEnabled
	 * 			If tap is enabled as a boolean value.
	 * @param hasChip
	 * 			If the card has a chip as a boolean value.
	 */
	public void createMembershipCard(String cardType, String number, String cardholder, String cvv, String pin, boolean isTapEnabled, boolean hasChip) {
		this.membershipCard = new Card(cardType, number, cardholder, cvv, pin, isTapEnabled, hasChip);
		this.membershipNumber = number;
		this.memberName = cardholder;
	}
	
	/**
	 * Get the membership card.
	 * 
	 * @return The membership card as type Card.
	 */
	public Card getMemberShipCard() {
		return membershipCard;
	}
}
