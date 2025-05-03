package cz.vutbr.networkemulator.model.parameters;

import javax.swing.table.DefaultTableModel;

import cz.vutbr.networkemulator.utils.NetworkEmulatorUtils;

public class Corruption extends Parameter {

    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 100;

    public Corruption(String value) {
        super(value);
    }

    @Override
    public void appendToCommand(StringBuilder cmd) {
        if (isValueSet()) {
            cmd.append(String.format(" corrupt %s%%", getValue()));
        }
    }

    @Override
    public void appendToTable(DefaultTableModel tableModel) {
        if (isValueSet()) {
            tableModel.addRow(new Object[] { NetworkEmulatorUtils.getString("table_corruption"), getValue() + "%" });
        }
    }
}
