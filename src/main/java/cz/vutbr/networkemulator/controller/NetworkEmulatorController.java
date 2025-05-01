package cz.vutbr.networkemulator.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.linux.TrafficControl;
import cz.vutbr.networkemulator.model.NetworkEmulator;
import cz.vutbr.networkemulator.model.NetworkInterface;
import cz.vutbr.networkemulator.model.TrafficClass;
import cz.vutbr.networkemulator.model.filter.Filter;
import cz.vutbr.networkemulator.model.parameters.Parameter;

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
            // if (!matcher.group(1).equals("lo")) {
            addNetworkInterface(matcher.group(1));
            // }
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

    public void addTrafficClass(String niName, String classId) {
        Optional<NetworkInterface> networkInterface = networkEmulator.getNetworkInterfaces()
                .stream()
                .filter(ni -> ni.getName().equals(niName))
                .findFirst();

        networkInterface.ifPresent(ni -> ni.addTrafficClass(classId));
    }

    public void removeTrafficClass(String niName, String classId) {
        Optional<NetworkInterface> networkInterface = networkEmulator.getNetworkInterfaces()
                .stream()
                .filter(ni -> ni.getName().equals(niName))
                .findFirst();

        networkInterface.ifPresent(ni -> ni.removeTrafficClass(classId));
    }

    public List<String> getTrafficClasses(String niName) {
        return networkEmulator.getNetworkInterfaces().stream()
                .filter(ni -> ni.getName().equals(niName))
                .flatMap(ni -> ni.getTrafficClasses().stream())
                .map(TrafficClass::getClassId)
                .collect(Collectors.toList());
    }

    public Filter getFilter(String niName, String classId) {
        return networkEmulator.getNetworkInterfaces().stream()
                .filter(ni -> ni.getName().equals(niName))
                .flatMap(ni -> ni.getTrafficClasses().stream())
                .filter(tc -> tc.getClassId().equals(classId))
                .findFirst()
                .map(TrafficClass::getFilter)
                .orElse(null);
    }

    public void setFilter(String niName, String classId, Filter filter) {
        networkEmulator.getNetworkInterfaces().stream()
                .filter(ni -> ni.getName().equals(niName))
                .flatMap(ni -> ni.getTrafficClasses().stream())
                .filter(tc -> tc.getClassId().equals(classId))
                .findFirst()
                .ifPresent(tc -> tc.setFilter(filter));
    }

    public void setParameters(String niName, String classId, List<Parameter> parameters) {
        networkEmulator.getNetworkInterfaces().stream()
                .filter(ni -> ni.getName().equals(niName))
                .flatMap(ni -> ni.getTrafficClasses().stream())
                .filter(tc -> tc.getClassId().equals(classId))
                .findFirst()
                .ifPresent(tc -> tc.setParameters(parameters));
    }

    public List<Parameter> getParameters(String niName, String classId) {
        return networkEmulator.getNetworkInterfaces().stream()
                .filter(ni -> ni.getName().equals(niName))
                .flatMap(ni -> ni.getTrafficClasses().stream())
                .filter(tc -> tc.getClassId().equals(classId))
                .findFirst()
                .map(TrafficClass::getParameters)
                .orElse(null);
    }

    public void initialize() {
        refreshNetworkInterfaces();
        getNetworkInterfaces().stream().forEach(TrafficControl::restoreDefaults);
    }

    public String getNetworkConfiguration() {
        StringBuilder sb = new StringBuilder();
        for (NetworkInterface ni : networkEmulator.getNetworkInterfaces()) {
            String dev = ni.getName();
            sb.append(dev).append(": ").append("\n");
            sb.append(TrafficControl.showQdiscAndFilters(dev));
        }

        return sb.toString();
    }

    public void startEmulation() {
        for (NetworkInterface ni : networkEmulator.getNetworkInterfaces()) {
            if (ni.getTrafficClasses().isEmpty()) {
                continue;
            }
            TrafficControl.setupRootQdisc(ni.getName());
            for (TrafficClass tc : ni.getTrafficClasses()) {
                String dev = ni.getName();
                String classId = tc.getClassId();
                String handleId = tc.getHandleId();
                List<Parameter> parameters = tc.getParameters();
                Filter filter = tc.getFilter();
                TrafficControl.setupTrafficClass(dev, classId, handleId, parameters);
                TrafficControl.setupFilter(dev, classId, handleId, filter);
            }
        }
    }

    public void stopEmulation() {
        for (NetworkInterface ni : networkEmulator.getNetworkInterfaces()) {
            String dev = ni.getName();
            TrafficControl.restoreDefaults(dev);
        }
    }
}
