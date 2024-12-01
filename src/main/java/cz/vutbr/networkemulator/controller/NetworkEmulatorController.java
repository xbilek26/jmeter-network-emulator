package cz.vutbr.networkemulator.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.net.NetworkInterface;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkEmulatorController {

    private static final Logger log = LoggerFactory.getLogger(NetworkEmulatorController.class);

    public ArrayList<String> networkInterfaces = new ArrayList<String>();

    public NetworkEmulatorController() {
        try {
            loadNetworkInterfaces();
            log.info("Network interfaces succesfully loaded." + networkInterfaces.toString());
        } catch (SocketException e) {
            log.error("Error loading network interfaces: " + e.getMessage());
        }
    }

    private void loadNetworkInterfaces() throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            if (networkInterface.isUp() && !networkInterface.isLoopback()) {
                this.networkInterfaces.add(networkInterface.getName());
            }
        }
    }

    public ArrayList<String> getNetworkInterfaces() {
        return networkInterfaces;
    }
}
