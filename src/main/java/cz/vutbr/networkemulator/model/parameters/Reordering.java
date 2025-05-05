package cz.vutbr.networkemulator.model.parameters;

import javax.swing.table.DefaultTableModel;

import cz.vutbr.networkemulator.utils.EmulatorUtils;
import cz.vutbr.networkemulator.verification.RangeVerifier;

public class Reordering extends Parameter {

    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 100;
    public static final int MIN_CORRELATION = 0;
    public static final int MAX_CORRELATION = 100;

    public static final boolean IS_VALUE_DOUBLE = true;
    public static final boolean IS_CORRELATION_DOUBLE = true;

    private final String correlation;

    public Reordering(String reordering, String correlation) {
        super(reordering);
        this.correlation = correlation;
    }

    @Override
    public void appendToCommand(StringBuilder cmd) {
        if (isValueValid()) {
            cmd.append(String.format(" reorder %s%%", value));
            if (isCorrelationValid()) {
                cmd.append(String.format(" %s%%", correlation));
            }
        }
    }

    @Override
    public void appendToTable(DefaultTableModel tableModel) {
        if (isValueValid()) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%s%%", value));
            if (isCorrelationValid()) {
                sb.append(String.format(", corr=%s%%", correlation));
            }

            tableModel.addRow(new Object[] { EmulatorUtils.getString("table_reordering"), sb.toString() });
        }
    }

    @Override
    public boolean isValueValid() {
        return RangeVerifier.isValid(value, MIN_VALUE, MAX_VALUE, IS_VALUE_DOUBLE);
    }

    public boolean isCorrelationValid() {
        return RangeVerifier.isValid(correlation, MIN_CORRELATION, MAX_CORRELATION, IS_CORRELATION_DOUBLE);
    }
}
