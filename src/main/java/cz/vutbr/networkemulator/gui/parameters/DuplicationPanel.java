package cz.vutbr.networkemulator.gui.parameters;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cz.vutbr.networkemulator.utils.NetworkEmulatorUtils;
import cz.vutbr.networkemulator.verification.RangeVerifier;

public class DuplicationPanel extends JPanel {

    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 100;
    private static final int MIN_CORRELATION = 0;
    private static final int MAX_CORRELATION = 100;

    private final JTextField valueField;
    private final JTextField correlationField;

    public DuplicationPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder(NetworkEmulatorUtils.getString("title_duplication")));

        valueField = new JTextField(8);
        correlationField = new JTextField(8);

        JLabel valueLabel = new JLabel(NetworkEmulatorUtils.getString("label_duplication_value"));
        valueLabel.setLabelFor(valueField);
        JLabel correlationLabel = new JLabel(NetworkEmulatorUtils.getString("label_duplication_correlation"));
        correlationLabel.setLabelFor(correlationField);

        valueField.setInputVerifier(new RangeVerifier(MIN_VALUE, MAX_VALUE, true));
        correlationField.setInputVerifier(new RangeVerifier(MIN_CORRELATION, MAX_CORRELATION, true));

        add(valueLabel);
        add(valueField);
        add(Box.createHorizontalStrut(5));
        add(correlationLabel);
        add(correlationField);
    }

    public String getValue() {
        return valueField.getText().trim();
    }

    public String getCorrelation() {
        return correlationField.getText().trim();
    }

    public void setValue(String value) {
        valueField.setText(value);
    }

    public void setCorrelation(String correlation) {
        correlationField.setText(correlation);
    }

}
