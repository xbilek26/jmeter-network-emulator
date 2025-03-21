package cz.vutbr.networkemulator.utils;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.model.NetworkParameters;

public class NetworkEmulatorConverter {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(NetworkEmulatorConverter.class);

    public static NetworkParameters convertToNetworkParameters(CollectionProperty propertyParameters) {
        NetworkParameters networkParameters = new NetworkParameters();
        networkParameters.setSrcAddress(propertyParameters.get(0).getStringValue());
        networkParameters.setSrcPort(NumberUtils.toInt(propertyParameters.get(1).getStringValue(), -1));
        networkParameters.setDstAddress(propertyParameters.get(2).getStringValue());
        networkParameters.setDstPort(NumberUtils.toInt(propertyParameters.get(3).getStringValue(), -1));
        networkParameters.setDelayValue(NumberUtils.toInt(propertyParameters.get(4).getStringValue(), -1));
        networkParameters.setJitter(NumberUtils.toInt(propertyParameters.get(5).getStringValue(), -1));
        networkParameters.setDelayCorrelation(NumberUtils.toInt(propertyParameters.get(6).getStringValue(), -1));
        networkParameters.setDropValue(NumberUtils.toInt(propertyParameters.get(7).getStringValue(), -1));
        networkParameters.setDropCorrelation(NumberUtils.toInt(propertyParameters.get(8).getStringValue(), -1));
        networkParameters.setRate(NumberUtils.toInt(propertyParameters.get(9).getStringValue(), -1));
        networkParameters.setLoss(NumberUtils.toInt(propertyParameters.get(10).getStringValue(), -1));
        networkParameters.setReorderingValue(NumberUtils.toInt(propertyParameters.get(11).getStringValue(), -1));
        networkParameters.setReorderingCorrelation(NumberUtils.toInt(propertyParameters.get(12).getStringValue(), -1));
        networkParameters.setDuplicationValue(NumberUtils.toInt(propertyParameters.get(13).getStringValue(), -1));
        networkParameters.setDuplicationCorrelation(NumberUtils.toInt(propertyParameters.get(14).getStringValue(), -1));
        networkParameters.setCorruption(NumberUtils.toInt(propertyParameters.get(15).getStringValue(), -1));
    
        return networkParameters;
    }
}
