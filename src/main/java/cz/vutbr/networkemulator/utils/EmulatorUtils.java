package cz.vutbr.networkemulator.utils;

import java.awt.Image;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

public class EmulatorUtils {
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("cz.vutbr.networkemulator.messages");

    public static String getString(String key) {
        return BUNDLE.getString(key);
    }

    public static ImageIcon getImage(String name) {
        URL url = EmulatorUtils.class.getClassLoader().getResource("cz/vutbr/networkemulator/images/" + name.trim());
        if (url != null) {
            return new ImageIcon(url);
        } else {
            return null;
        }
    }

    public static ImageIcon getScaledIcon(ImageIcon icon, int width, int height) {
        Image image = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }
}