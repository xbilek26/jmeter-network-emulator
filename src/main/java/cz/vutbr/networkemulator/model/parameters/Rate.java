package cz.vutbr.networkemulator.model.parameters;

import javax.swing.table.DefaultTableModel;

public class Rate extends Parameter {

    public Rate(String value) {
        super("Rate", value);
    }

    @Override
    public void appendToCommand(StringBuilder cmd) {
        if (isValueSet()) {
            cmd.append(String.format(" rate %skbit", getValue()));
        }
    }

    @Override
    public void appendToTable(DefaultTableModel model) {
        if (isValueSet()) {
            model.addRow(new Object[]{getName(), getValue() + "kbps"});
        }
    }
}
