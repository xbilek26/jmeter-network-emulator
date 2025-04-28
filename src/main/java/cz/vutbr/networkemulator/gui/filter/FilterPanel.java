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

import cz.vutbr.networkemulator.utils.IpVersion;
import cz.vutbr.networkemulator.utils.NetworkEmulator;
import cz.vutbr.networkemulator.utils.Protocol;
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

    private final JRadioButton ipv4Button;
    private final JRadioButton ipv6Button;
    private final JRadioButton tcpButton;
    private final JRadioButton udpButton;
    private final JRadioButton icmpButton;
    private final JTextField srcAddressField;
    private final JComboBox<String> srcSubnetPrefixBox;
    private final JTextField dstAddressField;
    private final JComboBox<String> dstSubnetPrefixBox;
    private final JTextField srcPortField;
    private final JTextField dstPortField;
    private final JComboBox<String> l4protocolsBox;
    private final JTextField icmpTypeField;
    private final JTextField icmpCodeField;
    private final JPanel cards;

    private final IpAddressVerifier iPv4AddressVerifier;
    private final IpAddressVerifier iPv6AddressVerifier;

    public FilterPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createTitledBorder(NetworkEmulator.getString("title_reordering")));

        ipv4Button = new JRadioButton(NetworkEmulator.getString("label_ip_version_ipv4"));
        ipv6Button = new JRadioButton(NetworkEmulator.getString("label_ip_version_ipv6"));
        ipv4Button.setSelected(true);
        ButtonGroup ipVersionGroup = new ButtonGroup();
        ipVersionGroup.add(ipv4Button);
        ipVersionGroup.add(ipv6Button);

        tcpButton = new JRadioButton(NetworkEmulator.getString("label_protocol_tcp"));
        udpButton = new JRadioButton(NetworkEmulator.getString("label_protocol_udp"));
        icmpButton = new JRadioButton(NetworkEmulator.getString("label_protocol_icmp"));
        tcpButton.setSelected(true);
        ButtonGroup protocolGroup = new ButtonGroup();
        protocolGroup.add(tcpButton);
        protocolGroup.add(udpButton);
        protocolGroup.add(icmpButton);

        icmpTypeField = new JTextField(8);
        icmpCodeField = new JTextField(8);

        srcAddressField = new JTextField(20);
        srcSubnetPrefixBox = new JComboBox<>();
        srcSubnetPrefixBox.setModel(new DefaultComboBoxModel<>(PREFIX_LENGTHS_IPV4));
        srcPortField = new JTextField(7);
        dstAddressField = new JTextField(20);
        dstSubnetPrefixBox = new JComboBox<>();
        dstSubnetPrefixBox.setModel(new DefaultComboBoxModel<>(PREFIX_LENGTHS_IPV4));
        dstPortField = new JTextField(7);

        JLabel ipVersionLabel = new JLabel(NetworkEmulator.getString("label_ip_version"));
        JLabel protocolLabel = new JLabel(NetworkEmulator.getString("label_protocol"));
        JLabel srcAddressLabel = new JLabel(NetworkEmulator.getString("label_src_address"));
        srcAddressLabel.setLabelFor(srcAddressField);
        JLabel dstAddressLabel = new JLabel(NetworkEmulator.getString("label_dst_address"));
        dstAddressLabel.setLabelFor(dstAddressField);
        JLabel srcPortLabel = new JLabel(NetworkEmulator.getString("label_src_port"));
        srcPortLabel.setLabelFor(srcPortField);
        JLabel dstPortLabel = new JLabel(NetworkEmulator.getString("label_dst_port"));
        dstPortLabel.setLabelFor(dstPortField);
        JLabel icmpTypeLabel = new JLabel(NetworkEmulator.getString("label_icmp_type"));
        icmpTypeLabel.setLabelFor(icmpTypeField);
        JLabel icmpCodeLabel = new JLabel(NetworkEmulator.getString("label_icmp_code"));
        icmpCodeLabel.setLabelFor(icmpCodeField);

        srcSubnetPrefixBox.setPreferredSize(new Dimension(90, srcAddressField.getPreferredSize().height));
        dstSubnetPrefixBox.setPreferredSize(new Dimension(90, dstAddressField.getPreferredSize().height));

        l4protocolsBox = new JComboBox<>(PROTOCOLS);
        l4protocolsBox.setPreferredSize(new Dimension(120, srcPortField.getPreferredSize().height));

        l4protocolsBox.addActionListener(_ -> {
            String selectedL4Protocol = (String) l4protocolsBox.getSelectedItem();
            Integer port = PROTOCOL_PORTS.get(selectedL4Protocol);
            if (!selectedL4Protocol.isEmpty()) {
                dstPortField.setText(port.toString());
            }
        });

        ipv4Button.addItemListener(_ -> updatePrefixBoxes());
        ipv6Button.addItemListener(_ -> updatePrefixBoxes());
        tcpButton.addItemListener(_ -> updateCards());
        udpButton.addItemListener(_ -> updateCards());
        icmpButton.addItemListener(_ -> updateCards());

        iPv4AddressVerifier = new IpAddressVerifier(IpVersion.IPv4);
        iPv6AddressVerifier = new IpAddressVerifier(IpVersion.IPv6);

        srcAddressField.setInputVerifier(iPv4AddressVerifier);
        dstAddressField.setInputVerifier(iPv4AddressVerifier);
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

        JPanel addressPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addressPanel.add(srcAddressLabel);
        addressPanel.add(srcAddressField);
        addressPanel.add(srcSubnetPrefixBox);
        addressPanel.add(dstAddressLabel);
        addressPanel.add(dstAddressField);
        addressPanel.add(dstSubnetPrefixBox);

        JPanel portPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        portPanel.add(srcPortLabel);
        portPanel.add(srcPortField);
        portPanel.add(dstPortLabel);
        portPanel.add(dstPortField);
        portPanel.add(l4protocolsBox);

        JPanel icmpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        icmpPanel.add(icmpTypeLabel);
        icmpPanel.add(icmpTypeField);
        icmpPanel.add(icmpCodeLabel);
        icmpPanel.add(icmpCodeField);

        cards = new JPanel(new CardLayout());
        cards.add(portPanel, "port");
        cards.add(icmpPanel, "icmp");

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapper.add(cards);

        add(topPanel);
        add(addressPanel);
        add(wrapper);
    }

    private void updatePrefixBoxes() {
        if (ipv6Button.isSelected()) {
            srcAddressField.setInputVerifier(iPv6AddressVerifier);
            dstAddressField.setInputVerifier(iPv6AddressVerifier);
            srcSubnetPrefixBox.setModel(new DefaultComboBoxModel<>(PREFIX_LENGTHS_IPV6));
            dstSubnetPrefixBox.setModel(new DefaultComboBoxModel<>(PREFIX_LENGTHS_IPV6));
        } else {
            srcAddressField.setInputVerifier(iPv4AddressVerifier);
            dstAddressField.setInputVerifier(iPv4AddressVerifier);
            srcSubnetPrefixBox.setModel(new DefaultComboBoxModel<>(PREFIX_LENGTHS_IPV4));
            dstSubnetPrefixBox.setModel(new DefaultComboBoxModel<>(PREFIX_LENGTHS_IPV4));
        }
    }

    private void updateCards() {
        CardLayout cl = (CardLayout) cards.getLayout();
        if (isIcmpSelected()) {
            cl.show(cards, "icmp");
        } else {
            cl.show(cards, "port");
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

    private boolean isIcmpSelected() {
        return icmpButton.isSelected();
    }

    public String getSrcAddress() {
        return srcAddressField.getText().trim();
    }

    public void setSrcAddress(String srcAddress) {
        srcAddressField.setText(srcAddress);
    }

    public String getSrcSubnetPrefix() {
        return (String) srcSubnetPrefixBox.getSelectedItem();
    }

    public void setSrcSubnetPrefix(String srcSubnetPrefix) {
        srcSubnetPrefixBox.setSelectedItem(srcSubnetPrefix);
    }

    public String getDstAddress() {
        return dstAddressField.getText().trim();
    }

    public void setDstAddress(String dstAddress) {
        dstAddressField.setText(dstAddress);
    }

    public String getDstSubnetPrefix() {
        return (String) dstSubnetPrefixBox.getSelectedItem();
    }

    public void setDstSubnetPrefix(String dstSubnetPrefix) {
        dstSubnetPrefixBox.setSelectedItem(dstSubnetPrefix);
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
