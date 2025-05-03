package cz.vutbr.networkemulator.gui.parameters;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cz.vutbr.networkemulator.model.parameters.Limit;
import cz.vutbr.networkemulator.utils.EmulatorUtils;
import cz.vutbr.networkemulator.verification.RangeVerifier;
import net.miginfocom.swing.MigLayout;

public class LimitPanel extends JPanel {

    private final JTextField valueField;

    public LimitPanel() {
        // layout and border
        setLayout(new MigLayout("insets 5", "[][grow]", "grow"));
        setBorder(BorderFactory.createTitledBorder(EmulatorUtils.getString("title_limit")));

        // initialisations
        valueField = new JTextField(10);

        // labels
        JLabel valueLabel = new JLabel(EmulatorUtils.getString("label_limit"));
        valueLabel.setLabelFor(valueField);

        // verifiers
        valueField.setInputVerifier(new RangeVerifier(Limit.MIN_VALUE, Limit.MAX_VALUE, true));

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
