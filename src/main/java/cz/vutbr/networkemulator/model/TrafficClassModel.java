package cz.vutbr.networkemulator.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a traffic class that holds network parameters.
 *
 * @author Frantisek Bilek (xbilek26)
 */
public class TrafficClassModel {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(TrafficClassModel.class);

    String name;
    NetworkParameters networkParameters;

    public TrafficClassModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NetworkParameters getNetworkParameters() {
        return networkParameters;
    }

    public void setNetworkParameters(NetworkParameters networkParameters) {
        this.networkParameters = networkParameters;
    }
}
