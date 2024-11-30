package cz.vutbr.networkemulator.model;

import org.apache.jmeter.testelement.AbstractTestElement;

public class NetworkInterface extends AbstractTestElement {

    private static final String INTERFACE_NAME = "NetworkInterfaceElement.interfaceName";

    public void setInterfaceName(String name) {
        setProperty(INTERFACE_NAME, name);
    }

    public String getInterfaceName() {
        return getPropertyAsString(INTERFACE_NAME);
    }
}
