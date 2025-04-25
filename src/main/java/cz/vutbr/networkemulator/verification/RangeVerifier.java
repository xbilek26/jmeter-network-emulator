package cz.vutbr.networkemulator.verification;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.jmeter.util.JMeterUtils;

public class RangeVerifier extends InputVerifier {

    private final double min, max;
    private final boolean isDouble;

    public RangeVerifier(double min, double max, boolean allowDecimal) {
        this.min = min;
        this.max = max;
        this.isDouble = allowDecimal;
    }

    public static boolean isValid(String text, double min, double max, boolean isDouble) {
        try {
            if (text == null || text.trim().isEmpty()) {
                return false;
            }
            if (isDouble) {
                double value = Double.parseDouble(text.trim());
                return value >= min && value <= max;
            } else {
                int value = Integer.parseInt(text.trim());
                return value >= min && value <= max;
            }
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
                boolean isInRange;
                if (isDouble) {
                    double value = Double.parseDouble(input);
                    isInRange = value >= min && value <= max;
                } else {
                    int value = Integer.parseInt(input);
                    isInRange = value >= min && value <= max;
                }

                if (isInRange) {
                    return true;
                } else {
                    String minFormatted = (min % 1 == 0) ? String.format("%d", (int) min) : String.format("%.2f", min);
                    String maxFormatted = (max % 1 == 0) ? String.format("%d", (int) max) : String.format("%.2f", max);

                    JOptionPane.showMessageDialog(
                            null,
                            String.format("Enter a value between %s and %s.", minFormatted, maxFormatted),
                            JMeterUtils.getLocaleString("Bad Value"),
                            JOptionPane.ERROR_MESSAGE
                    );
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Enter a valid number.",
                        JMeterUtils.getLocaleString("Bad Value"),
                        JOptionPane.ERROR_MESSAGE
                );
                return false;
            }
        }

        return true;
    }
}
