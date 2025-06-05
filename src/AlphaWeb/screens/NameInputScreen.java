package AlphaWeb.screens;

import AlphaWeb.utils.FontManager;
import AlphaWeb.utils.NavigationManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NameInputScreen extends JFrame {
    public NameInputScreen() {
        setTitle("Enter Your Name");
        setSize(1200, 800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        BackgroundPanel panel = new BackgroundPanel("/AlphaWeb/assets/background2.png");
        panel.setLayout(new GridBagLayout()); // Changed to GridBagLayout for better centering

        // Title Label
        JLabel titleLabel = new JLabel("Enter Your Name", SwingConstants.CENTER);
        titleLabel.setFont(FontManager.getFont(50f));
        titleLabel.setForeground(Color.RED);

        // Name input field
        JTextField nameField = new JTextField(20);
        nameField.setFont(FontManager.getFont(30f));
        nameField.setPreferredSize(new Dimension(400, 50));
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.PINK),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        nameField.setBackground(Color.yellow);

        // Custom rounded shadow button
        RoundedShadowButton submitButton = new RoundedShadowButton("SUBMIT");
        submitButton.setFont(FontManager.getFont(40f));
        submitButton.setPreferredSize(new Dimension(250, 100));
        
        // Add hover effects
        submitButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                showFunkyDialog("Name cannot be empty!");
            } else {
                dispose();
                NavigationManager.goToCategory(this, name);
            }
        });

        // Main content panel with BoxLayout for vertical arrangement
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        // Add components with proper alignment and spacing
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(60)); // Space between title and text field
        
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(nameField);
        contentPanel.add(Box.createVerticalStrut(80)); // Space between text field and button
        
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(submitButton);

        // GridBagConstraints for centering the content panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(contentPanel, gbc);

        setContentPane(panel);
        setVisible(true);
    }
    
    private void showFunkyDialog(String message) {
        // Customize JOptionPane appearance
        UIManager.put("OptionPane.background",Color.MAGENTA); 
        UIManager.put("Panel.background", Color.MAGENTA);
        UIManager.put("Button.background", Color.YELLOW);
        UIManager.put("Button.foreground", Color.RED);
        UIManager.put("Button.font", FontManager.getFont(16f));

        // Create a custom message label with funky styling
        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(FontManager.getFont(20f));
        messageLabel.setForeground(Color.CYAN);

        // Create a custom panel with funky styling
        JPanel customPanel = new JPanel(new BorderLayout()); // Pink
        customPanel.setBackground(Color.MAGENTA);
        customPanel.setBorder(BorderFactory.createDashedBorder(Color.GREEN, 5, 5));
        customPanel.add(messageLabel, BorderLayout.CENTER);

        // Create the option pane
        JOptionPane optionPane = new JOptionPane(
            customPanel,
            JOptionPane.WARNING_MESSAGE,
            JOptionPane.DEFAULT_OPTION,
            null,
            new Object[]{"TRY AGAIN"},
            "TRY AGAIN"
        );

        // Create and customize the dialog
        JDialog dialog = optionPane.createDialog(this, "OOPS!");
        dialog.getContentPane().setBackground(Color.MAGENTA);
        
        // Apply custom button styling
        for (Component comp : dialog.getContentPane().getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                for (Component subComp : panel.getComponents()) {
                    if (subComp instanceof JButton) {
                        JButton button = (JButton) subComp;
                        button.setBackground(Color.YELLOW);
                        button.setForeground(Color.RED);
                        button.setFont(FontManager.getFont(18f));
                        button.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.RED, 2),
                            BorderFactory.createEmptyBorder(5, 15, 5, 15)
                        ));
                        button.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseEntered(MouseEvent e) {
                                button.setBackground(new Color(50, 205, 50)); // Lime green
                                button.setForeground(Color.BLACK);
                            }
                            @Override
                            public void mouseExited(MouseEvent e) {
                                button.setBackground(Color.YELLOW);
                                button.setForeground(Color.RED);
                            }
                        });
                    }
                }
            }
        }

        dialog.setVisible(true);
        
        // Reset UI manager properties
        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
        UIManager.put("Button.background", null);
        UIManager.put("Button.foreground", null);
        UIManager.put("Button.font", null);
    }
}