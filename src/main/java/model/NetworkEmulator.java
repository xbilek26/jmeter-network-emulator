package model;

import org.apache.jmeter.testelement.AbstractTestElement;

import java.util.ArrayList;
import java.util.List;

public class NetworkEmulator extends AbstractTestElement {
    private List<String> networkInterfaces;
    private List<String> addedInterfaces;

    public NetworkEmulator() {
        networkInterfaces = new ArrayList<>();
        addedInterfaces = new ArrayList<>();
    }

    public List<String> getNetworkInterfaces() {
        return networkInterfaces;
    }

    public void setNetworkInterfaces(List<String> interfaces) {
        this.networkInterfaces = interfaces;
    }

    public List<String> getAddedInterfaces() {
        return addedInterfaces;
    }

    public void addInterface(String interfaceName) {
        if (!addedInterfaces.contains(interfaceName)) {
            addedInterfaces.add(interfaceName);
        }
    }

    public boolean isInterfaceAdded(String interfaceName) {
        return addedInterfaces.contains(interfaceName);
    }
}
