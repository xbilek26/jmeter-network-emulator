package cz.vutbr.networkemulator.model.parameters;

import javax.swing.table.DefaultTableModel;

import cz.vutbr.networkemulator.utils.EmulatorUtils;
import cz.vutbr.networkemulator.verification.RangeVerifier;

public class Delay extends Parameter {

    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 274877;
    public static final int MIN_JITTER = 0;
    public static final int MAX_JITTER = 274877;
    public static final int MIN_CORRELATION = 0;
    public static final int MAX_CORRELATION = 100;

    public static final boolean IS_VALUE_DOUBLE = true;
    public static final boolean IS_JITTER_DOUBLE = true;
    public static final boolean IS_CORRELATION_DOUBLE = true;

    private String jitter;
    private String correlation;
    private String distribution;

    public Delay(String delay, String jitter, String correlation, String distribution) {
        super(delay);
        this.jitter = jitter;
        this.correlation = correlation;
        this.distribution = distribution;
    }

    @Override
    public void appendToCommand(StringBuilder cmd) {
        if (isValueValid()) {
            cmd.append(String.format(" delay %sms", getValue()));
            if (isJitterValid()) {
                cmd.append(String.format(" %sms", jitter));
                if (isCorrelationValid()) {
                    cmd.append(String.format(" %s%%", correlation));
                }
                if (isDistributionValid()) {
                    cmd.append(String.format(" distribution %s", distribution));
                }
            }
        }
    }

    @Override
    public void appendToTable(DefaultTableModel tableModel) {
        if (isValueValid()) {
            StringBuilder sb = new StringBuilder();
            sb.append(getValue()).append("ms");
            if (isJitterValid()) {
                sb.append(String.format(" ±%sms", jitter));
                if (isCorrelationValid()) {
                    sb.append(String.format(", corr=%s%%", correlation));
                }
                if (isDistributionValid()) {
                    sb.append(String.format(", dist=%s", distribution));
                }
            }

            tableModel.addRow(new Object[] { EmulatorUtils.getString("table_delay"), sb.toString() });
        }
    }

    public String getJitter() {
        return jitter;
    }

    public void setJitter(String jitter) {
        this.jitter = jitter;
    }

    public String getCorrelation() {
        return correlation;
    }

    public void setCorrelation(String correlation) {
        this.correlation = correlation;
    }

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    @Override
    public boolean isValueValid() {
        return RangeVerifier.isValid(getValue(), MIN_VALUE, MAX_VALUE, IS_VALUE_DOUBLE);
    }

    public boolean isJitterValid() {
        return RangeVerifier.isValid(jitter, MIN_JITTER, MAX_JITTER, IS_JITTER_DOUBLE);
    }

    public boolean isCorrelationValid() {
        return RangeVerifier.isValid(correlation, MIN_CORRELATION, MAX_CORRELATION, IS_CORRELATION_DOUBLE);
    }

    public boolean isDistributionValid() {
        return distribution != null && !distribution.isEmpty();
    }
}
