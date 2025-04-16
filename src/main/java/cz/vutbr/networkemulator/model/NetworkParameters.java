package cz.vutbr.networkemulator.model;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.utils.NetworkEmulatorConstants;

/**
 * Represents network parameters that can be used for network emulation.
 *
 * @author Frantisek Bilek (xbilek26)
 */
public class NetworkParameters {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(NetworkParameters.class);

    private String srcAddress;
    private int srcPort;
    private String dstAddress;
    private int dstPort;
    private int delayValue;
    private int jitter;
    private int delayCorrelation;
    // TODO: Distribution
    private int lossValue;
    private int lossCorrelation;
    private int rate;
    private int reorderingValue;
    private int reorderingCorrelation;
    private int duplicationValue;
    private int duplicationCorrelation;
    private int corruption;

    public String getSrcAddress() {
        return srcAddress;
    }

    public void setSrcAddress(String srcAddress) {
        this.srcAddress = srcAddress;
    }

    public int getSrcPort() {
        return srcPort;
    }

    public void setSrcPort(int srcPort) {
        this.srcPort = srcPort;
    }

    public String getDstAddress() {
        return dstAddress;
    }

    public void setDstAddress(String dstAddress) {
        this.dstAddress = dstAddress;
    }

    public int getDstPort() {
        return dstPort;
    }

    public void setDstPort(int dstPort) {
        this.dstPort = dstPort;
    }

    public int getDelayValue() {
        return delayValue;
    }

    public void setDelayValue(int delayValue) {
        this.delayValue = delayValue;
    }

    public int getJitter() {
        return jitter;
    }

    public void setJitter(int jitter) {
        this.jitter = jitter;
    }

    public int getDelayCorrelation() {
        return delayCorrelation;
    }

    public void setDelayCorrelation(int delayCorrelation) {
        this.delayCorrelation = delayCorrelation;
    }

    public int getLossValue() {
        return lossValue;
    }

    public void setLossValue(int lossValue) {
        this.lossValue = lossValue;
    }

    public int getLossCorrelation() {
        return lossCorrelation;
    }

    public void setLossCorrelation(int lossCorrelation) {
        this.lossCorrelation = lossCorrelation;
    }

    // TODO: Distribution
    public int getRate() {
        return rate;
    }

    public void setRate(int rateValue) {
        this.rate = rateValue;
    }

    public int getReorderingValue() {
        return reorderingValue;
    }

    public void setReorderingValue(int reorderingValue) {
        this.reorderingValue = reorderingValue;
    }

    public int getReorderingCorrelation() {
        return reorderingCorrelation;
    }

    public void setReorderingCorrelation(int reorderingCorrelation) {
        this.reorderingCorrelation = reorderingCorrelation;
    }

    public int getDuplicationValue() {
        return duplicationValue;
    }

    public void setDuplicationValue(int duplicationValue) {
        this.duplicationValue = duplicationValue;
    }

    public int getDuplicationCorrelation() {
        return duplicationCorrelation;
    }

    public void setDuplicationCorrelation(int duplicationCorrelation) {
        this.duplicationCorrelation = duplicationCorrelation;
    }

    public int getCorruption() {
        return corruption;
    }

    public void setCorruption(int corruptionValue) {
        this.corruption = corruptionValue;
    }

    public Map<String, Integer> getSetParameters() {
        Map<String, Integer> params = new LinkedHashMap<>();

        if (srcPort != -1) {
            params.put(NetworkEmulatorConstants.SRC_PORT, srcPort);
        }
        if (dstPort != -1) {
            params.put(NetworkEmulatorConstants.DST_PORT, dstPort);
        }
        if (delayValue != -1) {
            params.put(NetworkEmulatorConstants.DELAY_VALUE, delayValue);
        }
        if (jitter != -1) {
            params.put(NetworkEmulatorConstants.JITTER, jitter);
        }
        if (delayCorrelation != -1) {
            params.put(NetworkEmulatorConstants.DELAY_CORRELATION, delayCorrelation);
        }
        if (lossValue != -1) {
            params.put(NetworkEmulatorConstants.LOSS_VALUE, lossValue);
        }
        if (lossCorrelation != -1) {
            params.put(NetworkEmulatorConstants.LOSS_CORRELATION, lossCorrelation);
        }
        if (rate != -1) {
            params.put(NetworkEmulatorConstants.RATE, rate);
        }
        if (reorderingValue != -1) {
            params.put(NetworkEmulatorConstants.REORDERING_VALUE, reorderingValue);
        }
        if (reorderingCorrelation != -1) {
            params.put(NetworkEmulatorConstants.REORDERING_CORRELATION, reorderingCorrelation);
        }
        if (duplicationValue != -1) {
            params.put(NetworkEmulatorConstants.DUPLICATION_VALUE, duplicationValue);
        }
        if (duplicationCorrelation != -1) {
            params.put(NetworkEmulatorConstants.DUPLICATION_CORRELATION, duplicationCorrelation);
        }
        if (corruption != -1) {
            params.put(NetworkEmulatorConstants.CORRUPTION, corruption);
        }

        return params;
    }
}
