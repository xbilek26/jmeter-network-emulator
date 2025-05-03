package cz.vutbr.networkemulator.gui.filter;

import java.awt.CardLayout;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.text.PlainDocument;

import cz.vutbr.networkemulator.model.filter.Filter;
import cz.vutbr.networkemulator.utils.EmulatorUtils;
import cz.vutbr.networkemulator.utils.enums.IpVersion;
import cz.vutbr.networkemulator.utils.enums.Protocol;
import cz.vutbr.networkemulator.verification.IpAddressVerifier;
import cz.vutbr.networkemulator.verification.RangeVerifier;
import net.miginfocom.swing.MigLayout;

public class FilterPanel extends JPanel {

    private static final String[] PREFIX_LENGTHS_IPV4 = {
            "/32", "/31", "/30", "/29", "/28", "/27", "/26", "/25",
            "/24", "/23", "/22", "/21", "/20", "/19", "/18", "/17", "/16"
    };

    private static final String[] PREFIX_LENGTHS_IPV6 = {
            "/128", "/124", "/120", "/116", "/112", "/108", "/104", "/100",
            "/96", "/92", "/88", "/84", "/80", "/76", "/72", "/68", "/64"
    };

    private static final String[] PROTOCOLS = {
            "",
            "HTTP",
            "HTTPS",
            "FTP",
            "SSH",
            "DNS",
            "SMTP",
            "POP3",
            "IMAP"
    };

    private static final Map<String, Integer> PROTOCOL_PORTS = Map.of(
            "HTTP", 80,
            "HTTPS", 443,
            "FTP", 21,
            "SSH", 22,
            "DNS", 53,
            "SMTP", 25,
            "POP3", 110,
            "IMAP", 143);

    private final JRadioButton ipv4Button;
    private final JRadioButton ipv6Button;
    private final JRadioButton tcpButton;
    private final JRadioButton udpButton;
    private final JRadioButton icmpButton;
    private final JTextField ipv4SrcAddressField;
    private final JComboBox<String> ipv4SrcSubnetPrefixBox;
    private final JTextField ipv4DstAddressField;
    private final JComboBox<String> ipv4DstSubnetPrefixBox;
    private final JTextField ipv6SrcAddressField;
    private final JComboBox<String> ipv6SrcSubnetPrefixBox;
    private final JTextField ipv6DstAddressField;
    private final JComboBox<String> ipv6DstSubnetPrefixBox;
    private final JPanel ipAddressCards;
    private final JTextField srcPortField;
    private final JComboBox<String> srcL4ProtocolBox;
    private final JTextField dstPortField;
    private final JComboBox<String> dstL4ProtocolBox;
    private final JTextField icmpTypeField;
    private final JTextField icmpCodeField;
    private final JPanel protocolCards;
    private final JTextField ipv4DscpField;
    private final JTextField ipv6DscpField;
    private final PlainDocument dscpShared;
    private final JTextField ipv4EcnField;
    private final JTextField ipv6EcnField;
    private final PlainDocument ecnShared;
    private final JTextField flowLabelField;
    private final JPanel qosCards;

