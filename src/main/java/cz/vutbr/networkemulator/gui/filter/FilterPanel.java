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
    private final JComboBox<String> srcPrefixBox;
    private final JTextField dstAddressField;
    private final JComboBox<String> dstPrefixBox;
    private final JTextField srcPortField;
    private final JTextField dstPortField;
    private final JComboBox<String> l4protocolsBox;
    private final JTextField icmpTypeField;
    private final JTextField icmpCodeField;
    private final JPanel cards;

    private final DefaultComboBoxModel<String> modelIpv4;
    private final DefaultComboBoxModel<String> modelIpv6;

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
        srcPrefixBox = new JComboBox<>();
        srcPrefixBox.setModel(modelIpv4);
        srcPortField = new JTextField(7);
        dstAddressField = new JTextField(20);
        dstPrefixBox = new JComboBox<>();
        dstPrefixBox.setModel(modelIpv4);
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

        srcPrefixBox.setPreferredSize(new Dimension(90, srcAddressField.getPreferredSize().height));
        dstPrefixBox.setPreferredSize(new Dimension(90, dstAddressField.getPreferredSize().height));

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
        srcAddressField.setInputVerifier(new IpAddressVerifier());
        dstAddressField.setInputVerifier(new IpAddressVerifier());
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
        addressPanel.add(srcPrefixBox);
        addressPanel.add(dstAddressLabel);
        addressPanel.add(dstAddressField);
        addressPanel.add(dstPrefixBox);

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
            srcPrefixBox.setModel(modelIpv6);
            dstPrefixBox.setModel(modelIpv6);
        } else {
            srcPrefixBox.setModel(modelIpv4);
            dstPrefixBox.setModel(modelIpv4);
        }
    }

    private void updateCards() {
        boolean isIcmp = getProtocol().equals(Constants.ICMP_PROTOCOL);
        CardLayout cl = (CardLayout) cards.getLayout();
        if (isIcmp) {
            cl.show(cards, "icmp");
        } else {
            cl.show(cards, "port");
        }
    }

    public String getIpVersion() {
        if (ipv6Button.isSelected()) {
            return Constants.IPV6;
        }
        return Constants.IPV4;
    }

    public void setIpVersion(String ipVersion) {
        switch (ipVersion) {
            case Constants.IPV6 -> {
                ipv6Button.setSelected(true);
            }
            default -> {
                ipv4Button.setSelected(true);
            }
        }
    }

    public String getProtocol() {
        if (udpButton.isSelected()) {
            return Constants.UDP_PROTOCOL;
        }
        if (icmpButton.isSelected()) {
            return Constants.ICMP_PROTOCOL;
        }

        return Constants.TCP_PROTOCOL;
    }

    public void setProtocol(String protocol) {
        switch (protocol) {
            case Constants.UDP_PROTOCOL -> udpButton.setSelected(true);
            case Constants.ICMP_PROTOCOL -> icmpButton.setSelected(true);
            default -> tcpButton.setSelected(true);
        }
    }

    public String getSrcAddress() {
        return srcAddressField.getText().trim();
    }

    public void setSrcAddress(String srcAddress) {
        srcAddressField.setText(srcAddress);
    }

    public String getSrcPrefix() {
        return (String) srcPrefixBox.getSelectedItem();
    }

    public void setSrcPrefix(String srcPrefix) {
        srcPrefixBox.setSelectedItem(srcPrefix);
    }

    public String getDstAddress() {
        return dstAddressField.getText().trim();
    }

    public void setDstAddress(String dstAddress) {
        dstAddressField.setText(dstAddress);
    }

    public String getDstPrefix() {
        return (String) dstPrefixBox.getSelectedItem();
    }

    public void setDstPrefix(String dstSubnetPrefix) {
        dstPrefixBox.setSelectedItem(dstSubnetPrefix);
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
