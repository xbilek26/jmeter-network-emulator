package cz.vutbr.networkemulator.model.parameters;

import javax.swing.table.DefaultTableModel;

public class Delay extends Parameter {

    private String jitter;
    private String correlation;
    private String distribution;

    public Delay(String delay, String jitter, String correlation, String distribution) {
        super("Delay", delay);
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
                cmd.append(String.format(" %sms", getJitter()));
                if (isCorrelationSet()) {
                    cmd.append(String.format(" %s%%", getCorrelation()));
                }
                if (isDistributionSet()) {
                    cmd.append(String.format(" distribution %s", getDistribution()));
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
                sb.append(" ±").append(getJitter()).append("ms");
                if (isCorrelationSet()) {
                    sb.append(", corr=").append(getCorrelation()).append("%");
                }
                if (isDistributionSet()) {
                    sb.append(", dist=").append(getDistribution());
                }
            }

            tableModel.addRow(new Object[]{"Delay", sb.toString()});
        }
    }
}
