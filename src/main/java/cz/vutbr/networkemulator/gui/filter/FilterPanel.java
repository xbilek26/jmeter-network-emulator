package cz.vutbr.networkemulator.gui.filter;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
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

    private final ButtonGroup ipProtocolGroup;
    private final JRadioButton tcpButton;
    private final JRadioButton udpButton;
    private final JRadioButton icmpButton;
    private final JTextField srcAddressField;
    private final JComboBox<String> srcSubnetMaskBox;
    private final JTextField dstAddressField;
    private final JComboBox<String> dstSubnetMaskBox;
    private final JTextField srcPortField;
    private final JTextField dstPortField;
    private final JComboBox<String> protocolsBox;
    private final JTextField icmpTypeField;
    private final JTextField icmpCodeField;
    private final JPanel cards;

    public FilterPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createTitledBorder(Constants.TITLE_FILTER_PANEL));

        tcpButton = new JRadioButton(Constants.TCP_PROTOCOL);
        udpButton = new JRadioButton(Constants.UDP_PROTOCOL);
        icmpButton = new JRadioButton(Constants.ICMP_PROTOCOL);
        tcpButton.setSelected(true);

        ipProtocolGroup = new ButtonGroup();
        ipProtocolGroup.add(tcpButton);
        ipProtocolGroup.add(udpButton);
        ipProtocolGroup.add(icmpButton);

        icmpTypeField = new JTextField(8);
        icmpCodeField = new JTextField(8);

        srcAddressField = new JTextField(20);
        srcSubnetMaskBox = new JComboBox<>(Constants.SUBNET_MASKS);
        srcPortField = new JTextField(7);
        dstAddressField = new JTextField(20);
        dstSubnetMaskBox = new JComboBox<>(Constants.SUBNET_MASKS);
        dstPortField = new JTextField(7);

        JLabel ipProtocolLabel = new JLabel(Constants.LABEL_IP_PROTOCOL);
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

        srcSubnetMaskBox.setPreferredSize(new Dimension(90, srcAddressField.getPreferredSize().height));
        dstSubnetMaskBox.setPreferredSize(new Dimension(90, dstAddressField.getPreferredSize().height));

        protocolsBox = new JComboBox<>(Constants.PROTOCOLS);
        protocolsBox.setPreferredSize(new Dimension(120, srcPortField.getPreferredSize().height));

        protocolsBox.addActionListener(e -> {
            String selectedProtocol = (String) protocolsBox.getSelectedItem();
            Integer port = Constants.PROTOCOL_PORTS.get(selectedProtocol);
            if (!selectedProtocol.isEmpty()) {
                dstPortField.setText(port.toString());
            }
        });

        srcAddressField.setInputVerifier(new IpAddressVerifier());
        dstAddressField.setInputVerifier(new IpAddressVerifier());
        srcPortField.setInputVerifier(new RangeVerifier(MIN_PORT_VALUE, MAX_PORT_VALUE, false));
        dstPortField.setInputVerifier(new RangeVerifier(MIN_PORT_VALUE, MAX_PORT_VALUE, false));
        icmpTypeField.setInputVerifier(new RangeVerifier(MIN_ICMP_TYPE_VALUE, MAX_ICMP_TYPE_VALUE, false));
        icmpCodeField.setInputVerifier(new RangeVerifier(MIN_ICMP_CODE_VALUE, MAX_ICMP_CODE_VALUE, false));
        tcpButton.addItemListener(e -> updateCards());
        udpButton.addItemListener(e -> updateCards());
        icmpButton.addItemListener(e -> updateCards());

        JPanel iPprotocolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        iPprotocolPanel.add(ipProtocolLabel);
        iPprotocolPanel.add(tcpButton);
        iPprotocolPanel.add(udpButton);
        iPprotocolPanel.add(icmpButton);

        JPanel addressPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addressPanel.add(srcAddressLabel);
        addressPanel.add(srcAddressField);
        addressPanel.add(srcSubnetMaskBox);
        addressPanel.add(dstAddressLabel);
        addressPanel.add(dstAddressField);
        addressPanel.add(dstSubnetMaskBox);

        JPanel portPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        portPanel.add(srcPortLabel);
        portPanel.add(srcPortField);
        portPanel.add(dstPortLabel);
        portPanel.add(dstPortField);
        portPanel.add(protocolsBox);

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

        add(wrapper);

        add(iPprotocolPanel);
        add(addressPanel);
        add(wrapper);
    }

    private void updateCards() {
        boolean isIcmp = getIpProtocol().equals(Constants.ICMP_PROTOCOL);
        CardLayout cl = (CardLayout) cards.getLayout();
        if (isIcmp) {
            cl.show(cards, "icmp");
        } else {
            cl.show(cards, "port");
        }
    }

    public String getIpProtocol() {
        if (udpButton.isSelected()) {
            return Constants.UDP_PROTOCOL;
        }
        if (icmpButton.isSelected()) {
            return Constants.ICMP_PROTOCOL;
        }

        return Constants.TCP_PROTOCOL;
    }

    public String getSrcAddress() {
        return srcAddressField.getText().trim();
    }

    public String getSrcSubnetMask() {
        return (String) srcSubnetMaskBox.getSelectedItem();
    }

    public String getDstAddress() {
        return dstAddressField.getText().trim();
    }

    public String getDstSubnetMask() {
        return (String) dstSubnetMaskBox.getSelectedItem();
    }

    public String getSrcPort() {
        return srcPortField.getText().trim();
    }

    public String getDstPort() {
        return dstPortField.getText().trim();
    }

    public String getIcmpType() {
        return icmpTypeField.getText().trim();
    }

    public String getIcmpCode() {
        return icmpCodeField.getText().trim();
    }

    public void setIpProtocol(String ipProtocol) {
        switch (ipProtocol) {
            case Constants.UDP_PROTOCOL ->
                udpButton.setSelected(true);
            case Constants.ICMP_PROTOCOL ->
                icmpButton.setSelected(true);
            default ->
                tcpButton.setSelected(true);
        }
    }

    public void setSrcAddress(String srcAddress) {
        srcAddressField.setText(srcAddress);
    }

    public void setSrcSubnetMask(String srcSubnetMask) {
        srcSubnetMaskBox.setSelectedItem(srcSubnetMask);
    }

    public void setDstAddress(String dstAddress) {
        dstAddressField.setText(dstAddress);
    }

    public void setDstSubnetMask(String dstSubnetMask) {
        dstSubnetMaskBox.setSelectedItem(dstSubnetMask);
    }

    public void setSrcPort(String srcPort) {
        srcPortField.setText(srcPort);
    }

    public void setDstPort(String dstPort) {
        dstPortField.setText(dstPort);
    }

    public void setIcmpType(String icmpType) {
        icmpTypeField.setText(icmpType);
    }

    public void setIcmpCode(String icmpCode) {
        icmpCodeField.setText(icmpCode);
    }

}
