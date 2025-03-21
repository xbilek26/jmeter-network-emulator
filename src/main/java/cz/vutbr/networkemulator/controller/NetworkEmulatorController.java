package cz.vutbr.networkemulator.controller;

import java.util.Optional;

import cz.vutbr.networkemulator.model.NetworkEmulator;
import cz.vutbr.networkemulator.model.NetworkInterface;
import cz.vutbr.networkemulator.model.NetworkParameters;
import cz.vutbr.networkemulator.model.TrafficClass;

public class NetworkEmulatorController {

    private final NetworkEmulator networkEmulator;

    public NetworkEmulatorController(NetworkEmulator networkEmulator) {
        this.networkEmulator = networkEmulator;
    }

    public void refreshInterfaces() {
        networkEmulator.clearNetworkInterfaces();
        networkEmulator.addNetworkInterface("ens33");
        networkEmulator.addNetworkInterface("eth0");
    }

    public NetworkEmulator getNetworkEmulator() {
        return networkEmulator;
    }

    public void addNetworkInterface(String name) {
        if (networkEmulator.getNetworkInterfaces().stream().noneMatch(ni -> ni.getName().equals(name))) {
            networkEmulator.addNetworkInterface(name);
        }
    }

    public void removeNetworkInterface(String name) {
        if (networkEmulator.getNetworkInterfaces().stream().anyMatch(ni -> ni.getName().equals(name))) {
            networkEmulator.removeNetworkInterface(name);
        }
    }

    public void addTrafficClassToInterface(String interfaceName, String className) {
        Optional<NetworkInterface> networkInterface = networkEmulator.getNetworkInterfaces()
                .stream()
                .filter(ni -> ni.getName().equals(interfaceName))
                .findFirst();

        networkInterface.ifPresent(ni -> ni.addTrafficClass(className));
    }

    public void removeTrafficClassFromInterface(String interfaceName, String className) {
        Optional<NetworkInterface> networkInterface = networkEmulator.getNetworkInterfaces()
                .stream()
                .filter(ni -> ni.getName().equals(interfaceName))
                .findFirst();

        networkInterface.ifPresent(ni -> ni.removeTrafficClass(className));
    }

    public void setNetworkParameters(String interfaceName, String className, NetworkParameters networkParameters) {
        networkEmulator.getNetworkInterfaces().stream()
                .filter(ni -> ni.getName().equals(interfaceName))
                .flatMap(ni -> ni.getTrafficClasses().stream())
                .filter(tc -> tc.getName().equals(className))
                .findFirst()
                .ifPresent(tc -> tc.setNetworkParameters(networkParameters));
    }

    public NetworkParameters getNetworkParameters(String interfaceName, String className) {
        return networkEmulator.getNetworkInterfaces().stream()
                .filter(ni -> ni.getName().equals(interfaceName))
                .flatMap(ni -> ni.getTrafficClasses().stream())
                .filter(tc -> tc.getName().equals(className))
                .findFirst()
                .map(TrafficClass::getNetworkParameters)
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