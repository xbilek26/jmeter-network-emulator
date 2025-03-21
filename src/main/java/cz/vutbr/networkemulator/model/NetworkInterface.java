package cz.vutbr.networkemulator.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public void addTrafficClass(String className) {
        if (className != null && !hasTrafficClass(className)) {
            trafficClasses.add(new TrafficClass(className));
        }
    }    

    public void removeTrafficClass(String className) {
        if (className != null) {
            trafficClasses.removeIf(tc -> tc.getName().equals(className));
        }
    }

    public boolean hasTrafficClass(String className) {
        return trafficClasses.stream().anyMatch(tc -> tc.getName().equals(className));
    }
    
}
