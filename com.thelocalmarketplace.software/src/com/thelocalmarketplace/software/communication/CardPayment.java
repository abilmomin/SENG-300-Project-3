import javax.swing.*;
import java.awt.*;

public class CardPayment extends JFrame {

    public CardPayment() {
        setTitle("Select Card Payment Method");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JRadioButton swipeButton = new JRadioButton("Swipe");
        JRadioButton tapButton = new JRadioButton("Tap");
        JRadioButton insertButton = new JRadioButton("Insert Card");

        ButtonGroup group = new ButtonGroup();
        group.add(swipeButton);
        group.add(tapButton);
        group.add(insertButton);

        JPanel radioPanel = new JPanel(new GridLayout(0, 1));
        radioPanel.add(swipeButton);
        radioPanel.add(tapButton);
        radioPanel.add(insertButton);

        JButton finishPaymentButton = new JButton("Finish Payment");
        finishPaymentButton.addActionListener(e -> {
            // Placeholder action listener
            JOptionPane.showMessageDialog(this, "Payment Method Selected. Processing Payment...", "Payment", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        add(radioPanel);
        add(finishPaymentButton);

        setVisible(true);
    }
}
