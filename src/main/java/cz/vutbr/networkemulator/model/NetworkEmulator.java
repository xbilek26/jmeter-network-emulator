package cz.vutbr.networkemulator.model;

import cz.vutbr.networkemulator.controller.NetworkEmulatorController;

import org.apache.jmeter.testelement.AbstractTestElement;

import java.net.SocketException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkEmulator extends AbstractTestElement {

    private static final Logger log = LoggerFactory.getLogger(NetworkEmulator.class);

    private NetworkEmulatorController networkEmulatorController;
    private ArrayList<String> availableNetworkInterfaces = new ArrayList<>();

    public NetworkEmulator() {
        networkEmulatorController = new NetworkEmulatorController();
        try {
            setAvaiableNetworkInterfaces(networkEmulatorController.getNetworkInterfacesNames());
        } catch (SocketException e) {
            log.error("Error loading network interfaces: " + e.getMessage());
        }
    }

    public void setAvaiableNetworkInterfaces(ArrayList<String> interfaces) {
        if (interfaces != null) {
            availableNetworkInterfaces.clear();
            availableNetworkInterfaces.addAll(interfaces);
        }
    }

    public ArrayList<String> getAvailableNetworkInterfaces() {
        return new ArrayList<>(availableNetworkInterfaces);
    }
}
