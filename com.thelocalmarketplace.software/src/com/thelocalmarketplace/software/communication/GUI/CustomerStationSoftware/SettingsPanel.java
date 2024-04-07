package com.thelocalmarketplace.software.communication.GUI.CustomerStationSoftware;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    private JComboBox<String> languageDropdown;
    private JComboBox<String> accessibilityDropdown;

    private String[] languages = {"English"};
    private String[] accessibilitySettings = {"Default"};

    public SettingsPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        // Language settings
        JPanel languagePanel = new JPanel(new BorderLayout());
        languageDropdown = new JComboBox<>(languages);
        languagePanel.add(new JLabel("Language: "), BorderLayout.NORTH);
        languagePanel.add(languageDropdown, BorderLayout.CENTER);

        // Accessibility settings
        JPanel accessibilityPanel = new JPanel(new BorderLayout());
        accessibilityDropdown = new JComboBox<>(accessibilitySettings);
        accessibilityPanel.add(new JLabel("Accessibility: "), BorderLayout.NORTH);
        accessibilityPanel.add(accessibilityDropdown, BorderLayout.CENTER);

        add(languagePanel);
        add(accessibilityPanel);
    }
}
