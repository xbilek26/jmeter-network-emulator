package cz.vutbr.networkemulator.utils;

public final class NetworkEmulatorConstants {

    public static final String BTN_ADD_TRAFFIC_CLASS = "Add Traffic Class";
    public static final String BTN_REMOVE_TRAFFIC_CLASS = "Remove Traffic Class";
    public static final String BTN_REFRESH_INTERFACES = "Refresh Interfaces";
    public static final String NETWORK_EMULATOR_STATIC_LABEL = "Network Emulator";
    public static final String NETWORK_EMULATOR_LABEL_RESOURCE = "network_emulator";

    public static final String MSG_GENERAL_ERROR = "Error";
    public static final String MSG_ONE_INSTANCE_ALLOWED = "Only one instance of Network Emulator is allowed in JMeter Tree.";
    public static final String MSG_NO_INTERFACE_SELECTED = "No Interface Selected!";
    public static final String MSG_EMULATION_RUNNING = "Emulation is Running.";
    public static final String MSG_EMULATION_STOPPED = "Emulation is stopped.";
    public static final String MSG_ENTER_TRAFFIC_CLASS_NAME = "Enter traffic class name:";
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

    public static final String LABEL_SRC_ADDRESS = "Src Address:";
    public static final String LABEL_SRC_PORT = "Src Port:";
    public static final String LABEL_DST_ADDRESS = "Dst Address:";
    public static final String LABEL_DST_PORT = "Dst Port:";
    public static final String LABEL_DELAY_VALUE = "Startup Delay (ms):";
    public static final String LABEL_JITTER = "Jitter (ms):";
    public static final String LABEL_DELAY_CORRELATION = "Correlation (%):";
    public static final String LABEL_DISTRIBUTION = "Distribution:";
    public static final String LABEL_LOSS_VALUE = "Packet Loss (%):";
    public static final String LABEL_LOSS_CORRELATION = "Correlation (%):";
    public static final String LABEL_RATE = "Rate (kbps):";
    public static final String LABEL_LOSS = "Loss (%):";
    public static final String LABEL_REORDERING_VALUE = "Reordering (%):";
    public static final String LABEL_REORDERING_CORRELATION = "Correlation (%):";
    public static final String LABEL_DUPLICATION_VALUE = "Duplication (%):";
    public static final String LABEL_DUPLICATION_CORRELATION = "Correlation (%):";
    public static final String LABEL_CORRUPTION = "Corruption (%):";

    private NetworkEmulatorConstants() {
    }
}
