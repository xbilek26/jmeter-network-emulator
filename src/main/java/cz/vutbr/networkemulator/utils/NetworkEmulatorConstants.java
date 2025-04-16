package cz.vutbr.networkemulator.utils;

public final class NetworkEmulatorConstants {

    public static final String BTN_ADD_TRAFFIC_CLASS = "Add Traffic Class";
    public static final String BTN_REMOVE_TRAFFIC_CLASS = "Remove Traffic Class";
    public static final String BTN_REFRESH_INTERFACES = "Refresh Interfaces";
    public static final String NETWORK_EMULATOR_STATIC_LABEL = "Network Emulator";
    public static final String NETWORK_EMULATOR_LABEL_RESOURCE = "network_emulator";

    public static final String MSG_GENERAL_ERROR = "Error";
    public static final String MSG_ONE_INSTANCE_ALLOWED = "Only one instance of Network Emulator is allowed in JMeter Tree.";
    public static final String MSG_EMULATION_RUNNING = "Emulation is running.";
    public static final String MSG_EMULATION_STOPPED = "Emulation is stopped.";
    public static final String MSG_ENTER_VALID_IP_ADDRESS = "Enter a valid IPv4 or IPv6 Address.";
    public static final String BAD_ADDRESS = "Bad Address";
    public static final String ROOT_NODE_NAME = "Network Interfaces";
    public static final String BTN_START_EMULATION = "Start";
    public static final String BTN_STOP_EMULATION = "Stop";

    public static final String TITLE_MAIN_PANEL = "Main Panel";
    public static final String TITLE_CONFIG_PANEL = "Configuration Panel";
    public static final String TITLE_FILTER_PANEL = "Filter";
    public static final String TITLE_DELAY_PANEL = "Delay";
    public static final String TITLE_LOSS_PANEL = "Packet Loss";
    public static final String TITLE_RATE_PANEL = "Rate";
    public static final String TITLE_REORDERING_PANEL = "Reordering";
    public static final String TITLE_DUPLICATION_PANEL = "Duplication";
    public static final String TITLE_CORRUPTION_PANEL = "Corruption";

    public static final String SRC_ADDRESS = "Src Address";
    public static final String SRC_PORT = "Src Port";
    public static final String DST_ADDRESS = "Dst Address";
    public static final String DST_PORT = "Dst Port";
    public static final String DELAY_VALUE = "Startup Delay";
    public static final String JITTER = "Jitter";
    public static final String DELAY_CORRELATION = "Delay Correlation";
    public static final String DISTRIBUTION = "Distribution";
    public static final String LOSS_VALUE = "Packet Loss";
    public static final String LOSS_CORRELATION = "Loss Correlation";
    public static final String RATE = "Rate";
    public static final String REORDERING_VALUE = "Reordering";
    public static final String REORDERING_CORRELATION = "Reordering Correlation";
    public static final String DUPLICATION_VALUE = "Duplication";
    public static final String DUPLICATION_CORRELATION = "Duplication Correlation";
    public static final String CORRUPTION = "Corruption";
    public static final String CORRELATION = "Correlation";

    public static final String LABEL_SRC_ADDRESS = SRC_ADDRESS + ":";
    public static final String LABEL_SRC_PORT = SRC_PORT + ":";
    public static final String LABEL_DST_ADDRESS = DST_ADDRESS + ":";
    public static final String LABEL_DST_PORT = DST_PORT + ":";
    public static final String LABEL_DELAY_VALUE = DELAY_VALUE + " (ms):";
    public static final String LABEL_JITTER = JITTER + " (ms):";
    public static final String LABEL_DELAY_CORRELATION = CORRELATION + " (%):";
    public static final String LABEL_DISTRIBUTION = DISTRIBUTION + ":";
    public static final String LABEL_LOSS_VALUE = LOSS_VALUE + " (%):";
    public static final String LABEL_LOSS_CORRELATION = CORRELATION + " (%):";
    public static final String LABEL_RATE = RATE + " (kbps):";
    public static final String LABEL_REORDERING_VALUE = REORDERING_VALUE + " (%):";
    public static final String LABEL_REORDERING_CORRELATION = CORRELATION + " (%):";
    public static final String LABEL_DUPLICATION_VALUE = DUPLICATION_VALUE + " (%):";
    public static final String LABEL_DUPLICATION_CORRELATION = CORRELATION + " (%):";
    public static final String LABEL_CORRUPTION = CORRUPTION + " (%):";

    private NetworkEmulatorConstants() {
    }
}
