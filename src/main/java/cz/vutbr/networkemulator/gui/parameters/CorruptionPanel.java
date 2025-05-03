package cz.vutbr.networkemulator.gui.parameters;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cz.vutbr.networkemulator.model.parameters.Corruption;
import cz.vutbr.networkemulator.utils.EmulatorUtils;
import cz.vutbr.networkemulator.verification.RangeVerifier;
import net.miginfocom.swing.MigLayout;

public class CorruptionPanel extends JPanel {

    private final JTextField valueField;
    private final JTextField correlationField;

    public CorruptionPanel() {
        // layout and border
        setLayout(new MigLayout("insets 5", "[][grow][][grow]", "grow"));
        setBorder(BorderFactory.createTitledBorder(EmulatorUtils.getString("title_corruption")));

        // initialisations
        valueField = new JTextField(10);
        correlationField = new JTextField(10);


        // labels
        JLabel valueLabel = new JLabel(EmulatorUtils.getString("label_corruption_value"));
        valueLabel.setLabelFor(valueField);
        JLabel correlationLabel = new JLabel(EmulatorUtils.getString("label_corruption_correlation"));
        correlationLabel.setLabelFor(correlationField);


        // verifiers
        valueField.setInputVerifier(new RangeVerifier(Corruption.MIN_VALUE, Corruption.MAX_VALUE, Corruption.IS_VALUE_DOUBLE));
        correlationField.setInputVerifier(new RangeVerifier(Corruption.MIN_VALUE, Corruption.MAX_VALUE, Corruption.IS_VALUE_DOUBLE));

        // add components
        add(valueLabel);
        add(valueField, "growx, growy, gapright 5");
        add(correlationLabel);
        add(correlationField, "growx, growy");
    }

    public String getValue() {
        return valueField.getText().trim();
    }

    public void setValue(String value) {
        valueField.setText(value);
    }

    public String getCorrelation() {
        return correlationField.getText().trim();
    }

    public void setCorrelation(String correlation) {
        correlationField.setText(correlation);
    }
}
