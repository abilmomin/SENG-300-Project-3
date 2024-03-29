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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.jjjwelectronics.scale.ElectronicScaleBronze;
import com.jjjwelectronics.scale.ElectronicScaleGold;
import com.jjjwelectronics.scale.ElectronicScaleSilver;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.tdc.banknote.Banknote;
import com.tdc.banknote.IBanknoteDispenser;
import com.tdc.coin.Coin;
import com.tdc.coin.ICoinDispenser;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.hardware.external.CardIssuer;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

import powerutility.PowerGrid;


/**
 * This class is a Demo of the functions created in software.
 * Is extra, not required for the assignment, but the professor recommended it.
 */
public class DemoIterationTwo {

    public static void main(String[] args) {
        // Represents user input
        Scanner input = new Scanner(System.in);

        // Initialize station, scale, and denominations
        AbstractSelfCheckoutStation station = null;
        AbstractElectronicScale scale = null;
        BigDecimal[] denominations = {BigDecimal.valueOf(3), BigDecimal.valueOf(5), BigDecimal.valueOf(1)};

        // Configure all the selfcheckoutstation fields
        AbstractSelfCheckoutStation.configureCurrency(Currency.getInstance(Locale.CANADA));
        AbstractSelfCheckoutStation.configureBanknoteDenominations(denominations);
        AbstractSelfCheckoutStation.configureBanknoteStorageUnitCapacity(10);
        AbstractSelfCheckoutStation.configureCoinDenominations(denominations);
        AbstractSelfCheckoutStation.configureCoinStorageUnitCapacity(10);
        AbstractSelfCheckoutStation.configureCoinTrayCapacity(10);
        AbstractSelfCheckoutStation.configureCoinDispenserCapacity(10);

        // Create all fields related to bank and card
        CardIssuer cardIssuer = new CardIssuer("Seng300 Bank", 10);
        Card creditCard = new Card("Credit", "21", "Holder1", "211");
        Card debitCard = new Card("Debit", "12", "Holder2", "122");
        Calendar expiry = Calendar.getInstance();
        expiry.add(Calendar.YEAR, 5);

        // Add each card's data into the cardIssuer
        cardIssuer.addCardData("21", "Holder1", expiry, "211", 2000);
        cardIssuer.addCardData("12", "Holder2", expiry, "122", 2000);

        // User input for which self-checkout station user wants.
        System.out.println("Enter '1' for gold system.");
        System.out.println("Enter '2' for silver system.");
        System.out.println("Enter '3' for bronze system.");

        int stationType = input.nextInt();
        input.nextLine();

        // Initialize the station and scale based on user input
        switch (stationType) {
            case 1:
                station = new SelfCheckoutStationGold();
                scale = new ElectronicScaleGold();
                break;
            case 2:
                station = new SelfCheckoutStationSilver();
                scale = new ElectronicScaleSilver();
                break;
            case 3:
                station = new SelfCheckoutStationBronze();
                scale = new ElectronicScaleBronze();
                break;
        }

        // Activate and get power to each necessary component of the self-checkout station
        PowerGrid.engageUninterruptiblePowerSource();
        station.plugIn(PowerGrid.instance());
        station.coinStorage.activate();
        station.coinSlot.activate();
        station.coinValidator.activate();
        station.banknoteStorage.activate();
        station.banknoteValidator.activate();
        station.banknoteInput.activate();

        // activate scale
        scale.plugIn(PowerGrid.instance());
        scale.turnOn();
        scale.enable();

        for (Map.Entry<BigDecimal, ICoinDispenser> entry : station.coinDispensers.entrySet()) {
            entry.getValue().activate();
        }
        for (Entry<BigDecimal, IBanknoteDispenser> entry : station.banknoteDispensers.entrySet()) {
            entry.getValue().activate();
        }


        SelfCheckoutStationSoftware software = new SelfCheckoutStationSoftware();

        try {

            // Starts a new session.
            software.startSession(input);

            // Create order and addItemViaBarcode scan objects
            Order order = new Order(scale);

            // Create barcodes for two items, an apple and banana
            Numeral[] list = {Numeral.valueOf((byte) 5), Numeral.valueOf((byte) 5)};
            Numeral[] list1 = {Numeral.valueOf((byte) 7), Numeral.valueOf((byte) 4)};
            Barcode barcodeOfApple = new Barcode(list);
            Barcode barcodeOfBanana = new Barcode(list1);

            // Create a string to hold user input.
            String itemInput;

            // Makes barcoded product for apple and banana
            BarcodedProduct apple = new BarcodedProduct(barcodeOfApple, "An apple", 5, 1.00);
            BarcodedProduct banana = new BarcodedProduct(barcodeOfApple, "A banana", 3, 2.00);

            ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcodeOfApple, apple);
            ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcodeOfBanana, banana);

            // Make an arraylist for the coins added
            ArrayList<Coin> coinsList = new ArrayList<>();


            BarcodedItem barcodedItem;
            Mass mass;
            double productWeight;

