package cz.vutbr.networkemulator.validation;

import java.util.regex.Pattern;

public class Validation {

    public static String percent(String stringToValid) {
        if (Pattern.matches("^(\\d+|\\d+[.]\\d+)%$", stringToValid)) {
            return "Ok.";
        } else {
            return "Not Ok!!";
        }
    }

}
