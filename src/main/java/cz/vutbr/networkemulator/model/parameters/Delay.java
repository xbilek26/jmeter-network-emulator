package cz.vutbr.networkemulator.model.parameters;

import javax.swing.table.DefaultTableModel;

import cz.vutbr.networkemulator.utils.NetworkEmulatorUtils;

public class Delay extends Parameter {

    private String jitter;
    private String correlation;
    private String distribution;

    public Delay(String delay, String jitter, String correlation, String distribution) {
        super(delay);
        this.jitter = jitter;
        this.correlation = correlation;
        this.distribution = distribution;
    }

    public String getJitter() {
        return jitter;
    }

    public void setJitter(String jitter) {
        this.jitter = jitter;
    }

    public boolean isJitterSet() {
        return jitter != null && !jitter.isEmpty();
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

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    public boolean isDistributionSet() {
        return distribution != null && !distribution.isEmpty();
    }

    @Override
    public void appendToCommand(StringBuilder cmd) {
        if (isValueSet()) {
            cmd.append(String.format(" delay %sms", getValue()));
            if (isJitterSet()) {
                cmd.append(String.format(" %sms", this.jitter));
                if (isCorrelationSet()) {
                    cmd.append(String.format(" %s%%", this.correlation));
                }
                if (isDistributionSet()) {
                    cmd.append(String.format(" distribution %s", this.distribution));
                }
            }
        }
    }

    @Override
    public void appendToTable(DefaultTableModel tableModel) {
        if (isValueSet()) {
            StringBuilder sb = new StringBuilder();
            sb.append(getValue()).append("ms");
            if (isJitterSet()) {
                sb.append(String.format(" ±%sms", this.jitter));
                if (isCorrelationSet()) {
                    sb.append(String.format(", corr=%s%%", this.correlation));
                }
                if (isDistributionSet()) {
                    sb.append(String.format(", dist=%s", this.distribution));
                }
            }

            tableModel.addRow(new Object[]{NetworkEmulatorUtils.getString("table_delay"), sb.toString()});
        }
    }
}
