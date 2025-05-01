package cz.vutbr.networkemulator.model.filter;

import javax.swing.table.DefaultTableModel;

import cz.vutbr.networkemulator.utils.NetworkEmulatorUtils;
import cz.vutbr.networkemulator.utils.enums.IpVersion;
import cz.vutbr.networkemulator.utils.enums.Protocol;

public class Filter {

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

    public String getIpVersion() {
        return ipVersion.getName();
    }

    public void setIpVersion(IpVersion ipVersion) {
        this.ipVersion = ipVersion;
    }

    public boolean isIpVersionSet() {
        return ipVersion != null;
    }

    public String getProtocol() {
        return protocol.getName();
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public boolean isProtocolSet() {
        return protocol != null;
    }

    public String getIpv4SrcAddress() {
        return ipv4SrcAddress;
    }

    public void setIpv4SrcAddress(String address) {
        this.ipv4SrcAddress = address;
    }

    public boolean isIpv4SrcAddressSet() {
        return ipv4SrcAddress != null && !ipv4SrcAddress.isEmpty();
    }

    public String getIpv4SrcSubnetPrefix() {
        return ipv4SrcSubnetPrefix;
    }

    public void setIpv4SrcSubnetPrefix(String subnetPrefix) {
        this.ipv4SrcSubnetPrefix = subnetPrefix;
    }

    public boolean isIpv4SrcsubnetPrefixSet() {
        return ipv4SrcSubnetPrefix != null && !ipv4SrcSubnetPrefix.isEmpty();
    }

    public String getIpv4DstAddress() {
        return ipv4DstAddress;
    }

    public void setIpv4DstAddress(String address) {
        this.ipv4DstAddress = address;
    }

    public boolean isIpv4DstAddressSet() {
        return ipv4DstAddress != null && !ipv4DstAddress.isEmpty();
    }

    public String getIpv4DstSubnetPrefix() {
        return ipv4DstSubnetPrefix;
    }

    public void setIpv4DstSubnetPrefix(String subnetPrefix) {
        this.ipv4DstSubnetPrefix = subnetPrefix;
    }

    public boolean isIpv4DstsubnetPrefixSet() {
        return ipv4DstSubnetPrefix != null && !ipv4DstSubnetPrefix.isEmpty();
    }

    public String getIpv6SrcAddress() {
        return ipv6SrcAddress;
    }

    public void setIpv6SrcAddress(String address) {
        this.ipv6SrcAddress = address;
    }

    public boolean isIpv6SrcAddressSet() {
        return ipv6SrcAddress != null && !ipv6SrcAddress.isEmpty();
    }

    public String getIpv6SrcSubnetPrefix() {
        return ipv6SrcSubnetPrefix;
    }

    public void setIpv6SrcSubnetPrefix(String subnetPrefix) {
        this.ipv6SrcSubnetPrefix = subnetPrefix;
    }

    public boolean isIpv6SrcSubnetPrefixSet() {
        return ipv6SrcSubnetPrefix != null && !ipv6SrcSubnetPrefix.isEmpty();
    }

    public String getIpv6DstAddress() {
        return ipv6DstAddress;
    }

    public void setIpv6DstAddress(String address) {
        this.ipv6DstAddress = address;
    }

    public boolean isIpv6DstAddressSet() {
        return ipv6DstAddress != null && !ipv6DstAddress.isEmpty();
    }

    public String getIpv6DstSubnetPrefix() {
        return ipv6DstSubnetPrefix;
    }

    public void setIpv6DstSubnetPrefix(String subnetPrefix) {
        this.ipv6DstSubnetPrefix = subnetPrefix;
    }

    public boolean isIpv6DstSubnetPrefixSet() {
        return ipv6DstSubnetPrefix != null && !ipv6DstSubnetPrefix.isEmpty();
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
        String ipCmd = this.ipVersion.getTcCommand();
        if (ipVersion.equals(IpVersion.IPv4)) {
            if (isIpv4SrcAddressSet() && isIpv4SrcsubnetPrefixSet()) {
                cmd.append(String.format(" match %s src %s%s", ipCmd, ipv4SrcAddress, ipv4SrcSubnetPrefix));
            }
    
            if (isIpv4DstAddressSet() && isIpv4DstsubnetPrefixSet()) {
                cmd.append(String.format(" match %s dst %s%s", ipCmd, ipv4DstAddress, ipv4DstSubnetPrefix));
            }
        } else if (ipVersion.equals(IpVersion.IPv6)) {
            if (isIpv6SrcAddressSet() && isIpv6SrcSubnetPrefixSet()) {
                cmd.append(String.format(" match %s src %s%s", ipCmd, ipv6SrcAddress, ipv6SrcSubnetPrefix));
            }
    
            if (isIpv6DstAddressSet() && isIpv6DstSubnetPrefixSet()) {
                cmd.append(String.format(" match %s dst %s%s", ipCmd, ipv6DstAddress, ipv6DstSubnetPrefix));
            }
        }
        

        if (isProtocolSet()) {
            cmd.append(String.format(" match %s protocol %d 0xff", ipCmd, protocol.getNumber()));

            switch (protocol) {
                case Protocol.UDP, Protocol.TCP -> {
                    if (isSrcPortSet()) {
                        cmd.append(String.format(" match %s sport %s 0xffff", ipCmd, srcPort));
                    }
                    if (isDstPortSet()) {
                        cmd.append(String.format(" match %s dport %s 0xffff", ipCmd, dstPort));
                    }
                }
                case Protocol.ICMP -> {
                    if (isIcmpTypeSet()) {
                        cmd.append(String.format(" match %s icmp_type %s 0xff", ipCmd, icmpType));
                    }
                    if (isIcmpCodeSet()) {
                        cmd.append(String.format(" match %s icmp_code %s 0xff", ipCmd, icmpCode));
                    }
                }
            }
        }
    }

    public void appendToTable(DefaultTableModel model) {
        if (isProtocolSet()) {
            model.addRow(new Object[]{
                NetworkEmulatorUtils.getString("table_protocol"),
                this.protocol
            });

            switch (this.protocol) {
                case Protocol.TCP, Protocol.UDP -> {
                    if (isSrcPortSet()) {
                        model.addRow(new Object[]{
                            NetworkEmulatorUtils.getString("table_src_port"),
                            this.srcPort
                        });
                    }
                    if (isDstPortSet()) {
                        model.addRow(new Object[]{
                            NetworkEmulatorUtils.getString("table_dst_port"),
                            this.dstPort
                        });
                    }
                }
                case Protocol.ICMP -> {
                    if (isIcmpTypeSet()) {
                        model.addRow(new Object[]{
                            NetworkEmulatorUtils.getString("table_icmp_type"),
                            this.icmpType
                        });
                    }
                    if (isIcmpCodeSet()) {
                        model.addRow(new Object[]{
                            NetworkEmulatorUtils.getString("table_icmp_code"),
                            this.icmpCode
                        });
                    }
                }
            }
        }

        if (ipVersion.equals(IpVersion.IPv4)) {
            if (isIpv4SrcAddressSet()) {
                model.addRow(new Object[]{
                    NetworkEmulatorUtils.getString("table_src_address"),
                    ipv4SrcAddress + ipv4SrcSubnetPrefix
                });
            }
            if (isIpv4DstAddressSet()) {
                model.addRow(new Object[]{
                    NetworkEmulatorUtils.getString("table_dst_address"),
                    ipv4DstAddress + ipv4DstSubnetPrefix
                });
            }
        } else if (ipVersion.equals(IpVersion.IPv6)) {
            if (isIpv6SrcAddressSet()) {
                model.addRow(new Object[]{
                    NetworkEmulatorUtils.getString("table_src_address"),
                    ipv6SrcAddress + ipv6SrcSubnetPrefix
                });
            }
            if (isIpv6DstAddressSet()) {
                model.addRow(new Object[]{
                    NetworkEmulatorUtils.getString("table_dst_address"),
                    ipv6DstAddress + ipv6DstSubnetPrefix
                });
            }
        }

        
    }

}
