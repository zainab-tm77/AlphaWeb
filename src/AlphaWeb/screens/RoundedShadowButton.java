package AlphaWeb.screens;

import AlphaWeb.utils.FontManager;
import javax.swing.*;
import java.awt.*;

public class RoundedShadowButton extends JButton {
    public RoundedShadowButton(String text) {
        super(text);
        setFont(FontManager.getFont(40f));
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        int width = getWidth();
        int height = getHeight();

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(5, 5, width - 10, height - 10, 50, 50);

        g2.setColor(new Color(220, 20, 60));
        g2.fillRoundRect(0, 0, width - 10, height - 10, 50, 50);

        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    public void paintBorder(Graphics g) {
        // No border
    }
}
