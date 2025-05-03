package cz.vutbr.networkemulator.model.filter;

import javax.swing.table.DefaultTableModel;

import cz.vutbr.networkemulator.utils.EmulatorUtils;
import cz.vutbr.networkemulator.utils.enums.IpVersion;
import cz.vutbr.networkemulator.utils.enums.Protocol;
import cz.vutbr.networkemulator.verification.IpAddressVerifier;
import cz.vutbr.networkemulator.verification.RangeVerifier;

public class Filter {

    public static final int MIN_PORT = 0;
    public static final int MAX_PORT = 65535;
    public static final int MIN_ICMP_TYPE = 0;
    public static final int MAX_ICMP_TYPE = 255;
    public static final int MIN_ICMP_CODE = 0;
    public static final int MAX_ICMP_CODE = 255;
    public static final int MIN_DSCP = 0;
    public static final int MAX_DSCP = 63;
    public static final int MIN_ECN = 0;
    public static final int MAX_ECN = 3;
    public static final int MIN_FLOW_LABEL = 0;
    public static final int MAX_FLOW_LABEL = 1048575;

    public static final boolean IS_PORT_DOUBLE = false;
    public static final boolean IS_ICMP_TYPE_DOUBLE = false;
    public static final boolean IS_ICMP_CODE_DOUBLE = false;
    public static final boolean IS_DSCP_DOUBLE = false;
    public static final boolean IS_ECN_DOUBLE = false;
    public static final boolean IS_FLOW_LABEL_DOUBLE = false;

    private IpVersion ipVersion;
    private Protocol protocol;
    private String ipv4SrcAddress;
    private String ipv4SrcSubnetPrefix;
    private String ipv4DstAddress;
    private String ipv4DstSubnetPrefix;
    private String ipv6SrcAddress;
    private String ipv6SrcSubnetPrefix;
    private String ipv6DstAddress;
    private String ipv6DstSubnetPrefix;
    private String srcPort;
    private String dstPort;
    private String icmpType;
    private String icmpCode;
    private String dscp;
    private String ecn;
    private String flowLabel;

    public void appendToCommand(StringBuilder cmd) {
        String ipCmd = this.ipVersion.getTcCommand();
        if (ipVersion.equals(IpVersion.IPv4)) {
            if (isIpv4SrcAddressValid() && isIpv4SrcsubnetPrefixValid()) {
                cmd.append(String.format(" match %s src %s%s", ipCmd, ipv4SrcAddress, ipv4SrcSubnetPrefix));
            }

            if (isIpv4DstAddressValid() && isIpv4DstsubnetPrefixValid()) {
                cmd.append(String.format(" match %s dst %s%s", ipCmd, ipv4DstAddress, ipv4DstSubnetPrefix));
            }
        } else if (ipVersion.equals(IpVersion.IPv6)) {
            if (isIpv6SrcAddressValid() && isIpv6SrcSubnetPrefixValid()) {
                cmd.append(String.format(" match %s src %s%s", ipCmd, ipv6SrcAddress, ipv6SrcSubnetPrefix));
            }

            if (isIpv6DstAddressValid() && isIpv6DstSubnetPrefixValid()) {
                cmd.append(String.format(" match %s dst %s%s", ipCmd, ipv6DstAddress, ipv6DstSubnetPrefix));
            }
        }

        if (isProtocolValid()) {
            cmd.append(String.format(" match %s protocol %d 0xff", ipCmd, protocol.getNumber()));

            switch (protocol) {
                case Protocol.UDP, Protocol.TCP -> {
                    if (isSrcPortValid()) {
                        cmd.append(String.format(" match %s sport %s 0xffff", ipCmd, srcPort));
                    }
                    if (isDstPortValid()) {
                        cmd.append(String.format(" match %s dport %s 0xffff", ipCmd, dstPort));
                    }
                }
                case Protocol.ICMP -> {
                    if (isIcmpTypeValid()) {
                        cmd.append(String.format(" match %s icmp_type %s 0xff", ipCmd, icmpType));
                    }
                    if (isIcmpCodeValid()) {
                        cmd.append(String.format(" match %s icmp_code %s 0xff", ipCmd, icmpCode));
                    }
                }
            }
        }

        if (isDscpValid() || isEcnValid()) {
            if (ipVersion.equals(IpVersion.IPv4)) {
                cmd.append(String.format(" match %s dsfield %s 0xff", ipCmd, calculateDiffServValue()));
            } else if (ipVersion.equals(IpVersion.IPv6)) {
                cmd.append(String.format(" match %s priority %s 0xff", ipCmd, calculateDiffServValue()));
            }
        }

        if (isFlowLabelValid()) {
            if (ipVersion.equals(IpVersion.IPv6)) {
                cmd.append(String.format(" match %s flowlabel %s 0x000fffff", ipCmd, flowLabel));
            }
        }
    }

