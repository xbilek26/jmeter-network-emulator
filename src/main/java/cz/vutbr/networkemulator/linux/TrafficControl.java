package cz.vutbr.networkemulator.linux;

import cz.vutbr.networkemulator.model.NetworkParameters;
import cz.vutbr.networkemulator.utils.NetworkEmulatorConstants;

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

        appendDelay(cmd, params.getDelayValue(), params.getJitter(), params.getDelayCorrelation(), params.getDistribution());
        appendLoss(cmd, params.getLossValue(), params.getLossCorrelation());
        appendRate(cmd, params.getRate());
        appendReordering(cmd, params.getReorderingValue(), params.getReorderingCorrelation());
        appendDuplication(cmd, params.getDuplicationValue(), params.getDuplicationCorrelation());
        appendCorruption(cmd, params.getCorruption());

        CommandRunner.runCommand(cmd.toString());
    }

    private static void appendDelay(StringBuilder cmd, int delay, int jitter, int correlation, String distribution) {
        if (delay != -1) {
            cmd.append(String.format(" delay %sms", delay));
            if (jitter != -1) {
                cmd.append(String.format(" %sms", jitter));
                if (correlation != -1) {
                    cmd.append(String.format(" %s%%", correlation));
                }
                cmd.append(String.format(" distribution %s", distribution));
            }
        }
    }

    private static void appendLoss(StringBuilder cmd, int loss, int correlation) {
        if (loss != -1) {
            cmd.append(String.format(" loss %s%%", loss));
            if (correlation != -1) {
                cmd.append(String.format(" %s%%", correlation));
            }
        }
    }

    private static void appendRate(StringBuilder cmd, int rate) {
        if (rate != -1) {
            cmd.append(String.format(" rate %skbit", rate));
        }
    }

    private static void appendReordering(StringBuilder cmd, int reordering, int correlation) {
        if (reordering != -1) {
            cmd.append(String.format(" reorder %s%%", reordering));
            if (correlation != -1) {
                cmd.append(String.format(" %s%%", correlation));
            }
        }
    }

    private static void appendDuplication(StringBuilder cmd, int duplication, int correlation) {
        if (duplication != -1) {
            cmd.append(String.format(" duplicate %s%%", duplication));
            if (correlation != -1) {
                cmd.append(String.format(" %s%%", correlation));
            }
        }
    }

    private static void appendCorruption(StringBuilder cmd, int corruption) {
        if (corruption != -1) {
            cmd.append(String.format(" corrupt %s%%", corruption));
        }
    }

    public static void setupFilter(String dev, String classId, String handleId, NetworkParameters params) {

        StringBuilder cmd = new StringBuilder();
        cmd.append(String.format("tc filter add dev %s protocol ip parent 1:0 prio 1 u32", dev));

        if (params.getSrcAddress() != null && !params.getSrcAddress().isEmpty()) {
            cmd.append(String.format(" match ip src %s%s", params.getSrcAddress(), params.getSrcSubnetMask()));
        }

        if (params.getDstAddress() != null && !params.getDstAddress().isEmpty()) {
            cmd.append(String.format(" match ip dst %s", params.getDstAddress(), params.getDstSubnetMask()));
        }

        switch (params.getIpProtocol()) {
            case NetworkEmulatorConstants.UDP_PROTOCOL:
                cmd.append(" match ip protocol 17 0xff");
                break;
            case NetworkEmulatorConstants.ICMP_PROTOCOL:
                cmd.append(" match ip protocol 1 0xff");
                break;
            case NetworkEmulatorConstants.TCP_PROTOCOL:
            default:
                cmd.append(" match ip protocol 6 0xff");
                break;
        }

        if (params.getSrcPort() != -1) {
            cmd.append(String.format(" match ip sport %d 0xffff", params.getSrcPort()));
        }

        if (params.getDstPort() != -1) {
            cmd.append(String.format(" match ip dport %d 0xffff", params.getDstPort()));
        }

        cmd.append(String.format(" flowid %s", classId));

        System.out.println("filter command " + cmd.toString());
        CommandRunner.runCommand(cmd.toString());
    }

    public static String showQDiscs() {
        return CommandRunner.runCommand("tc qdisc show");
    }

    public static void restoreDefaults(String dev) {
        CommandRunner.runCommand(String.format("tc qdisc del dev %s root", dev));
    }

}
