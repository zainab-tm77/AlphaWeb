package AlphaWeb.screens;

import AlphaWeb.utils.MusicPlayer;
import AlphaWeb.utils.FontManager;
import AlphaWeb.utils.NavigationManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ThemeScreen extends JFrame {
    private String selectedTheme;
    private final ArrayList<String> themes = new ArrayList<>();
    private final ArrayList<JLabel> characterLabels = new ArrayList<>();
    private final String category, level, playerName;

    public ThemeScreen(String category, String level, String playerName) {
        this.category = category;
        this.level = level;
        this.playerName = playerName;
        setTitle("Select Theme");
        setSize(1200, 800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        MusicPlayer musicPlayer = MusicPlayer.getInstance();
        if (!musicPlayer.isPlaying()) {
            musicPlayer.play("/AlphaWeb/utils/music.wav");
        }

        BackgroundPanel backgroundPanel = new BackgroundPanel("/AlphaWeb/assets/background2.png");
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 20, 20));
        backgroundPanel.setLayout(new BorderLayout(20, 20));

        JLabel title = new JLabel("CHARACTER", SwingConstants.CENTER);
        title.setFont(FontManager.getFont(60f));
        title.setForeground(Color.RED);
        backgroundPanel.add(title, BorderLayout.NORTH);

        themes.add("chef");
        themes.add("cowboy");
        themes.add("theif");
        themes.add("nard");

        JPanel characterPanel = new JPanel(new GridBagLayout());
        characterPanel.setOpaque(false);

        for (String theme : themes) {
            JLabel label = loadCharacterLabel(theme);
            if (label != null) {
                characterLabels.add(label);
            }
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.CENTER;

        for (int i = 0; i < characterLabels.size(); i++) {
            gbc.gridx = i;
            characterPanel.add(characterLabels.get(i), gbc);
        }

        backgroundPanel.add(characterPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setOpaque(false);

        RoundedShadowButton confirmButton = new RoundedShadowButton("Confirm");
        confirmButton.setFont(FontManager.getFont(40f));
        confirmButton.setPreferredSize(new Dimension(250, 100));
        confirmButton.addActionListener(e -> {
            if (selectedTheme != null) {
                NavigationManager.goToGame(this, category, level, selectedTheme, this.playerName);
            } else {
                showFunkyDialog("Please select a character theme.");
            }
        });
        bottomPanel.add(confirmButton);

        RoundedShadowButton backButton = new RoundedShadowButton("Back");
        backButton.setFont(FontManager.getFont(40f));
        backButton.setPreferredSize(new Dimension(250, 100));
        backButton.addActionListener(e -> NavigationManager.goBack(this, () -> new LevelScreen(category, this.playerName)));
        bottomPanel.add(backButton);

        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);
        setContentPane(backgroundPanel);
        setVisible(true);
    }

    private void showFunkyDialog(String message) {
        // Customize JOptionPane appearance
        UIManager.put("OptionPane.background", Color.MAGENTA);
        UIManager.put("Panel.background", Color.MAGENTA);
        UIManager.put("Button.background", Color.YELLOW);
        UIManager.put("Button.foreground", Color.RED);
        UIManager.put("Button.font", FontManager.getFont(16f));

        // Create a custom message label for funky text styling
        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(FontManager.getFont(20f));
        messageLabel.setForeground(Color.CYAN);

        // Create a custom panel with a funky border
        JPanel customPanel = new JPanel(new BorderLayout());
        customPanel.setBackground(Color.MAGENTA);
        customPanel.setBorder(BorderFactory.createDashedBorder(Color.GREEN, 5, 5));
        customPanel.add(messageLabel, BorderLayout.CENTER);

        // Show the dialog with the custom panel
        JOptionPane optionPane = new JOptionPane(
            customPanel,
            JOptionPane.WARNING_MESSAGE,
            JOptionPane.DEFAULT_OPTION,
            null,
            new Object[]{"OK"},
            "OK"
        );

        // Create and customize the dialog
        JDialog dialog = optionPane.createDialog(this, "CHOOSE YOUR CHARACTER!");
        dialog.getContentPane().setBackground(Color.MAGENTA);

        // Customize the button after the dialog is created
        for (Component comp : dialog.getContentPane().getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                for (Component subComp : panel.getComponents()) {
                    if (subComp instanceof JButton) {
                        JButton button = (JButton) subComp;
                        button.setBackground(Color.YELLOW);
                        button.setForeground(Color.RED);
                        button.setFont(FontManager.getFont(16f));
                        button.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                        button.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseEntered(MouseEvent e) {
                                button.setBackground(Color.GREEN);
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

        // Reset UIManager properties to avoid affecting other dialogs
        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
        UIManager.put("Button.background", null);
        UIManager.put("Button.foreground", null);
        UIManager.put("Button.font", null);
    }

    private JLabel loadCharacterLabel(String theme) {
        String path = "/AlphaWeb/assets/" + theme + "_0.png";
        ImageIcon icon = null;
        java.net.URL resource = getClass().getResource(path);
        if (resource != null) {
            icon = new ImageIcon(resource);
        } else {
            System.err.println("Image not found: " + path);
        }
        if (icon == null || icon.getImage() == null) {
            JLabel label = new JLabel(theme, SwingConstants.CENTER);
            label.setForeground(Color.GREEN);
            label.setBackground(Color.YELLOW);
            label.setOpaque(true);
            label.setPreferredSize(new Dimension(200, 300));
            return label;
        }
        Image scaledImg = icon.getImage().getScaledInstance(200, 300, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(scaledImg));
        label.setBorder(BorderFactory.createLineBorder(Color.white));
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedTheme = theme;
                for (JLabel l : characterLabels) {
                    l.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                }
                label.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
            }
        });
        return label;
    }
}