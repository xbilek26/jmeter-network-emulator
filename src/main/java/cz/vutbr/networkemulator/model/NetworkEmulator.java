package cz.vutbr.networkemulator.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkEmulator {

    private static final Logger log = LoggerFactory.getLogger(NetworkEmulator.class);

    private final List<NetworkInterface> networkInterfaces;

    public NetworkEmulator() {
        this.networkInterfaces = new ArrayList<>();
        log.info("Created network emulator");
    }

    public List<NetworkInterface> getNetworkInterfaces() {
        return networkInterfaces;
    }

    public void addNetworkInterface(String name) {
        networkInterfaces.add(new NetworkInterface(name));
    }

    public void removeNetworkInterface(String name) {
        networkInterfaces.removeIf(ni -> ni.getName().equals(name));
    }

    public void clearNetworkInterfaces() {
        networkInterfaces.clear();
    }
}