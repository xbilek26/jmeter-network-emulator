package cz.vutbr.networkemulator.model.parameters;

import javax.swing.table.DefaultTableModel;

import cz.vutbr.networkemulator.utils.NetworkEmulatorUtils;

public class Duplication extends Parameter {

    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 100;
    public static final int MIN_CORRELATION = 0;
    public static final int MAX_CORRELATION = 100;

    private String correlation;

    public Duplication(String value, String correlation) {
        super(value);
        this.correlation = correlation;
    }

    public String getCorrelation() {
        return correlation;
    }

    public void setCorrelation(String correlation) {
        this.correlation = correlation;
    }

    public boolean isCorrelationSet() {
        return correlation != null && !correlation.isEmpty();
    }

    @Override
    public void appendToCommand(StringBuilder cmd) {
        if (isValueSet()) {
            cmd.append(String.format(" duplicate %s%%", getValue()));
            if (isCorrelationSet()) {
                cmd.append(String.format(" %s%%", correlation));
            }
        }
    }

    @Override
    public void appendToTable(DefaultTableModel tableModel) {
        if (isValueSet()) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%s%%", getValue()));
            if (isCorrelationSet()) {
                sb.append(String.format(", corr=%s%%", correlation));
            }

            tableModel.addRow(new Object[] { NetworkEmulatorUtils.getString("table_duplication"), sb.toString() });
        }
    }

}
