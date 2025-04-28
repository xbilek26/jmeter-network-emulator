package cz.vutbr.networkemulator.utils;

import java.util.ResourceBundle;

public class Messages {
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("cz.vutbr.networkemulator.messages");

    public static String get(String key) {
        return BUNDLE.getString(key);
    }
}