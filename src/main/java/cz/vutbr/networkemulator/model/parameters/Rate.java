package cz.vutbr.networkemulator.model.parameters;

import javax.swing.table.DefaultTableModel;

import cz.vutbr.networkemulator.utils.NetworkEmulator;

public class Rate extends Parameter {

    private String overhead;

    public Rate(String value, String overhead) {
        super(value);
        this.overhead = overhead;
    }

    public String getOverhead() {
        return overhead;
    }

    public void setOverhead(String overhead) {
        this.overhead = overhead;
    }

    public boolean isOverheadSet() {
        return overhead != null && !overhead.isEmpty();
    }

    @Override
    public void appendToCommand(StringBuilder cmd) {
        if (isValueSet()) {
            cmd.append(String.format(" rate %skbit", getValue()));
            if (isOverheadSet()) {
                cmd.append(String.format(" %s", this.overhead));
            }
        }
    }

    @Override
    public void appendToTable(DefaultTableModel tableModel) {
        if (isValueSet()) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%skbps", getValue()));
            if (isOverheadSet()) {
                sb.append(String.format(", overhead=%sB", this.overhead));
            }
            tableModel.addRow(new Object[]{NetworkEmulator.getString("table_rate"), sb.toString()});
        }
    }
}
