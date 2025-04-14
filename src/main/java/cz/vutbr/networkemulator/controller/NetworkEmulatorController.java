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

import cz.vutbr.networkemulator.linux.CommandRunner;
import cz.vutbr.networkemulator.model.NetworkEmulatorModel;
import cz.vutbr.networkemulator.model.NetworkInterfaceModel;
import cz.vutbr.networkemulator.model.NetworkParameters;
import cz.vutbr.networkemulator.model.TrafficClassModel;

public class NetworkEmulatorController {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(NetworkEmulatorController.class);

    private final NetworkEmulatorModel networkEmulator;
    private final CommandRunner runner = CommandRunner.getInstance();

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
            runner.runCommand(String.format("tc qdisc del dev %s root", ni.getName()));
        }
    }

    public String getNetworkConfiguration() {
        return runner.runCommand("tc qdisc show");

    }

    public void runEmulation() {
        for (NetworkInterfaceModel ni : networkEmulator.getNetworkInterfaces()) {
            setupRootQdisc(ni.getName());
            for (TrafficClassModel tc : ni.getTrafficClasses()) {
                setupTrafficClass(ni.getName(), tc);
            }
        }
    }

    private void setupRootQdisc(String dev) {
        runner.runCommand(String.format("tc qdisc del dev %s root", dev));
        runner.runCommand(String.format("tc qdisc add dev %s root handle 1: htb", dev));
        runner.runCommand(String.format("tc class add dev %s parent 1: classid 1:1 htb rate 4gbps quantum 1514", dev));
    }

    private void setupTrafficClass(String dev, TrafficClassModel tc) {
        String classId = tc.getName() + "0";
        String handleId = tc.getName().substring(2) + "0:";
        runner.runCommand(String.format("tc class add dev %s parent 1:1 classid %s htb rate 4gbps quantum 1514", dev, classId));

        NetworkParameters p = tc.getNetworkParameters();
        if (p != null) {
            String command = buildNetemCommand(dev, classId, handleId, p);
            runner.runCommand(command);
        }
    }

    private String buildNetemCommand(String dev, String classId, String handleId, NetworkParameters params) {
        StringBuilder cmd = new StringBuilder();
        cmd.append(String.format("tc qdisc add dev %s parent %s handle %s netem", dev, classId, handleId));

        appendIfSet(cmd, "delay", params.getDelayValue(), "ms");
        appendIfSet(cmd, "", params.getJitter(), params.getDelayCorrelation(), "ms");
        appendIfSet(cmd, "loss", params.getLossValue(), params.getLossCorrelation());
        appendIfSet(cmd, "rate", params.getRate(), "kbit");
        appendIfSet(cmd, "reorder", params.getReorderingValue(), params.getReorderingCorrelation());
        appendIfSet(cmd, "duplicate", params.getDuplicationValue(), params.getDuplicationCorrelation());
        appendIfSet(cmd, "corrupt", params.getCorruption());

        return cmd.toString();
    }

    private void appendIfSet(StringBuilder cmd, String keyword, int value) {
        appendIfSet(cmd, keyword, value, -1, null);
    }

    private void appendIfSet(StringBuilder cmd, String keyword, int value, int correlation) {
        appendIfSet(cmd, keyword, value, correlation, null);
    }

    private void appendIfSet(StringBuilder cmd, String keyword, int value, String unit) {
        appendIfSet(cmd, keyword, value, -1, unit);
    }

    private void appendIfSet(StringBuilder cmd, String keyword, int value, int correlation, String unit) {
        if (value == -1) {
            return;
        }

        if (!keyword.isEmpty()) {
            cmd.append(" ").append(keyword);
        }

        cmd.append(" ").append(value);

        if (unit != null) {
            cmd.append(unit);
        }

        if (correlation != -1) {
            cmd.append(" ").append(correlation);
        }
    }

}
