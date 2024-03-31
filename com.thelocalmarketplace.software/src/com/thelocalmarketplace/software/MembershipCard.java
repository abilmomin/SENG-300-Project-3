package com.thelocalmarketplace.software;

import com.jjjwelectronics.card.Card;

public class MembershipCard {

	private String membershipNumber;
	private String memberName;
	private Card membershipCard;
	
	public void createNewMember(String membershipNumber, String memberName) {
		if (isMembershipNumberValid(membershipNumber)) {
			this.membershipNumber = membershipNumber; 
		}
		this.memberName = memberName;
	}
	
	public String getMembershipNumber() {
		return membershipNumber;
	}
	
	public boolean isMembershipNumberValid(String membershipNumber) {
		String num = "\\d{10}";
		return membershipNumber.matches(num);
	}
	
	public String getMemberName() {
		return memberName;
	}
	
	public void createMembershipCard(String cardType, String number, String cardholder, String cvv, String pin, boolean isTapEnabled, boolean hasChip) {
		this.membershipCard = new Card(cardType, number, cardholder, cvv, pin, isTapEnabled, hasChip);
		this.membershipNumber = number;
		this.memberName = cardholder;
	}
	public Card getMemberShipCard() {
		return membershipCard;
	}
	
	
	
	
	
	
}