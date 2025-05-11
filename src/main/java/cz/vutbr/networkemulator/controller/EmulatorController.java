package cz.vutbr.networkemulator.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.model.EmulationRule;
import cz.vutbr.networkemulator.model.Emulator;
import cz.vutbr.networkemulator.model.NetworkInterface;
import cz.vutbr.networkemulator.model.filter.Filter;
import cz.vutbr.networkemulator.model.parameters.Parameter;
import cz.vutbr.networkemulator.tc.TrafficControl;

public class EmulatorController {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(EmulatorController.class);

    private final Emulator emulator;

    private static class SingletonHolder {

        private static final EmulatorController INSTANCE = new EmulatorController(new Emulator());
    }

    private EmulatorController(Emulator emulator) {
        this.emulator = emulator;
    }

    public static EmulatorController getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void refreshNetworkInterfaces() {
        emulator.clearNetworkInterfaces();

        Pattern pattern = Pattern.compile("dev\\s+(\\S+)");
        Matcher matcher = pattern.matcher(TrafficControl.showQDiscs());
        while (matcher.find()) {
            // if (!matcher.group(1).equals("lo")) {
            addNetworkInterface(matcher.group(1));
            // }
        }
    }

    public Emulator getEmulator() {
        return emulator;
    }

    public void addNetworkInterface(String niName) {
        if (emulator.getNetworkInterfaces().stream().noneMatch(ni -> ni.getName().equals(niName))) {
            emulator.addNetworkInterface(niName);
        }
    }

    public void removeNetworkInterface(String niName) {
        if (emulator.getNetworkInterfaces().stream().anyMatch(ni -> ni.getName().equals(niName))) {
            emulator.removeNetworkInterface(niName);
        }
    }

    public Set<String> getNetworkInterfaces() {
        return emulator.getNetworkInterfaces().stream()
                .map(NetworkInterface::getName)
                .collect(Collectors.toSet());
    }

    public void clearNetworkInterfaces() {
        emulator.clearNetworkInterfaces();
    }

    public void addEmulationRule(String niName, String classId) {
        Optional<NetworkInterface> networkInterface = emulator.getNetworkInterfaces()
                .stream()
                .filter(ni -> ni.getName().equals(niName))
                .findFirst();

        networkInterface.ifPresent(ni -> ni.addEmulationRule(classId));
    }

    public void removeEmulationRule(String niName, String classId) {
        Optional<NetworkInterface> networkInterface = emulator.getNetworkInterfaces()
                .stream()
                .filter(ni -> ni.getName().equals(niName))
                .findFirst();

        networkInterface.ifPresent(ni -> ni.removeEmulationRule(classId));
    }

    public List<String> getEmulationRule(String niName) {
        return emulator.getNetworkInterfaces().stream()
                .filter(ni -> ni.getName().equals(niName))
                .flatMap(ni -> ni.getEmulationRules().stream())
                .map(EmulationRule::getClassId)
                .collect(Collectors.toList());
    }

    public Filter getFilter(String niName, String classId) {
        return emulator.getNetworkInterfaces().stream()
                .filter(ni -> ni.getName().equals(niName))
                .flatMap(ni -> ni.getEmulationRules().stream())
                .filter(er -> er.getClassId().equals(classId))
                .findFirst()
                .map(EmulationRule::getFilter)
                .orElse(null);
    }

    public void setFilter(String niName, String classId, Filter filter) {
        emulator.getNetworkInterfaces().stream()
                .filter(ni -> ni.getName().equals(niName))
                .flatMap(ni -> ni.getEmulationRules().stream())
                .filter(er -> er.getClassId().equals(classId))
                .findFirst()
                .ifPresent(er -> er.setFilter(filter));
    }

    public void setParameters(String niName, String classId, List<Parameter> parameters) {
        emulator.getNetworkInterfaces().stream()
                .filter(ni -> ni.getName().equals(niName))
                .flatMap(ni -> ni.getEmulationRules().stream())
                .filter(er -> er.getClassId().equals(classId))
                .findFirst()
                .ifPresent(er -> er.setParameters(parameters));
    }

    public List<Parameter> getParameters(String niName, String classId) {
        return emulator.getNetworkInterfaces().stream()
                .filter(ni -> ni.getName().equals(niName))
                .flatMap(ni -> ni.getEmulationRules().stream())
                .filter(er -> er.getClassId().equals(classId))
                .findFirst()
                .map(EmulationRule::getParameters)
                .orElse(null);
    }

    public void initialize() {
        refreshNetworkInterfaces();
        getNetworkInterfaces().stream().forEach(TrafficControl::restoreDefaults);
    }

    public String getNetworkConfiguration() {
        StringBuilder sb = new StringBuilder();
        for (NetworkInterface ni : emulator.getNetworkInterfaces()) {
            String dev = ni.getName();
            sb.append(TrafficControl.showQdiscAndFilters(dev));
        }

        return sb.toString();
    }

    public void startEmulation() {
        for (NetworkInterface ni : emulator.getNetworkInterfaces()) {
            if (ni.getEmulationRules().isEmpty()) {
                continue;
            }
            TrafficControl.setupRootQdisc(ni.getName());
            for (EmulationRule er : ni.getEmulationRules()) {
                String dev = ni.getName();
                String classId = er.getClassId();
                String handleId = er.getHandleId();
                List<Parameter> parameters = er.getParameters();
                Filter filter = er.getFilter();
                TrafficControl.setupEmulationRule(dev, classId, handleId, parameters);
                TrafficControl.setupFilter(dev, classId, handleId, filter);
            }
        }
    }

    public void stopEmulation() {
        for (NetworkInterface ni : emulator.getNetworkInterfaces()) {
            String dev = ni.getName();
            TrafficControl.restoreDefaults(dev);
        }
    }
}
