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
import com.thelocalmarketplace.software.product.PersonalBag;
import java.awt.event.WindowEvent;
import com.jjjwelectronics.bag.ReusableBag;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class represents the graphical user interface (GUI) for a self-checkout station designed for customers,
 * providing functionalities such as scanning barcodes, entering PLU codes, managing the cart, and making payments.
*/
@SuppressWarnings("serial")
public class CustomerStation extends JFrame {

	public JPanel cartItemsPanel;
    public JLabel totalPriceLabel;
    public JPanel menuPanel;
    public JPanel mainPanel;
    public JPanel cartPanel;
    public JPanel PLUPanel;
    public JPanel payButtonPanel;
    public JTextField screenTextField;
    public JPanel addItemBtnPanel;
    public SelfCheckoutStationSoftware stationSoftwareInstance;
    public AttendantPageGUI attendantGUI;
    public boolean needsAssistance = false;
    public int selectedStation;
    public SelectPayment paymentWindow;
    public SearchProductByText searchProductByText;
    public BaggingArea baggingArea;
    public SettingsPanel settingsPanel;
    public ArrayList<JButton> cartItemButtons;
    public JButton selectedCartItemButton = null;
    public AddtoBagging addToBagPopUp;
    public JPanel keypadPanel;
    public BagPurchaseInput bagPurchasePanel;
    
    /**
     * Constructs a new CustomerStation object representing a self-checkout station for customers.
     * 
     * @param selectedStation          
     * 			The identifier of the selected self-checkout station.
     * @param stationSoftwareInstance  
     * 			The instance of the self-checkout station's software.
     * @param scale                    
     * 			The electronic scale used in the station.
     * @param attendantGUI             
     * 			The graphical user interface for attendant interactions.
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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        keypadPanel = createKeypadPanel();
        
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
    
    public SelectPayment getPaymentWindow() {
    	return paymentWindow;
    }

    /**
     * Updates the display to reflect the amount of money paid by the customer.
     * 
     * @param addedFunds  
     * 			The amount of money added by the customer.
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
            
            addToBagPopUp = new AddtoBagging(product, stationSoftwareInstance, attendantGUI, baggingArea);
        }
    }

    /**
     * Handles the action of using own bags by the customer.
     */
	private void handleUseOwnBags() {
        int option = JOptionPane.showConfirmDialog(this, "Please add your bags now, click OK when complete.", "Add Bags", 
        		JOptionPane.OK_CANCEL_OPTION);
        
        // Check if the user clicked OK
        if (option == JOptionPane.OK_OPTION) {
            AbstractElectronicScale scale = (AbstractElectronicScale) stationSoftwareInstance.getStationHardware().getBaggingArea();
            new PersonalBag(stationSoftwareInstance, scale, this, this.attendantGUI);
        } else {
        	 this.dispose();
        }
    }
	
