package cz.vutbr.networkemulator.model;

import org.apache.jmeter.testelement.AbstractTestElement;

import java.util.ArrayList;

public class NetworkEmulator extends AbstractTestElement {

    private ArrayList<String> availableNetworkInterfaces;
    private ArrayList<String> addedInterfaces;
    public void setAvaiableNetworkInterfaces(ArrayList<String> availableNetworkInterfaces) {
        this.availableNetworkInterfaces = availableNetworkInterfaces;
    }

    public NetworkEmulator() {
        availableNetworkInterfaces = new ArrayList<>();
        addedInterfaces = new ArrayList<>();
    }

    public ArrayList<String> getAvailableNetworkInterfaces() {
        return availableNetworkInterfaces;
    }

    public ArrayList<String> getAddedInterfaces() {
        return addedInterfaces;
    }

    public void addInterface(String networkInterface) {
        if (!addedInterfaces.contains(networkInterface)) {
            addedInterfaces.add(networkInterface);
        }
    }

    public void removeInterface(String networkInterface) {
        if (addedInterfaces.contains(networkInterface)) {
            addedInterfaces.remove(networkInterface);
        }
    }

    public boolean isInterfaceAdded(String networkInterface) {
        return addedInterfaces.contains(networkInterface);
    }

    public ArrayList<String> getAddedNetworkInterfaces() {
        return addedInterfaces;
    }
}
