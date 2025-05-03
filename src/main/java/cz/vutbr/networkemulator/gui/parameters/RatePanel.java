package cz.vutbr.networkemulator.gui.parameters;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cz.vutbr.networkemulator.utils.NetworkEmulatorUtils;
import cz.vutbr.networkemulator.verification.RangeVerifier;
import net.miginfocom.swing.MigLayout;

public class RatePanel extends JPanel {

    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 10000;
    private static final int MIN_OVERHEAD = 0;
    private static final int MAX_OVERHEAD = 10000;

    private final JTextField valueField;
    private final JTextField overheadField;

    public RatePanel() {
        // layout and border
        setLayout(new MigLayout("insets 5", "[][grow][][grow]", "grow"));
        setBorder(BorderFactory.createTitledBorder(NetworkEmulatorUtils.getString("title_rate")));

        // initialisations
        valueField = new JTextField(10);
        overheadField = new JTextField(10);

        // labels
        JLabel valueLabel = new JLabel(NetworkEmulatorUtils.getString("label_rate"));
        JLabel overheadLabel = new JLabel(NetworkEmulatorUtils.getString("label_overhead"));
        valueLabel.setLabelFor(valueLabel);
        overheadLabel.setLabelFor(overheadField);

        // verifiers
        valueField.setInputVerifier(new RangeVerifier(MIN_VALUE, MAX_VALUE, true));
        overheadField.setInputVerifier(new RangeVerifier(MIN_OVERHEAD, MAX_OVERHEAD, false));

        // add components
        add(valueLabel);
        add(valueField, "growx, growy, gapright 5");
        add(overheadLabel);
        add(overheadField, "growx, growy");
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
