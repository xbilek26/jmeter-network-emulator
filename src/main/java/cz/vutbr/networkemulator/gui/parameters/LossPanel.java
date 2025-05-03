package cz.vutbr.networkemulator.gui.parameters;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cz.vutbr.networkemulator.model.parameters.Loss;
import cz.vutbr.networkemulator.utils.NetworkEmulatorUtils;
import cz.vutbr.networkemulator.verification.RangeVerifier;
import net.miginfocom.swing.MigLayout;

public class LossPanel extends JPanel {

    private final JTextField valueField;
    private final JTextField correlationField;

    public LossPanel() {
        // layout and border
        setLayout(new MigLayout("insets 5", "[][grow][][grow]", "grow"));
        setBorder(BorderFactory.createTitledBorder(NetworkEmulatorUtils.getString("title_loss")));

        // initialisations
        valueField = new JTextField(10);
        correlationField = new JTextField(10);

        // labels
        JLabel valueLabel = new JLabel(NetworkEmulatorUtils.getString("label_loss_value"));
        JLabel correlationLabel = new JLabel(NetworkEmulatorUtils.getString("label_loss_correlation"));
        valueLabel.setLabelFor(valueField);
        correlationLabel.setLabelFor(correlationField);

        // verifiers
        valueField.setInputVerifier(new RangeVerifier(Loss.MIN_VALUE, Loss.MAX_VALUE, true));
        correlationField.setInputVerifier(new RangeVerifier(Loss.MIN_CORRELATION, Loss.MAX_CORRELATION, true));

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
