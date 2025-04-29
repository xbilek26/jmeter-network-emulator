package cz.vutbr.networkemulator.gui.parameters;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cz.vutbr.networkemulator.utils.NetworkEmulatorUtils;
import cz.vutbr.networkemulator.verification.RangeVerifier;

public class CorruptionPanel extends JPanel {

    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 100;

    private final JTextField valueField;

    public CorruptionPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder(NetworkEmulatorUtils.getString("title_corruption")));
        
        valueField = new JTextField(8);

        JLabel valueLabel = new JLabel(NetworkEmulatorUtils.getString("label_corruption"));
        valueLabel.setLabelFor(valueField);

        valueField.setInputVerifier(new RangeVerifier(MIN_VALUE, MAX_VALUE, true));

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