	/**
	 * Handles the action of purchasing bags by the customer.
	 */
	private void handlePurchaseBags() {
	    bagPurchasePanel = new BagPurchaseInput(this);
	    bagPurchasePanel.setVisible(true);
	    
	    int numOfBags = bagPurchasePanel.getNumOfBags();
	    if (numOfBags > 0) {
			ReusableBag[] bags = new ReusableBag[numOfBags];
			
			for (int i = 0; i < numOfBags; i++)
				bags[i] = new ReusableBag();
			
			try {
				stationSoftwareInstance.getProductHandler().purchaseBags(bags);
				updateTotalOwedDisplay();
                JOptionPane.showMessageDialog(this, numOfBags + " bags have been added to your purchase.",
                        "Bags Purchased", JOptionPane.INFORMATION_MESSAGE);
			} catch (OverloadedDevice | EmptyDevice e) {
				e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Unable to add" + numOfBags + " bags.",
                        "Bags Not Purchased", JOptionPane.INFORMATION_MESSAGE);
			}
	    }
	}
	
	/**
	 * Sets the payment as successful and closes the current window.
	 * 
	 * @param change  
	 * 			The change to be returned to the customer.
	 */
    public void setPaymentSuccesful(double change) {
        this.dispose();
        new PaymentSuccess(change, stationSoftwareInstance, attendantGUI);
    }
    
    /**
     * Extracts the product name from the text of a cart item button.
     * 
     * @param buttonText  
     * 			The text of the cart item button.
     * @return the extracted product name.
     */
    private String extractProductName(String buttonText) {
        String regex = "^(.*?)\\s-\\s\\$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(buttonText);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }
    
    /**
     * Adds a product to the cart and updates the display accordingly.
     * 
     * @param productName  
     * 				The name of the product to be added.
     * @param price        
     * 				The price of the product.
     */
    public void addProductToCart(String productName, double price) {
        JButton itemButton = new JButton(productName + " - $" + String.format("%.2f", price));
        itemButton.addActionListener(e -> {
            if (selectedCartItemButton != null) {
                selectedCartItemButton.setBackground(null);
            }
            selectedCartItemButton = itemButton; // Update the selected item button
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
            if (!itemRemoved)
            	stationSoftwareInstance.addTotalOrderPrice(-1);
            
            cartItemsPanel.remove(selectedCartItemButton); 
            selectedCartItemButton = null;
            refreshCartPanel();

            double totalPrice = stationSoftwareInstance.getTotalOrderPrice();

            totalPriceLabel.setText("Total Price: $" + String.format("%.2f", totalPrice));
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item to remove.");
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
     * @param text            
     * 				The text to be displayed on the button.
     * @param actionListener 
     * 				The action listener for the button.
     * @return the created button.
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
     * @return the created keypad panel.
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
        String current = screenTextField.getText();
        String newText = current + digit;
        
        Font currentFont = screenTextField.getFont();
        Font newFont = new Font(currentFont.getName(), currentFont.getStyle(), 24);
        screenTextField.setFont(newFont);
        
        screenTextField.setText(newText);
    };
    
    /**
     * Creates the screen panel for displaying PLU codes.
     * 
     * @return the created screen panel.
     */
    public JPanel createScreenPanel() {
    	JPanel screenPanel = new JPanel(new BorderLayout());
        screenTextField = new JTextField();
        screenTextField.setEditable(false);
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
    	if (cartPanel.isVisible()) {
    		cartPanel.setVisible(false);
    		getContentPane().add(PLUPanel, BorderLayout.EAST);
    		PLUPanel.setVisible(true);
    		
            // To refresh 
            revalidate();
            repaint();
    	} else {
    		if (!PLUPanel.isVisible())
    			cartPanel.setVisible(true);
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
     * @param message  
     * 				The message to be displayed.
     */
    public void customerPopUp(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    
    /**
     * Displays a popup for adding an item to the bagging area.
     * 
     * @param product  
     * 				The product to be added to the bagging area.
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
     * @param amountDue  
     * 				The amount due for payment.
     */
    public void displayAmountDuePopup(BigDecimal amountDue) {
    	attendantGUI.warnForChange(amountDue);
    }
    
    /**
     * Clears the assistance signal raised by the customer.
     */
    public void clearSignal() {
        needsAssistance = false;
        attendantGUI.setStationAssistanceRequested(selectedStation - 1, false);
    }
	    
	/**
	 * Retrieves the signal indicating if assistance is requested by the customer.
	 * 
	 * @return true if assistance is requested, otherwise false.
	 */
    public boolean getSignal() {
        return needsAssistance;
    }

    
    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            dispose(); // Dispose only this instance
        } else {
            super.processWindowEvent(e);
        }
    }
	/**
	 * Retrieves the attendant GUI associated with the self-checkout station.
	 * 
	 * @return the attendant GUI.
	 */
    public AttendantPageGUI getAttendantGUI() {
    	return attendantGUI;
    }
    
    public void setAttendantGUI(AttendantPageGUI gui) {
    	attendantGUI = gui;
    }
}