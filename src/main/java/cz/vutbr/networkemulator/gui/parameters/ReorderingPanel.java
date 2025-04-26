package cz.vutbr.networkemulator.gui.parameters;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import cz.vutbr.networkemulator.utils.Constants;
import cz.vutbr.networkemulator.verification.RangeVerifier;

public class ReorderingPanel extends JPanel {

    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 100;
    private static final int MIN_CORRELATION = 0;
    private static final int MAX_CORRELATION = 100;

    private final JTextField valueField;
    private final JTextField correlationField;

    public ReorderingPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder(Constants.TITLE_REORDERING_PANEL));

        valueField = new JTextField(8);
        correlationField = new JTextField(8);

        JLabel valueLabel = new JLabel(Constants.LABEL_REORDERING_VALUE);
        valueLabel.setLabelFor(valueField);
        JLabel correlationLabel = new JLabel(Constants.LABEL_REORDERING_CORRELATION);
        correlationLabel.setLabelFor(correlationField);

        valueField.setInputVerifier(new RangeVerifier(MIN_VALUE, MAX_VALUE, true));
        correlationField.setInputVerifier(new RangeVerifier(MIN_CORRELATION, MAX_CORRELATION, true));
        correlationField.setEnabled(false);
        addInputListener();

        add(valueLabel);
        add(valueField);
        add(correlationLabel);
        add(correlationField);
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
        boolean isValueValid = RangeVerifier.isValid(valueField.getText(), MIN_VALUE, MAX_VALUE, true);
        correlationField.setEnabled(isValueValid);
    }

    public String getValue() {
        return valueField.getText().trim();
    }

    public String getCorrelation() {
        return correlationField.getText().trim();
    }

    public void setValue(String value) {
        valueField.setText(value);
        updateFields();
    }

    public void setCorrelation(String correlation) {
        correlationField.setText(correlation);
    }

}
