package cz.vutbr.networkemulator.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.model.NetworkParameters;

public class NetworkEmulatorConverter {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(NetworkEmulatorConverter.class);

    public static NetworkParameters convertToNetworkParameters(List<String> listParams) {
        NetworkParameters params = new NetworkParameters();
        params.setIpProtocol(listParams.get(0));
        params.setSrcAddress(listParams.get(1));
        params.setSrcSubnetMask(listParams.get(2));
        params.setSrcPort(NumberUtils.toInt(listParams.get(3), -1));
        params.setDstAddress(listParams.get(4));
        params.setDstSubnetMask(listParams.get(5));
        params.setDstPort(NumberUtils.toInt(listParams.get(6), -1));
        params.setDelayValue(NumberUtils.toInt(listParams.get(7), -1));
        params.setJitter(NumberUtils.toInt(listParams.get(8), -1));
        params.setDelayCorrelation(NumberUtils.toInt(listParams.get(9), -1));
        params.setDistribution(listParams.get(10));
        params.setLossValue(NumberUtils.toInt(listParams.get(11), -1));
        params.setLossCorrelation(NumberUtils.toInt(listParams.get(12), -1));
        params.setRate(NumberUtils.toInt(listParams.get(13), -1));
        params.setReorderingValue(NumberUtils.toInt(listParams.get(14), -1));
        params.setReorderingCorrelation(NumberUtils.toInt(listParams.get(15), -1));
        params.setDuplicationValue(NumberUtils.toInt(listParams.get(16), -1));
        params.setDuplicationCorrelation(NumberUtils.toInt(listParams.get(17), -1));
        params.setCorruption(NumberUtils.toInt(listParams.get(18), -1));

        return params;
    }

    public static List<String> convertToList(CollectionProperty property) {
        List<String> list = new ArrayList<>();
        if (property == null) {
            return list;
        }
        for (int i = 0; i < property.size(); i++) {
            list.add(property.get(i).getStringValue());
        }
        return list;
    }

    public static String convertToString(StringProperty property) {
        if (property == null) {
            return "";
        }
        return property.getStringValue();
    }

}