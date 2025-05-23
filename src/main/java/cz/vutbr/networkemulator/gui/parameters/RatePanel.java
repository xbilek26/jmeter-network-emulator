package cz.vutbr.networkemulator.gui.parameters;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cz.vutbr.networkemulator.model.parameters.Rate;
import cz.vutbr.networkemulator.utils.EmulatorUtils;
import cz.vutbr.networkemulator.verification.RangeVerifier;
import net.miginfocom.swing.MigLayout;

public class RatePanel extends JPanel {

    private final JTextField valueField;
    private final JTextField overheadField;

    public RatePanel() {
        // layout and border
        setLayout(new MigLayout("insets 5", "[][grow][][grow]", "grow"));
        setBorder(BorderFactory.createTitledBorder(EmulatorUtils.getString("title_rate")));

        // initialisations
        valueField = new JTextField(10);
        overheadField = new JTextField(10);

        // labels
        JLabel valueLabel = new JLabel(EmulatorUtils.getString("label_rate"));
        JLabel overheadLabel = new JLabel(EmulatorUtils.getString("label_overhead"));
        valueLabel.setLabelFor(valueLabel);
        overheadLabel.setLabelFor(overheadField);

        // verifiers
        valueField.setInputVerifier(new RangeVerifier(Rate.MIN_VALUE, Rate.MAX_VALUE, Rate.IS_VALUE_DOUBLE));
        overheadField.setInputVerifier(new RangeVerifier(Rate.MIN_OVERHEAD, Rate.MAX_OVERHEAD, Rate.IS_OVERHEAD_DOUBLE));

        // add components
        add(valueLabel);
        add(valueField, "growx, growy, gapright 5");
        add(overheadLabel);
        add(overheadField, "growx, growy");
    }

    public String getValue() {
        return valueField.getText().trim();
    }

    public void setValue(String value) {
        valueField.setText(value);
    }

    public String getOverhead() {
        return overheadField.getText().trim();
    }

    public void setOverhead(String overhead) {
        overheadField.setText(overhead);
    }
}
