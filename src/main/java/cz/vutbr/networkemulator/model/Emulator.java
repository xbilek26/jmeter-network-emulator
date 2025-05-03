package cz.vutbr.networkemulator.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a network emulator that holds network interfaces.
 *
 * @author Frantisek Bilek (xbilek26)
 */
public class Emulator {

    private static final Logger log = LoggerFactory.getLogger(Emulator.class);

    private final List<NetworkInterface> networkInterfaces;

    public Emulator() {
        this.networkInterfaces = new ArrayList<>();
        log.info("Created network emulator");
    }

    public List<NetworkInterface> getNetworkInterfaces() {
        return networkInterfaces;

    }

    public void addNetworkInterface(String niName) {
        networkInterfaces.add(new NetworkInterface(niName));

    }

    public void removeNetworkInterface(String niName) {
        networkInterfaces.removeIf(ni -> ni.getName().equals(niName));
    }

    public void clearNetworkInterfaces() {
        networkInterfaces.clear();
    }
}
