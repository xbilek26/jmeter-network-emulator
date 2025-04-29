package cz.vutbr.networkemulator.model.parameters;

import javax.swing.table.DefaultTableModel;

import cz.vutbr.networkemulator.utils.NetworkEmulator;

public class Duplication extends Parameter {

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
                cmd.append(String.format(" %s%%", this.correlation));
            }
        }
    }

    @Override
    public void appendToTable(DefaultTableModel tableModel) {
        if (isValueSet()) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%s%%", getValue()));
            if (isCorrelationSet()) {
                sb.append(String.format(", corr=%s%%", this.correlation));
            }

            tableModel.addRow(new Object[]{NetworkEmulator.getString("table_duplication"), sb.toString()});
        }
    }

}
