package com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware;

import javax.swing.*;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.jjjwelectronics.scale.IElectronicScale;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedItem;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.AddownBag;
import com.thelocalmarketplace.software.ProductsDatabase;
import com.thelocalmarketplace.software.communication.GUI.AttendantStation.AttendantPageGUI;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.product.Products;
import com.jjjwelectronics.bag.ReusableBag;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;

public class CustomerStation extends JFrame {

    private JTextArea cartTextArea;
    private JLabel totalPriceLabel;
    private JPanel menuPanel;
    private JPanel mainPanel;
    private JPanel cartPanel;
    private JPanel PLUPanel;
    private JPanel payButtonPanel;
    private JTextField screenTextField;
    private JPanel addItemBtnPanel;
    private AddownBag addOwnBag;
    private SelfCheckoutStationSoftware stationSoftwareInstance;
    private AbstractElectronicScale scale;
    private Products products;
    private AttendantPageGUI attendantGUI;
    private boolean needsAssistance = false;
    private int selectedStation;
    private SelectPayment paymentWindow;
    private SearchProductByText visualAddPanel;
    
    public CustomerStation(int selectedStation, SelfCheckoutStationSoftware stationSoftwareInstance, AbstractElectronicScale scale, AttendantPageGUI attendantGUI) {
    	this.scale = scale;
    	this.stationSoftwareInstance = stationSoftwareInstance;
    	this.attendantGUI = attendantGUI;
        this.selectedStation = selectedStation;
    	stationSoftwareInstance.setGUI(this);
    	products = new Products(stationSoftwareInstance);
    	paymentWindow = new SelectPayment(stationSoftwareInstance);
    	visualAddPanel = new SearchProductByText();
    	
    	
        setTitle("Self-Checkout Station " + selectedStation);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        
        // Main panel
        mainPanel = new JPanel(new BorderLayout());

        // Menu panel
        menuPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Buttons
        // the following nulls inside the buttons should be replaced by the corresponding callback functions 
        //associated with the popups
        JButton useOwnBagsBtn = createButton("Use Own Bags", e -> {
        	handleUseOwnBags();
        });
        
        JButton scanBarcodeBtn = createButton("Scan Barcode", e -> {
        	handleScanBarcode();
        });
        
        JButton enterPLUBtn = createButton("Enter PLU Code", e -> {
        	
        });
        
        JButton searchProductBtn = createButton("Search Product", e -> {
        	visualAddPanel.setVisible(true);
        });
        

        JButton removeItemBtn = createButton("Remove Item", e -> {
        	
        });
        
        JButton purchaseBagsBtn = createButton("Purchase Bags", e -> {
        	handlePurchaseBags();
        });
        
        JButton viewBaggingAreaBtn = createButton("View Bagging Area", e -> {
        	
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
        //menuPanel.add(scanBarcodeBtn);
        //menuPanel.add(enterPLUBtn);
        //menuPanel.add(searchProductBtn);
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

        // Cart contents
        cartTextArea = new JTextArea(20, 30);
        cartTextArea.setEditable(false);
        JScrollPane cartScrollPane = new JScrollPane(cartTextArea);

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
        
        PLUPanel.add(keypadPanel);
        PLUPanel.add(screenPanel, BorderLayout.NORTH);
        
        enterPLUBtn.addActionListener(e -> {
        	replaceCartPanelWithKeypadPanel();
        });
        
        // Add main panel to frame
        add(mainPanel);

        // Add sample products to the cart
        // addProductToCart("Apple", 1.99);
        // addProductToCart("Banana", 0.99);

        // Set frame visible
        setVisible(true);
    }
    
    public void updatePaidDisplay(double addedFunds) {
    	paymentWindow.updateTotalPaidValueLabel(addedFunds);
    }
    
    public void updateTotalOwedDisplay() {
    	paymentWindow.updatePanel();
    }
    
    private void dontBagItem() {
		// TODO Auto-generated method stub
    	ArrayList<Item> orderList = stationSoftwareInstance.getOrder();
    	if (!orderList.isEmpty()) {
    		int lastIndex = orderList.size() - 1;

            // Extract the last item from the list
            Item lastItem = orderList.get(lastIndex);
            double massInGramsDouble = lastItem.getMass().inGrams().doubleValue();
    		products.handleBulkyItem(massInGramsDouble,this.attendantGUI);
    	} else {
    	    // Handle the case when the list is empty
    		JOptionPane.showMessageDialog(this, "Scan Item First");
    	}
	}
    
    private void handleScanBarcode() {
    	// Get a random barcode from the available barcoded products
    	ArrayList<Barcode> barcodes = ProductsDatabase.getBarcodes();    	
    	Random random = new Random();   	
        int randomIndex = random.nextInt(barcodes.size());
        Barcode barcode = barcodes.get(randomIndex);
        
        // Get the weight of that product
        double productWeight = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode).getExpectedWeight();
        Mass mass = new Mass(productWeight);
        BarcodedItem barcodedItem = new BarcodedItem(barcode, mass); 
    	
    	IBarcodeScanner scanner = stationSoftwareInstance.getStationHardware().getMainScanner();
    	scanner.scan(barcodedItem);
    	        	
    	BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
    	
    	new AddtoBagging(product, stationSoftwareInstance, productWeight, attendantGUI);
    }

	private void handleUseOwnBags() {
        // Display a message dialog to prompt the user
        int option = JOptionPane.showConfirmDialog(this, "Please add your bags now, click OK when complete.", "Add Bags", JOptionPane.OK_CANCEL_OPTION);
        
        // Check if the user clicked OK
        if (option == JOptionPane.OK_OPTION) {
            AbstractElectronicScale scale = (AbstractElectronicScale) stationSoftwareInstance.getStationHardware().getBaggingArea();
            addOwnBag = new AddownBag(stationSoftwareInstance, scale, this, this.attendantGUI);
        } else {
        	 JOptionPane.getRootFrame().dispose();
        }
    }
	
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
				e.printStackTrace(); // do we alert the attendant???????????????????????????????
			}
			
