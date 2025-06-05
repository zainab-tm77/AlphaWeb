package AlphaWeb.utils;

import java.awt.*;
import java.io.InputStream;

public class FontManager {
    private static Font customFont;

    static {
        try {
            InputStream is = FontManager.class.getResourceAsStream("/AlphaWeb/assets/font.ttf");
            if (is != null) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(20f);
            } else {
                customFont = new Font("Arial", Font.PLAIN, 20);
            }
        } catch (Exception e) {
            customFont = new Font("Arial", Font.PLAIN, 20);
        }
    }

    public static Font getFont(float size) {
        return customFont.deriveFont(size);
    }
}