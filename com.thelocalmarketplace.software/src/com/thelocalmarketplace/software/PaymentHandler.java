/**
 * Yotam Rojnov (UCID: 30173949)
 * Duncan McKay (UCID: 30177857)
 * Mahfuz Alam (UCID:30142265)
 * Luis Trigueros Granillo (UCID: 30167989)
 * Lilia Skumatova (UCID: 30187339)
 * Abdelrahman Abbas (UCID: 30110374)
 * Talaal Irtija (UCID: 30169780)
 * Alejandro Cardona (UCID: 30178941)
 * Alexandre Duteau (UCID: 30192082)
 * Grace Johnson (UCID: 30149693)
 * Abil Momin (UCID: 30154771)
 * Tara Ghasemi M. Rad (UCID: 30171212)
 * Izabella Mawani (UCID: 30179738)
 * Binish Khalid (UCID: 30061367)
 * Fatima Khalid (UCID: 30140757)
 * Lucas Kasdorf (UCID: 30173922)
 * Emily Garcia-Volk (UCID: 30140791)
 * Yuinikoru Futamata (UCID: 30173228)
 * Joseph Tandyo (UCID: 30182561)
 * Syed Haider (UCID: 30143096)
 * Nami Marwah (UCID: 30178528)
 */

package com.thelocalmarketplace.software;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.Item;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.card.AbstractCardReader;
import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.card.Card.CardData;
import com.jjjwelectronics.card.CardReaderBronze;
import com.jjjwelectronics.card.CardReaderGold;
import com.jjjwelectronics.card.CardReaderSilver;
import com.jjjwelectronics.card.MagneticStripeFailureException;
import com.jjjwelectronics.printer.ReceiptPrinterBronze;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.NoCashAvailableException;
import com.tdc.banknote.Banknote;
import com.tdc.coin.Coin;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedItem;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.hardware.external.CardIssuer;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import powerutility.PowerGrid;

/**
 * Manages the payment process with coins for a self-checkout system.
 * Handles coin insertion, validation, and change dispensing.
 */
public class PaymentHandler {

	public BigDecimal amountSpent;
	public BigDecimal changeRemaining = BigDecimal.ZERO;
	public BigDecimal totalPrice = new BigDecimal(0);
	public BigDecimal amountInserted;
	private AbstractSelfCheckoutStation checkoutSystem = null;
	private ArrayList<Item> allItemOrders;
	private ReceiptPrinterBronze printerBronze;

	private Order order; // Represents the customer order
	// Consider adapting the other methods to reflect this global variable.
	
	public PaymentHandler(AbstractSelfCheckoutStation station, Order order) throws EmptyDevice, OverloadedDevice {
		if (station == null)
			throw new NullPointerException("No argument may be null.");
		if (station instanceof SelfCheckoutStationBronze)
			this.checkoutSystem = (SelfCheckoutStationBronze) station;
		else if (station instanceof SelfCheckoutStationSilver)
			this.checkoutSystem = (SelfCheckoutStationSilver) station;
		else if (station instanceof SelfCheckoutStationGold)
			this.checkoutSystem = (SelfCheckoutStationGold) station;
		this.allItemOrders = order.getOrder();
		this.totalPrice = BigDecimal.valueOf(order.getTotalPrice());
		this.printerBronze = new ReceiptPrinterBronze();
		this.printerBronze.plugIn(PowerGrid.instance());
		this.printerBronze.turnOn();
		this.printerBronze.addInk(ReceiptPrinterBronze.MAXIMUM_INK);
		this.printerBronze.addPaper(ReceiptPrinterBronze.MAXIMUM_PAPER);

		this.order = order;
	}

	public AbstractSelfCheckoutStation getStation() {
		return checkoutSystem;
	}

	/**
	 * will be used to help with Signaling to the Customer the updated amount
	 * due after the insertion of each coin.
	 *
	 * @return money left to pay
	 */
	public BigDecimal getChangeRemaining() {
		return this.changeRemaining;
	}


