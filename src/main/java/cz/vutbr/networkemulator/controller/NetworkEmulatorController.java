package cz.vutbr.networkemulator.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.io.Serializable;
import java.net.NetworkInterface;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkEmulatorController implements Serializable {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(NetworkEmulatorController.class);

    public ArrayList<String> getNetworkInterfacesNames() throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        ArrayList<String> networkInterfacesNames = new ArrayList<>();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            // uncomment in future
            //if (networkInterface.isUp() && !networkInterface.isLoopback()) {
                networkInterfacesNames.add(networkInterface.getName());
            //}
        }

        return networkInterfacesNames;
    }
}
