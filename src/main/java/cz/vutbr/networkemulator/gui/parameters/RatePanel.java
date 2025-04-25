package cz.vutbr.networkemulator.gui.parameters;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import cz.vutbr.networkemulator.utils.NetworkEmulatorConstants;
import cz.vutbr.networkemulator.verification.RangeVerifier;

public class RatePanel extends JPanel {

    private final JTextField valueField;
    private final JTextField overheadField;

    public RatePanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder(NetworkEmulatorConstants.TITLE_RATE_PANEL));

        valueField = new JTextField(8);
        overheadField = new JTextField(8);

        JLabel valueLabel = new JLabel(NetworkEmulatorConstants.LABEL_RATE);
        valueLabel.setLabelFor(valueLabel);
        JLabel overheadLabel = new JLabel(NetworkEmulatorConstants.LABEL_OVERHEAD);
        overheadLabel.setLabelFor(overheadField);

        valueField.setInputVerifier(new RangeVerifier(0, 100000));
        overheadField.setInputVerifier(new RangeVerifier(0, 100000));
        overheadField.setEnabled(false);
        addInputListener();

        add(valueLabel);
        add(valueField);
        add(overheadLabel);
        add(overheadField);
    }

    private void addInputListener() {
        DocumentListener inputListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFields();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFields();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFields();
            }
        };

        valueField.getDocument().addDocumentListener(inputListener);
    }

    private void updateFields() {
        boolean isValueValid = RangeVerifier.isValid(valueField.getText(), 0, 100000);
        overheadField.setEnabled(isValueValid);
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
