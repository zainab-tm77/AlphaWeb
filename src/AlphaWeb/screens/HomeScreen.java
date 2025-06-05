package AlphaWeb.screens;

import AlphaWeb.utils.FontManager;
import AlphaWeb.utils.NavigationManager;
import AlphaWeb.utils.MusicPlayer;
import javax.swing.*;
import java.awt.*;

public class HomeScreen extends JFrame {
    public HomeScreen() {
        setTitle("AlphaWeb - Home");
        setSize(1200, 800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Start music only if not already playing
        MusicPlayer musicPlayer = MusicPlayer.getInstance();
        if (!musicPlayer.isPlaying()) {
            musicPlayer.play("/AlphaWeb/utils/music.wav");
        }

        BackgroundPanel panel = new BackgroundPanel("/AlphaWeb/assets/background.png");
        panel.setLayout(new BorderLayout());

        JLabel title = new JLabel("AlphaWeb", SwingConstants.CENTER);
        title.setFont(FontManager.getFont(200f));
        title.setForeground(Color.YELLOW);
        title.setBorder(BorderFactory.createEmptyBorder(200, 0, 50, 0));
        panel.add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50));

        RoundedShadowButton startButton = new RoundedShadowButton("GO!");
        startButton.setFont(FontManager.getFont(60f));
        startButton.setPreferredSize(new Dimension(150, 150));
        startButton.addActionListener(e -> {
            dispose();
            new NameInputScreen().setVisible(true);
        });
        buttonPanel.add(startButton);

        panel.add(buttonPanel, BorderLayout.CENTER);

        setContentPane(panel);
        setVisible(true);
    }
}