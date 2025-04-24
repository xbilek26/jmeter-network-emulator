package cz.vutbr.networkemulator.model.filter;

import javax.swing.table.DefaultTableModel;

import cz.vutbr.networkemulator.utils.NetworkEmulatorConstants;

public class Filter {

    public static final String UDP = "UDP";
    public static final String TCP = "TCP";
    public static final String ICMP = "ICMP";

    private String ipProtocol;
    private String srcAddress;
    private String srcSubnetMask;
    private String srcPort;
    private String dstAddress;
    private String dstSubnetMask;
    private String dstPort;

    public String getIpProtocol() {
        return ipProtocol;
    }

    public void setIpProtocol(String ipProtocol) {
        this.ipProtocol = ipProtocol;
    }

    public boolean isIpProtocolSet() {
        return ipProtocol != null && !ipProtocol.isEmpty();
    }

    public String getSrcAddress() {
        return srcAddress;
    }

    public void setSrcAddress(String srcAddress) {
        this.srcAddress = srcAddress;
    }

    public boolean isSrcAddressSet() {
        return srcAddress != null && !srcAddress.isEmpty();
    }

    public String getSrcSubnetMask() {
        return srcSubnetMask;
    }

    public void setSrcSubnetMask(String srcSubnetMask) {
        this.srcSubnetMask = srcSubnetMask;
    }

    public boolean isSrcSubnetMaskSet() {
        return srcSubnetMask != null && !srcSubnetMask.isEmpty();
    }

    public String getSrcPort() {
        return srcPort;
    }

    public void setSrcPort(String srcPort) {
        this.srcPort = srcPort;
    }

    public boolean isSrcPortSet() {
        return srcPort != null && !srcPort.isEmpty();
    }

    public String getDstAddress() {
        return dstAddress;
    }

    public void setDstAddress(String dstAddress) {
        this.dstAddress = dstAddress;
    }

    public boolean isDstAddressSet() {
        return dstAddress != null && !dstAddress.isEmpty();
    }

    public String getDstSubnetMask() {
        return dstSubnetMask;
    }

    public void setDstSubnetMask(String dstSubnetMask) {
        this.dstSubnetMask = dstSubnetMask;
    }

    public boolean isDstSubnetMaskSet() {
        return dstSubnetMask != null && !dstSubnetMask.isEmpty();
    }

    public String getDstPort() {
        return dstPort;
    }

    public void setDstPort(String dstPort) {
        this.dstPort = dstPort;
    }

    public boolean isDstPortSet() {
        return dstPort != null && !dstPort.isEmpty();
    }

    public void appendToCommand(StringBuilder cmd) {
        if (isSrcAddressSet() && isSrcSubnetMaskSet()) {
            cmd.append(String.format(" match ip src %s%s", getSrcAddress(), getSrcSubnetMask()));
        }

        if (isDstAddressSet() && isDstSubnetMaskSet()) {
            cmd.append(String.format(" match ip dst %s%s", getDstAddress(), getDstSubnetMask()));
        }

        if (isIpProtocolSet()) {
            switch (getIpProtocol()) {
                case UDP:
                    cmd.append(" match ip protocol 17 0xff");
                    break;
                case ICMP:
                    cmd.append(" match ip protocol 1 0xff");
                    break;
                case TCP:
                default:
                    cmd.append(" match ip protocol 6 0xff");
                    break;
            }
        }

        if (isSrcPortSet()) {
            cmd.append(String.format(" match ip sport %s 0xffff", getSrcPort()));
        }

        if (isDstPortSet()) {
            cmd.append(String.format(" match ip dport %s 0xffff", getDstPort()));
        }
    }

    public void appendToTable(DefaultTableModel model) {
        if (isIpProtocolSet()) {
            model.addRow(new Object[]{
                NetworkEmulatorConstants.IP_PROTOCOL,
                getIpProtocol()
            });
        }
        if (isSrcAddressSet()) {
            model.addRow(new Object[]{
                NetworkEmulatorConstants.SRC_ADDRESS,
                getSrcAddress() + getSrcSubnetMask()
            });
        }
        if (isSrcPortSet()) {
            model.addRow(new Object[]{
                NetworkEmulatorConstants.SRC_PORT,
                getSrcPort()
            });
        }
        if (isDstAddressSet()) {
            model.addRow(new Object[]{
                NetworkEmulatorConstants.DST_ADDRESS,
                getDstAddress() + getDstSubnetMask()
            });
        }
        if (isDstPortSet()) {
            model.addRow(new Object[]{
                NetworkEmulatorConstants.DST_PORT,
                getDstPort()
            });
        }
    }

}
