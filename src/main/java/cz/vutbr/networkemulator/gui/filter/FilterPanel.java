package cz.vutbr.networkemulator.gui.filter;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

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

import cz.vutbr.networkemulator.utils.Constants;
import cz.vutbr.networkemulator.utils.IpVersion;
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

    private final DefaultComboBoxModel<String> modelIpv4;
    private final DefaultComboBoxModel<String> modelIpv6;

    private final IpAddressVerifier iPv4AddressVerifier;
    private final IpAddressVerifier iPv6AddressVerifier;

    public FilterPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createTitledBorder(Constants.TITLE_FILTER_PANEL));

        ipv4Button = new JRadioButton(Constants.IPV4);
        ipv6Button = new JRadioButton(Constants.IPV6);
        ipv4Button.setSelected(true);
        ButtonGroup ipVersionGroup = new ButtonGroup();
        ipVersionGroup.add(ipv4Button);
        ipVersionGroup.add(ipv6Button);

        tcpButton = new JRadioButton(Constants.TCP_PROTOCOL);
        udpButton = new JRadioButton(Constants.UDP_PROTOCOL);
        icmpButton = new JRadioButton(Constants.ICMP_PROTOCOL);
        tcpButton.setSelected(true);
        ButtonGroup protocolGroup = new ButtonGroup();
        protocolGroup.add(tcpButton);
        protocolGroup.add(udpButton);
        protocolGroup.add(icmpButton);

        icmpTypeField = new JTextField(8);
        icmpCodeField = new JTextField(8);

        modelIpv4 = new DefaultComboBoxModel<>(Constants.PREFIX_LENGTHS_IPV4);
        modelIpv6 = new DefaultComboBoxModel<>(Constants.PREFIX_LENGTHS_IPV6);

        srcAddressField = new JTextField(20);
        srcSubnetPrefixBox = new JComboBox<>();
        srcSubnetPrefixBox.setModel(modelIpv4);
        srcPortField = new JTextField(7);
        dstAddressField = new JTextField(20);
        dstSubnetPrefixBox = new JComboBox<>();
        dstSubnetPrefixBox.setModel(modelIpv4);
        dstPortField = new JTextField(7);

        JLabel ipVersionLabel = new JLabel(Constants.LABEL_IP_VERSION);
        JLabel protocolLabel = new JLabel(Constants.LABEL_PROTOCOL);
        JLabel srcAddressLabel = new JLabel(Constants.LABEL_SRC_ADDRESS);
        srcAddressLabel.setLabelFor(srcAddressField);
        JLabel dstAddressLabel = new JLabel(Constants.LABEL_DST_ADDRESS);
        dstAddressLabel.setLabelFor(dstAddressField);
        JLabel srcPortLabel = new JLabel(Constants.LABEL_SRC_PORT);
        srcPortLabel.setLabelFor(srcPortField);
        JLabel dstPortLabel = new JLabel(Constants.LABEL_DST_PORT);
        dstPortLabel.setLabelFor(dstPortField);
        JLabel icmpTypeLabel = new JLabel(Constants.LABEL_ICMP_TYPE);
        icmpTypeLabel.setLabelFor(icmpTypeField);
        JLabel icmpCodeLabel = new JLabel(Constants.LABEL_ICMP_CODE);
        icmpCodeLabel.setLabelFor(icmpCodeField);

        srcSubnetPrefixBox.setPreferredSize(new Dimension(90, srcAddressField.getPreferredSize().height));
        dstSubnetPrefixBox.setPreferredSize(new Dimension(90, dstAddressField.getPreferredSize().height));

        l4protocolsBox = new JComboBox<>(Constants.PROTOCOLS);
        l4protocolsBox.setPreferredSize(new Dimension(120, srcPortField.getPreferredSize().height));

        l4protocolsBox.addActionListener(_ -> {
            String selectedL4Protocol = (String) l4protocolsBox.getSelectedItem();
            Integer port = Constants.PROTOCOL_PORTS.get(selectedL4Protocol);
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
            srcSubnetPrefixBox.setModel(modelIpv6);
            dstSubnetPrefixBox.setModel(modelIpv6);
        } else {
            srcAddressField.setInputVerifier(iPv4AddressVerifier);
            dstAddressField.setInputVerifier(iPv4AddressVerifier);
            srcSubnetPrefixBox.setModel(modelIpv4);
            dstSubnetPrefixBox.setModel(modelIpv4);
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
