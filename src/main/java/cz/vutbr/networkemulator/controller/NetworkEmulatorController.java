package cz.vutbr.networkemulator.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.linux.tc.TrafficControl;
import cz.vutbr.networkemulator.model.NetworkEmulator;
import cz.vutbr.networkemulator.model.NetworkInterface;
import cz.vutbr.networkemulator.model.NetworkParameters;
import cz.vutbr.networkemulator.model.TrafficClass;

public class NetworkEmulatorController {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(NetworkEmulatorController.class);

    private final NetworkEmulator networkEmulator;

    private static class SingletonHolder {

        private static final NetworkEmulatorController INSTANCE = new NetworkEmulatorController(new NetworkEmulator());
    }

    private NetworkEmulatorController(NetworkEmulator networkEmulator) {
        this.networkEmulator = networkEmulator;
    }

    public static NetworkEmulatorController getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void refreshNetworkInterfaces() {
        networkEmulator.clearNetworkInterfaces();

        Pattern pattern = Pattern.compile("dev\\s+(\\S+)");
        Matcher matcher = pattern.matcher(TrafficControl.showQDiscs());
        while (matcher.find()) {
            if (!matcher.group(1).equals("lo")) {
                addNetworkInterface(matcher.group(1));
            }
        }
    }

    public NetworkEmulator getNetworkEmulator() {
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
                .map(NetworkInterface::getName)
                .collect(Collectors.toSet());
    }

    public void clearNetworkInterfaces() {
        networkEmulator.clearNetworkInterfaces();
    }

    public void addTrafficClass(String niName, String tcName) {
        Optional<NetworkInterface> networkInterface = networkEmulator.getNetworkInterfaces()
                .stream()
                .filter(ni -> ni.getName().equals(niName))
                .findFirst();

        networkInterface.ifPresent(ni -> ni.addTrafficClass(tcName));
    }

    public void removeTrafficClass(String niName, String tcName) {
        Optional<NetworkInterface> networkInterface = networkEmulator.getNetworkInterfaces()
                .stream()
                .filter(ni -> ni.getName().equals(niName))
                .findFirst();

        networkInterface.ifPresent(ni -> ni.removeTrafficClass(tcName));
    }

    public List<String> getTrafficClasses(String niName) {
        return networkEmulator.getNetworkInterfaces().stream()
                .filter(ni -> ni.getName().equals(niName))
                .flatMap(ni -> ni.getTrafficClasses().stream())
                .map(TrafficClass::getName)
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
                .map(TrafficClass::getNetworkParameters)
                .orElse(null);
    }

    public void restoreNetworkConfiguration() {
        for (NetworkInterface ni : networkEmulator.getNetworkInterfaces()) {
            String dev = ni.getName();
            TrafficControl.restoreDefaults(dev);
        }
    }

    public String getNetworkConfiguration() {
        return TrafficControl.showQDiscs();
    }

    public void runEmulation() {
        for (NetworkInterface ni : networkEmulator.getNetworkInterfaces()) {
            TrafficControl.setupRootQdisc(ni.getName());
            for (TrafficClass tc : ni.getTrafficClasses()) {
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
