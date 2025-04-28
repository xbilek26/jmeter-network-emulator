package cz.vutbr.networkemulator.gui.parameters;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cz.vutbr.networkemulator.utils.Constants;
import cz.vutbr.networkemulator.verification.RangeVerifier;

public class DelayPanel extends JPanel {

    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 1000000;
    private static final int MIN_JITTER = 0;
    private static final int MAX_JITTER = 1000000;
    private static final int MIN_CORRELATION = 0;
    private static final int MAX_CORRELATION = 100;

    private final JTextField valueField;
    private final JTextField jitterField;
    private final JTextField correlationField;
    private final JComboBox<String> distributionsBox;

    public DelayPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder(Constants.TITLE_DELAY_PANEL));

        valueField = new JTextField(8);
        jitterField = new JTextField(8);
        correlationField = new JTextField(8);
        distributionsBox = new JComboBox<>(Constants.DISTRIBUTIONS);
        distributionsBox.setPreferredSize(new Dimension(180, correlationField.getPreferredSize().height));

        JLabel valueLabel = new JLabel(Constants.LABEL_DELAY_VALUE);
        valueLabel.setLabelFor(valueField);
        JLabel jitterLabel = new JLabel(Constants.LABEL_JITTER);
        jitterLabel.setLabelFor(jitterField);
        JLabel correlationLabel = new JLabel(Constants.LABEL_DELAY_CORRELATION);
        correlationLabel.setLabelFor(correlationField);
        JLabel distributionsLabel = new JLabel(Constants.LABEL_DISTRIBUTION);
        distributionsLabel.setLabelFor(distributionsBox);

        valueField.setInputVerifier(new RangeVerifier(MIN_VALUE, MAX_VALUE, true));
        jitterField.setInputVerifier(new RangeVerifier(MIN_JITTER, MAX_JITTER, true));
        correlationField.setInputVerifier(new RangeVerifier(MIN_CORRELATION, MAX_CORRELATION, true));
        jitterField.setEnabled(false);
        correlationField.setEnabled(false);
        distributionsBox.setEnabled(false);

        add(valueLabel);
        add(valueField);
        add(jitterLabel);
        add(jitterField);
        add(correlationLabel);
        add(correlationField);
        add(distributionsLabel);
        add(distributionsBox);
    }

    public String getValue() {
        return valueField.getText().trim();
    }

    public String getJitter() {
        return jitterField.getText().trim();
    }

    public String getCorrelation() {
        return correlationField.getText().trim();
    }

    public String getDistribution() {
        return (String) distributionsBox.getSelectedItem();
    }

    public void setValue(String value) {
        valueField.setText(value);
    }

    public void setJitter(String jitter) {
        jitterField.setText(jitter);
    }

    public void setCorrelation(String correlation) {
        correlationField.setText(correlation);
    }

    public void setDistribution(String distribution) {
        distributionsBox.setSelectedItem(distribution);
    }

}
