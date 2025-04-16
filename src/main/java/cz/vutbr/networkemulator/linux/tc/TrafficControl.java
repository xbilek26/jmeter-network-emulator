package cz.vutbr.networkemulator.linux.tc;

import cz.vutbr.networkemulator.linux.CommandRunner;
import cz.vutbr.networkemulator.model.NetworkParameters;

public class TrafficControl {

    public static void setupRootQdisc(String dev) {
        CommandRunner.runCommand(String.format("tc qdisc del dev %s root", dev));
        CommandRunner.runCommand(String.format("tc qdisc add dev %s root handle 1: htb", dev));
        CommandRunner.runCommand(String.format("tc class add dev %s parent 1: classid 1:1 htb rate 4gbps quantum 1514", dev));
    }

    public static void setupTrafficClass(String dev, String classId, String handleId, NetworkParameters params) {
        CommandRunner.runCommand(String.format("tc class add dev %s parent 1:1 classid %s htb rate 4gbps quantum 1514", dev, classId));

        StringBuilder cmd = new StringBuilder();
        cmd.append(String.format("tc qdisc add dev %s parent %s handle %s netem", dev, classId, handleId));

        appendIfSet(cmd, "delay", params.getDelayValue(), "ms");
        appendIfSet(cmd, "", params.getJitter(), params.getDelayCorrelation(), "ms");
        appendIfSet(cmd, "loss", params.getLossValue(), params.getLossCorrelation());
        appendIfSet(cmd, "rate", params.getRate(), "kbit");
        appendIfSet(cmd, "reorder", params.getReorderingValue(), params.getReorderingCorrelation());
        appendIfSet(cmd, "duplicate", params.getDuplicationValue(), params.getDuplicationCorrelation());
        appendIfSet(cmd, "corrupt", params.getCorruption());

        CommandRunner.runCommand(cmd.toString());
    }

    private static void appendIfSet(StringBuilder cmd, String keyword, int value) {
        appendIfSet(cmd, keyword, value, -1, null);
    }

    private static void appendIfSet(StringBuilder cmd, String keyword, int value, int correlation) {
        appendIfSet(cmd, keyword, value, correlation, null);
    }

    private static void appendIfSet(StringBuilder cmd, String keyword, int value, String unit) {
        appendIfSet(cmd, keyword, value, -1, unit);
    }

    private static void appendIfSet(StringBuilder cmd, String keyword, int value, int correlation, String unit) {
        if (value == -1) {
            return;
        }

        if (!keyword.isEmpty()) {
            cmd.append(" ").append(keyword);
        }

        cmd.append(" ").append(value);

        if (unit != null) {
            cmd.append(unit);
        }

        if (correlation != -1) {
            cmd.append(" ").append(correlation);
        }
    }

    public static void setupFilter(String dev, String classId, String handleId, NetworkParameters params) {

        StringBuilder cmd = new StringBuilder();
        cmd.append(String.format("tc filter add dev %s protocol ip parent 1:0 prio 1 u32", dev));

        if (params.getSrcAddress() != null && !params.getSrcAddress().isEmpty()) {
            cmd.append(String.format(" match ip src %s", params.getSrcAddress()));
        }

        if (params.getDstAddress() != null && !params.getDstAddress().isEmpty()) {
            cmd.append(String.format(" match ip dst %s", params.getDstAddress()));
        }

        // TODO: implement other protocols (TCP for now)
        cmd.append(" match ip protocol 6 0xff");

        if (params.getSrcPort() != -1) {
            cmd.append(String.format(" match ip sport %d 0xffff", params.getSrcPort()));
        }

        if (params.getDstPort() != -1) {
            cmd.append(String.format(" match ip dport %d 0xffff", params.getDstPort()));
        }

        cmd.append(String.format(" flowid %s", classId));

        CommandRunner.runCommand(cmd.toString());
    }

    public static String showQDiscs() {
        return CommandRunner.runCommand("tc qdisc show");
    }

    public static void restoreDefaults(String dev) {
        CommandRunner.runCommand(String.format("tc qdisc del dev %s root", dev));
    }

}
