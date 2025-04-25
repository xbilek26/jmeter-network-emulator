package cz.vutbr.networkemulator.gui.filter;

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

import cz.vutbr.networkemulator.utils.NetworkEmulatorConstants;
import cz.vutbr.networkemulator.verification.IpAddressVerifier;
import cz.vutbr.networkemulator.verification.RangeVerifier;

public class FilterPanel extends JPanel {

    private static final int MIN_PORT_VALUE = 0;
    private static final int MAX_PORT_VALUE = 65535;

    private final ButtonGroup ipProtocolGroup;
    private final JRadioButton tcpButton;
    private final JRadioButton udpButton;
    private final JRadioButton icmpButton;
    private final JTextField srcAddressField;
    private final JComboBox<String> srcSubnetMaskBox;
    private final JLabel srcPortLabel;
    private final JTextField srcPortField;
    private final JTextField dstAddressField;
    private final JComboBox<String> dstSubnetMaskBox;
    private final JLabel dstPortLabel;
    private final JTextField dstPortField;
    private final JComboBox<String> protocolsBox;

    public FilterPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createTitledBorder(NetworkEmulatorConstants.TITLE_FILTER_PANEL));

        tcpButton = new JRadioButton(NetworkEmulatorConstants.TCP_PROTOCOL);
        udpButton = new JRadioButton(NetworkEmulatorConstants.UDP_PROTOCOL);
        icmpButton = new JRadioButton(NetworkEmulatorConstants.ICMP_PROTOCOL);
        tcpButton.setSelected(true);

        ipProtocolGroup = new ButtonGroup();
        ipProtocolGroup.add(tcpButton);
        ipProtocolGroup.add(udpButton);
        ipProtocolGroup.add(icmpButton);

        srcAddressField = new JTextField(20);
        srcSubnetMaskBox = new JComboBox<>(NetworkEmulatorConstants.SUBNET_MASKS);
        srcPortField = new JTextField(7);
        dstAddressField = new JTextField(20);
        dstSubnetMaskBox = new JComboBox<>(NetworkEmulatorConstants.SUBNET_MASKS);
        dstPortField = new JTextField(7);

        JLabel ipProtocolLabel = new JLabel(NetworkEmulatorConstants.LABEL_IP_PROTOCOL);
        JLabel srcAddressLabel = new JLabel(NetworkEmulatorConstants.LABEL_SRC_ADDRESS);
        srcAddressLabel.setLabelFor(srcAddressField);
        JLabel dstAddressLabel = new JLabel(NetworkEmulatorConstants.LABEL_DST_ADDRESS);
        dstAddressLabel.setLabelFor(dstAddressField);
        srcPortLabel = new JLabel(NetworkEmulatorConstants.LABEL_SRC_PORT);
        srcPortLabel.setLabelFor(srcPortField);
        dstPortLabel = new JLabel(NetworkEmulatorConstants.LABEL_DST_PORT);
        dstPortLabel.setLabelFor(dstPortField);

        srcSubnetMaskBox.setPreferredSize(new Dimension(90, srcAddressField.getPreferredSize().height));
        dstSubnetMaskBox.setPreferredSize(new Dimension(90, dstAddressField.getPreferredSize().height));

        protocolsBox = new JComboBox<>(NetworkEmulatorConstants.PROTOCOLS);
        protocolsBox.setPreferredSize(new Dimension(120, srcPortField.getPreferredSize().height));

        protocolsBox.addActionListener(e -> {
            String selectedProtocol = (String) protocolsBox.getSelectedItem();
            Integer port = NetworkEmulatorConstants.PROTOCOL_PORTS.get(selectedProtocol);
            if (!selectedProtocol.isEmpty()) {
                dstPortField.setText(port.toString());
            }
        });

        dstAddressField.setInputVerifier(new IpAddressVerifier());
        dstPortField.setInputVerifier(new RangeVerifier(MIN_PORT_VALUE, MAX_PORT_VALUE, false));
        srcAddressField.setInputVerifier(new IpAddressVerifier());
        srcPortField.setInputVerifier(new RangeVerifier(MIN_PORT_VALUE, MAX_PORT_VALUE, false));
        tcpButton.addItemListener(e -> updateFields());
        udpButton.addItemListener(e -> updateFields());
        icmpButton.addItemListener(e -> updateFields());

        JPanel iPprotocolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        iPprotocolPanel.add(ipProtocolLabel);
        iPprotocolPanel.add(tcpButton);
        iPprotocolPanel.add(udpButton);
        iPprotocolPanel.add(icmpButton);

        JPanel srcPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        srcPanel.add(srcAddressLabel);
        srcPanel.add(srcAddressField);
        srcPanel.add(srcSubnetMaskBox);
        srcPanel.add(srcPortLabel);
        srcPanel.add(srcPortField);

        JPanel dstPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dstPanel.add(dstAddressLabel);
        dstPanel.add(dstAddressField);
        dstPanel.add(dstSubnetMaskBox);
        dstPanel.add(dstPortLabel);
        dstPanel.add(dstPortField);
        dstPanel.add(protocolsBox);

        add(iPprotocolPanel);
        add(srcPanel);
        add(dstPanel);
    }

    private void updateFields() {
        boolean isIcmp = getIpProtocol().equals(NetworkEmulatorConstants.ICMP_PROTOCOL);

        srcPortLabel.setVisible(!isIcmp);
        srcPortField.setVisible(!isIcmp);
        dstPortLabel.setVisible(!isIcmp);
        dstPortField.setVisible(!isIcmp);
        protocolsBox.setVisible(!isIcmp);
    }

    public String getIpProtocol() {
        if (udpButton.isSelected()) {
            return NetworkEmulatorConstants.UDP_PROTOCOL;
        }
        if (icmpButton.isSelected()) {
            return NetworkEmulatorConstants.ICMP_PROTOCOL;
        }

        return NetworkEmulatorConstants.TCP_PROTOCOL;
    }

    public String getSrcAddress() {
        return srcAddressField.getText().trim();
    }

    public String getSrcSubnetMask() {
        return (String) srcSubnetMaskBox.getSelectedItem();
    }

    public String getSrcPort() {
        return srcPortField.getText().trim();
    }

    public String getDstAddress() {
        return dstAddressField.getText().trim();
    }

    public String getDstSubnetMask() {
        return (String) dstSubnetMaskBox.getSelectedItem();
    }

    public String getDstPort() {
        return dstPortField.getText().trim();
    }

    public void setIpProtocol(String ipProtocol) {
        switch (ipProtocol) {
            case NetworkEmulatorConstants.UDP_PROTOCOL ->
                udpButton.setSelected(true);
            case NetworkEmulatorConstants.ICMP_PROTOCOL ->
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

    public void setSrcPort(String srcPort) {
        srcPortField.setText(srcPort);
    }

    public void setDstAddress(String dstAddress) {
        dstAddressField.setText(dstAddress);
    }

    public void setDstSubnetMask(String dstSubnetMask) {
        dstSubnetMaskBox.setSelectedItem(dstSubnetMask);
    }

    public void setDstPort(String dstPort) {
        dstPortField.setText(dstPort);
    }

}
