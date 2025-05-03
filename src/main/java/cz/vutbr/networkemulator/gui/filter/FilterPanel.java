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

import cz.vutbr.networkemulator.utils.NetworkEmulatorUtils;
import cz.vutbr.networkemulator.utils.enums.IpVersion;
import cz.vutbr.networkemulator.utils.enums.Protocol;
import cz.vutbr.networkemulator.verification.IpAddressVerifier;
import cz.vutbr.networkemulator.verification.RangeVerifier;
import net.miginfocom.swing.MigLayout;

public class FilterPanel extends JPanel {

    private static final int MIN_PORT_VALUE = 0;
    private static final int MAX_PORT_VALUE = 65535;
    private static final int MIN_ICMP_TYPE_VALUE = 0;
    private static final int MAX_ICMP_TYPE_VALUE = 255;
    private static final int MIN_ICMP_CODE_VALUE = 0;
    private static final int MAX_ICMP_CODE_VALUE = 255;
    private static final int MIN_DSCP_VALUE = 0;
    private static final int MAX_DSCP_VALUE = 63;
    private static final int MIN_ECN_VALUE = 0;
    private static final int MAX_ECN_VALUE = 3;

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
    private final JPanel ipVersionCards;
    private final JTextField srcPortField;
    private final JComboBox<String> srcL4ProtocolBox;
    private final JTextField dstPortField;
    private final JComboBox<String> dstL4protocolBox;
    private final JTextField icmpTypeField;
    private final JTextField icmpCodeField;
    private final JPanel protocolCards;
    private final JPanel diffServPanel;
    private final JTextField dscpField;
    private final JTextField ecnField;

