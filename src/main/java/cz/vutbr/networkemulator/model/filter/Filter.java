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
    private String dstAddress;
    private String dstSubnetMask;
    private String srcPort;
    private String dstPort;
    private String icmpType;
    private String icmpCode;

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

    public String getSrcPort() {
        return srcPort;
    }

    public void setSrcPort(String srcPort) {
        this.srcPort = srcPort;
    }

    public boolean isSrcPortSet() {
        return srcPort != null && !srcPort.isEmpty();
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

    public String getIcmpType() {
        return icmpType;
    }

    public void setIcmpType(String icmpType) {
        this.icmpType = icmpType;
    }

    public boolean isIcmpTypeSet() {
        return icmpType != null && !icmpType.isEmpty();
    }

    public String getIcmpCode() {
        return icmpCode;
    }

    public void setIcmpCode(String icmpCode) {
        this.icmpCode = icmpCode;
    }

    public boolean isIcmpCodeSet() {
        return icmpCode != null && !icmpCode.isEmpty();
    }

    public void appendToCommand(StringBuilder cmd) {
        if (isSrcAddressSet() && isSrcSubnetMaskSet()) {
            cmd.append(String.format(" match ip src %s%s", getSrcAddress(), getSrcSubnetMask()));
        }

        if (isDstAddressSet() && isDstSubnetMaskSet()) {
            cmd.append(String.format(" match ip dst %s%s", getDstAddress(), getDstSubnetMask()));
        }

        if (isIpProtocolSet()) {
            String protocol = getIpProtocol();

            cmd.append(" match ip protocol ")
                    .append(getProtocolNumber(protocol))
                    .append(" 0xff");

            switch (protocol) {
                case NetworkEmulatorConstants.UDP_PROTOCOL, NetworkEmulatorConstants.TCP_PROTOCOL -> {
                    if (isSrcPortSet()) {
                        cmd.append(String.format(" match ip sport %s 0xffff", getSrcPort()));
                    }
                    if (isDstPortSet()) {
                        cmd.append(String.format(" match ip dport %s 0xffff", getDstPort()));
                    }
                }
                case NetworkEmulatorConstants.ICMP_PROTOCOL -> {
                    if (isIcmpTypeSet()) {
                        cmd.append(String.format(" match ip icmp_type %s 0xff", getIcmpType()));
                    }
                    if (isIcmpCodeSet()) {
                        cmd.append(String.format(" match ip icmp_code %s 0xff", getIcmpCode()));
                    }
                }
            }
        }
    }

    private String getProtocolNumber(String protocol) {
        return switch (protocol) {
            case NetworkEmulatorConstants.UDP_PROTOCOL ->
                "17";
            case NetworkEmulatorConstants.ICMP_PROTOCOL ->
                "1";
            case NetworkEmulatorConstants.TCP_PROTOCOL ->
                "6";
            default ->
                throw new IllegalArgumentException("Unknown protocol: " + protocol);
        };
    }

    public void appendToTable(DefaultTableModel model) {
        if (isIpProtocolSet()) {
            String protocol = getIpProtocol();
            model.addRow(new Object[]{
                NetworkEmulatorConstants.IP_PROTOCOL,
                protocol
            });

            switch (protocol) {
                case NetworkEmulatorConstants.TCP_PROTOCOL, NetworkEmulatorConstants.UDP_PROTOCOL -> {
                    if (isSrcPortSet()) {
                        model.addRow(new Object[]{
                            NetworkEmulatorConstants.SRC_PORT,
                            getSrcPort()
                        });
                    }
                    if (isDstPortSet()) {
                        model.addRow(new Object[]{
                            NetworkEmulatorConstants.DST_PORT,
                            getDstPort()
                        });
                    }
                }
                case NetworkEmulatorConstants.ICMP_PROTOCOL -> {
                    if (isIcmpTypeSet()) {
                        model.addRow(new Object[]{
                            NetworkEmulatorConstants.ICMP_TYPE,
                            getIcmpType()
                        });
                    }
                    if (isIcmpCodeSet()) {
                        model.addRow(new Object[]{
                            NetworkEmulatorConstants.ICMP_CODE,
                            getIcmpCode()
                        });
                    }
                }
            }
        }

        if (isSrcAddressSet()) {
            model.addRow(new Object[]{
                NetworkEmulatorConstants.SRC_ADDRESS,
                getSrcAddress() + getSrcSubnetMask()
            });
        }
        if (isDstAddressSet()) {
            model.addRow(new Object[]{
                NetworkEmulatorConstants.DST_ADDRESS,
                getDstAddress() + getDstSubnetMask()
            });
        }
    }

}