            label: // label is defined to mark the exit point of user interaction
            while (true) {
            	
            	// Edge case (checkout for payment is only available when the order has some item in it.
            	if (order.isEmpty() == false) {
                	System.out.println("Enter '0' to finish with your order.");
                	System.out.println("Enter '1' to scan an Apple.");
                    System.out.println("Enter '2' to scan a Banana.");
                    
                }
            	
            	else {
            		
            		// Initially this will be printed as we don't have any item to pay for
            		System.out.println("Enter '1' to scan an Apple.");
            		System.out.println("Enter '2' to scan a Banana.");
            		
            	}

                itemInput = input.nextLine();		// Prompts for user input
                BarcodedItem itemGot;
               
                // The user chooses what item they want and the item is added to the order.
                // The price of the item is recorded
                switch (itemInput) {
                    case "0":
                        if (!order.isEmpty()) { // checks if the order list is not empty if thats the case it will ask for payment option as well
                            break label;  // get out of the loop if the user is done with order else they can keep adding stuff
                        } else {
                            // Assuming we are on the first cycle of the order, and user hits 0, when the only options prompted are to add an item
                            // then, we can use condition to separate this case
                            System.out.println("Unable to process input. Please try again.");
                            break;
                        }

                    case "1":
                        itemGot = order.addItemViaBarcodeScan(barcodeOfApple);
                        scale.addAnItem(itemGot);
                        break;
                    case "2":
                        itemGot = order.addItemViaBarcodeScan(barcodeOfBanana);
                        scale.addAnItem(itemGot);
                        break;
                    default:
                        System.out.println("Unable to process input. Please try again.");    // again prompts for adding a produce to the order cart
                        break;
                }
                order.displayOrder();
            }

            System.out.println("Would you like to remove any items from your order? (Yes/No)");
            String removeAny = input.nextLine();

            if(removeAny.equalsIgnoreCase("Yes")) {
                BarcodedItem removed = order.signalToRemoveItemFromOrder(input);
                scale.removeAnItem(removed);
            }
            
            PaymentHandler paymentHandler = new PaymentHandler(station, order);
            boolean breakWhileLoop = true;

            // Handle all user input for payment options
            while (breakWhileLoop) {
            	
                System.out.println("Checkout completed. How would you like to pay?");
                System.out.println("1. Credit Card");
                System.out.println("2. Debit Card");
                System.out.println("3. Coin");
                System.out.println("4. Banknote");
                System.out.print("Enter your choice (1/2/3/4): ");

                int paymentChoice = input.nextInt();
                input.nextLine();

                switch (paymentChoice) {

                    // Handles payment with credit card
                    case 1:
                        System.out.println("You pay $" + order.getTotalPrice() + " with your credit card.");
                        if (paymentHandler.payWithCreditViaSwipe(creditCard, order.getTotalPrice(), cardIssuer) == -1) {
                            System.out.println("Unsuccessful Payment! Please try again.");
                            break;
                        }
                        System.out.println("Successful Payment! The total price is now $" + order.getTotalPrice() + ".");

                        breakWhileLoop = false;
                        break;

                    // Handles payment with debit card
                    case 2:
                        System.out.println("You pay $" + order.getTotalPrice() + " with your debit card.");
                        if (paymentHandler.payWithDebitViaSwipe(debitCard, order.getTotalPrice(), cardIssuer) == -1) {
                            System.out.println("Unsuccessful Payment! Please try again.");
                            break;
                        }

                        System.out.println("Successful Payment! The total price is now $" + order.getTotalPrice() + ".");

                        breakWhileLoop = false;
                        break;

                    // Handles payment with coin
                    case 3:
                        System.out.println("You have selected to pay with coins.");
                        System.out.println("You insert " + order.getTotalPrice() + " $1 coins.");

                        // Add 1 dollar coins to the coinsList
                        for (int i = 0; i < (int) order.getTotalPrice(); i++) {
                            Coin coin = new Coin(Currency.getInstance(Locale.CANADA), BigDecimal.valueOf(1));
                            coinsList.add(coin);
                        }

                        if (paymentHandler.processPaymentWithCoins(coinsList)) {
                            System.out.println("Payment Successful!");
                        } else {
                            System.out.println("Unsuccessful Payment! Please try again.");
                            break;
                        }

                        breakWhileLoop = false;
                        break;

                    // Handles payment with banknotes
                    case 4:
                        System.out.println("You have selected to pay with banknote.");
                        int numberOf5DollarBills = (int) order.getTotalPrice() / 5;
                        int numberOf1DollarBills = (int) order.getTotalPrice() % 5;
                        ArrayList<Banknote> banknotes = new ArrayList<>();
                        for (int i=0; i < numberOf5DollarBills; i++) {
                            Banknote banknote = new Banknote(Currency.getInstance(Locale.CANADA), BigDecimal.valueOf(5));
                            banknotes.add(banknote);
                        }
                        for (int i=0; i < numberOf1DollarBills; i++) {
                            Banknote banknote = new Banknote(Currency.getInstance(Locale.CANADA), BigDecimal.valueOf(1));
                            banknotes.add(banknote);
                        }
                        System.out.println("You insert " + numberOf5DollarBills + " $5 bills and " + numberOf1DollarBills + " $1 bills.");
                        if (paymentHandler.processPaymentWithBanknotes(banknotes)) {
                            System.out.println("Payment Successful!");
                        } else {
                            System.out.println("Unsuccessful Payment! Please try again.");
                            break;
                        }
                        breakWhileLoop = false;
                        break;

                        // Handles invalid input
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            } // End of payment while loop

            // Handles receipt prompt and print
            System.out.println("Would you like a receipt? (Yes/No)");
            String receiptChoice = input.nextLine();
            
            if (receiptChoice.equalsIgnoreCase("Yes")) {
                System.out.println(paymentHandler.receiptPrinter(order));
            }
            // Proper closure of resources and final messages can be added here
            System.out.println("Thank you for using the self-checkout system.");

        } catch (Exception e) {
            System.out.println("Failed to initialize order: " + e.getMessage());
            e.printStackTrace();
        } finally {

            // Cleanup and close.
            if (input != null) {
                input.close();
            }
            System.exit(0);
        }
    }
}