    public FilterPanel() {
        // layout and border
        setLayout(new MigLayout("insets 5", "grow", "grow"));
        setBorder(BorderFactory.createTitledBorder(NetworkEmulatorUtils.getString("title_filter")));

        // initialisations
        ipv4Button = new JRadioButton(NetworkEmulatorUtils.getString("label_ip_version_ipv4"));
        ipv6Button = new JRadioButton(NetworkEmulatorUtils.getString("label_ip_version_ipv6"));
        ButtonGroup ipVersionGroup = new ButtonGroup();
        tcpButton = new JRadioButton(NetworkEmulatorUtils.getString("label_protocol_tcp"));
        udpButton = new JRadioButton(NetworkEmulatorUtils.getString("label_protocol_udp"));
        icmpButton = new JRadioButton(NetworkEmulatorUtils.getString("label_protocol_icmp"));
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
        dstL4protocolBox = new JComboBox<>(PROTOCOLS);
        icmpTypeField = new JTextField(10);
        icmpCodeField = new JTextField(10);
        dscpField = new JTextField(10);
        ecnField = new JTextField(10);

        // labels
        JLabel ipv4SrcAddressLabel = new JLabel(NetworkEmulatorUtils.getString("label_src_address"));
        JLabel ipv4DstAddressLabel = new JLabel(NetworkEmulatorUtils.getString("label_dst_address"));
        JLabel ipv6SrcAddressLabel = new JLabel(NetworkEmulatorUtils.getString("label_src_address"));
        JLabel ipv6DstAddressLabel = new JLabel(NetworkEmulatorUtils.getString("label_dst_address"));
        JLabel srcPortLabel = new JLabel(NetworkEmulatorUtils.getString("label_src_port"));
        JLabel dstPortLabel = new JLabel(NetworkEmulatorUtils.getString("label_dst_port"));
        JLabel icmpTypeLabel = new JLabel(NetworkEmulatorUtils.getString("label_icmp_type"));
        JLabel icmpCodeLabel = new JLabel(NetworkEmulatorUtils.getString("label_icmp_code"));
        JLabel dscpLabel = new JLabel(NetworkEmulatorUtils.getString("label_dscp"));
        JLabel ecnLabel = new JLabel(NetworkEmulatorUtils.getString("label_ecn"));
        ipv4SrcAddressLabel.setLabelFor(ipv4SrcAddressField);
        ipv4DstAddressLabel.setLabelFor(ipv4DstAddressField);
        ipv6SrcAddressLabel.setLabelFor(ipv6SrcAddressField);
        ipv6DstAddressLabel.setLabelFor(ipv6DstAddressField);
        srcPortLabel.setLabelFor(srcPortField);
        dstPortLabel.setLabelFor(dstPortField);
        icmpTypeLabel.setLabelFor(icmpTypeField);
        icmpCodeLabel.setLabelFor(icmpCodeField);
        dscpLabel.setLabelFor(dscpField);
        ecnLabel.setLabelFor(ecnField);

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

        dstL4protocolBox.addActionListener(_ -> {
            String selected = (String) dstL4protocolBox.getSelectedItem();
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
        srcPortField.setInputVerifier(new RangeVerifier(MIN_PORT_VALUE, MAX_PORT_VALUE, false));
        dstPortField.setInputVerifier(new RangeVerifier(MIN_PORT_VALUE, MAX_PORT_VALUE, false));
        icmpTypeField.setInputVerifier(new RangeVerifier(MIN_ICMP_TYPE_VALUE, MAX_ICMP_TYPE_VALUE, false));
        icmpCodeField.setInputVerifier(new RangeVerifier(MIN_ICMP_CODE_VALUE, MAX_ICMP_CODE_VALUE, false));
        dscpField.setInputVerifier(new RangeVerifier(MIN_DSCP_VALUE, MAX_DSCP_VALUE, false));
        ecnField.setInputVerifier(new RangeVerifier(MIN_ECN_VALUE, MAX_ECN_VALUE, false));

        // panels
        JPanel ipVersionPanel = new JPanel(new MigLayout("align center"));
        ipVersionPanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorUtils.getString("title_ip_version")));
        ipVersionPanel.add(ipv4Button);
        ipVersionPanel.add(ipv6Button);

        JPanel protocolPanel = new JPanel(new MigLayout("align center"));
        protocolPanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorUtils.getString("title_protocol")));
        protocolPanel.add(tcpButton);
        protocolPanel.add(udpButton);
        protocolPanel.add(icmpButton);

        JPanel ipv4AddressPanel = new JPanel(new MigLayout("", "[][grow][][][grow][]", "grow"));
        ipv4AddressPanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorUtils.getString("title_ipv4_address")));
        ipv4AddressPanel.add(ipv4SrcAddressLabel);
        ipv4AddressPanel.add(ipv4SrcAddressField, "growx, growy");
        ipv4AddressPanel.add(ipv4SrcSubnetPrefixBox, "growx, growy, gapright 5");
        ipv4AddressPanel.add(ipv4DstAddressLabel);
        ipv4AddressPanel.add(ipv4DstAddressField, "growx, growy");
        ipv4AddressPanel.add(ipv4DstSubnetPrefixBox, "growx, growy");

        JPanel ipv6AddressPanel = new JPanel(new MigLayout("", "[][grow][][][grow][]", "grow"));
        ipv6AddressPanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorUtils.getString("title_ipv6_address")));
        ipv6AddressPanel.add(ipv6SrcAddressLabel);
        ipv6AddressPanel.add(ipv6SrcAddressField, "growx, growy");
        ipv6AddressPanel.add(ipv6SrcSubnetPrefixBox, "growx, growy, gapright 5");
        ipv6AddressPanel.add(ipv6DstAddressLabel);
        ipv6AddressPanel.add(ipv6DstAddressField, "growx, growy");
        ipv6AddressPanel.add(ipv6DstSubnetPrefixBox, "growx, growy");

        ipVersionCards = new JPanel(new CardLayout());
        ipVersionCards.add(ipv4AddressPanel, "ipv4");
        ipVersionCards.add(ipv6AddressPanel, "ipv6");

        JPanel portPanel = new JPanel(new MigLayout("", "[][grow][grow]", "grow"));
        portPanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorUtils.getString("title_port")));
        portPanel.add(srcPortLabel);
        portPanel.add(srcPortField, "growx, growy");
        portPanel.add(srcL4ProtocolBox, "growx, growy, wrap");
        portPanel.add(dstPortLabel);
        portPanel.add(dstPortField, "growx, growy");
        portPanel.add(dstL4protocolBox, "growx, growy");

        JPanel icmpPanel = new JPanel(new MigLayout("", "[][grow]", "grow"));
        icmpPanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorUtils.getString("title_icmp")));
        icmpPanel.add(icmpTypeLabel);
        icmpPanel.add(icmpTypeField, "growx, growy, wrap");
        icmpPanel.add(icmpCodeLabel);
        icmpPanel.add(icmpCodeField, "growx, growy");

        protocolCards = new JPanel(new CardLayout());
        protocolCards.add(portPanel, "port");
        protocolCards.add(icmpPanel, "icmp");

        diffServPanel = new JPanel(new MigLayout("", "[][grow]", "grow"));
        diffServPanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorUtils.getString("title_ipv4_diffserv_name")));
        diffServPanel.add(dscpLabel);
        diffServPanel.add(dscpField, "growx, growy, wrap");
        diffServPanel.add(ecnLabel);
        diffServPanel.add(ecnField, "growx, growy");

        // add components
        add(ipVersionPanel, "growx, growy");
        add(protocolPanel, "growx, growy, wrap");
        add(ipVersionCards, "growx, growy, span, wrap");
        add(protocolCards, "growx, growy");
        add(diffServPanel, "growx, growy");
    }

    private void updateIpVersion() {
        CardLayout cards = (CardLayout) ipVersionCards.getLayout();
        if (isIpv4Selected()) {
            cards.show(ipVersionCards, "ipv4");
            diffServPanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorUtils.getString("title_ipv4_diffserv_name")));
        } else {
            cards.show(ipVersionCards, "ipv6");
            diffServPanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorUtils.getString("title_ipv6_diffserv_name")));
        }
    }

    private void updateProtocol() {
        CardLayout cards = (CardLayout) protocolCards.getLayout();
        if (isIcmpSelected()) {
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

    private boolean isIpv4Selected() {
        return ipv4Button.isSelected();
    }

    private boolean isIcmpSelected() {
        return icmpButton.isSelected();
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

    public void setSrcPort(String srcPort) {
        srcPortField.setText(srcPort);
    }

    public String getDstPort() {
        return dstPortField.getText().trim();
    }

    public void setDstPort(String dstPort) {
        dstPortField.setText(dstPort);
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
        return dscpField.getText().trim();
    }

    public void setDscp(String dscp) {
        dscpField.setText(dscp);
    }

    public String getEcn() {
        return ecnField.getText().trim();
    }

    public void setEcn(String ecn) {
        ecnField.setText(ecn);
    }

}
