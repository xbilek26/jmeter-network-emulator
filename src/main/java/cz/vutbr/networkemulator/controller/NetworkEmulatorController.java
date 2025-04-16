package cz.vutbr.networkemulator.controller;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.linux.tc.TrafficControl;
import cz.vutbr.networkemulator.model.NetworkEmulatorModel;
import cz.vutbr.networkemulator.model.NetworkInterfaceModel;
import cz.vutbr.networkemulator.model.NetworkParameters;
import cz.vutbr.networkemulator.model.TrafficClassModel;

public class NetworkEmulatorController {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(NetworkEmulatorController.class);

    private final NetworkEmulatorModel networkEmulator;

    private static class SingletonHolder {

        private static final NetworkEmulatorController INSTANCE = new NetworkEmulatorController(new NetworkEmulatorModel());
    }

    private NetworkEmulatorController(NetworkEmulatorModel networkEmulator) {
        this.networkEmulator = networkEmulator;
    }

    public static NetworkEmulatorController getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void refreshNetworkInterfaces() {
        networkEmulator.clearNetworkInterfaces();
        try {
            Enumeration<NetworkInterface> phyNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (phyNetworkInterfaces.hasMoreElements()) {
                NetworkInterface phyNetworkInterface = phyNetworkInterfaces.nextElement();
                String phyNetworkInterfaceName = phyNetworkInterface.getName();

                if (!phyNetworkInterfaceName.equals("lo")) {
                    addNetworkInterface(phyNetworkInterfaceName);
                }

            }
        } catch (SocketException ex) {
        }
    }

    public NetworkEmulatorModel getNetworkEmulator() {
        return networkEmulator;
    }

    public void addNetworkInterface(String niName) {
        if (networkEmulator.getNetworkInterfaces().stream().noneMatch(ni -> ni.getName().equals(niName))) {
            networkEmulator.addNetworkInterface(niName);
        }
    }

    public void removeNetworkInterface(String niName) {
        if (networkEmulator.getNetworkInterfaces().stream().anyMatch(ni -> ni.getName().equals(niName))) {
            networkEmulator.removeNetworkInterface(niName);
        }
    }

    public Set<String> getNetworkInterfaces() {
        return networkEmulator.getNetworkInterfaces().stream()
                .map(NetworkInterfaceModel::getName)
                .collect(Collectors.toSet());
    }

    public void clearNetworkInterfaces() {
        networkEmulator.clearNetworkInterfaces();
    }

    public void addTrafficClass(String niName, String tcName) {
        Optional<NetworkInterfaceModel> networkInterface = networkEmulator.getNetworkInterfaces()
                .stream()
                .filter(ni -> ni.getName().equals(niName))
                .findFirst();

        networkInterface.ifPresent(ni -> ni.addTrafficClass(tcName));
    }

    public void removeTrafficClass(String niName, String tcName) {
        Optional<NetworkInterfaceModel> networkInterface = networkEmulator.getNetworkInterfaces()
                .stream()
                .filter(ni -> ni.getName().equals(niName))
                .findFirst();

        networkInterface.ifPresent(ni -> ni.removeTrafficClass(tcName));
    }

    public List<String> getTrafficClasses(String niName) {
        return networkEmulator.getNetworkInterfaces().stream()
                .filter(ni -> ni.getName().equals(niName))
                .flatMap(ni -> ni.getTrafficClasses().stream())
                .map(TrafficClassModel::getName)
                .collect(Collectors.toList());
    }

    public void setNetworkParameters(String niName, String tcName, NetworkParameters networkParameters) {
        networkEmulator.getNetworkInterfaces().stream()
                .filter(ni -> ni.getName().equals(niName))
                .flatMap(ni -> ni.getTrafficClasses().stream())
                .filter(tc -> tc.getName().equals(tcName))
                .findFirst()
                .ifPresent(tc -> tc.setNetworkParameters(networkParameters));
    }

    public NetworkParameters getNetworkParameters(String niName, String tcName) {
        return networkEmulator.getNetworkInterfaces().stream()
                .filter(ni -> ni.getName().equals(niName))
                .flatMap(ni -> ni.getTrafficClasses().stream())
                .filter(tc -> tc.getName().equals(tcName))
                .findFirst()
                .map(TrafficClassModel::getNetworkParameters)
                .orElse(null);
    }

    public void restoreNetworkConfiguration() {
        for (NetworkInterfaceModel ni : networkEmulator.getNetworkInterfaces()) {
            String dev = ni.getName();
            TrafficControl.restoreDefaults(dev);
        }
    }

    public String getNetworkConfiguration() {
        return TrafficControl.showQDiscs();
    }

    public void runEmulation() {
        for (NetworkInterfaceModel ni : networkEmulator.getNetworkInterfaces()) {
            TrafficControl.setupRootQdisc(ni.getName());
            for (TrafficClassModel tc : ni.getTrafficClasses()) {
                String dev = ni.getName();
                String classId = tc.getName() + "0";
                String handleId = tc.getName().substring(2) + "0:";
                NetworkParameters params = tc.getNetworkParameters();
                TrafficControl.setupTrafficClass(dev, classId, handleId, params);
                TrafficControl.setupFilter(dev, classId, handleId, params);
            }
        }
    }
}
