package cz.vutbr.networkemulator.gui.parameters;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cz.vutbr.networkemulator.model.parameters.Reordering;
import cz.vutbr.networkemulator.utils.EmulatorUtils;
import cz.vutbr.networkemulator.verification.RangeVerifier;
import net.miginfocom.swing.MigLayout;

public class ReorderingPanel extends JPanel {

    private final JTextField valueField;
    private final JTextField correlationField;

    public ReorderingPanel() {
        // layout and border
        setLayout(new MigLayout("insets 5", "[][grow][][grow]", "grow"));
        setBorder(BorderFactory.createTitledBorder(EmulatorUtils.getString("title_reordering")));

        // initialisations
        valueField = new JTextField(10);
        correlationField = new JTextField(10);

        // labels
        JLabel valueLabel = new JLabel(EmulatorUtils.getString("label_reordering_value"));
        JLabel correlationLabel = new JLabel(EmulatorUtils.getString("label_reordering_correlation"));
        valueLabel.setLabelFor(valueField);
        correlationLabel.setLabelFor(correlationField);

        // verifiers
        valueField.setInputVerifier(new RangeVerifier(Reordering.MIN_VALUE, Reordering.MAX_VALUE, Reordering.IS_VALUE_DOUBLE));
        correlationField.setInputVerifier(new RangeVerifier(Reordering.MIN_CORRELATION, Reordering.MAX_CORRELATION, true));

        // add components
        add(valueLabel);
        add(valueField, "growx, growy, gapright 5");
        add(correlationLabel);
        add(correlationField, "growx, growy");
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
