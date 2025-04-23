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

        appendDelay(cmd, params);
        appendLoss(cmd, params);
        appendRate(cmd, params);
        appendReordering(cmd, params);
        appendDuplication(cmd, params);
        appendCorruption(cmd, params);

        CommandRunner.runCommand(cmd.toString());
    }

    private static void appendDelay(StringBuilder cmd, NetworkParameters params) {
        if (params.isDelayValueSet()) {
            cmd.append(String.format(" delay %sms", params.getDelayValue()));
            if (params.isJitterSet()) {
                cmd.append(String.format(" %sms", params.getJitter()));
                if (params.isDelayCorrelationSet()) {
                    cmd.append(String.format(" %s%%", params.getDelayCorrelation()));
                }
                if (params.isDistributionSet()) {
                    cmd.append(String.format(" distribution %s", params.getDistribution()));
                }
            }
        }
    }

    private static void appendLoss(StringBuilder cmd, NetworkParameters params) {
        if (params.isLossValueSet()) {
            cmd.append(String.format(" loss %s%%", params.getLossValue()));
            if (params.isLossCorrelationSet()) {
                cmd.append(String.format(" %s%%", params.getLossCorrelation()));
            }
        }
    }

    private static void appendRate(StringBuilder cmd, NetworkParameters params) {
        if (params.isRateSet()) {
            cmd.append(String.format(" rate %skbit", params.getRate()));
        }
    }

    private static void appendReordering(StringBuilder cmd, NetworkParameters params) {
        if (params.isReorderingValueSet()) {
            cmd.append(String.format(" reorder %s%%", params.getReorderingValue()));
            if (params.isReorderingCorrelationSet()) {
                cmd.append(String.format(" %s%%", params.getReorderingCorrelation()));
            }
        }
    }

    private static void appendDuplication(StringBuilder cmd, NetworkParameters params) {
        if (params.isDuplicationValueSet()) {
            cmd.append(String.format(" duplicate %s%%", params.getDuplicationValue()));
            if (params.isDuplicationCorrelationSet()) {
                cmd.append(String.format(" %s%%", params.getDuplicationCorrelation()));
            }
        }
    }

    private static void appendCorruption(StringBuilder cmd, NetworkParameters params) {
        if (params.isCorruptionSet()) {
            cmd.append(String.format(" corrupt %s%%", params.getCorruption()));
        }
    }

    public static void setupFilter(String dev, String classId, String handleId, NetworkParameters params) {

        StringBuilder cmd = new StringBuilder();
        cmd.append(String.format("tc filter add dev %s protocol ip parent 1:0 prio 1 u32", dev));

        if (params.isSrcAddressSet() && params.isSrcSubnetMaskSet()) {
            cmd.append(String.format(" match ip src %s%s", params.getSrcAddress(), params.getSrcSubnetMask()));
        }

        if (params.isDstAddressSet() && params.isDstSubnetMaskSet()) {
            cmd.append(String.format(" match ip dst %s%s", params.getDstAddress(), params.getDstSubnetMask()));
        }

        if (params.isIpProtocolSet()) {
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
        }

        if (params.isSrcPortSet()) {
            cmd.append(String.format(" match ip sport %d 0xffff", params.getSrcPort()));
        }

        if (params.isDstPortSet()) {
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
