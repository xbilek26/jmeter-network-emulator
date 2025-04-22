package cz.vutbr.networkemulator.utils;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.model.NetworkParameters;

public class NetworkEmulatorConverter {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(NetworkEmulatorConverter.class);

    public static NetworkParameters convertToNetworkParameters(CollectionProperty propertyParams) {
        NetworkParameters params = new NetworkParameters();
        params.setIpProtocol(propertyParams.get(0).getStringValue());
        params.setSrcAddress(propertyParams.get(1).getStringValue());
        params.setSrcSubnetMask(propertyParams.get(2).getStringValue());
        params.setSrcPort(NumberUtils.toInt(propertyParams.get(3).getStringValue(), -1));
        params.setDstAddress(propertyParams.get(4).getStringValue());
        params.setDstSubnetMask(propertyParams.get(5).getStringValue());
        params.setDstPort(NumberUtils.toInt(propertyParams.get(6).getStringValue(), -1));
        params.setDelayValue(NumberUtils.toInt(propertyParams.get(7).getStringValue(), -1));
        params.setJitter(NumberUtils.toInt(propertyParams.get(8).getStringValue(), -1));
        params.setDelayCorrelation(NumberUtils.toInt(propertyParams.get(9).getStringValue(), -1));
        params.setDistribution(propertyParams.get(10).getStringValue());
        params.setLossValue(NumberUtils.toInt(propertyParams.get(11).getStringValue(), -1));
        params.setLossCorrelation(NumberUtils.toInt(propertyParams.get(12).getStringValue(), -1));
        params.setRate(NumberUtils.toInt(propertyParams.get(13).getStringValue(), -1));
        params.setReorderingValue(NumberUtils.toInt(propertyParams.get(14).getStringValue(), -1));
        params.setReorderingCorrelation(NumberUtils.toInt(propertyParams.get(15).getStringValue(), -1));
        params.setDuplicationValue(NumberUtils.toInt(propertyParams.get(16).getStringValue(), -1));
        params.setDuplicationCorrelation(NumberUtils.toInt(propertyParams.get(17).getStringValue(), -1));
        params.setCorruption(NumberUtils.toInt(propertyParams.get(18).getStringValue(), -1));
    
        return params;
    }
}
