package cz.vutbr.networkemulator.gui.parameters;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cz.vutbr.networkemulator.model.parameters.Corruption;
import cz.vutbr.networkemulator.utils.NetworkEmulatorUtils;
import cz.vutbr.networkemulator.verification.RangeVerifier;
import net.miginfocom.swing.MigLayout;

public class CorruptionPanel extends JPanel {

    private final JTextField valueField;

    public CorruptionPanel() {
        // layout and border
        setLayout(new MigLayout("insets 5", "[][grow]", "grow"));
        setBorder(BorderFactory.createTitledBorder(NetworkEmulatorUtils.getString("title_corruption")));

        // initialisations
        valueField = new JTextField(10);

        // labels
        JLabel valueLabel = new JLabel(NetworkEmulatorUtils.getString("label_corruption"));
        valueLabel.setLabelFor(valueField);

        // verifiers
        valueField.setInputVerifier(new RangeVerifier(Corruption.MIN_VALUE, Corruption.MAX_VALUE, true));

        // add components
        add(valueLabel);
        add(valueField, "growx, growy");
    }

    public String getValue() {
        return valueField.getText().trim();
    }

    public void setValue(String value) {
        valueField.setText(value);
    }

}
