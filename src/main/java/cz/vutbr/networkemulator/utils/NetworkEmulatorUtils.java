package cz.vutbr.networkemulator.utils;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

public class NetworkEmulatorUtils {
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("cz.vutbr.networkemulator.messages");

    public static String getString(String key) {
        return BUNDLE.getString(key);
    }

    public static ImageIcon getImage(String name) {
        URL url = NetworkEmulatorUtils.class.getClassLoader().getResource("cz/vutbr/networkemulator/images/" + name.trim());
        if (url != null) {
            return new ImageIcon(url);
        } else {
            return null;
        }
    }
}