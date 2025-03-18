package cz.vutbr.networkemulator.model;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkEmulator extends AbstractTestElement {

    private static final Logger log = LoggerFactory.getLogger(NetworkEmulator.class);

    public static final String NETWORK_INTERFACES = "NetworkEmulatorTestElement.networkInterfaces";
    public static final String TRAFFIC_CLASSES = "NetworkEmulatorTestElement.trafficClasses";

}