    public void appendToTable(DefaultTableModel model) {
        if (ipVersion.equals(IpVersion.IPv4)) {
            if (isIpv4SrcAddressValid()) {
                model.addRow(new Object[] {
                        EmulatorUtils.getString("table_src_address"),
                        ipv4SrcAddress + ipv4SrcSubnetPrefix
                });
            }
            if (isIpv4DstAddressValid()) {
                model.addRow(new Object[] {
                        EmulatorUtils.getString("table_dst_address"),
                        ipv4DstAddress + ipv4DstSubnetPrefix
                });
            }
        } else if (ipVersion.equals(IpVersion.IPv6)) {
            if (isIpv6SrcAddressValid()) {
                model.addRow(new Object[] {
                        EmulatorUtils.getString("table_src_address"),
                        ipv6SrcAddress + ipv6SrcSubnetPrefix
                });
            }
            if (isIpv6DstAddressValid()) {
                model.addRow(new Object[] {
                        EmulatorUtils.getString("table_dst_address"),
                        ipv6DstAddress + ipv6DstSubnetPrefix
                });
            }
        }

        if (isProtocolValid()) {
            model.addRow(new Object[] {
                    EmulatorUtils.getString("table_protocol"),
                    protocol
            });

            switch (protocol) {
                case Protocol.TCP, Protocol.UDP -> {
                    if (isSrcPortValid()) {
                        model.addRow(new Object[] {
                                EmulatorUtils.getString("table_src_port"),
                                srcPort
                        });
                    }
                    if (isDstPortValid()) {
                        model.addRow(new Object[] {
                                EmulatorUtils.getString("table_dst_port"),
                                dstPort
                        });
                    }
                }
                case Protocol.ICMP -> {
                    if (isIcmpTypeValid()) {
                        model.addRow(new Object[] {
                                EmulatorUtils.getString("table_icmp_type"),
                                icmpType
                        });
                    }
                    if (isIcmpCodeValid()) {
                        model.addRow(new Object[] {
                                EmulatorUtils.getString("table_icmp_code"),
                                icmpCode
                        });
                    }
                }
            }
        }

        if (isDscpValid()) {
            model.addRow(new Object[] {
                    EmulatorUtils.getString("table_dscp"), dscp
            });
        }

        if (isEcnValid()) {
            model.addRow(new Object[] {
                    EmulatorUtils.getString("table_ecn"), ecn
            });
        }

        if (isFlowLabelValid()) {
            if (ipVersion.equals(IpVersion.IPv6)) {
                model.addRow(new Object[] {
                        EmulatorUtils.getString("table_flow_label"), flowLabel
                });
            }
        }
    }

    private String calculateDiffServValue() {
        int dscpInt;
        int ecnInt;

        try {
            dscpInt = Integer.parseInt(dscp);
        } catch (NumberFormatException e) {
            dscpInt = 0;
        }

        try {
            ecnInt = Integer.parseInt(ecn);
        } catch (NumberFormatException e) {
            ecnInt = 0;
        }

        return Integer.toString(dscpInt * 4 + ecnInt);
    }

    public String getIpVersion() {
        return ipVersion.getName();
    }

    public void setIpVersion(IpVersion ipVersion) {
        this.ipVersion = ipVersion;
    }

