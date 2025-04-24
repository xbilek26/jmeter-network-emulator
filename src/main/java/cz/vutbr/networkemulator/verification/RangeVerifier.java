package cz.vutbr.networkemulator.verification;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.jmeter.util.JMeterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RangeVerifier extends InputVerifier {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(RangeVerifier.class);

    private final int min, max;

    public RangeVerifier(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public static boolean isValid(String text, int min, int max) {
        try {
            if (text == null || text.trim().isEmpty()) {
                return false;
            }
            int value = Integer.parseInt(text.trim());
            return value >= min && value <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean verify(JComponent source) {
        if (source instanceof JTextField field) {

            String input = field.getText().trim();

            if (input.isEmpty()) {
                return true;
            }

            try {
                int value = Integer.parseInt(input);
                boolean isInRange = value >= min && value <= max;

                if (isInRange) {
                    return true;
                } else {
                    JOptionPane.showMessageDialog(
                            null,
                            String.format("Enter a value between %d and %d.", min, max),
                            JMeterUtils.getLocaleString("Bad Value"),
                            JOptionPane.ERROR_MESSAGE
                    );

                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        null,
                        String.format("Enter a number.", min, max),
                        JMeterUtils.getLocaleString("Bad Value"),
                        JOptionPane.ERROR_MESSAGE
                );

                return false;
            }
        }

        return true;
    }
}
