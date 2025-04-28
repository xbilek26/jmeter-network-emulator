package cz.vutbr.networkemulator.linux;

import java.util.List;

import cz.vutbr.networkemulator.model.filter.Filter;
import cz.vutbr.networkemulator.model.parameters.Parameter;

public class TrafficControl {

    public static void setupRootQdisc(String dev) {
        CommandRunner.runCommand(String.format("tc qdisc del dev %s root", dev));
        CommandRunner.runCommand(String.format("tc qdisc add dev %s root handle 1: htb", dev));
        CommandRunner.runCommand(String.format("tc class add dev %s parent 1: classid 1:1 htb rate 4gbps quantum 1514", dev));
    }

    public static void setupTrafficClass(String dev, String classId, String handleId, List<Parameter> parameters) {
        CommandRunner.runCommand(String.format("tc class add dev %s parent 1:1 classid %s htb rate 4gbps quantum 1514", dev, classId));

        StringBuilder cmd = new StringBuilder();
        cmd.append(String.format("tc qdisc add dev %s parent %s handle %s netem", dev, classId, handleId));

        for (Parameter parameter : parameters) {
            parameter.appendToCommand(cmd);
        }

        CommandRunner.runCommand(cmd.toString());
    }

    public static void setupFilter(String dev, String classId, String handleId, Filter filter) {

        StringBuilder cmd = new StringBuilder();
        cmd.append(String.format("tc filter add dev %s protocol ip parent 1:0 prio 1 u32", dev));

        filter.appendToCommand(cmd);

        cmd.append(String.format(" flowid %s", classId));

        CommandRunner.runCommand(cmd.toString());
    }

    public static String showQDiscs() {
        return CommandRunner.runCommand("tc qdisc show");
    }

    public static String showFilter(String dev) {
        return CommandRunner.runCommand(String.format("tc filter show dev %s", dev));
    }

    public static void restoreDefaults(String dev) {
        CommandRunner.runCommand(String.format("tc qdisc del dev %s root", dev));
    }

}
