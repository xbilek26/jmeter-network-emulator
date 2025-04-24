package cz.vutbr.networkemulator.model.parameters;

import javax.swing.table.DefaultTableModel;

public class Corruption extends Parameter {

    public Corruption(String value) {
        super("Corruption", value);
    }

    @Override
    public void appendToCommand(StringBuilder cmd) {
        if (isValueSet()) {
            cmd.append(String.format(" corrupt %s%%", getValue()));
        }
    }

    @Override
    public void appendToTable(DefaultTableModel model) {
        if (isValueSet()) {
            model.addRow(new Object[]{getName(), getValue() + "%"});
        }
    }
}
