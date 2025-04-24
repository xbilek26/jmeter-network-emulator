package cz.vutbr.networkemulator.gui.parameters;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cz.vutbr.networkemulator.utils.NetworkEmulatorConstants;
import cz.vutbr.networkemulator.verification.RangeVerifier;

public class ReorderingPanel extends JPanel {

    private final JTextField valueField;
    private final JTextField correlationField;

    public ReorderingPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder(NetworkEmulatorConstants.TITLE_REORDERING_PANEL));

        valueField = new JTextField(8);
        correlationField = new JTextField(8);

        JLabel valueLabel = new JLabel(NetworkEmulatorConstants.LABEL_REORDERING_VALUE);
        valueLabel.setLabelFor(valueField);
        JLabel correlationLabel = new JLabel(NetworkEmulatorConstants.LABEL_REORDERING_CORRELATION);
        correlationLabel.setLabelFor(correlationField);

        valueField.setInputVerifier(new RangeVerifier(0, 100));
        correlationField.setInputVerifier(new RangeVerifier(0, 100));

        add(valueLabel);
        add(valueField);
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
