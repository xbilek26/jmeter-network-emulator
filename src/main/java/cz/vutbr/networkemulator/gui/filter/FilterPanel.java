package cz.vutbr.networkemulator.gui.filter;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
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

public class FilterPanel extends JPanel {

    private static final int MIN_PORT_VALUE = 0;
    private static final int MAX_PORT_VALUE = 65535;
    private static final int MIN_ICMP_TYPE_VALUE = 0;
    private static final int MAX_ICMP_TYPE_VALUE = 255;
    private static final int MIN_ICMP_CODE_VALUE = 0;
    private static final int MAX_ICMP_CODE_VALUE = 255;

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
    
    private static final String[] ICMP_TYPES = {
                "",
                "Echo Reply",
                "Destination Unreachable",
                "Redirect",
                "Echo Request",
                "Time Exceeded",
                "Parameter Problem",
        };

    private static final Map<String, Integer> ICMP_TYPE_NAMES = Map.of(
        "Echo Reply", 0,
        "Destination Unreachable", 3,
        "Redirect", 5,
        "Echo Request", 8,
        "Time Exceeded", 11,
        "Parameter Problem", 12
    );

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
    private final JTextField srcPortField;
    private final JTextField dstPortField;
    private final JComboBox<String> l4protocolsBox;
    private final JTextField icmpTypeField;
    private final JComboBox<String> icmpTypeBox;
    private final JTextField icmpCodeField;
    private final JPanel ipVersionCards;
    private final JPanel protocolCards;

    private final IpAddressVerifier iPv4AddressVerifier;
    private final IpAddressVerifier iPv6AddressVerifier;

