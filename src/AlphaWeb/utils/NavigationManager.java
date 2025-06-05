package AlphaWeb.utils;

import AlphaWeb.screens.*;
import AlphaWeb.core.HighScore;
import javax.swing.JFrame;

public class NavigationManager {
    private static String savedCategory;
    private static String savedLevel;
    private static String savedTheme;
    private static String savedPlayerName;
    private static HowToPlayScreen currentHowToPlayScreen;
    
    public static void goToCategory(JFrame currentFrame, String playerName) {
        currentFrame.dispose();
        new CategoryScreen(playerName).setVisible(true);
    }

    public static void goToLevel(JFrame currentFrame, String category, String playerName) {
        currentFrame.dispose();
        new LevelScreen(category, playerName).setVisible(true);
    }

    public static void goToGame(JFrame currentFrame, String category, String level, String theme, String playerName) {
        currentFrame.dispose();
        // Store parameters before showing HowToPlay
        savedCategory = category;
        savedLevel = level;
        savedTheme = theme;
        savedPlayerName = playerName;
        new HowToPlayScreen(category, level, theme, playerName).setVisible(true);
    }

    public static void goToGameFromHowToPlay(JFrame currentFrame, String category, 
                                           String level, String theme, String playerName) {
        // Save parameters
        savedCategory = category;
        savedLevel = level;
        savedTheme = theme;
        savedPlayerName = playerName;
        
        if (currentFrame instanceof HowToPlayScreen) {
            currentHowToPlayScreen = (HowToPlayScreen) currentFrame;
            currentHowToPlayScreen.setVisible(false);
        }
        new GameScreen(category, level, theme, playerName).setVisible(true);
    }

    public static void returnToHowToPlay(JFrame currentFrame) {
        currentFrame.dispose();
        if (currentHowToPlayScreen != null) {
            currentHowToPlayScreen.restartVideo();
            currentHowToPlayScreen.setVisible(true);
        } else {
            // Create new instance with saved parameters
            new HowToPlayScreen(savedCategory, savedLevel, savedTheme, savedPlayerName).setVisible(true);
        }
    }
    
    public static void showResult(JFrame currentFrame, int score, String level, String category, 
                                String theme, String playerName, int totalTime, boolean hasCompletedMaster) {
        currentFrame.dispose();
        HighScore highScore = new HighScore();
        new ResultScreen(score, level, score >= 50 ? "Win" : "Loss", 
                        highScore.getScores(), playerName, totalTime, 
                        hasCompletedMaster, theme, category).setVisible(true);
    }

    public static void goBack(JFrame currentFrame, java.util.function.Supplier<JFrame> previousScreenSupplier) {
        currentFrame.dispose();
        previousScreenSupplier.get().setVisible(true);
    }

    public static void exit() {
        System.exit(0);
    }
}