    public FilterPanel() {
        // layout and border
        setLayout(new MigLayout("insets 5", "grow", "grow"));
        setBorder(BorderFactory.createTitledBorder(EmulatorUtils.getString("title_filter")));

        // initialisations
        ipv4Button = new JRadioButton(EmulatorUtils.getString("label_ip_version_ipv4"));
        ipv6Button = new JRadioButton(EmulatorUtils.getString("label_ip_version_ipv6"));
        ButtonGroup ipVersionGroup = new ButtonGroup();
        tcpButton = new JRadioButton(EmulatorUtils.getString("label_protocol_tcp"));
        udpButton = new JRadioButton(EmulatorUtils.getString("label_protocol_udp"));
        icmpButton = new JRadioButton(EmulatorUtils.getString("label_protocol_icmp"));
        ButtonGroup protocolGroup = new ButtonGroup();
        ipv4SrcAddressField = new JTextField(10);
        ipv4SrcSubnetPrefixBox = new JComboBox<>();
        ipv4DstAddressField = new JTextField(10);
        ipv4DstSubnetPrefixBox = new JComboBox<>();
        ipv6SrcAddressField = new JTextField(10);
        ipv6SrcSubnetPrefixBox = new JComboBox<>();
        ipv6DstAddressField = new JTextField(10);
        ipv6DstSubnetPrefixBox = new JComboBox<>();
        srcPortField = new JTextField(10);
        srcL4ProtocolBox = new JComboBox<>(PROTOCOLS);
        dstPortField = new JTextField(10);
        dstL4ProtocolBox = new JComboBox<>(PROTOCOLS);
        icmpTypeField = new JTextField(10);
        icmpCodeField = new JTextField(10);
        ipv4DscpField = new JTextField(10);
        ipv6DscpField = new JTextField(10);
        ipv4EcnField = new JTextField(10);
        ipv6EcnField = new JTextField(10);
        flowLabelField = new JTextField(10);

        dscpShared = new PlainDocument();
        ipv4DscpField.setDocument(dscpShared);
        ipv6DscpField.setDocument(dscpShared);
        ecnShared = new PlainDocument();
        ipv4EcnField.setDocument(ecnShared);
        ipv6EcnField.setDocument(ecnShared);

        // labels
        JLabel ipv4SrcAddressLabel = new JLabel(EmulatorUtils.getString("label_src_address"));
        JLabel ipv4DstAddressLabel = new JLabel(EmulatorUtils.getString("label_dst_address"));
        JLabel ipv6SrcAddressLabel = new JLabel(EmulatorUtils.getString("label_src_address"));
        JLabel ipv6DstAddressLabel = new JLabel(EmulatorUtils.getString("label_dst_address"));
        JLabel srcPortLabel = new JLabel(EmulatorUtils.getString("label_src_port"));
        JLabel dstPortLabel = new JLabel(EmulatorUtils.getString("label_dst_port"));
        JLabel icmpTypeLabel = new JLabel(EmulatorUtils.getString("label_icmp_type"));
        JLabel icmpCodeLabel = new JLabel(EmulatorUtils.getString("label_icmp_code"));
        JLabel ipv4DscpLabel = new JLabel(EmulatorUtils.getString("label_dscp"));
        JLabel ipv6DscpLabel = new JLabel(EmulatorUtils.getString("label_dscp"));
        JLabel ipv4EcnLabel = new JLabel(EmulatorUtils.getString("label_ecn"));
        JLabel ipv6EcnLabel = new JLabel(EmulatorUtils.getString("label_ecn"));
        JLabel flowLabelLabel = new JLabel(EmulatorUtils.getString("label_flow_label"));
        ipv4SrcAddressLabel.setLabelFor(ipv4SrcAddressField);
        ipv4DstAddressLabel.setLabelFor(ipv4DstAddressField);
        ipv6SrcAddressLabel.setLabelFor(ipv6SrcAddressField);
        ipv6DstAddressLabel.setLabelFor(ipv6DstAddressField);
        srcPortLabel.setLabelFor(srcPortField);
        dstPortLabel.setLabelFor(dstPortField);
        icmpTypeLabel.setLabelFor(icmpTypeField);
        icmpCodeLabel.setLabelFor(icmpCodeField);
        ipv4DscpLabel.setLabelFor(ipv4DscpField);
        ipv6DscpLabel.setLabelFor(ipv6DscpField);
        ipv4EcnLabel.setLabelFor(ipv4EcnField);
        ipv6EcnLabel.setLabelFor(ipv6EcnField);
        flowLabelLabel.setLabelFor(flowLabelField);

        ipv4Button.setSelected(true);
        tcpButton.setSelected(true);
        ipv4SrcSubnetPrefixBox.setModel(new DefaultComboBoxModel<>(PREFIX_LENGTHS_IPV4));
        ipv4DstSubnetPrefixBox.setModel(new DefaultComboBoxModel<>(PREFIX_LENGTHS_IPV4));
        ipv6SrcSubnetPrefixBox.setModel(new DefaultComboBoxModel<>(PREFIX_LENGTHS_IPV6));
        ipv6DstSubnetPrefixBox.setModel(new DefaultComboBoxModel<>(PREFIX_LENGTHS_IPV6));

        ipVersionGroup.add(ipv4Button);
        ipVersionGroup.add(ipv6Button);
        protocolGroup.add(tcpButton);
        protocolGroup.add(udpButton);
        protocolGroup.add(icmpButton);

        ipv4Button.addItemListener(_ -> updateIpVersion());
        ipv6Button.addItemListener(_ -> updateIpVersion());
        tcpButton.addItemListener(_ -> updateProtocol());
        udpButton.addItemListener(_ -> updateProtocol());
        icmpButton.addItemListener(_ -> updateProtocol());

        // listeners
        srcL4ProtocolBox.addActionListener(_ -> {
            String selected = (String) srcL4ProtocolBox.getSelectedItem();
            Integer port = PROTOCOL_PORTS.get(selected);
            if (!selected.isEmpty()) {
                srcPortField.setText(port.toString());
            } else {
                srcPortField.setText("");
            }
        });

        dstL4ProtocolBox.addActionListener(_ -> {
            String selected = (String) dstL4ProtocolBox.getSelectedItem();
            Integer port = PROTOCOL_PORTS.get(selected);
            if (!selected.isEmpty()) {
                dstPortField.setText(port.toString());
            } else {
                dstPortField.setText("");
            }
        });

        // verifiers
        ipv4SrcAddressField.setInputVerifier(new IpAddressVerifier(IpVersion.IPv4));
        ipv4DstAddressField.setInputVerifier(new IpAddressVerifier(IpVersion.IPv4));
        ipv6SrcAddressField.setInputVerifier(new IpAddressVerifier(IpVersion.IPv6));
        ipv6DstAddressField.setInputVerifier(new IpAddressVerifier(IpVersion.IPv6));
        srcPortField.setInputVerifier(new RangeVerifier(Filter.MIN_PORT, Filter.MAX_PORT, Filter.IS_PORT_DOUBLE));
        dstPortField.setInputVerifier(new RangeVerifier(Filter.MIN_PORT, Filter.MAX_PORT, Filter.IS_PORT_DOUBLE));
        icmpTypeField.setInputVerifier(new RangeVerifier(Filter.MIN_ICMP_TYPE, Filter.MAX_ICMP_TYPE, Filter.IS_ICMP_TYPE_DOUBLE));
        icmpCodeField.setInputVerifier(new RangeVerifier(Filter.MIN_ICMP_CODE, Filter.MAX_ICMP_CODE, Filter.IS_ICMP_CODE_DOUBLE));
        ipv4DscpField.setInputVerifier(new RangeVerifier(Filter.MIN_DSCP, Filter.MAX_DSCP, Filter.IS_DSCP_DOUBLE));
        ipv6DscpField.setInputVerifier(new RangeVerifier(Filter.MIN_DSCP, Filter.MAX_DSCP, Filter.IS_DSCP_DOUBLE));
        ipv4EcnField.setInputVerifier(new RangeVerifier(Filter.MIN_ECN, Filter.MAX_ECN, Filter.IS_ECN_DOUBLE));
        ipv6EcnField.setInputVerifier(new RangeVerifier(Filter.MIN_ECN, Filter.MAX_ECN, Filter.IS_ECN_DOUBLE));
        flowLabelField.setInputVerifier(new RangeVerifier(Filter.MIN_FLOW_LABEL, Filter.MAX_FLOW_LABEL, Filter.IS_FLOW_LABEL_DOUBLE));

        // panels
        JPanel ipVersionPanel = new JPanel(new MigLayout("flowx, alignx center, aligny center"));
        ipVersionPanel.setBorder(BorderFactory.createTitledBorder(EmulatorUtils.getString("title_ip_version")));
        ipVersionPanel.add(ipv4Button);
        ipVersionPanel.add(ipv6Button);

        JPanel protocolPanel = new JPanel(new MigLayout("flowx, alignx center, aligny center"));
        protocolPanel.setBorder(BorderFactory.createTitledBorder(EmulatorUtils.getString("title_protocol")));
        protocolPanel.add(tcpButton);
        protocolPanel.add(udpButton);
        protocolPanel.add(icmpButton);

        JPanel ipv4AddressPanel = new JPanel(new MigLayout("", "[][grow][][][grow][]", "grow"));
        ipv4AddressPanel.setBorder(BorderFactory.createTitledBorder(EmulatorUtils.getString("title_ipv4_address")));
        ipv4AddressPanel.add(ipv4SrcAddressLabel);
        ipv4AddressPanel.add(ipv4SrcAddressField, "growx, growy");
        ipv4AddressPanel.add(ipv4SrcSubnetPrefixBox, "growx, growy, gapright 5");
        ipv4AddressPanel.add(ipv4DstAddressLabel);
        ipv4AddressPanel.add(ipv4DstAddressField, "growx, growy");
        ipv4AddressPanel.add(ipv4DstSubnetPrefixBox, "growx, growy");

        JPanel ipv6AddressPanel = new JPanel(new MigLayout("", "[][grow][][][grow][]", "grow"));
        ipv6AddressPanel.setBorder(BorderFactory.createTitledBorder(EmulatorUtils.getString("title_ipv6_address")));
        ipv6AddressPanel.add(ipv6SrcAddressLabel);
        ipv6AddressPanel.add(ipv6SrcAddressField, "growx, growy");
        ipv6AddressPanel.add(ipv6SrcSubnetPrefixBox, "growx, growy, gapright 5");
        ipv6AddressPanel.add(ipv6DstAddressLabel);
        ipv6AddressPanel.add(ipv6DstAddressField, "growx, growy");
        ipv6AddressPanel.add(ipv6DstSubnetPrefixBox, "growx, growy");

        ipAddressCards = new JPanel(new CardLayout());
        ipAddressCards.add(ipv4AddressPanel, "ipv4");
        ipAddressCards.add(ipv6AddressPanel, "ipv6");

        JPanel portPanel = new JPanel(new MigLayout("", "[][grow][grow]", "grow"));
        portPanel.setBorder(BorderFactory.createTitledBorder(EmulatorUtils.getString("title_port")));
        portPanel.add(srcPortLabel);
        portPanel.add(srcPortField, "growx, growy");
        portPanel.add(srcL4ProtocolBox, "growx, growy, wrap");
        portPanel.add(dstPortLabel);
        portPanel.add(dstPortField, "growx, growy");
        portPanel.add(dstL4ProtocolBox, "growx, growy");

        JPanel icmpPanel = new JPanel(new MigLayout("", "[][grow]", "grow"));
        icmpPanel.setBorder(BorderFactory.createTitledBorder(EmulatorUtils.getString("title_icmp")));
        icmpPanel.add(icmpTypeLabel);
        icmpPanel.add(icmpTypeField, "growx, growy, wrap");
        icmpPanel.add(icmpCodeLabel);
        icmpPanel.add(icmpCodeField, "growx, growy");

        protocolCards = new JPanel(new CardLayout());
        protocolCards.add(portPanel, "port");
        protocolCards.add(icmpPanel, "icmp");

        JPanel tosPanel = new JPanel(new MigLayout("", "[][grow]", "grow"));
        tosPanel.setBorder(BorderFactory.createTitledBorder(EmulatorUtils.getString("title_tos")));
        tosPanel.add(ipv4DscpLabel);
        tosPanel.add(ipv4DscpField, "growx, growy, wrap");
        tosPanel.add(ipv4EcnLabel);
        tosPanel.add(ipv4EcnField, "growx, growy");

        JPanel trafficClassPanel = new JPanel(new MigLayout("", "[][grow]", "grow"));
        trafficClassPanel.setBorder(BorderFactory.createTitledBorder(EmulatorUtils.getString("title_traffic_class")));
        trafficClassPanel.add(ipv6DscpLabel);
        trafficClassPanel.add(ipv6DscpField, "growx, growy, wrap");
        trafficClassPanel.add(ipv6EcnLabel);
        trafficClassPanel.add(ipv6EcnField, "growx, growy");

        JPanel flowLabelPanel = new JPanel(new MigLayout("", "[][grow]", "grow"));
        flowLabelPanel.setBorder(BorderFactory.createTitledBorder(EmulatorUtils.getString("title_flow_label")));
        flowLabelPanel.add(flowLabelLabel);
        flowLabelPanel.add(flowLabelField, "growx, growy");

        JPanel ipv6QosWrapper = new JPanel(new MigLayout("insets 0", "grow", "grow"));
        ipv6QosWrapper.add(trafficClassPanel, "growx, growy");
        ipv6QosWrapper.add(flowLabelPanel, "growx, growy");

        qosCards = new JPanel(new CardLayout());
        qosCards.add(tosPanel, "qos_ipv4");
        qosCards.add(ipv6QosWrapper, "qos_ipv6");

        // add components
        add(ipVersionPanel, "growx, growy");
        add(protocolPanel, "growx, growy, wrap");
        add(ipAddressCards, "growx, growy, span, wrap");
        add(protocolCards, "growx, growy");
        add(qosCards, "growx, growy");
    }

