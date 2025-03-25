package cz.vutbr.networkemulator.controller;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.model.NetworkEmulatorModel;
import cz.vutbr.networkemulator.model.NetworkInterfaceModel;
import cz.vutbr.networkemulator.model.NetworkParameters;
import cz.vutbr.networkemulator.model.TrafficClassModel;

public class NetworkEmulatorController {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(NetworkEmulatorController.class);

    private final NetworkEmulatorModel networkEmulator;

    public NetworkEmulatorController(NetworkEmulatorModel networkEmulator) {
        this.networkEmulator = networkEmulator;
    }

    public void refreshInterfaces() {
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

    public List<String> getNetworkInterfaces() {
        return networkEmulator.getNetworkInterfaces().stream()
                .map(NetworkInterfaceModel::getName)
                .collect(Collectors.toList());
    }

    public void addTrafficClassToInterface(String niName, String tcName) {
        Optional<NetworkInterfaceModel> networkInterface = networkEmulator.getNetworkInterfaces()
                .stream()
                .filter(ni -> ni.getName().equals(niName))
                .findFirst();

        networkInterface.ifPresent(ni -> ni.addTrafficClass(tcName));
    }

    public void removeTrafficClassFromInterface(String niName, String tcName) {
        Optional<NetworkInterfaceModel> networkInterface = networkEmulator.getNetworkInterfaces()
                .stream()
                .filter(ni -> ni.getName().equals(niName))
                .findFirst();

        networkInterface.ifPresent(ni -> ni.removeTrafficClass(tcName));
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

    public void printNetworkConfiguration() {
        networkEmulator.getNetworkInterfaces().forEach(networkInterface -> {
            System.out.println("Interface: " + networkInterface.getName());

            networkInterface.getTrafficClasses().forEach(trafficClass -> {
                System.out.println("  Traffic Class: " + trafficClass.getName());

                NetworkParameters parameters = trafficClass.getNetworkParameters();
                if (parameters != null) {
                    System.out.println("    Parameters:");
                    System.out.println("      Src Address: " + parameters.getSrcAddress());
                    System.out.println("      Src Port: " + parameters.getSrcPort());
                    System.out.println("      Dst Address: " + parameters.getDstAddress());
                    System.out.println("      Dst Port: " + parameters.getDstPort());
                    System.out.println("      Delay Value: " + parameters.getDelayValue());
                    System.out.println("      Jitter: " + parameters.getJitter());
                    System.out.println("      Delay Correlation: " + parameters.getDelayCorrelation());
                    System.out.println("      Drop Value: " + parameters.getDropValue());
                    System.out.println("      Drop Correlation: " + parameters.getDropCorrelation());
                    System.out.println("      Rate: " + parameters.getRate());
                    System.out.println("      Loss: " + parameters.getLoss());
                    System.out.println("      Reordering Value: " + parameters.getReorderingValue());
                    System.out.println("      Reordering Correlation: " + parameters.getReorderingCorrelation());
                    System.out.println("      Duplication Value: " + parameters.getDuplicationValue());
                    System.out.println("      Duplication Correlation: " + parameters.getDuplicationCorrelation());
                    System.out.println("      Corruption Value: " + parameters.getCorruption());
                } else {
                    System.out.println("    No parameters set for this traffic class.");
                }
            });
        });
    }

    public void runEmulation() {
    }

}
