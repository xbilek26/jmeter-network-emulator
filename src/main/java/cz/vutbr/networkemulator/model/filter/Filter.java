package cz.vutbr.networkemulator.model.filter;

import javax.swing.table.DefaultTableModel;

import cz.vutbr.networkemulator.utils.Constants;

public class Filter {

    private String ipVersion;
    private String protocol;
    private String srcAddress;
    private String srcSubnetMask;
    private String dstAddress;
    private String dstSubnetMask;
    private String srcPort;
    private String dstPort;
    private String icmpType;
    private String icmpCode;

    public String getIpVersion() {
        return ipVersion;
    }

    public void setIpVersion(String ipVersion) {
        this.ipVersion = ipVersion;
    }

    public boolean isIpVersionSet() {
        return ipVersion != null && !ipVersion.isEmpty();
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String ipProtocol) {
        this.protocol = ipProtocol;
    }

    public boolean isProtocolSet() {
        return protocol != null && !protocol.isEmpty();
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
        String ipCmd = resolveIpVersion();
        if (isSrcAddressSet() && isSrcSubnetMaskSet()) {
            cmd.append(String.format(" match %s src %s%s", ipCmd, this.srcAddress, this.srcSubnetMask));
        }

        if (isDstAddressSet() && isDstSubnetMaskSet()) {
            cmd.append(String.format(" match %s dst %s%s", ipCmd, this.dstAddress, this.dstSubnetMask));
        }

        if (isProtocolSet()) {
            cmd.append(String.format(" match %s protocol %s 0xff", ipCmd, resolveProtocolNumber()));

            switch (this.protocol) {
                case Constants.UDP_PROTOCOL, Constants.TCP_PROTOCOL -> {
                    if (isSrcPortSet()) {
                        cmd.append(String.format(" match %s sport %s 0xffff", ipCmd, this.srcPort));
                    }
                    if (isDstPortSet()) {
                        cmd.append(String.format(" match %s dport %s 0xffff", ipCmd, this.dstPort));
                    }
                }
                case Constants.ICMP_PROTOCOL -> {
                    if (isIcmpTypeSet()) {
                        cmd.append(String.format(" match %s icmp_type %s 0xff", ipCmd, this.icmpType));
                    }
                    if (isIcmpCodeSet()) {
                        cmd.append(String.format(" match %s icmp_code %s 0xff", ipCmd, this.icmpCode));
                    }
                }
            }
        }
    }

    private String resolveIpVersion() {
        if (!isIpVersionSet()) {
            throw new IllegalStateException("IP version must be set.");
        }
        return switch (this.ipVersion) {
            case Constants.IPV4 ->
                "ip";
            case Constants.IPV6 ->
                "ip6";
            default ->
                throw new IllegalArgumentException("Unknown ipVersion: " + ipVersion);
        };
    }

    private String resolveProtocolNumber() {
        if (!isProtocolSet()) {
            throw new IllegalStateException("Protocol must be set.");
        }
        return switch (this.protocol) {
            case Constants.UDP_PROTOCOL ->
                "17";
            case Constants.ICMP_PROTOCOL ->
                "1";
            case Constants.TCP_PROTOCOL ->
                "6";
            default ->
                throw new IllegalArgumentException("Unknown protocol: " + protocol);
        };
    }

    public void appendToTable(DefaultTableModel model) {
        if (isProtocolSet()) {
            model.addRow(new Object[]{
                Constants.PROTOCOL,
                this.protocol
            });

            switch (this.protocol) {
                case Constants.TCP_PROTOCOL, Constants.UDP_PROTOCOL -> {
                    if (isSrcPortSet()) {
                        model.addRow(new Object[]{
                            Constants.SRC_PORT,
                            this.srcPort
                        });
                    }
                    if (isDstPortSet()) {
                        model.addRow(new Object[]{
                            Constants.DST_PORT,
                            this.dstPort
                        });
                    }
                }
                case Constants.ICMP_PROTOCOL -> {
                    if (isIcmpTypeSet()) {
                        model.addRow(new Object[]{
                            Constants.ICMP_TYPE,
                            this.icmpType
                        });
                    }
                    if (isIcmpCodeSet()) {
                        model.addRow(new Object[]{
                            Constants.ICMP_CODE,
                            this.icmpCode
                        });
                    }
                }
            }
        }

        if (isSrcAddressSet()) {
            model.addRow(new Object[]{
                Constants.SRC_ADDRESS,
                this.srcAddress + this.srcSubnetMask
            });
        }
        if (isDstAddressSet()) {
            model.addRow(new Object[]{
                Constants.DST_ADDRESS,
                this.dstAddress + this.dstSubnetMask
            });
        }
    }

}