    private void updateIpVersion() {
        CardLayout ipVersionLayout = (CardLayout) ipAddressCards.getLayout();
        CardLayout qosLayout = (CardLayout) qosCards.getLayout();
        if (ipv4Button.isSelected()) {
            ipVersionLayout.show(ipAddressCards, "ipv4");
            qosLayout.show(qosCards, "qos_ipv4");
        } else {
            ipVersionLayout.show(ipAddressCards, "ipv6");
            qosLayout.show(qosCards, "qos_ipv6");
        }
    }

    private void updateProtocol() {
        CardLayout cards = (CardLayout) protocolCards.getLayout();
        if (icmpButton.isSelected()) {
            cards.show(protocolCards, "icmp");
        } else {
            cards.show(protocolCards, "port");
        }
    }

    public IpVersion getIpVersion() {
        if (ipv6Button.isSelected()) {
            return IpVersion.IPv6;
        }
        return IpVersion.IPv4;
    }

    public void setIpVersion(String ipVersion) {
        if (ipVersion.equals(IpVersion.IPv6.getName())) {
            ipv6Button.setSelected(true);
        } else {
            ipv4Button.setSelected(true);
        }
    }

    public Protocol getProtocol() {
        if (udpButton.isSelected()) {
            return Protocol.UDP;
        }
        if (icmpButton.isSelected()) {
            return Protocol.ICMP;
        }

        return Protocol.TCP;
    }

