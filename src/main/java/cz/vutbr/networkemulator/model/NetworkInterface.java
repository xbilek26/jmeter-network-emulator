package cz.vutbr.networkemulator.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a network interface that holds traffic classes.
 *
 * @author Frantisek Bilek (xbilek26)
 */
public class NetworkInterface {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(NetworkInterface.class);

    private String name;
    private final List<TrafficClass> trafficClasses;

    public NetworkInterface(String name) {
        this.name = name;
        this.trafficClasses = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TrafficClass> getTrafficClasses() {
        return new ArrayList<>(trafficClasses);
    }

    public void addTrafficClass(String tcName) {
        if (tcName != null && !hasTrafficClass(tcName)) {
            trafficClasses.add(new TrafficClass(tcName));
        }
    }

    public void removeTrafficClass(String tcName) {
        if (tcName != null) {
            trafficClasses.removeIf(tc -> tc.getName().equals(tcName));
        }
    }

    public boolean hasTrafficClass(String tcName) {
        return trafficClasses.stream().anyMatch(tc -> tc.getName().equals(tcName));
    }

}
