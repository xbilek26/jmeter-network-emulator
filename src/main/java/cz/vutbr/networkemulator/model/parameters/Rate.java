package cz.vutbr.networkemulator.model.parameters;

import javax.swing.table.DefaultTableModel;

import cz.vutbr.networkemulator.utils.EmulatorUtils;
import cz.vutbr.networkemulator.verification.RangeVerifier;

public class Rate extends Parameter {

    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = Integer.MAX_VALUE;
    public static final int MIN_OVERHEAD = 0;
    public static final int MAX_OVERHEAD = Integer.MAX_VALUE;

    public static final boolean IS_VALUE_DOUBLE = true;
    public static final boolean IS_OVERHEAD_DOUBLE = false;

    private final String overhead;

    public Rate(String value, String overhead) {
        super(value);
        this.overhead = overhead;
    }

    @Override
    public void appendToCommand(StringBuilder cmd) {
        if (isValueValid()) {
            cmd.append(String.format(" rate %skbit", value));
            if (isOverheadValid()) {
                cmd.append(String.format(" %s", overhead));
            }
        }
    }

    @Override
    public void appendToTable(DefaultTableModel tableModel) {
        if (isValueValid()) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%skbps", value));
            if (isOverheadValid()) {
                sb.append(String.format(", overhead=%sB", overhead));
            }
            tableModel.addRow(new Object[] { EmulatorUtils.getString("table_rate"), sb.toString() });
        }
    }

    @Override
    public boolean isValueValid() {
        return RangeVerifier.isValid(value, MIN_VALUE, MAX_VALUE, IS_VALUE_DOUBLE);
    }

    public boolean isOverheadValid() {
        return RangeVerifier.isValid(overhead, MIN_OVERHEAD, MAX_OVERHEAD, IS_OVERHEAD_DOUBLE);
    }
}