    public FilterPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createTitledBorder(NetworkEmulatorUtils.getString("title_filter")));

        ipv4Button = new JRadioButton(NetworkEmulatorUtils.getString("label_ip_version_ipv4"));
        ipv6Button = new JRadioButton(NetworkEmulatorUtils.getString("label_ip_version_ipv6"));
        ipv4Button.setSelected(true);
        ButtonGroup ipVersionGroup = new ButtonGroup();
        ipVersionGroup.add(ipv4Button);
        ipVersionGroup.add(ipv6Button);

        tcpButton = new JRadioButton(NetworkEmulatorUtils.getString("label_protocol_tcp"));
        udpButton = new JRadioButton(NetworkEmulatorUtils.getString("label_protocol_udp"));
        icmpButton = new JRadioButton(NetworkEmulatorUtils.getString("label_protocol_icmp"));
        tcpButton.setSelected(true);
        ButtonGroup protocolGroup = new ButtonGroup();
        protocolGroup.add(tcpButton);
        protocolGroup.add(udpButton);
        protocolGroup.add(icmpButton);

        icmpTypeField = new JTextField(8);
        icmpCodeField = new JTextField(8);

        ipv4SrcAddressField = new JTextField(10);
        ipv4SrcSubnetPrefixBox = new JComboBox<>();
        ipv4SrcSubnetPrefixBox.setModel(new DefaultComboBoxModel<>(PREFIX_LENGTHS_IPV4));
        ipv4DstAddressField = new JTextField(10);
        ipv4DstSubnetPrefixBox = new JComboBox<>();
        ipv4DstSubnetPrefixBox.setModel(new DefaultComboBoxModel<>(PREFIX_LENGTHS_IPV4));
        
        ipv6SrcAddressField = new JTextField(20);
        ipv6SrcSubnetPrefixBox = new JComboBox<>();
        ipv6SrcSubnetPrefixBox.setModel(new DefaultComboBoxModel<>(PREFIX_LENGTHS_IPV6));
        ipv6DstAddressField = new JTextField(20);
        ipv6DstSubnetPrefixBox = new JComboBox<>();
        ipv6DstSubnetPrefixBox.setModel(new DefaultComboBoxModel<>(PREFIX_LENGTHS_IPV6));

        srcPortField = new JTextField(7);
        dstPortField = new JTextField(7);

        JLabel ipVersionLabel = new JLabel(NetworkEmulatorUtils.getString("label_ip_version"));
        JLabel protocolLabel = new JLabel(NetworkEmulatorUtils.getString("label_protocol"));
        JLabel ipv4SrcAddressLabel = new JLabel(NetworkEmulatorUtils.getString("label_src_address"));
        ipv4SrcAddressLabel.setLabelFor(ipv4SrcAddressField);
        JLabel ipv4DstAddressLabel = new JLabel(NetworkEmulatorUtils.getString("label_dst_address"));
        ipv4DstAddressLabel.setLabelFor(ipv4DstAddressField);
        JLabel ipv6SrcAddressLabel = new JLabel(NetworkEmulatorUtils.getString("label_src_address"));
        ipv6SrcAddressLabel.setLabelFor(ipv6SrcAddressField);
        JLabel ipv6DstAddressLabel = new JLabel(NetworkEmulatorUtils.getString("label_dst_address"));
        ipv6DstAddressLabel.setLabelFor(ipv6DstAddressField);
        JLabel srcPortLabel = new JLabel(NetworkEmulatorUtils.getString("label_src_port"));
        srcPortLabel.setLabelFor(srcPortField);
        JLabel dstPortLabel = new JLabel(NetworkEmulatorUtils.getString("label_dst_port"));
        dstPortLabel.setLabelFor(dstPortField);
        JLabel icmpTypeLabel = new JLabel(NetworkEmulatorUtils.getString("label_icmp_type"));
        icmpTypeLabel.setLabelFor(icmpTypeField);
        JLabel icmpCodeLabel = new JLabel(NetworkEmulatorUtils.getString("label_icmp_code"));
        icmpCodeLabel.setLabelFor(icmpCodeField);

        ipv4SrcSubnetPrefixBox.setPreferredSize(new Dimension(90,
                ipv4SrcAddressField.getPreferredSize().height));
        ipv4DstSubnetPrefixBox.setPreferredSize(new Dimension(90,
                ipv4DstAddressField.getPreferredSize().height));

        ipv6SrcSubnetPrefixBox.setPreferredSize(new Dimension(90,
                ipv6SrcAddressField.getPreferredSize().height));
        ipv6DstSubnetPrefixBox.setPreferredSize(new Dimension(90,
                ipv6DstAddressField.getPreferredSize().height));

        l4protocolsBox = new JComboBox<>(PROTOCOLS);
        l4protocolsBox.setPreferredSize(new Dimension(110, srcPortField.getPreferredSize().height));

        l4protocolsBox.addActionListener(_ -> {
            String selectedL4Protocol = (String) l4protocolsBox.getSelectedItem();
            Integer port = PROTOCOL_PORTS.get(selectedL4Protocol);
            if (!selectedL4Protocol.isEmpty()) {
                dstPortField.setText(port.toString());
            } else {
                dstPortField.setText("");
            }
        });

        icmpTypeBox = new JComboBox<>(ICMP_TYPES);
        icmpTypeBox.setPreferredSize(new Dimension(240, icmpTypeField.getPreferredSize().height));
        icmpTypeBox.addActionListener(_ -> {
            String selectedIcmpTypeName = (String) icmpTypeBox.getSelectedItem();
            Integer icmpType = ICMP_TYPE_NAMES.get(selectedIcmpTypeName);
            if (!selectedIcmpTypeName.isEmpty()) {
                icmpTypeField.setText(icmpType.toString());
            } else {
                icmpTypeField.setText("");
            }
        });

        ipv4Button.addItemListener(_ -> updateIpVersion());
        ipv6Button.addItemListener(_ -> updateIpVersion());
        tcpButton.addItemListener(_ -> updateProtocol());
        udpButton.addItemListener(_ -> updateProtocol());
        icmpButton.addItemListener(_ -> updateProtocol());

        iPv4AddressVerifier = new IpAddressVerifier(IpVersion.IPv4);
        iPv6AddressVerifier = new IpAddressVerifier(IpVersion.IPv6);

        ipv4SrcAddressField.setInputVerifier(iPv4AddressVerifier);
        ipv4DstAddressField.setInputVerifier(iPv4AddressVerifier);
        ipv6SrcAddressField.setInputVerifier(iPv6AddressVerifier);
        ipv6DstAddressField.setInputVerifier(iPv6AddressVerifier);
        srcPortField.setInputVerifier(new RangeVerifier(MIN_PORT_VALUE, MAX_PORT_VALUE, false));
        dstPortField.setInputVerifier(new RangeVerifier(MIN_PORT_VALUE, MAX_PORT_VALUE, false));
        icmpTypeField.setInputVerifier(new RangeVerifier(MIN_ICMP_TYPE_VALUE, MAX_ICMP_TYPE_VALUE, false));
        icmpCodeField.setInputVerifier(new RangeVerifier(MIN_ICMP_CODE_VALUE, MAX_ICMP_CODE_VALUE, false));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(ipVersionLabel);
        topPanel.add(ipv4Button);
        topPanel.add(ipv6Button);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(protocolLabel);
        topPanel.add(tcpButton);
        topPanel.add(udpButton);
        topPanel.add(icmpButton);

        JPanel ipv4AddressPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ipv4AddressPanel.add(ipv4SrcAddressLabel);
        ipv4AddressPanel.add(ipv4SrcAddressField);
        ipv4AddressPanel.add(ipv4SrcSubnetPrefixBox);
        ipv4AddressPanel.add(Box.createHorizontalStrut(5));
        ipv4AddressPanel.add(ipv4DstAddressLabel);
        ipv4AddressPanel.add(ipv4DstAddressField);
        ipv4AddressPanel.add(ipv4DstSubnetPrefixBox);

        JPanel ipv6AddressPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ipv6AddressPanel.add(ipv6SrcAddressLabel);
        ipv6AddressPanel.add(ipv6SrcAddressField);
        ipv6AddressPanel.add(ipv6SrcSubnetPrefixBox);
        ipv6AddressPanel.add(Box.createHorizontalStrut(5));
        ipv6AddressPanel.add(ipv6DstAddressLabel);
        ipv6AddressPanel.add(ipv6DstAddressField);
        ipv6AddressPanel.add(ipv6DstSubnetPrefixBox);

        ipVersionCards = new JPanel(new CardLayout());
        ipVersionCards.add(ipv4AddressPanel, "ipv4");
        ipVersionCards.add(ipv6AddressPanel, "ipv6");

        JPanel ipVersionCardsWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        ipVersionCardsWrapper.add(ipVersionCards);

        JPanel portPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        portPanel.add(srcPortLabel);
        portPanel.add(srcPortField);
        portPanel.add(Box.createHorizontalStrut(5));
        portPanel.add(dstPortLabel);
        portPanel.add(dstPortField);
        portPanel.add(l4protocolsBox);

        JPanel icmpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        icmpPanel.add(icmpTypeLabel);
        icmpPanel.add(icmpTypeField);
        icmpPanel.add(icmpTypeBox);
        icmpPanel.add(Box.createHorizontalStrut(5));
        icmpPanel.add(icmpCodeLabel);
        icmpPanel.add(icmpCodeField);

        protocolCards = new JPanel(new CardLayout());
        protocolCards.add(portPanel, "port");
        protocolCards.add(icmpPanel, "icmp");

        JPanel protocolCardsWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        protocolCardsWrapper.add(protocolCards);

        add(topPanel);
        add(ipVersionCardsWrapper);
        add(protocolCardsWrapper);
    }

    private void updateIpVersion() {
        CardLayout cards = (CardLayout) ipVersionCards.getLayout();
        if (isIpv4Selected()) {
            cards.show(ipVersionCards, "ipv4");
        } else {
            cards.show(ipVersionCards, "ipv6");
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

}
