package cz.vutbr.networkemulator.gui.parameters;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cz.vutbr.networkemulator.utils.NetworkEmulatorUtils;
import cz.vutbr.networkemulator.verification.RangeVerifier;

public class RatePanel extends JPanel {

    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 10000;
    private static final int MIN_OVERHEAD = 0;
    private static final int MAX_OVERHEAD = 10000;

    private final JTextField valueField;
    private final JTextField overheadField;

    public RatePanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder(NetworkEmulatorUtils.getString("title_rate")));

        valueField = new JTextField(8);
        overheadField = new JTextField(8);

        JLabel valueLabel = new JLabel(NetworkEmulatorUtils.getString("label_rate"));
        valueLabel.setLabelFor(valueLabel);
        JLabel overheadLabel = new JLabel(NetworkEmulatorUtils.getString("label_overhead"));
        overheadLabel.setLabelFor(overheadField);

        valueField.setInputVerifier(new RangeVerifier(MIN_VALUE, MAX_VALUE, true));
        overheadField.setInputVerifier(new RangeVerifier(MIN_OVERHEAD, MAX_OVERHEAD, false));

        add(valueLabel);
        add(valueField);
        add(overheadLabel);
        add(overheadField);
    }

    public String getValue() {
        return valueField.getText().trim();
    }

    public String getOverhead() {
        return overheadField.getText().trim();
    }

    public void setValue(String value) {
        valueField.setText(value);
    }

    public void setOverhead(String overhead) {
        overheadField.setText(overhead);
    }

}