    public void setProtocol(String protocol) {
        if (protocol.equals(Protocol.UDP.getName())) {
            udpButton.setSelected(true);
        } else if (protocol.equals(Protocol.ICMP.getName())) {
            icmpButton.setSelected(true);
        } else {
            tcpButton.setSelected(true);
        }
    }

    public String getIpv4SrcAddress() {
        return ipv4SrcAddressField.getText().trim();
    }

    public void setIpv4SrcAddress(String address) {
        ipv4SrcAddressField.setText(address);
    }

    public String getIpv4SrcSubnetPrefix() {
        return (String) ipv4SrcSubnetPrefixBox.getSelectedItem();
    }

    public void setIpv4SrcSubnetPrefix(String subnetPrefix) {
        ipv4SrcSubnetPrefixBox.setSelectedItem(subnetPrefix);
    }

    public String getIpv4DstAddress() {
        return ipv4DstAddressField.getText().trim();
    }

    public void setIpv4DstAddress(String address) {
        ipv4DstAddressField.setText(address);
    }

    public String getIpv4DstSubnetPrefix() {
        return (String) ipv4DstSubnetPrefixBox.getSelectedItem();
    }

    public void setIpv4DstSubnetPrefix(String subnetPrefix) {
        ipv4DstSubnetPrefixBox.setSelectedItem(subnetPrefix);
    }

