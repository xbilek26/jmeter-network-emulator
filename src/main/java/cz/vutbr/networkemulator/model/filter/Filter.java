package cz.vutbr.networkemulator.model.filter;

import javax.swing.table.DefaultTableModel;

import cz.vutbr.networkemulator.utils.Constants;
import cz.vutbr.networkemulator.utils.IpVersion;
import cz.vutbr.networkemulator.utils.Protocol;

public class Filter {

    private IpVersion ipVersion;
    private Protocol protocol;
    private String srcAddress;
    private String srcSubnetPrefix;
    private String dstAddress;
    private String dstSubnetPrefix;
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

    public String getSrcAddress() {
        return srcAddress;
    }

    public void setSrcAddress(String srcAddress) {
        this.srcAddress = srcAddress;
    }

    public boolean isSrcAddressSet() {
        return srcAddress != null && !srcAddress.isEmpty();
    }

    public String getSrcSubnetPrefix() {
        return srcSubnetPrefix;
    }

    public void setSrcSubnetPrefix(String srcsubnetPrefix) {
        this.srcSubnetPrefix = srcsubnetPrefix;
    }

    public boolean isSrcsubnetPrefixSet() {
        return srcSubnetPrefix != null && !srcSubnetPrefix.isEmpty();
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

    public String getDstSubnetPrefix() {
        return dstSubnetPrefix;
    }

    public void setDstSubnetPrefix(String dstSubnetPrefix) {
        this.dstSubnetPrefix = dstSubnetPrefix;
    }

    public boolean isDstsubnetPrefixSet() {
        return dstSubnetPrefix != null && !dstSubnetPrefix.isEmpty();
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
        if (isSrcAddressSet() && isSrcsubnetPrefixSet()) {
            cmd.append(String.format(" match %s src %s%s", ipCmd, this.srcAddress, this.srcSubnetPrefix));
        }

        if (isDstAddressSet() && isDstsubnetPrefixSet()) {
            cmd.append(String.format(" match %s dst %s%s", ipCmd, this.dstAddress, this.dstSubnetPrefix));
        }

        if (isProtocolSet()) {
            cmd.append(String.format(" match %s protocol %d 0xff", ipCmd, this.protocol.getNumber()));

            switch (this.protocol) {
                case Protocol.UDP, Protocol.TCP -> {
                    if (isSrcPortSet()) {
                        cmd.append(String.format(" match %s sport %s 0xffff", ipCmd, this.srcPort));
                    }
                    if (isDstPortSet()) {
                        cmd.append(String.format(" match %s dport %s 0xffff", ipCmd, this.dstPort));
                    }
                }
                case Protocol.ICMP -> {
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

    public void appendToTable(DefaultTableModel model) {
        if (isProtocolSet()) {
            model.addRow(new Object[]{
                Constants.PROTOCOL,
                this.protocol
            });

            switch (this.protocol) {
                case Protocol.TCP, Protocol.UDP -> {
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
                case Protocol.ICMP -> {
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
                this.srcAddress + this.srcSubnetPrefix
            });
        }
        if (isDstAddressSet()) {
            model.addRow(new Object[]{
                Constants.DST_ADDRESS,
                this.dstAddress + this.dstSubnetPrefix
            });
        }
    }

}
