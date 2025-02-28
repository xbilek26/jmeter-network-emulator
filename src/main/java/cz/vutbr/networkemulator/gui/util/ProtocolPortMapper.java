package cz.vutbr.networkemulator.gui.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ProtocolPortMapper {

    private static final Map<String, Integer> protocolToPort = new HashMap<>();
    private static final Map<Integer, String> portToProtocol = new HashMap<>();

    static {
        protocolToPort.put("http", 80);
        protocolToPort.put("https", 443);
        protocolToPort.put("ftp", 21);
        protocolToPort.put("ssh", 22);
        protocolToPort.put("telnet", 23);
        protocolToPort.put("smtp", 25);
        protocolToPort.put("dns", 53);
        protocolToPort.put("dhcp", 67);
        protocolToPort.put("pop3", 110);
        protocolToPort.put("imap", 143);
        protocolToPort.put("snmp", 161);
        protocolToPort.put("irc", 6667);

        for (var entry : protocolToPort.entrySet()) {
            portToProtocol.put(entry.getValue(), entry.getKey());
        }
    }

    public static Integer getPort(String protocol) {
        return protocolToPort.get(protocol.toLowerCase());
    }

    public static String getProtocol(int port) {
        return portToProtocol.get(port);
    }

    public static Vector<String> getProtocolsNames() {
        return new Vector<String>(protocolToPort.keySet());
    }
}