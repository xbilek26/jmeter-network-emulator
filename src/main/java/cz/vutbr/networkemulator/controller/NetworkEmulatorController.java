package cz.vutbr.networkemulator.controller;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NetworkEmulatorController {

    private List<String> networkInterfaces;

    public void refreshInterfaces() {
        networkInterfaces = new ArrayList<>();
        try {
            Iterator<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces().asIterator();
            while (interfaces.hasNext()) {
                networkInterfaces.add(interfaces.next().toString());
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public List<String> getNetworkInterfaces() {
        return networkInterfaces;
    }

}