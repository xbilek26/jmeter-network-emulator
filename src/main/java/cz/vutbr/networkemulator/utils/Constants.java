package cz.vutbr.networkemulator.utils;

import java.util.Map;

public final class Constants {

    public static final String BTN_ADD_TRAFFIC_CLASS = "Add Traffic Class";
    public static final String BTN_REMOVE_TRAFFIC_CLASS = "Remove Traffic Class";
    public static final String BTN_REFRESH_INTERFACES = "Refresh Interfaces";
    public static final String NETWORK_EMULATOR_STATIC_LABEL = "Network Emulator";
    public static final String NETWORK_EMULATOR_LABEL_RESOURCE = "network_emulator";

    public static final String MSG_GENERAL_ERROR = "Error";
    public static final String MSG_ONE_INSTANCE_ALLOWED = "Only one instance of Network Emulator is allowed in JMeter Tree.";
    public static final String MSG_EMULATION_RUNNING = "Emulation is running.";
    public static final String MSG_EMULATION_STOPPED = "Emulation is stopped.";
    public static final String MSG_ENTER_VALID_IPV4_ADDRESS = "Enter a valid IPv4 Address.";
    public static final String MSG_BAD_IPV4_ADDRESS = "Bad IPv4 Address";
    public static final String MSG_ENTER_VALID_IPV6_ADDRESS = "Enter a valid IPv6 Address.";
    public static final String MSG_BAD_IPV6_ADDRESS = "Bad IPv6 Address";
    public static final String BTN_START_EMULATION = "Start";
    public static final String BTN_STOP_EMULATION = "Stop";

    public static final String TITLE_NETWORK_SETTINGS = "Network Settings";
    public static final String TITLE_CONTROLS = "Controls";
    public static final String TITLE_CONFIG_PANEL = "Configuration Panel";
    public static final String TITLE_FILTER_PANEL = "Filter";
    public static final String TITLE_DELAY_PANEL = "Delay";
    public static final String TITLE_REORDERING_PANEL = "Reordering";
    public static final String TITLE_LOSS_PANEL = "Packet Loss";
    public static final String TITLE_RATE_PANEL = "Rate";
    public static final String TITLE_DUPLICATION_PANEL = "Duplication";
    public static final String TITLE_CORRUPTION_PANEL = "Corruption";

    public static final String ROOT_NODE_NAME = "Network Interfaces";

    public static final String IP_VERSION = "IP version";
    public static final String IPV4 = "IPv4";
    public static final String IPV6 = "IPv6";
    public static final String PROTOCOL = "Protocol";
    public static final String TCP_PROTOCOL = "TCP";
    public static final String UDP_PROTOCOL = "UDP";
    public static final String ICMP_PROTOCOL = "ICMP";
    public static final String SRC_ADDRESS = "Src Address";
    public static final String DST_ADDRESS = "Dst Address";
    public static final String SRC_PORT = "Src Port";
    public static final String DST_PORT = "Dst Port";
    public static final String ICMP_TYPE = "ICMP Type";
    public static final String ICMP_CODE = "ICMP Code";
    public static final String DELAY_VALUE = "Startup Delay";
    public static final String JITTER = "Jitter";
    public static final String DELAY_CORRELATION = "Delay Correlation";
    public static final String DISTRIBUTION = "Distribution";
    public static final String REORDERING_VALUE = "Reordering";
    public static final String REORDERING_CORRELATION = "Reordering Correlation";
    public static final String LOSS_VALUE = "Packet Loss";
    public static final String LOSS_CORRELATION = "Loss Correlation";
    public static final String RATE = "Rate";
    public static final String OVERHEAD = "Overhead";
    public static final String DUPLICATION_VALUE = "Duplication";
    public static final String DUPLICATION_CORRELATION = "Duplication Correlation";
    public static final String CORRUPTION = "Corruption";
    public static final String CORRELATION = "Correlation";

    public static final String LABEL_IP_VERSION = IP_VERSION + ":";
    public static final String LABEL_PROTOCOL = PROTOCOL + ":";
    public static final String LABEL_SRC_ADDRESS = SRC_ADDRESS + ":";
    public static final String LABEL_DST_ADDRESS = DST_ADDRESS + ":";
    public static final String LABEL_SRC_PORT = SRC_PORT + ":";
    public static final String LABEL_DST_PORT = DST_PORT + ":";
    public static final String LABEL_ICMP_TYPE = ICMP_TYPE + ":";
    public static final String LABEL_ICMP_CODE = ICMP_CODE + ":";
    public static final String LABEL_DELAY_VALUE = DELAY_VALUE + " (ms):";
    public static final String LABEL_JITTER = JITTER + " (ms):";
    public static final String LABEL_DELAY_CORRELATION = CORRELATION + " (%):";
    public static final String LABEL_DISTRIBUTION = DISTRIBUTION + ":";
    public static final String LABEL_REORDERING_VALUE = REORDERING_VALUE + " (%):";
    public static final String LABEL_REORDERING_CORRELATION = CORRELATION + " (%):";
    public static final String LABEL_LOSS_VALUE = LOSS_VALUE + " (%):";
    public static final String LABEL_LOSS_CORRELATION = CORRELATION + " (%):";
    public static final String LABEL_RATE = RATE + " (kbps):";
    public static final String LABEL_OVERHEAD = OVERHEAD + " (B):";
    public static final String LABEL_DUPLICATION_VALUE = DUPLICATION_VALUE + " (%):";
    public static final String LABEL_DUPLICATION_CORRELATION = CORRELATION + " (%):";
    public static final String LABEL_CORRUPTION = CORRUPTION + " (%):";

    public static final String[] PREFIX_LENGTHS_IPV4 = {
        "/32", "/31", "/30", "/29", "/28", "/27", "/26", "/25",
        "/24", "/23", "/22", "/21", "/20", "/19", "/18", "/17", "/16"
    };

    public static final String[] PREFIX_LENGTHS_IPV6 = {
        "/128", "/124", "/120", "/116", "/112", "/108", "/104", "/100",
        "/96", "/92", "/88", "/84", "/80", "/76", "/72", "/68", "/64"
    };

    public static final String[] PROTOCOLS = {
        "",
        "HTTP",
        "HTTPS",
        "FTP",
        "SSH",
        "DNS",
        "SMTP",
        "POP3",
        "IMAP"
    };

    public static final Map<String, Integer> PROTOCOL_PORTS = Map.of(
            "HTTP", 80,
            "HTTPS", 443,
            "FTP", 21,
            "SSH", 22,
            "DNS", 53,
            "SMTP", 25,
            "POP3", 110,
            "IMAP", 143
    );

    public static final String[] DISTRIBUTIONS = {
        // "uniform", not present in /usr/lib/tc
        "",
        "normal",
        "pareto",
        "paretonormal"
    };

    private Constants() {
    }
}
