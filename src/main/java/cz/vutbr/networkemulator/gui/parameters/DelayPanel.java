package cz.vutbr.networkemulator.gui.parameters;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cz.vutbr.networkemulator.model.parameters.Delay;
import cz.vutbr.networkemulator.utils.EmulatorUtils;
import cz.vutbr.networkemulator.verification.RangeVerifier;
import net.miginfocom.swing.MigLayout;

public class DelayPanel extends JPanel {

    private static final String[] DISTRIBUTIONS = {
            // "uniform", not present in /usr/lib/tc
            "",
            "normal",
            "pareto",
            "paretonormal"
    };

    private final JTextField valueField;
    private final JTextField jitterField;
    private final JTextField correlationField;
    private final JComboBox<String> distributionsBox;

    public DelayPanel() {
        // layout and border
        setLayout(new MigLayout("insets 5", "[][grow][][grow][][grow][][grow]", "grow"));
        setBorder(BorderFactory.createTitledBorder(EmulatorUtils.getString("title_delay")));

        // initialisations
        valueField = new JTextField(10);
        jitterField = new JTextField(10);
        correlationField = new JTextField(10);
        distributionsBox = new JComboBox<>(DISTRIBUTIONS);

        // labels
        JLabel valueLabel = new JLabel(EmulatorUtils.getString("label_delay_value"));
        JLabel jitterLabel = new JLabel(EmulatorUtils.getString("label_delay_jitter"));
        JLabel correlationLabel = new JLabel(EmulatorUtils.getString("label_delay_correlation"));
        JLabel distributionsLabel = new JLabel(EmulatorUtils.getString("label_delay_distribution"));
        valueLabel.setLabelFor(valueField);
        jitterLabel.setLabelFor(jitterField);
        correlationLabel.setLabelFor(correlationField);
        distributionsLabel.setLabelFor(distributionsBox);

        // verifiers
        valueField.setInputVerifier(new RangeVerifier(Delay.MIN_VALUE, Delay.MAX_VALUE, Delay.IS_VALUE_DOUBLE));
        jitterField.setInputVerifier(new RangeVerifier(Delay.MIN_JITTER, Delay.MAX_JITTER, Delay.IS_JITTER_DOUBLE));
        correlationField.setInputVerifier(new RangeVerifier(Delay.MIN_CORRELATION, Delay.MAX_CORRELATION, Delay.IS_CORRELATION_DOUBLE));

        // add components
        add(valueLabel);
        add(valueField, "growx, growy, gapright 5");
        add(jitterLabel);
        add(jitterField, "growx, growy, gapright 5");
        add(correlationLabel);
        add(correlationField, "growx, growy, gapright 5");
        add(distributionsLabel);
        add(distributionsBox, "growx, growy");
    }

    public String getValue() {
        return valueField.getText().trim();
    }

    public void setValue(String value) {
        valueField.setText(value);
    }

    public String getJitter() {
        return jitterField.getText().trim();
    }

    public void setJitter(String jitter) {
        jitterField.setText(jitter);
    }

    public String getCorrelation() {
        return correlationField.getText().trim();
    }

    public void setCorrelation(String correlation) {
        correlationField.setText(correlation);
    }

    public String getDistribution() {
        return (String) distributionsBox.getSelectedItem();
    }

    public void setDistribution(String distribution) {
        distributionsBox.setSelectedItem(distribution);
    }
}
