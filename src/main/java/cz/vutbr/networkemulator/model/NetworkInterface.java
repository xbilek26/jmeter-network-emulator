package cz.vutbr.networkemulator.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a network interface that holds emulation rules.
 *
 * @author Frantisek Bilek (xbilek26)
 */
public class NetworkInterface {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(NetworkInterface.class);

    private String name;
    private final List<EmulationRule> emulationRules;

    public NetworkInterface(String name) {
        this.name = name;
        this.emulationRules = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EmulationRule> getEmulationRules() {
        return new ArrayList<>(emulationRules);
    }

    public void addEmulationRule(String classId) {
        if (classId != null && !hasEmulationRule(classId)) {
            emulationRules.add(new EmulationRule(classId));
        }
    }

    public void removeEmulationRule(String classId) {
        if (classId != null) {
            emulationRules.removeIf(tc -> tc.getClassId().equals(classId));
        }
    }

    public boolean hasEmulationRule(String classId) {
        return emulationRules.stream().anyMatch(tc -> tc.getClassId().equals(classId));
    }

}
