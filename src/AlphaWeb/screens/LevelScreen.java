package AlphaWeb.screens;

import AlphaWeb.utils.MusicPlayer;
import AlphaWeb.utils.FontManager;
import AlphaWeb.utils.NavigationManager;
import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class LevelScreen extends JFrame {
    private final JSlider levelSlider;
    private final JLabel levelLabel;
    private final JLabel emojiLabel; // Added for emoji GIF
    private final String category;
    private final String playerName;

    public LevelScreen(String category, String playerName) {
        this.playerName = playerName;
        this.category = category;
        setTitle("Select Level");
        setSize(1200, 800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        MusicPlayer musicPlayer = MusicPlayer.getInstance();
        if (!musicPlayer.isPlaying()) {
            musicPlayer.play("/AlphaWeb/utils/music.wav");
        }

        BackgroundPanel panel = new BackgroundPanel("/AlphaWeb/assets/background2.png");
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // 1. Title at top
        JLabel title = new JLabel("Select Level Difficulty", SwingConstants.CENTER);
        title.setFont(FontManager.getFont(60f));
        title.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 0, 20, 0);
        panel.add(title, gbc);

        // 2. Emoji centered (200x200)
        emojiLabel = new JLabel("", SwingConstants.CENTER);
        emojiLabel.setPreferredSize(new Dimension(200, 200));
        gbc.gridy = 1;
        gbc.weighty = 0.3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(emojiLabel, gbc);

        // 3. Slider panel
        JPanel sliderPanel = new JPanel(new BorderLayout());
        sliderPanel.setOpaque(false);

        levelSlider = new JSlider(1, 3, 1);
        levelSlider.setMajorTickSpacing(1);
        levelSlider.setPaintTicks(true);
        levelSlider.setPaintLabels(true);
        levelSlider.setSnapToTicks(true);
        levelSlider.setOpaque(false);
        levelSlider.setUI(new CustomSliderUI(levelSlider));
        levelSlider.setLabelTable(createLabelTable());
        levelSlider.setPreferredSize(new Dimension(750, 80));

        // Add ChangeListener to update GIF and label when slider value changes
        levelSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateLevelLabel();
            }
        });

        levelLabel = new JLabel("Current: Easy", SwingConstants.CENTER);
        levelLabel.setFont(FontManager.getFont(20f));
        levelLabel.setForeground(Color.RED);

        JPanel sliderContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        sliderContainer.setOpaque(false);
        sliderContainer.add(levelSlider);

        sliderPanel.add(sliderContainer, BorderLayout.CENTER);
        sliderPanel.add(levelLabel, BorderLayout.SOUTH);

        gbc.gridy = 2;
        gbc.weighty = 0.3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // Reduced insets.top from 20 to 5 to decrease space between emoji and slider
        gbc.insets = new Insets(5, 0, 0, 0);
        panel.add(sliderPanel, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        buttonPanel.setOpaque(false);

        RoundedShadowButton nextButton = new RoundedShadowButton("Next");
        nextButton.setFont(FontManager.getFont(40f));
        nextButton.setPreferredSize(new Dimension(250, 100));
        nextButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                nextButton.setBackground(new Color(135, 206, 250));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                nextButton.setBackground(new Color(100, 149, 237));
            }
        });
        nextButton.addActionListener(e -> {
            String difficulty = getSelectedDifficulty();
            System.out.println("Navigating to ThemeScreen with difficulty: " + difficulty);
            dispose();
            new ThemeScreen(category, difficulty, this.playerName).setVisible(true);
        });
        buttonPanel.add(nextButton);

        RoundedShadowButton backButton = new RoundedShadowButton("Back");
        backButton.setFont(FontManager.getFont(40f));
        backButton.setPreferredSize(new Dimension(250, 100));
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(135, 206, 250));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(100, 149, 237));
            }
        });
        backButton.addActionListener(e -> NavigationManager.goBack(this, () -> new CategoryScreen(this.playerName)));
        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);

        gbc.gridy = 3;
        gbc.weighty = 0.1; 
        gbc.insets = new Insets(30, 0, 20, 0);
        panel.add(buttonPanel, gbc);

        setContentPane(panel);
        setVisible(true);
        updateLevelLabel();
    }

    private void updateLevelLabel() {
        String difficulty = getSelectedDifficulty();
        levelLabel.setText("Current: " + difficulty);

        String gifPath = switch (levelSlider.getValue()) {
            case 1 -> "/AlphaWeb/assets/easy.gif";
            case 2 -> "/AlphaWeb/assets/medium.gif";
            case 3 -> "/AlphaWeb/assets/hard.gif";
            default -> "/AlphaWeb/assets/easy.gif";
        };

        try {
            URL gifURL = getClass().getResource(gifPath);
            if (gifURL != null) {
                // Load directly as ImageIcon to preserve animation
                ImageIcon icon = new ImageIcon(gifURL);
                emojiLabel.setIcon(icon);
                emojiLabel.setText("");
            } else {
                throw new Exception("GIF not found at: " + gifPath);
            }
        } catch (Exception e) {
            System.err.println("Error loading emoji: " + e.getMessage());
            emojiLabel.setIcon(null);
            emojiLabel.setText("No emoji!");
            emojiLabel.setFont(FontManager.getFont(20f));
            emojiLabel.setForeground(Color.RED);
        }
    }

    private String getSelectedDifficulty() {
        return switch (levelSlider.getValue()) {
            case 1 -> "Easy";
            case 2 -> "Medium";
            case 3 -> "Hard";
            default -> "Easy";
        };
    }

    private Hashtable<Integer, JLabel> createLabelTable() {
        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        JLabel easyLabel = new JLabel("Easy");
        easyLabel.setFont(FontManager.getFont(16f));
        easyLabel.setForeground(Color.RED);
        JLabel mediumLabel = new JLabel("Medium");
        mediumLabel.setFont(FontManager.getFont(16f));
        mediumLabel.setForeground(Color.RED);
        JLabel hardLabel = new JLabel("Hard");
        hardLabel.setFont(FontManager.getFont(16f));
        hardLabel.setForeground(Color.RED);
        labels.put(1, easyLabel);
        labels.put(2, mediumLabel);
        labels.put(3, hardLabel);
        return labels;
    }

    private static class CustomSliderUI extends BasicSliderUI {
        private static final int THUMB_SIZE = 40;
        private static final int TRACK_HEIGHT = 25;
        private static final int TRACK_WIDTH = 700;
        private static final int BORDER_THICKNESS = 4;

        public CustomSliderUI(JSlider slider) {
            super(slider);
        }

        @Override
        public void paintTrack(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Rectangle trackBounds = trackRect;

            int trackX = trackBounds.x + (trackBounds.width - TRACK_WIDTH) / 2;
            int trackY = trackBounds.y + trackBounds.height / 2 - TRACK_HEIGHT / 2;

            g2d.setColor(Color.RED);
            g2d.fillRoundRect(trackX, trackY, TRACK_WIDTH, TRACK_HEIGHT, 10, 10);
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(BORDER_THICKNESS));
            g2d.drawRoundRect(trackX, trackY, TRACK_WIDTH, TRACK_HEIGHT, 10, 10);
            g2d.setStroke(new BasicStroke(1));
        }

        @Override
        public void paintThumb(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Rectangle thumbBounds = thumbRect;
            g2d.setColor(Color.RED);
            g2d.fillOval(thumbBounds.x, thumbBounds.y, THUMB_SIZE, THUMB_SIZE);
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(BORDER_THICKNESS));
            g2d.drawOval(thumbBounds.x, thumbBounds.y, THUMB_SIZE, THUMB_SIZE);
            g2d.setStroke(new BasicStroke(1));
        }

        @Override
        protected Dimension getThumbSize() {
            return new Dimension(THUMB_SIZE, THUMB_SIZE);
        }

        @Override
        public void paintTicks(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.WHITE);
            for (int i = 1; i <= 3; i++) {
                int x = xPositionForValue(i);
                g2d.fillOval(x - 5, trackRect.y + trackRect.height / 2 - 5, 10, 10);
            }
        }
    }
}