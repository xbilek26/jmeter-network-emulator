package cz.vutbr.networkemulator.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private int dropValue;
    private int dropCorrelation;
    private int rate;
    private int loss;
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

    // TODO: Distribution
    public int getDropValue() {
        return dropValue;
    }

    public void setDropValue(int dropValue) {
        this.dropValue = dropValue;
    }

    public int getDropCorrelation() {
        return dropCorrelation;
    }

    public void setDropCorrelation(int dropCorrelation) {
        this.dropCorrelation = dropCorrelation;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rateValue) {
        this.rate = rateValue;
    }

    public int getLoss() {
        return loss;
    }

    public void setLoss(int lossValue) {
        this.loss = lossValue;
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

}
