package AlphaWeb;

import AlphaWeb.screens.HomeScreen;
import AlphaWeb.utils.MusicPlayer;

public class AlphaWeb {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            // Start music before HomeScreen
            MusicPlayer musicPlayer = MusicPlayer.getInstance();
            musicPlayer.play("/AlphaWeb/utils/music.wav");
            
            // Display HomeScreen
            new HomeScreen().setVisible(true);
        });
    }
}