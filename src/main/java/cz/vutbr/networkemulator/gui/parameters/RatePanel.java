package cz.vutbr.networkemulator.gui.parameters;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cz.vutbr.networkemulator.utils.NetworkEmulatorConstants;
import cz.vutbr.networkemulator.verification.RangeVerifier;

public class RatePanel extends JPanel {

    private final JTextField valueField;

    public RatePanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder(NetworkEmulatorConstants.TITLE_RATE_PANEL));
        
        valueField = new JTextField(8);

        JLabel valueLabel = new JLabel(NetworkEmulatorConstants.LABEL_RATE);
        valueLabel.setLabelFor(valueLabel);

        valueField.setInputVerifier(new RangeVerifier(0, 100000));

        add(valueLabel);
        add(valueField);
    }

    public String getValue() {
        return valueField.getText().trim();
    }

    public void setValue(String value) {
        valueField.setText(value);
    }

}
