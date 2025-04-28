package cz.vutbr.networkemulator.utils;

import java.util.ResourceBundle;

public class NetworkEmulator {
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("cz.vutbr.networkemulator.messages");

    public static String getString(String key) {
        return BUNDLE.getString(key);
    }
}