package AlphaWeb.screens;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private final Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        java.net.URL resource = getClass().getResource(imagePath);
        if (resource != null) {
            backgroundImage = new ImageIcon(resource).getImage();
        } else {
            System.err.println("Background image not found: " + imagePath);
            backgroundImage = null;
        }
        setOpaque(false);
        setLayout(new BorderLayout()); // Ensure BorderLayout is set by default
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}