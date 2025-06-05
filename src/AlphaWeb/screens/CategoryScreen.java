package AlphaWeb.screens;

import AlphaWeb.utils.MusicPlayer;
import AlphaWeb.utils.FontManager;
import AlphaWeb.utils.NavigationManager;
import javax.swing.*;
import java.awt.*;

public class CategoryScreen extends JFrame {
    private final String playerName;
    public CategoryScreen(String playerName) {
        this.playerName = playerName;
        setTitle("AlphaWeb - Select Category");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        MusicPlayer musicPlayer = MusicPlayer.getInstance();
        if (!musicPlayer.isPlaying()) {
            musicPlayer.play("/AlphaWeb/utils/music.wav");
        }

        BackgroundPanel backgroundPanel = new BackgroundPanel("/AlphaWeb/assets/background2.png");
        backgroundPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel heading = new JLabel("Choose Category", SwingConstants.CENTER);
        heading.setFont(FontManager.getFont(60f));
        heading.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 0, 80, 0);
        gbc.anchor = GridBagConstraints.PAGE_START;
        backgroundPanel.add(heading, gbc);

        String[][] categories = {
            {"Movie", "Animal"},
            {"Food", "Country"}
        };

        Font buttonFont = FontManager.getFont(50f);
        Dimension buttonSize = new Dimension(300, 150);

        for (int row = 0; row < categories.length; row++) {
            for (int col = 0; col < categories[row].length; col++) {
                final String category = categories[row][col];
                RoundedShadowButton button = new RoundedShadowButton(category);
                button.setFont(buttonFont);
                button.setPreferredSize(buttonSize);
                button.setOpaque(false);
                button.setContentAreaFilled(false);
                button.setBorderPainted(false);
                button.addActionListener(e -> {
                    dispose();
                    new LevelScreen(category, playerName);
                });

                gbc.gridx = col;
                gbc.gridy = row + 1;
                gbc.gridwidth = 1;
                gbc.insets = new Insets(10, 20, 10, 20);
                gbc.anchor = GridBagConstraints.CENTER;
                backgroundPanel.add(button, gbc);
            }
        }

        // Add Back Button with different size
        RoundedShadowButton backButton = new RoundedShadowButton("Back");
        backButton.setFont(buttonFont);
        backButton.setPreferredSize(new Dimension(250, 100)); // Smaller size: 200x100
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> {
            NavigationManager.goBack(this, () -> new NameInputScreen());
            dispose();
        });

        gbc.gridx = 0;
        gbc.gridy = 3; // Place below the category buttons
        gbc.gridwidth = 2; // Span across both columns
        gbc.insets = new Insets(70, 0, 0, 0); // Add some spacing above and below
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(backButton, gbc);

        setContentPane(backgroundPanel);
        setVisible(true);
    }
}