    public String getIpv6SrcAddress() {
        return ipv6SrcAddressField.getText().trim();
    }

    public void setIpv6SrcAddress(String address) {
        ipv6SrcAddressField.setText(address);
    }

    public String getIpv6SrcSubnetPrefix() {
        return (String) ipv6SrcSubnetPrefixBox.getSelectedItem();
    }

    public void setIpv6SrcSubnetPrefix(String subnetPrefix) {
        ipv6SrcSubnetPrefixBox.setSelectedItem(subnetPrefix);
    }

    public String getIpv6DstAddress() {
        return ipv6DstAddressField.getText().trim();
    }

    public void setIpv6DstAddress(String address) {
        ipv6DstAddressField.setText(address);
    }

    public String getIpv6DstSubnetPrefix() {
        return (String) ipv6DstSubnetPrefixBox.getSelectedItem();
    }

    public void setIpv6DstSubnetPrefix(String subnetPrefix) {
        ipv6DstSubnetPrefixBox.setSelectedItem(subnetPrefix);
    }

    public String getSrcPort() {
        return srcPortField.getText().trim();
    }

    public void setSrcPort(String port) {
        srcPortField.setText(port);
    }

    public String getSrcL4Protocol() {
        return (String) srcL4ProtocolBox.getSelectedItem();
    }

    public void setSrcL4Protocol(String l4Protocol) {
        srcL4ProtocolBox.setSelectedItem(l4Protocol);
    }

    public String getDstPort() {
        return dstPortField.getText().trim();
    }

    public void setDstPort(String port) {
        dstPortField.setText(port);
    }

    public String getDstL4Protocol() {
        return (String) dstL4ProtocolBox.getSelectedItem();
    }

    public void setDstL4Protocol(String l4Protocol) {
        dstL4ProtocolBox.setSelectedItem(l4Protocol);
    }

    public String getIcmpType() {
        return icmpTypeField.getText().trim();
    }

    public void setIcmpType(String icmpType) {
        icmpTypeField.setText(icmpType);
    }

    public String getIcmpCode() {
        return icmpCodeField.getText().trim();
    }

    public void setIcmpCode(String icmpCode) {
        icmpCodeField.setText(icmpCode);
    }

    public String getDscp() {
        return ipv4DscpField.getText().trim();
    }

    public void setDscp(String dscp) {
        ipv4DscpField.setText(dscp);
        ipv6DscpField.setText(dscp);
    }

    public String getEcn() {
        return ipv4EcnField.getText().trim();
    }

    public void setEcn(String ecn) {
        ipv4EcnField.setText(ecn);
        ipv6EcnField.setText(ecn);
    }

    public String getFlowLabel() {
        return flowLabelField.getText().trim();
    }

    public void setFlowLabel(String flowLabel) {
        flowLabelField.setText(flowLabel);
    }

}
