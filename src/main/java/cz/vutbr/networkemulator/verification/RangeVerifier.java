package cz.vutbr.networkemulator.verification;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

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

    @Override
    public boolean verify(JComponent input) {
        if (input instanceof JTextField field) {

            String text = field.getText().trim();

            if (text.isEmpty()) {
                return true;    
            }

            try {
                int value = Integer.parseInt(field.getText());
                return value >= min && value <= max;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return true;
    }
}