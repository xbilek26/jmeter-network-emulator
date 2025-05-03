package cz.vutbr.networkemulator.linux;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.vutbr.networkemulator.model.filter.Filter;
import cz.vutbr.networkemulator.model.parameters.Parameter;

public class TrafficControl {

    public static void setupRootQdisc(String dev) {
        CommandRunner.runCommand(String.format("tc qdisc del dev %s root", dev));
        CommandRunner.runCommand(String.format("tc qdisc add dev %s root handle 1: htb", dev));
        CommandRunner.runCommand(String.format("tc class add dev %s parent 1: classid 1:1 htb rate 4gbps quantum 1514", dev));
    }

    public static void setupEmulationRule(String dev, String classId, String handleId, List<Parameter> parameters) {
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

    public static String showQDisc(String dev) {
        return CommandRunner.runCommand(String.format("tc qdisc show dev %s", dev));
    }

    public static String showFilter(String dev) {
        return CommandRunner.runCommand(String.format("tc filter show dev %s", dev));
    }

    public static void restoreDefaults(String dev) {
        CommandRunner.runCommand(String.format("tc qdisc del dev %s root", dev));
    }

    public static String showQdiscAndFilters(String dev) {
        StringBuilder output = new StringBuilder();

        String qdiscOutput = showQDisc(dev);
        String[] qdiscLines = qdiscOutput.split("\\r?\\n");
        String filterOutput = showFilter(dev);
        String[] filterLines = filterOutput.split("\\r?\\n");

        for (String qdiscLine : qdiscLines) {
            // skip root htb
            if (qdiscLine.contains("root") && qdiscLine.contains("htb")) {
                continue;
            }

            qdiscLine = qdiscLine.replace("Kbit", "kbps")
                    .replace("Mbit", "Mbps")
                    .replace("bit", "bps")
                    .replaceAll("\\s+", " ");

            qdiscLine = String.format("%s: %s", dev, qdiscLine);

            output.append(qdiscLine).append("\n");

            // find parent x:y
            String parent = null;
            Pattern pattern = Pattern.compile("\\b(\\d+:\\d+)\\b");
            Matcher matcher = pattern.matcher(qdiscLine);
            if (matcher.find()) {
                parent = matcher.group(1);
            }

            boolean matchingFilter = false;
            for (String filterLine : filterLines) {
                if (filterLine.contains("flowid " + parent)) {
                    matchingFilter = true;
                    continue;
                }

                if (matchingFilter) {
                    if (filterLine.trim().startsWith("match")) {
                        output.append("        ").append(filterLine.trim()).append("\n");
                    } else if (!filterLine.startsWith(" ")) {
                        matchingFilter = false;
                    }
                }
            }
        }

        return output.toString();
    }
}
