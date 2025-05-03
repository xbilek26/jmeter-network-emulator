package cz.vutbr.networkemulator.verification;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import cz.vutbr.networkemulator.utils.EmulatorUtils;

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
                    JOptionPane.showMessageDialog(
                            null,
                            EmulatorUtils.getString("msg_enter_valid_number"),
                            EmulatorUtils.getString("msg_bad_value"),
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        null,
                        EmulatorUtils.getString("msg_enter_valid_number"),
                        EmulatorUtils.getString("msg_bad_value"),
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }
}
