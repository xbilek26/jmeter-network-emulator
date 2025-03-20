package cz.vutbr.networkemulator.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrafficClass {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(TrafficClass.class);

    String name;
    NetworkParameters networkParameters;

    public TrafficClass(String name) {
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