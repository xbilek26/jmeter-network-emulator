package cz.vutbr.networkemulator.model.parameters;

import javax.swing.table.DefaultTableModel;

import cz.vutbr.networkemulator.utils.EmulatorUtils;
import cz.vutbr.networkemulator.verification.RangeVerifier;

public class Limit extends Parameter {

    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = Integer.MAX_VALUE;

    public static final boolean IS_VALUE_DOUBLE = false;

    public Limit(String value) {
        super(value);
    }

    @Override
    public void appendToCommand(StringBuilder cmd) {
        if (isValueValid()) {
            cmd.append(String.format(" limit %s", getValue()));
        }
    }

    @Override
    public void appendToTable(DefaultTableModel tableModel) {
        if (isValueValid()) {
            tableModel.addRow(new Object[] { EmulatorUtils.getString("table_limit"), String.format("%spackets", getValue()) });
        }
    }

    @Override
    public boolean isValueValid() {
        return RangeVerifier.isValid(getValue(), MIN_VALUE, MAX_VALUE, IS_VALUE_DOUBLE);
    }
}
