package cz.vutbr.networkemulator.model.parameters;

import javax.swing.table.DefaultTableModel;

public class Rate extends Parameter {

    private String overhead;

    public Rate(String value, String overhead) {
        super("Rate", value);
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
                cmd.append(String.format(" %s", getOverhead()));
            }
        }
    }

    @Override
    public void appendToTable(DefaultTableModel tableModel) {
        if (isValueSet()) {
            StringBuilder sb = new StringBuilder();
            sb.append(getValue()).append("kbps");
            if (isOverheadSet()) {
                sb.append(", overhead=").append(getOverhead()).append("B");
            }
            tableModel.addRow(new Object[]{getName(), sb.toString()});
        }
    }
}