	/**
	 * Processes payment using coins inserted by the customer.
	 *
	 * @param coinsList List of coins inserted by the customer.
	 * @return true if payment is successful, false otherwise.
	 * @throws DisabledException        If the coin slot is disabled.
	 * @throws CashOverloadException    If the cash storage is overloaded.
	 * @throws NoCashAvailableException If no cash is available for dispensing
	 *                                  change.
	 * @throws OverloadedDevice
	 * @throws EmptyDevice
	 */
	public boolean processPaymentWithCoins(ArrayList<Coin> coinsList)
			throws DisabledException, CashOverloadException, NoCashAvailableException, EmptyDevice, OverloadedDevice {
		if (SelfCheckoutStationSoftware.getStationBlock()) {
			System.out.println("Blocked. Please add your item to the bagging area.");
			return false;
		}

		if (coinsList == null)
			throw new NullPointerException("coinsList cannot be null."); // Check for null parameters.

		BigDecimal value = new BigDecimal("0");
		for (Coin coin : coinsList) { // Calculate the total value of coins inserted.
			acceptInsertedCoin(coin);
			value = value.add(coin.getValue());
		}


		if (value.compareTo(this.totalPrice) < 0)
			return false; // Return false if the total value of valid coins is less than the total cost.

			// Return true if accurate change is dispensed.
		else{
			BigDecimal changeValue = value.subtract(this.totalPrice);
			if(dispenseAccurateChange(changeValue)) {
				this.changeRemaining = changeValue;
				this.amountSpent = this.totalPrice;
				return true;
			}
			else
				return false;
		}
	}

	public boolean processPaymentWithBanknotes(ArrayList<Banknote> Banknotes)
			throws DisabledException, CashOverloadException, NoCashAvailableException, EmptyDevice, OverloadedDevice {

		if (SelfCheckoutStationSoftware.getStationBlock()) {
			System.out.println("Blocked. Please add your item to the bagging area.");
			return false;
		}

		// first check if parameter is null or not
		if (Banknotes == null) {
			throw new NullPointerException("Banknotes cannot be null.");
		}
		//moved instances of BanknoteValidator and BanknoteInsertion
		BigDecimal valueOfAllAcceptedBanknotes = new BigDecimal("0");
		for (Banknote banknote : Banknotes) { // Calculate the total value of coins inserted.
			acceptInsertedBanknote(banknote);
			valueOfAllAcceptedBanknotes = valueOfAllAcceptedBanknotes.add(banknote.getDenomination());
		}

		//checks if the amount that was accepted is enough to make total cost go to 0 meaning that there was enough money to be
		//paid if not then return false ,s they will need to pay again
		if(valueOfAllAcceptedBanknotes.compareTo(this.totalPrice) < 0){
			return false;
		}// i need to return change

		//if value is equal or greater then cost
		// have to calculate the change value

		else{
			BigDecimal changeValue = valueOfAllAcceptedBanknotes.subtract(this.totalPrice);
			if(dispenseAccurateChange(changeValue)) {
				this.changeRemaining = changeValue;
				this.amountSpent = this.totalPrice;
				return true;
			}
			else
				return false;
		}
	}

