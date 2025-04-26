package cz.vutbr.networkemulator.model.parameters;

import javax.swing.table.DefaultTableModel;

public class Duplication extends Parameter {

    private String correlation;

    public Duplication(String value, String correlation) {
        super("Dupliction", value);
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
                cmd.append(String.format(" %s%%", getCorrelation()));
            }
        }
    }

    @Override
    public void appendToTable(DefaultTableModel model) {
        if (isValueSet()) {
            StringBuilder sb = new StringBuilder();
            sb.append(getValue()).append("%");
            if (isCorrelationSet()) {
                sb.append(", corr=").append(getCorrelation()).append("%");
            }

            model.addRow(new Object[]{getName(), sb.toString()});
        }
    }

}
