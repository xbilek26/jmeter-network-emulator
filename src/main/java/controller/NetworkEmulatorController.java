package controller;

import model.NetworkEmulator;

import java.util.List;
import java.util.ArrayList;
import java.net.NetworkInterface;
import java.net.SocketException;

public class NetworkEmulatorController {

    private final NetworkEmulator networkEmulator;

    public NetworkEmulatorController(NetworkEmulator networkEmulator) {
        this.networkEmulator = networkEmulator;
    }

    public void addInterface(String interfaceName) {
        networkEmulator.addInterface(interfaceName);
    }

    public void setAvailableInterfaces(Iterable<String> interfaces) {
        networkEmulator.setNetworkInterfaces((List<String>) interfaces);
    }

    public List<String> getAvailableInterfaces() {
        return networkEmulator.getNetworkInterfaces();
    }

    public NetworkEmulatorController() {
        networkEmulator = new NetworkEmulator();
        loadNetworkInterfaces();
    }

    private void loadNetworkInterfaces() {
        List<String> interfaces = new ArrayList<>();
        try {
            java.util.Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface iface = networkInterfaces.nextElement();
                interfaces.add(iface.getDisplayName());
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        networkEmulator.setNetworkInterfaces(interfaces);
    }

    public List<String> getNetworkInterfaces() {
        return networkEmulator.getNetworkInterfaces();
    }

    public boolean isInterfaceAdded(String interfaceName) {
        return networkEmulator.isInterfaceAdded(interfaceName);
    }
}
