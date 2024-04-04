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