	/**
	 * Dispenses the correct amount of change to the customer and gives them the
	 * choice to print a receipt.
	 *
	 * Implements change dispensing logic using available coin denominations.
	 *
	 * @param changeValue The amount of change to be dispensed.
	 * @return true if correct change is dispensed, false otherwise.
	 * @throws DisabledException        If the coin slot is disabled.
	 * @throws CashOverloadException    If the cash storage is overloaded.
	 * @throws NoCashAvailableException If no cash is available for dispensing
	 *                                  change.
	 * @throws OverloadedDevice
	 * @throws EmptyDevice
	 */
	public boolean dispenseAccurateChange(BigDecimal changeValue)
			throws DisabledException, CashOverloadException, NoCashAvailableException, EmptyDevice, OverloadedDevice {

		BigDecimal amountDispensed = new BigDecimal("0.0");
		BigDecimal remainingAmount = changeValue;
		List<BigDecimal> coinDenominations = this.checkoutSystem.coinDenominations;
		Collections.sort(coinDenominations);
		Collections.reverse(coinDenominations);
		List<BigDecimal> bankNoteDenominations = Arrays.stream(this.checkoutSystem.banknoteDenominations)
				.collect(Collectors.toList());
		Collections.sort(bankNoteDenominations);
		Collections.reverse(bankNoteDenominations);

		// This approach aims to find the optimal combination of denominations to minimize the
		// number of banknotes and coins used while considering the limited availability of
		// each denomination.
		while (remainingAmount.compareTo(BigDecimal.ZERO) > 0) {
			System.out.println("HELP");
			// If neither banknotes nor coins can be used, break the loop
			BigDecimal lowestCoin = coinDenominations.get(coinDenominations.size() - 1);
			BigDecimal lowestBankNote = bankNoteDenominations.get(bankNoteDenominations.size() - 1);
			BigDecimal lowestVal = lowestCoin.min(lowestBankNote);
			if (remainingAmount.compareTo(lowestVal) < 0 && remainingAmount.compareTo(BigDecimal.ZERO) > 0) {
				this.checkoutSystem.coinDispensers.get(lowestVal).emit();
				amountDispensed = changeValue;
				remainingAmount = BigDecimal.ZERO;
				break;
			}
			
			boolean dispensed = false;
			// Try using banknotes first
			for (BigDecimal bankNote : bankNoteDenominations) {
				if (remainingAmount.compareTo(bankNote) >= 0 && checkoutSystem.banknoteDispensers.get(bankNote).size() > 0) {
					checkoutSystem.banknoteDispensers.get(bankNote).emit();
					amountDispensed = amountDispensed.add(bankNote);
					remainingAmount = remainingAmount.subtract(bankNote);
					dispensed = true;
					break;
				}
			}

			// If no banknotes are available or insufficient, try using coins
			if (!dispensed) {
				for (BigDecimal coin : coinDenominations) {
					if (remainingAmount.compareTo(coin) >= 0 && checkoutSystem.coinDispensers.get(coin).size() > 0) {
						System.out.println(checkoutSystem.coinDispensers.get(coin).size());
						System.out.println(coin);
						checkoutSystem.coinDispensers.get(coin).emit();
						amountDispensed = amountDispensed.add(coin);
						remainingAmount = remainingAmount.subtract(coin);
						dispensed = true;
						break;
					}
				}
			}
			if(!dispensed)
				break;
		}

		return remainingAmount.compareTo(BigDecimal.ZERO) == 0;
//		if (remainingAmount.compareTo(BigDecimal.ZERO) == 0) {
//			Scanner receiptRequest = new Scanner(System.in);
//			System.out.println("Would you like a receipt?"); // Asks the user for a receipt
//			String receiptAnswer = receiptRequest.nextLine();
//			while (receiptAnswer.compareToIgnoreCase("yes") != 0 || receiptAnswer.compareToIgnoreCase("no") != 0) {
//				System.out.println("Sorry, that input is not acceptable. Try again."); // Keeps prompting user for receipt until "yes" or "no" answer
//				System.out.println("Would you like a receipt?");
//				receiptAnswer = receiptRequest.nextLine();}
//			if (receiptAnswer.compareToIgnoreCase("yes") == 0) { // If yes, receiptPrinter and thank user
//				receiptPrinter(this.order);
//				System.out.println("Thank you for your time. We hope to see you again!");
//				return true;}
//			if (receiptAnswer.compareToIgnoreCase("no") == 0) { // If no, thank user
//				System.out.println("No worries. Thank you for your time. We hope to see you again!");
//				return true;}}
//		return false;

	}

	/**
	 * Accepts a coin inserted by the customer into the coin slot.
	 *
	 * @param coin The coin to be validated and accepted.
	 * @return true if the coin is successfully accepted, false otherwise.
	 * @throws DisabledException     If the coin slot is disabled.
	 * @throws CashOverloadException If the cash storage is overloaded.
	 */
	public void acceptInsertedCoin(Coin coin) throws DisabledException, CashOverloadException {
		if(this.checkoutSystem.coinStorage.hasSpace()) {
			this.checkoutSystem.coinSlot.enable();
			this.checkoutSystem.coinSlot.receive(coin);
		}
		else {
			this.checkoutSystem.coinSlot.disable();
		}
	}

	/**
	 * Accepts a banknote inserted by the customer into the banknote slot
	 * @param banknote to be validated and accepted
	 * @return true if the banknote is successfully accepted, false otherwise
	 * @throws DisabledException if the banknote slot is disabled.
	 * @throws CashOverloadException if the banknote storage is overloaded
	 */
	public void acceptInsertedBanknote(Banknote banknote) throws DisabledException, CashOverloadException {
		if(this.checkoutSystem.banknoteInput.hasDanglingBanknotes()) {
			this.checkoutSystem.banknoteInput.removeDanglingBanknote();
		}
		if(this.checkoutSystem.banknoteStorage.hasSpace()) {
			this.checkoutSystem.banknoteInput.enable();
			this.checkoutSystem.banknoteInput.receive(banknote);
		}
		else {
			this.checkoutSystem.banknoteInput.disable();
		}
	}