    public String getProtocol() {
        return protocol.getName();
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public String getIpv4SrcAddress() {
        return ipv4SrcAddress;
    }

    public void setIpv4SrcAddress(String address) {
        this.ipv4SrcAddress = address;
    }

    public String getIpv4SrcSubnetPrefix() {
        return ipv4SrcSubnetPrefix;
    }

    public void setIpv4SrcSubnetPrefix(String subnetPrefix) {
        this.ipv4SrcSubnetPrefix = subnetPrefix;
    }

    public String getIpv4DstAddress() {
        return ipv4DstAddress;
    }

    public void setIpv4DstAddress(String address) {
        this.ipv4DstAddress = address;
    }

    public String getIpv4DstSubnetPrefix() {
        return ipv4DstSubnetPrefix;
    }

    public void setIpv4DstSubnetPrefix(String subnetPrefix) {
        this.ipv4DstSubnetPrefix = subnetPrefix;
    }

    public String getIpv6SrcAddress() {
        return ipv6SrcAddress;
    }

    public void setIpv6SrcAddress(String address) {
        this.ipv6SrcAddress = address;
    }

    public String getIpv6SrcSubnetPrefix() {
        return ipv6SrcSubnetPrefix;
    }

    public void setIpv6SrcSubnetPrefix(String subnetPrefix) {
        this.ipv6SrcSubnetPrefix = subnetPrefix;
    }

    public String getIpv6DstAddress() {
        return ipv6DstAddress;
    }

    public void setIpv6DstAddress(String address) {
        this.ipv6DstAddress = address;
    }

    public String getIpv6DstSubnetPrefix() {
        return ipv6DstSubnetPrefix;
    }

    public void setIpv6DstSubnetPrefix(String subnetPrefix) {
        this.ipv6DstSubnetPrefix = subnetPrefix;
    }

    public String getSrcPort() {
        return srcPort;
    }

    public void setSrcPort(String srcPort) {
        this.srcPort = srcPort;
    }

    public String getDstPort() {
        return dstPort;
    }

    public void setDstPort(String dstPort) {
        this.dstPort = dstPort;
    }

    public String getIcmpType() {
        return icmpType;
    }

    public void setIcmpType(String icmpType) {
        this.icmpType = icmpType;
    }

    public String getIcmpCode() {
        return icmpCode;
    }

    public void setIcmpCode(String icmpCode) {
        this.icmpCode = icmpCode;
    }

    public String getDscp() {
        return dscp;
    }

    public void setDscp(String dscp) {
        this.dscp = dscp;
    }

    public String getEcn() {
        return ecn;
    }

    public void setEcn(String ecn) {
        this.ecn = ecn;
    }

    public String getFlowLabel() {
        return flowLabel;
    }

    public void setFlowLabel(String flowLabel) {
        this.flowLabel = flowLabel;
    }

    public boolean isIpVersionValid() {
        return ipVersion != null;
    }

    public boolean isProtocolValid() {
        return protocol != null;
    }

    public boolean isIpv4SrcAddressValid() {
        return IpAddressVerifier.isValid(ipv4SrcAddress, IpVersion.IPv4);
    }

    public boolean isIpv4SrcsubnetPrefixValid() {
        return ipv4SrcSubnetPrefix != null && !ipv4SrcSubnetPrefix.isEmpty();
    }

    public boolean isIpv4DstAddressValid() {
        return IpAddressVerifier.isValid(ipv4DstAddress, IpVersion.IPv4);
    }

    public boolean isIpv4DstsubnetPrefixValid() {
        return ipv4DstSubnetPrefix != null && !ipv4DstSubnetPrefix.isEmpty();
    }

    public boolean isIpv6SrcAddressValid() {
        return IpAddressVerifier.isValid(ipv6SrcAddress, IpVersion.IPv6);
    }

    public boolean isIpv6SrcSubnetPrefixValid() {
        return ipv6SrcSubnetPrefix != null && !ipv6SrcSubnetPrefix.isEmpty();
    }

    public boolean isIpv6DstAddressValid() {
        return IpAddressVerifier.isValid(ipv6DstAddress, IpVersion.IPv6);
    }

    public boolean isIpv6DstSubnetPrefixValid() {
        return ipv6DstSubnetPrefix != null && !ipv6DstSubnetPrefix.isEmpty();
    }

    public boolean isSrcPortValid() {
        return RangeVerifier.isValid(srcPort, MIN_PORT, MAX_PORT, IS_PORT_DOUBLE);
    }

    public boolean isDstPortValid() {
        return RangeVerifier.isValid(dstPort, MIN_PORT, MAX_PORT, IS_PORT_DOUBLE);
    }

    public boolean isIcmpTypeValid() {
        return RangeVerifier.isValid(icmpType, MIN_ICMP_TYPE, MAX_ICMP_TYPE, IS_ICMP_TYPE_DOUBLE);
    }

    public boolean isIcmpCodeValid() {
        return RangeVerifier.isValid(icmpCode, MIN_ICMP_CODE, MAX_ICMP_CODE, IS_ICMP_CODE_DOUBLE);
    }

    public boolean isDscpValid() {
        return RangeVerifier.isValid(dscp, MIN_DSCP, MAX_DSCP, IS_DSCP_DOUBLE);
    }

    public boolean isEcnValid() {
        return RangeVerifier.isValid(ecn, MIN_ECN, MAX_ECN, IS_ECN_DOUBLE);
    }

    public boolean isFlowLabelValid() {
        return RangeVerifier.isValid(flowLabel, MIN_FLOW_LABEL, MAX_FLOW_LABEL, IS_FLOW_LABEL_DOUBLE);
    }
}
