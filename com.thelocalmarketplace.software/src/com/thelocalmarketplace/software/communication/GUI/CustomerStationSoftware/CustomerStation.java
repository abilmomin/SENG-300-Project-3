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


package com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware;

import javax.swing.*;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.*;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.ProductsDatabase;
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantPageGUI;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.communication.GUI.CustomerStationHardware.BaggingArea;
import com.thelocalmarketplace.software.product.AddownBag;
import com.jjjwelectronics.bag.ReusableBag;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("serial")
public class CustomerStation extends JFrame {

    private JPanel cartItemsPanel;
    private JLabel totalPriceLabel;
    private JPanel menuPanel;
    private JPanel mainPanel;
    private JPanel cartPanel;
    private JPanel PLUPanel;
    private JPanel payButtonPanel;
    private JTextField screenTextField;
    private JPanel addItemBtnPanel;
    private SelfCheckoutStationSoftware stationSoftwareInstance;
    private AttendantPageGUI attendantGUI;
    private boolean needsAssistance = false;
    private int selectedStation;
    private SelectPayment paymentWindow;
    private SearchProductByText searchProductByText;
    private BaggingArea baggingArea;
    private SettingsPanel settingsPanel;
    private ArrayList<JButton> cartItemButtons;

    private JButton selectedCartItemButton = null; 						// Variable to track the selected cart item button
    
    
    /**
     * Constructs a new CustomerStation object representing a self-checkout station for customers.
     * 
     * @param selectedStation          the identifier of the selected self-checkout station
     * @param stationSoftwareInstance  the instance of the self-checkout station's software
     * @param scale                    the electronic scale used in the station
     * @param attendantGUI             the graphical user interface for attendant interactions
     */
    public CustomerStation(int selectedStation, SelfCheckoutStationSoftware stationSoftwareInstance, AbstractElectronicScale scale, 
    						AttendantPageGUI attendantGUI) {
    	
    	this.stationSoftwareInstance = stationSoftwareInstance;
    	this.attendantGUI = attendantGUI;
        this.selectedStation = selectedStation;
    	stationSoftwareInstance.setGUI(this);
    	paymentWindow = new SelectPayment(stationSoftwareInstance);
        baggingArea = new BaggingArea();
    	searchProductByText = new SearchProductByText(stationSoftwareInstance, attendantGUI, baggingArea);
        
        cartItemButtons = new ArrayList<>();
    	
        setTitle("Self-Checkout Station " + selectedStation);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBorder(BorderFactory.createTitledBorder("Cart"));

        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        JScrollPane cartScrollPane = new JScrollPane(cartItemsPanel);
        
        // Main panel
        mainPanel = new JPanel(new BorderLayout());

        // Menu panel
        menuPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        settingsPanel = new SettingsPanel(stationSoftwareInstance);
        add(settingsPanel, BorderLayout.NORTH);
        
        
        JButton useOwnBagsBtn = createButton("Use Own Bags", e -> {
        	handleUseOwnBags();
        });
        
        JButton scanBarcodeBtn = createButton("Scan Barcode", e -> {
        	handleScanBarcode();
        });
        
        JButton enterPLUBtn = createButton("Enter PLU Code", e -> {
        	replaceCartPanelWithKeypadPanel();
        });

        JButton searchProductBtn = createButton("Search Product", e -> {
        	searchProductByText.setVisible(true);
        });

        JButton removeItemBtn = createButton("Remove Item", e -> {
            handleRemoveItem();
        });
        
        JButton purchaseBagsBtn = createButton("Purchase Bags", e -> {
        	handlePurchaseBags();
        });
        
        JButton viewBaggingAreaBtn = createButton("View Bagging Area", e -> {
            baggingArea.baggingAreaFrame.setVisible(true);
            baggingArea.baggingAreaFrame.revalidate();
        });
        
        JButton helpBtn = createButton("Help", e -> {
        	handleRequestAssistance();
        });   
        
        JButton addItemBtn = createButton("Add Item", e -> {
        	replaceGrids();
        });
        
        JButton backBtn = createButton("Back", e -> {
        	replaceGrids();
        });
        
        // Add buttons to menu panel
        menuPanel.add(useOwnBagsBtn);
        menuPanel.add(addItemBtn);
        menuPanel.add(removeItemBtn);
        menuPanel.add(purchaseBagsBtn);
        menuPanel.add(viewBaggingAreaBtn);
        menuPanel.add(helpBtn);
        
        
         addItemBtnPanel = new JPanel(new GridLayout(2, 2,10,10));
         addItemBtnPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
         
         addItemBtnPanel.add(scanBarcodeBtn);
         addItemBtnPanel.add(enterPLUBtn);
         addItemBtnPanel.add(searchProductBtn);
         addItemBtnPanel.add(backBtn);
         
        // Cart panel
        cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBorder(BorderFactory.createTitledBorder("Cart"));


        // Total price label
        totalPriceLabel = new JLabel("Total Price: $0.00");
        totalPriceLabel.setFont(totalPriceLabel.getFont().deriveFont(Font.BOLD, 16));
        JPanel totalPricePanel = new JPanel(new BorderLayout());
        totalPricePanel.add(totalPriceLabel, BorderLayout.WEST);

        // Pay button
        JButton payButton = new JButton("Pay");
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// Open SelectPayment window after payment
            	paymentWindow.setVisible(true);
            }
        });
        
        payButton.setFont(payButton.getFont().deriveFont(Font.BOLD, 16));
        payButtonPanel = new JPanel(new BorderLayout());
        payButtonPanel.add(payButton, BorderLayout.CENTER);

        // Add components to cart panel
        cartPanel.add(cartScrollPane, BorderLayout.CENTER);
        cartPanel.add(totalPricePanel, BorderLayout.NORTH);
        cartPanel.add(payButtonPanel, BorderLayout.SOUTH);

        // Add panels to main panel
        mainPanel.add(menuPanel, BorderLayout.CENTER);
        mainPanel.add(cartPanel, BorderLayout.EAST);
        
        //PLU panel
        PLUPanel = new JPanel(new BorderLayout());
        
        // PLU code keypad
        JPanel keypadPanel = createKeypadPanel();
        
        // PLU code screen
        JPanel screenPanel = createScreenPanel();
        
        JButton returnBtn = new JButton("Return to cart");
        returnBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        returnBtn.setForeground(new Color(199, 100, 2));
    	returnBtn.setBackground(new Color(245, 228, 211));
    	returnBtn.setPreferredSize(new Dimension(100, 50));

    	returnBtn.addActionListener(e -> {
    		screenTextField.setText("");
       		PLUPanel.setVisible(false);
       		getContentPane().add(cartPanel, BorderLayout.EAST);
       		cartPanel.setVisible(true);
            // To refresh
       		revalidate();
       		repaint();
    	});

    	PLUPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        PLUPanel.add(keypadPanel);
        PLUPanel.add(screenPanel, BorderLayout.NORTH);
        PLUPanel.add(returnBtn, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);

        // Set frame visible
        setVisible(true);
    }
    
    /**
     * Updates the display to reflect the amount of money paid by the customer.
     * 
     * @param addedFunds  the amount of money added by the customer
     */
    public void updatePaidDisplay(double addedFunds) {
    	paymentWindow.updateTotalPaidValueLabel(addedFunds);
    }
    
    

	/**
	 * Updates the display to reflect the current status of the self-checkout station.
	 */
    public void updateStatusDisplay() {
    	settingsPanel.updateStatus();
    }
    
    
    /**
     * Updates the display to reflect the total amount owed for the items in the cart.
     */
    public void updateTotalOwedDisplay() {
    	paymentWindow.updatePanel();
    }
    
    
    /**
     * Handles the action of scanning a barcode to add an item to the cart.
     */
    private void handleScanBarcode() {
        
        ArrayList<Barcode> barcodes = ProductsDatabase.getBarcodes();        
        Random random = new Random();       
        int randomIndex = random.nextInt(barcodes.size());
        Barcode barcode = barcodes.get(randomIndex);
        
        // Get the weight of that product
        double productWeight = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode).getExpectedWeight();
        Mass mass = new Mass(productWeight);
        BarcodedItem barcodedItem = new BarcodedItem(barcode, mass); 
        
        int initialOrderSize = stationSoftwareInstance.getOrder().size();
        
        IBarcodeScanner scanner = stationSoftwareInstance.getStationHardware().getMainScanner();
        scanner.scan(barcodedItem);
        
        int newOrderSize = stationSoftwareInstance.getOrder().size();
        
        // Check if the scan didn't fail
        if (newOrderSize > initialOrderSize) {
            BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
            
            new AddtoBagging(product, stationSoftwareInstance, attendantGUI, baggingArea);
        }
    }
    
    
    /**
     * Handles the action of using own bags by the customer.
     */
	private void handleUseOwnBags() {
        // Display a message dialog to prompt the user
        int option = JOptionPane.showConfirmDialog(this, "Please add your bags now, click OK when complete.", "Add Bags", 
        		JOptionPane.OK_CANCEL_OPTION);
        
        // Check if the user clicked OK
        if (option == JOptionPane.OK_OPTION) {
            AbstractElectronicScale scale = (AbstractElectronicScale) stationSoftwareInstance.getStationHardware().getBaggingArea();
            new AddownBag(stationSoftwareInstance, scale, this, this.attendantGUI);
        } else {
        	 this.dispose();
        }
    }
	
	
	/**
	 * Handles the action of purchasing bags by the customer.
	 */
	private void handlePurchaseBags() {
	    BagPurchaseInput dialog = new BagPurchaseInput(this);
	    dialog.setVisible(true);
	    
	    int numOfBags = dialog.getNumOfBags();
	    if (numOfBags > 0) {
			ReusableBag[] bags = new ReusableBag[numOfBags];
			
			for (int i = 0; i < numOfBags; i++)
				bags[i] = new ReusableBag();
			
			try {
				stationSoftwareInstance.getProductHandler().PurchaseBags(bags);
			} catch (OverloadedDevice | EmptyDevice e) {
				e.printStackTrace(); 
			}
			
	        JOptionPane.showMessageDialog(this, numOfBags + " bags have been added to your purchase.", "Bags Purchased", 
	        		JOptionPane.INFORMATION_MESSAGE);
	    }
	}
	
	
	/**
	 * Sets the payment as successful and closes the current window.
	 * 
	 * @param change  the change to be returned to the customer
	 */
    public void setPaymentSuccesful(double change) {
        this.dispose();
        new PaymentSuccess(change, stationSoftwareInstance, attendantGUI);
    }

    
    /**
     * Extracts the product name from the text of a cart item button.
     * 
     * @param buttonText  the text of the cart item button
     * @return            the extracted product name
     */
    private String extractProductName(String buttonText) {
        
    	// Define the regex pattern
        String regex = "^(.*?)\\s-\\s\\$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(buttonText);

        if (matcher.find()) {
            // Return the first capturing group, which contains the product name
            return matcher.group(1);
        }

        // Return an empty string or a default value if the pattern does not match
        return "";
    }
    
    
    /**
     * Adds a product to the cart and updates the display accordingly.
     * 
     * @param productName  the name of the product to be added
     * @param price        the price of the product
     */
    public void addProductToCart(String productName, double price) {
        JButton itemButton = new JButton(productName + " - $" + String.format("%.2f", price));
        itemButton.addActionListener(e -> {
            if (selectedCartItemButton != null) {
                // Optionally reset the visual state of the previously selected button
                selectedCartItemButton.setBackground(null);
            }
            selectedCartItemButton = itemButton; // Update the selected item button
            // Optionally update the visual state of the selected button
            itemButton.setBackground(Color.LIGHT_GRAY);
        });
        cartItemsPanel.add(itemButton);
        cartItemButtons.add(itemButton);
        refreshCartPanel();

        double totalPrice = stationSoftwareInstance.getTotalOrderPrice();
        totalPriceLabel.setText("Total Price: $" + String.format("%.2f", totalPrice));

    }
    
    
    /**
     * Handles the action of removing an item from the cart.
     */
    public void handleRemoveItem() {
        if (selectedCartItemButton != null) {
        	boolean itemRemoved = false;
        	
            
            ArrayList<Item> listOfOrders = stationSoftwareInstance.getOrder();
            for (Item item : listOfOrders) {
                if (item instanceof PLUCodedItem) {
                    PLUCodedItem pluItem = (PLUCodedItem) item;

                    PriceLookUpCode pluCode = pluItem.getPLUCode();
                    PLUCodedProduct product = ProductDatabases.PLU_PRODUCT_DATABASE.get(pluCode);

                    // create regex to match the product description
                    String productGettingRemoved = extractProductName(selectedCartItemButton.getText());

                    if (product.getDescription().equals(productGettingRemoved)) {
                        stationSoftwareInstance.getProductHandler().removeItemFromOrder(pluItem);
                        boolean itemRemovedFromBaggingArea = baggingArea.itemToRemove(productGettingRemoved);
                        itemRemoved = true;

                        if(itemRemovedFromBaggingArea)
                            new RemoveFromBagging(pluItem, stationSoftwareInstance, attendantGUI, baggingArea);
                        else stationSoftwareInstance.setStationUnblock();
                        break;
                    }
                } else if (item instanceof BarcodedItem) {
                    BarcodedItem barcodeItem = (BarcodedItem) item;

                    Barcode barcode = barcodeItem.getBarcode();
                    BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
                    barcodeItem.getMass().inGrams().longValue();

                    // create regex to match the product description
                    String productGettingRemoved = extractProductName(selectedCartItemButton.getText());

                    if (product.getDescription().equals(productGettingRemoved)) {
                        stationSoftwareInstance.getProductHandler().removeItemFromOrder(barcodeItem);
                        itemRemoved = true;

                        boolean itemRemovedFromBaggingArea = baggingArea.itemToRemove(productGettingRemoved);
                        if(itemRemovedFromBaggingArea)
                            new RemoveFromBagging(barcodeItem, stationSoftwareInstance, attendantGUI, baggingArea);
                        else stationSoftwareInstance.setStationUnblock();
                        break;
                    }
                }
            }
            
            if (!itemRemoved) {
            	stationSoftwareInstance.addTotalOrderPrice(-1);
            }
            
            cartItemsPanel.remove(selectedCartItemButton); 
            selectedCartItemButton = null; 											// Clear the selected item
            refreshCartPanel(); 													// Refresh the UI to reflect the removal

            double totalPrice = stationSoftwareInstance.getTotalOrderPrice();

            totalPriceLabel.setText("Total Price: $" + String.format("%.2f", totalPrice));
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item to remove."); // Prompt if no item is selected
        }
    }

   
    
    /**
     * Refreshes the cart panel to reflect changes in the cart items.
     */
    private void refreshCartPanel() {
        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
        
    }
    
    /**
     * Creates a button with the specified text and action listener.
     * 
     * @param text            the text to be displayed on the button
     * @param actionListener the action listener for the button
     * @return                the created button
     */
    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton("<html><center>" + text + "</center></html>");
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        if (actionListener != null) {
            button.addActionListener(actionListener);
        }
        return button;
    }

    /**
     * Creates the keypad panel for entering PLU codes.
     * 
     * @return  the created keypad panel
     */
    public JPanel createKeypadPanel() {
    	JPanel keypadPanel = new JPanel();
    	keypadPanel.setLayout(new GridLayout(4,4,10,10));
    	keypadPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    	
    	for (int i = 1; i <= 9; i++) {
            JButton button = new JButton(Integer.toString(i));
            button.setFont(new Font("Arial", Font.PLAIN, 16));
            keypadPanel.add(button);
            button.addActionListener(addNum);
        }
    	
    	JButton enter = new JButton("Enter");
    	enter.setFont(new Font("Arial", Font.PLAIN, 16));
    	enter.setBackground(new Color(214, 255, 217));
        enter.setForeground(new Color(42, 120, 48));
    	keypadPanel.add(enter);
    	
    	JButton button0 = new JButton("0");
    	button0.setFont(new Font("Arial", Font.PLAIN, 16));
    	keypadPanel.add(button0);
    	button0.addActionListener(addNum);
    	
    	JButton delete = new JButton("Delete");
    	delete.setFont(new Font("Arial", Font.PLAIN, 16));
        delete.setBackground(new Color(227, 188, 188));
        delete.setForeground(Color.RED);
    	keypadPanel.add(delete);
    	
    	delete.addActionListener(e -> {
        	String currentText = screenTextField.getText();
        	if (!currentText.isEmpty()) {
                 // Remove the last character from the text
                 String newText = currentText.substring(0, currentText.length() - 1);
                 screenTextField.setText(newText);
        	}
        });
    	
    	enter.addActionListener(e -> {
    	    String userInput = screenTextField.getText();
    	    
    	    if (userInput.length() == 4) {
        	    PLUCodedProduct product = stationSoftwareInstance.matchCodeAndPLUProduct(userInput);
        	    
        	    if (product != null) {
                    PLUCodedItem pluItem = new PLUCodedItem(product.getPLUCode(), new Mass(1.0));

                    stationSoftwareInstance.getProductHandler().addItemByPLUCode(pluItem);

            	    AddtoBagging popup = new AddtoBagging(product, stationSoftwareInstance, attendantGUI, baggingArea);
        			popup.setVisible(true);
            	    screenTextField.setText("");

            	    replaceCartPanelWithKeypadPanel();
        	    } else {
                    JOptionPane.showMessageDialog(this, "No product found for corresponding PLU code.");
        	    }
    	    } else {
                JOptionPane.showMessageDialog(this, "PLU code must be 4 digits.");
    	    }
    	});

    	 return keypadPanel;
    }
   
    
    /**
     * Handles the action of adding a digit to the PLU code screen.
     */
    ActionListener addNum = e -> {
        JButton button = (JButton) e.getSource();
        String digit = button.getText();
        String current = screenTextField.getText(); 								// Append the digit to the screen
        String newText = current + digit;
        
        Font currentFont = screenTextField.getFont();
        Font newFont = new Font(currentFont.getName(), currentFont.getStyle(), 24); // Adjust the font size as needed
        screenTextField.setFont(newFont);
        
        screenTextField.setText(newText);
    };
    
    
    /**
     * Creates the screen panel for displaying PLU codes.
     * 
     * @return  the created screen panel
     */
    public JPanel createScreenPanel() {
    	JPanel screenPanel = new JPanel(new BorderLayout());
        screenTextField = new JTextField(); 										// Initialize the screen text field
        screenTextField.setEditable(false); 										// Make it read-only
        screenTextField.setBackground(Color.WHITE);
        screenTextField.setHorizontalAlignment(JTextField.CENTER);
        screenPanel.add(screenTextField, BorderLayout.CENTER);
        screenPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        screenTextField.setPreferredSize(new Dimension(100, 50));
        
        return screenPanel;
    }
    
    
    /**
     * Replaces the cart panel with the PLU code entry panel.
     */
    private void replaceCartPanelWithKeypadPanel() {
        // Remove cart panel
    	if (cartPanel.isVisible()) {
    		cartPanel.setVisible(false);
    		getContentPane().add(PLUPanel, BorderLayout.EAST);
    		PLUPanel.setVisible(true);
    		
            // To refresh 
            revalidate();
            repaint();
    	} else {
    		if (!PLUPanel.isVisible()) {
    			cartPanel.setVisible(true);

    		} 
    	}
    }
    
    
    /**
     * Replaces the menu panel with the add item button panel or vice versa.
     */
    private void replaceGrids() {
       
        mainPanel.remove(menuPanel);
        mainPanel.remove(addItemBtnPanel);

        if (menuPanel.isVisible()) {
            
            menuPanel.setVisible(false);
            addItemBtnPanel.setVisible(true);
            
            PLUPanel.setVisible(false);

        } else {
           
            addItemBtnPanel.setVisible(false);
            menuPanel.setVisible(true);
            PLUPanel.setVisible(false);
            cartPanel.setVisible(true);
        }

        // Add the panels back to mainPanel with the same constraints
        mainPanel.add(menuPanel, BorderLayout.CENTER);
        mainPanel.add(addItemBtnPanel, BorderLayout.CENTER);

        // To refresh 
        revalidate();
        repaint();
    }
    
    /**
     * Displays a popup message to the customer.
     * 
     * @param message  the message to be displayed
     */
    public void customerPopUp(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    
    
    /**
     * Displays a popup for adding an item to the bagging area.
     * 
     * @param product  the product to be added to the bagging area
     */
    public void customerBaggingAreaPopUp(Product product) {
        new AddtoBagging(product, stationSoftwareInstance, attendantGUI, baggingArea);
    }
    
    public boolean canMakePopUp = true;
    
    
    /**
     * Initiates a popup request for removing an item from the cart.
     */
    public void customerRemoveItemPopUp() {
        if (canMakePopUp && !cartItemButtons.isEmpty()) {
            canMakePopUp = false;
            JButton itemButton = cartItemButtons.get(cartItemButtons.size() - 1);

            if (selectedCartItemButton != null) 
                selectedCartItemButton.setBackground(null); 
            
            selectedCartItemButton = itemButton;
            selectedCartItemButton.setBackground(Color.LIGHT_GRAY);
            
            String itemName = extractProductName(selectedCartItemButton.getText());
            
            new RemoveItemRequest(itemName, stationSoftwareInstance, attendantGUI);
        }
    }

    
    /**
     * Handles the action of requesting assistance from an attendant.
     */
    public void handleRequestAssistance() {
        needsAssistance = true;
        attendantGUI.setStationAssistanceRequested(selectedStation - 1, true);

    }
    
    
    /**
     * Displays a popup message to inform the customer about the amount due for payment.
     * 
     * @param amountDue  the amount due for payment
     */
    public void displayAmountDuePopup(BigDecimal amountDue) {
    	attendantGUI.warnForChange(amountDue);
    }
    
    /**
     * Clears the assistance signal raised by the customer.
     */
    public void clearSignal() {
        needsAssistance = false;
        attendantGUI.setStationAssistanceRequested(selectedStation - 1, false);}
    
	    
	/**
	 * Retrieves the signal indicating if assistance is requested by the customer.
	 * 
	 * @return  true if assistance is requested, otherwise false
	 */
    public boolean getSignal() {
        return needsAssistance;
    }
    
    

	/**
	 * Retrieves the attendant GUI associated with the self-checkout station.
	 * 
	 * @return  the attendant GUI
	 */
    public AttendantPageGUI getAttendantGUI() {
    	return attendantGUI;
    }
}