	/**
	 * Prints a receipt for the customer, with all the products' info, price, the
	 * total cost, total amount paid, and change due.
	 * @throws OverloadedDevice
	 * @throws EmptyDevice
	 */
	public String receiptPrinter(Order order) throws EmptyDevice, OverloadedDevice {

		ArrayList<String> receiptItems = new ArrayList<String>();

		for (int i = 0; i < order.getOrder().size(); i++) {
			String productDescription;
			Item item = order.getOrder().get(i);

			if (item instanceof BarcodedItem) { // Gets the product description and the price of a barcoded product
				BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(((BarcodedItem) item).getBarcode());
				productDescription = product.getDescription();
				long price = product.getPrice();
				receiptItems.add(productDescription + " $" + String.format("%.2f", (float)price));
			}

			else if (item instanceof PLUCodedItem) { // Gets the product description and the price of a product inputted
				// through price-lookup (PLU)
				PLUCodedProduct product = ProductDatabases.PLU_PRODUCT_DATABASE.get(((PLUCodedItem) item).getPLUCode());
				productDescription = product.getDescription();
				long price = product.getPrice();
				receiptItems.add(productDescription + " $" + String.format("%.2f", (float)price));
			}
			else {
				throw new NullPointerException("This product is not a supported product, can not be registered for a price");
			}
		}

		BigDecimal purchaseValue = new BigDecimal(String.valueOf(order.getTotalPrice()));
		BigDecimal amountPaid = amountSpent;
		BigDecimal changeDue = changeRemaining;


		receiptItems.add("Total: $" + String.format("%.2f", purchaseValue));
		receiptItems.add("Paid: $" + String.format("%.2f", amountPaid));
		receiptItems.add("Change: $" + String.format("%.2f", changeDue));

		for (int i = 0; i < receiptItems.size(); i++) {
			this.printerBronze.print('\n');
			
			for (int j = 0; j < receiptItems.get(i).length(); j++) {
				this.printerBronze.print(receiptItems.get(i).charAt(j));
			}
		}

		this.printerBronze.cutPaper();
		return this.printerBronze.removeReceipt();
	}


	/**
	 * Loads coins into the coin dispensers for change.
	 *
	 * @param coins Coins to be loaded into the dispensers.
	 * @throws CashOverloadException If the coin dispensers are overloaded with
	 *                               coins.
	 */
	public void loadCoinDispenser(Coin... coins) throws CashOverloadException {
		if (coins == null) {
			throw new NullPointerSimulationException("coins instance cannot be null.");
		}
		for (Coin c : coins) {
			if (c == null) {
				throw new NullPointerSimulationException("coin instance cannot be null.");
			}
			BigDecimal v = c.getValue();
			try {
				this.checkoutSystem.coinDispensers.get(v).load(c);
			} catch (CashOverloadException e) {
				throw new CashOverloadException("Coin Dispenser for coins of value " + v.doubleValue() + " is full.");
			} catch (NullPointerException e) {
				throw new NullPointerException("This coin type does not exist.");
			}
		}
	}

	/**
	 * Loads banknotes into the banknote dispensers for change.
	 *
	 * @param banknotes Banknote to be loaded into the dispensers.
	 * @throws CashOverloadException If the banknote dispensers are overloaded with
	 *                               banknotes.
	 */
	public void loadBanknoteDispenser(Banknote ...banknotes) throws CashOverloadException {
		if (banknotes == null) {
			throw new NullPointerSimulationException("coins instance cannot be null.");
		}
		for (Banknote b : banknotes) {
			if (b == null) {
				throw new NullPointerSimulationException("coin instance cannot be null.");
			}
			BigDecimal v = ((Banknote) b).getDenomination();
			try {
				this.checkoutSystem.banknoteDispensers.get(v).load(b);
			} catch (CashOverloadException e) {
				throw new CashOverloadException("BankNote Dispenser for banknote of value " + v.doubleValue() + " is full.");
			} catch (NullPointerException e) {
				throw new NullPointerException("This banknote type does not exist.");
			}
		}
	}


