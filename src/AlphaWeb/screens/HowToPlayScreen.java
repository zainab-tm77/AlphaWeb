package AlphaWeb.screens;

import AlphaWeb.screens.RoundedShadowButton;
import AlphaWeb.utils.FontManager;
import AlphaWeb.utils.NavigationManager;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javax.swing.*;
import java.awt.*;

public class HowToPlayScreen extends JFrame {
    private final String category;
    private final String level;
    private final String theme;
    private final String playerName;
    private MediaPlayer mediaPlayer;
    private JFXPanel fxPanel;
    private boolean isFirstTime = true;

    public HowToPlayScreen(String category, String level, String theme, String playerName) {
        this.category = category;
        this.level = level;
        this.theme = theme;
        this.playerName = playerName;

        initializeUI();
    }

    private void initializeUI() {
        setTitle("How to Play");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with absolute layout for positioning
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null); // Enable absolute positioning

        // 1. Create JavaFX panel for video background
        this.fxPanel = new JFXPanel(); // Store as field
        fxPanel.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(fxPanel, JLayeredPane.DEFAULT_LAYER);// base layer

        // 2. Add skip button at bottom-right
        // 2. Add skip button at bottom-right
        RoundedShadowButton skipButton = createSkipButton();

        // Set custom spacing from frame borders
        int marginRight = 40;
        int marginBottom = 120;
        int buttonWidth = 200;
        int buttonHeight = 70;

        skipButton.setBounds(
            getWidth() - buttonWidth - marginRight,
            getHeight() - buttonHeight - marginBottom,
            buttonWidth,
            buttonHeight
        );

        // Add button on a higher layer so it stays above the video
        layeredPane.add(skipButton, JLayeredPane.PALETTE_LAYER);
         // top layer

        setContentPane(layeredPane);
        initializeFX(fxPanel);
        setVisible(true);
    }
    
    public void restartVideo() {
        if (mediaPlayer != null) {
            javafx.application.Platform.runLater(() -> {
                mediaPlayer.seek(javafx.util.Duration.ZERO);
                mediaPlayer.play();
            });
        } else {
            initializeFX(fxPanel);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            restartVideo();
        }
    }

    private RoundedShadowButton createSkipButton() {
        RoundedShadowButton button = new RoundedShadowButton("Skip");
        button.setFont(FontManager.getFont(28f));
        button.setPreferredSize(new Dimension(200, 70));
        button.setForeground(Color.WHITE);
        button.addActionListener(e -> navigateToNextScreen());
        return button;
    }

    private void initializeFX(JFXPanel fxPanel) {
        javafx.application.Platform.runLater(() -> {
            try {
                String videoPath = getClass().getResource("/AlphaWeb/assets/FOX.mp4").toExternalForm();
                Media media = new Media(videoPath);
                mediaPlayer = new MediaPlayer(media);
                MediaView mediaView = new MediaView(mediaPlayer);
                
                // Stretch video to fill entire background
                mediaView.setPreserveRatio(false);
                mediaView.setFitWidth(getWidth());
                mediaView.setFitHeight(getHeight());
                
                javafx.scene.Scene scene = new javafx.scene.Scene(
                    new javafx.scene.layout.StackPane(mediaView)
                );
                scene.setFill(javafx.scene.paint.Color.BLACK);
                
                fxPanel.setScene(scene);
                
                // Auto-navigate when video ends
                mediaPlayer.setOnEndOfMedia(this::navigateToNextScreen);
                
                mediaPlayer.play();
                
                mediaPlayer.setOnError(() -> {
                    System.err.println("Media error: " + mediaPlayer.getError().getMessage());
                    navigateToNextScreen(); // Proceed even if video fails
                });
            } catch (Exception e) {
                System.err.println("Error loading video: " + e.getMessage());
                navigateToNextScreen(); // Proceed if video can't load
            }
        });
    }

    private void navigateToNextScreen() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        NavigationManager.goToGameFromHowToPlay(this, category, level, theme, playerName);
    }

    @Override
    public void dispose() {
        if (mediaPlayer != null) {
            javafx.application.Platform.runLater(mediaPlayer::dispose);
        }
        super.dispose();
    }
}