package cz.vutbr.networkemulator.model.parameters;

import javax.swing.table.DefaultTableModel;

import cz.vutbr.networkemulator.utils.EmulatorUtils;
import cz.vutbr.networkemulator.verification.RangeVerifier;

public class Corruption extends Parameter {

    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 100;

    public static final boolean IS_VALUE_DOUBLE = true;

    public Corruption(String value) {
        super(value);
    }

    @Override
    public void appendToCommand(StringBuilder cmd) {
        if (isValueValid()) {
            cmd.append(String.format(" corrupt %s%%", getValue()));
        }
    }

    @Override
    public void appendToTable(DefaultTableModel tableModel) {
        if (isValueValid()) {
            tableModel.addRow(new Object[] { EmulatorUtils.getString("table_corruption"), getValue() + "%" });
        }
    }

    @Override
    public boolean isValueValid() {
        return RangeVerifier.isValid(getValue(), MIN_VALUE, MAX_VALUE, IS_VALUE_DOUBLE);
    }
}