	/**
	 * Processes a credit card payment via swipe.
	 *
	 * @param card         The credit card to be used for payment.
	 * @param amountCharged The amount to be charged to the credit card.
	 * @param cardIssuer   The card issuer responsible for authorizing the transaction.
	 * @throws IOException          If an I/O error occurs.
	 * @throws EmptyDevice          If the checkout station device is empty.
	 * @throws OverloadedDevice     If the checkout station device is overloaded.
	 */
	public int payWithCreditViaSwipe(Card card, double amountCharged, CardIssuer cardIssuer) throws IOException {
		try {
			if (SelfCheckoutStationSoftware.getStationBlock()) {
				System.out.println("Blocked. Please add your item to the bagging area.");
				return -1;
			}

			AbstractCardReader cardReader = null;

			if (checkoutSystem instanceof SelfCheckoutStationBronze) {
				cardReader = new CardReaderBronze();
			}
			else if (checkoutSystem instanceof SelfCheckoutStationSilver) {
				cardReader = new CardReaderSilver();
			}
			else if (checkoutSystem instanceof SelfCheckoutStationGold) {
				cardReader = new CardReaderGold();
			}
			cardReader.plugIn(PowerGrid.instance());
			cardReader.turnOn();
			CardData data = cardReader.swipe(card);

			long holdNumber = cardIssuer.authorizeHold(data.getNumber(), amountCharged);
			if (holdNumber == -1) {
				// HOLD FAILED
				System.out.println("The hold on the card failed. Please try again.");
				return -1;
			}
			boolean transaction = cardIssuer.postTransaction(data.getNumber(), holdNumber, amountCharged);
			if (!transaction) {
				// TRANSACTION FAILED
				cardIssuer.releaseHold(data.getNumber(), holdNumber); // Remove the hold.
				System.out.println("The transaction failed. Please try again.");
				return -1;
			}
			order.removeTotalPrice((long) amountCharged); // Update the total amount due to the customer
			amountSpent = BigDecimal.valueOf(amountCharged);
			changeRemaining = BigDecimal.ZERO;
			// Receipt printing is handled inside the demo
			return 1;
		} catch (MagneticStripeFailureException msfe) {
			System.out.println("Card Swipe failed, please try again!");
			return -1;
		}
	}


	/**
	 * Processes a debit card payment via swipe.
	 *
	 * @param card         The debit card to be used for payment.
	 * @param amountCharged The amount to be charged to the debit card.
	 * @param cardIssuer   The card issuer responsible for authorizing the transaction.
	 * @throws IOException          If an I/O error occurs.
	 * @throws EmptyDevice          If the checkout station device is empty.
	 * @throws OverloadedDevice     If the checkout station device is overloaded.
	 */
	public int payWithDebitViaSwipe(Card card, double amountCharged, CardIssuer cardIssuer) throws IOException {
		try {
			if (SelfCheckoutStationSoftware.getStationBlock()) {
				System.out.println("Blocked. Please add your item to the bagging area.");
				return -1;
			}

			AbstractCardReader cardReader = null;

			if (checkoutSystem instanceof SelfCheckoutStationBronze) {
				cardReader = new CardReaderBronze();
			}
			else if (checkoutSystem instanceof SelfCheckoutStationSilver) {
				cardReader = new CardReaderSilver();
			}
			else if (checkoutSystem instanceof SelfCheckoutStationGold) {
				cardReader = new CardReaderGold();
			}
			cardReader.plugIn(PowerGrid.instance());
			cardReader.turnOn();
			CardData data = cardReader.swipe(card);

			long holdNumber = cardIssuer.authorizeHold(data.getNumber(), amountCharged);
			if (holdNumber == -1) {
				// HOLD FAILED
				System.out.println("The hold on the card failed. Please try again.");
				return -1;
			}
			boolean transaction = cardIssuer.postTransaction(data.getNumber(), holdNumber, amountCharged);
			if (!transaction) {
				// TRANSACTION FAILED
				cardIssuer.releaseHold(data.getNumber(), holdNumber); // Remove the hold.
				System.out.println("The transaction failed. Please try again.");
				return -1;
			}
			order.removeTotalPrice((long) amountCharged); // Update the total amount due to the customer
			amountSpent = BigDecimal.valueOf(amountCharged);
			changeRemaining = BigDecimal.ZERO;
			return 1;
			// Receipt printing is handled inside the demo
		} catch (MagneticStripeFailureException msfe) {
			System.out.println("Card Swipe failed, please try again!");
			return -1;
		}
	}
}