	        JOptionPane.showMessageDialog(this, numOfBags + " bags have been added to your purchase.", "Bags Purchased", JOptionPane.INFORMATION_MESSAGE);
	    }
	}
	
    public void setPaymentSuccesful(double change) {
        this.dispose();
        PaymentSuccess paymentSuccess = new PaymentSuccess(change, stationSoftwareInstance);

    }
    
    // I MADE THIS PUBLIC IDK IF IM RIGHT BUT IM USING THIS IN COORDINATION!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // Currently PLU prices are in cents, 
    public void addProductToCart(String productName, double price) {
    	double priceInDollars = price;
        cartTextArea.append(productName + " - $" + priceInDollars + "\n");
        double currentTotal = Double.parseDouble(totalPriceLabel.getText().replace("Total Price: $", ""));
        currentTotal += priceInDollars;
        totalPriceLabel.setText("Total Price: $" + String.format("%.2f", currentTotal));
    }

    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton("<html><center>" + text + "</center></html>");
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        if (actionListener != null) {
            button.addActionListener(actionListener);
        }
        return button;
    }

    public void askForMembershipNumber() {
    	MembershipNumberInput dialog = new MembershipNumberInput(this);
        dialog.frameInit();
    }
    
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

    	    PLUCodedProduct product = stationSoftwareInstance.matchCodeAndPLUProduct(userInput);

    	    if (product != null) {
    	        // If a product is found, display the AddtoBagging popup
    	        AddtoBagging popup = new AddtoBagging(product, stationSoftwareInstance, 0.0, attendantGUI);
    	        popup.setVisible(true);
    	    } else {
    	        // If no product is found, show an error message
    	        JOptionPane.showMessageDialog(this, "Product not found. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
    	    }

    	    // Clear the PLU input field
    	    screenTextField.setText("");

    	    // Switch back to the previous panel if needed
    	    replaceCartPanelWithKeypadPanel();
    	});

    	
    	 return keypadPanel;
    }
   
    ActionListener addNum = e -> {
        JButton button = (JButton) e.getSource();
        String digit = button.getText();
        String current = screenTextField.getText(); // Append the digit to the screen
        String newText = current + digit;
        
        Font currentFont = screenTextField.getFont();
        Font newFont = new Font(currentFont.getName(), currentFont.getStyle(), 24); // Adjust the font size as needed
        screenTextField.setFont(newFont);
        
        screenTextField.setText(newText);
    };
    
    public JPanel createScreenPanel() {
    	JPanel screenPanel = new JPanel(new BorderLayout());
        screenTextField = new JTextField(); // Initialize the screen text field
        screenTextField.setEditable(false); // Make it read-only
        screenTextField.setBackground(Color.WHITE);
        screenTextField.setHorizontalAlignment(JTextField.CENTER);
        screenPanel.add(screenTextField, BorderLayout.CENTER);
        screenPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        screenTextField.setPreferredSize(new Dimension(100, 50));
        
        return screenPanel;
    }
    
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
    private void replaceGrids() {
        // Remove current panels from mainPanel
        mainPanel.remove(menuPanel);
        mainPanel.remove(addItemBtnPanel);

        if (menuPanel.isVisible()) {
            // Hide menuPanel and show addItemBtnPanel
            menuPanel.setVisible(false);
            addItemBtnPanel.setVisible(true);
            
            PLUPanel.setVisible(false);

        } else {
            // Hide addItemBtnPanel and show menuPanel
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

    public void customerPopUp(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    
    public void customerBaggingAreaPopUp(Product product) {
    	double weight = 0.0;
    	if (product instanceof BarcodedProduct) {
    		BarcodedProduct barcodedProduct = (BarcodedProduct) product;
    		weight = barcodedProduct.getExpectedWeight();
    	}
    	new AddtoBagging(product, stationSoftwareInstance, weight, attendantGUI);
    }

    public void handleRequestAssistance() {
        needsAssistance = true;
        attendantGUI.setStationAssistanceRequested(selectedStation - 1, true);

    }

    public void clearSignal() {
        needsAssistance = false;
        attendantGUI.setStationAssistanceRequested(selectedStation - 1, false);}

    public boolean getSignal() {
        return needsAssistance;
    }


}


