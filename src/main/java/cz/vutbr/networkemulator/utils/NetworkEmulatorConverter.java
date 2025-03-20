package cz.vutbr.networkemulator.utils;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.jmeter.config.Argument;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.model.NetworkParameters;

public class NetworkEmulatorConverter {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(NetworkEmulatorConverter.class);

    private static final String SRC_ADDRESS = "srcAddress";
    private static final String SRC_PORT = "srcPort";
    private static final String DST_ADDRESS = "dstAddress";
    private static final String DST_PORT = "dstPort";
    private static final String DELAY_VALUE = "delayValue";
    private static final String DROP_VALUE = "dropValue";
    private static final String RATE_VALUE = "rateValue";
    private static final String LOSS_VALUE = "lossValue";
    private static final String REORDERING_VALUE = "reorderingValue";
    private static final String DUPLICATION_VALUE = "duplicationValue";
    private static final String CORRUPTION_VALUE = "corruptionValue";
    private static final String DELAY_CORRELATION = "delayCorrelation";
    private static final String DROP_CORRELATION = "dropCorrelation";
    private static final String DUPLICATION_CORRELATION = "duplicationCorrelation";
    private static final String REORDERING_CORRELATION = "reorderingCorrelation";
    private static final String JITTER_VALUE = "jitterValue";

    public static NetworkParameters convertToNetworkParameters(CollectionProperty propertyParameters) {
        NetworkParameters networkParameters = new NetworkParameters();

        for (int i = 0; i < propertyParameters.size(); i++) {
            Argument argument = (Argument) propertyParameters.get(i).getObjectValue();
            String name = argument.getName();
            String value = argument.getValue();
            switch (name) {
                case "srcAddress":
                    networkParameters.setSrcAddress(value);
                    break;
                case "srcPort":
                    networkParameters.setSrcPort(NumberUtils.toInt(value, -1));
                    break;
                case "dstAddress":
                    networkParameters.setDstAddress(value);
                    break;
                case "dstPort":
                    networkParameters.setDstPort(NumberUtils.toInt(value, -1));
                    break;
                case "delayValue":
                    networkParameters.setDelayValue(NumberUtils.toInt(value, -1));
                    break;
                case "delayCorrelation":
                    networkParameters.setDelayCorrelation(NumberUtils.toInt(value, -1));
                    break;
                case "dropValue":
                    networkParameters.setDropValue(NumberUtils.toInt(value, -1));
                    break;
                case "dropCorrelation":
                    networkParameters.setDropCorrelation(NumberUtils.toInt(value, -1));
                    break;
                case "rateValue":
                    networkParameters.setRateValue(NumberUtils.toInt(value, -1));
                    break;
                case "lossValue":
                    networkParameters.setLossValue(NumberUtils.toInt(value, -1));
                    break;
                case "reorderingValue":
                    networkParameters.setReorderingValue(NumberUtils.toInt(value, -1));
                    break;
                case "reorderingCorrelation":
                    networkParameters.setReorderingCorrelation(NumberUtils.toInt(value, -1));
                    break;
                case "duplicationValue":
                    networkParameters.setDuplicationValue(NumberUtils.toInt(value, -1));
                    break;
                case "duplicationCorrelation":
                    networkParameters.setDuplicationCorrelation(NumberUtils.toInt(value, -1));
                    break;
                case "corruptionValue":
                    networkParameters.setCorruptionValue(NumberUtils.toInt(value, -1));
                    break;
                default:
                    break;
            }
        }

        return networkParameters;
    }

    public static CollectionProperty convertToCollectionProperty(NetworkParameters networkParameters) {
        CollectionProperty propertyParameters = new CollectionProperty();
    
        propertyParameters.addItem(new Argument(SRC_ADDRESS, networkParameters.getSrcAddress()));
        propertyParameters.addItem(new Argument(SRC_PORT, String.valueOf(networkParameters.getSrcPort())));
        propertyParameters.addItem(new Argument(DST_ADDRESS, networkParameters.getDstAddress()));
        propertyParameters.addItem(new Argument(DST_PORT, String.valueOf(networkParameters.getDstPort())));
        propertyParameters.addItem(new Argument(DELAY_VALUE, String.valueOf(networkParameters.getDelayValue())));
        propertyParameters.addItem(new Argument(DROP_VALUE, String.valueOf(networkParameters.getDropValue())));
        propertyParameters.addItem(new Argument(RATE_VALUE, String.valueOf(networkParameters.getRateValue())));
        propertyParameters.addItem(new Argument(LOSS_VALUE, String.valueOf(networkParameters.getLossValue())));
        propertyParameters.addItem(new Argument(REORDERING_VALUE, String.valueOf(networkParameters.getReorderingValue())));
        propertyParameters.addItem(new Argument(DUPLICATION_VALUE, String.valueOf(networkParameters.getDuplicationValue())));
        propertyParameters.addItem(new Argument(CORRUPTION_VALUE, String.valueOf(networkParameters.getCorruptionValue())));
        propertyParameters.addItem(new Argument(DELAY_CORRELATION, String.valueOf(networkParameters.getDelayCorrelation())));
        propertyParameters.addItem(new Argument(DROP_CORRELATION, String.valueOf(networkParameters.getDropCorrelation())));
        propertyParameters.addItem(new Argument(DUPLICATION_CORRELATION, String.valueOf(networkParameters.getDuplicationCorrelation())));
        propertyParameters.addItem(new Argument(REORDERING_CORRELATION, String.valueOf(networkParameters.getReorderingCorrelation())));
        propertyParameters.addItem(new Argument(JITTER_VALUE, String.valueOf(networkParameters.getJitterValue())));
    
        return propertyParameters;
    }
}
