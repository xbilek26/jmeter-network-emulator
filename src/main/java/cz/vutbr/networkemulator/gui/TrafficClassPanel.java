package cz.vutbr.networkemulator.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.model.filter.Filter;
import cz.vutbr.networkemulator.model.parameters.Corruption;
import cz.vutbr.networkemulator.model.parameters.Delay;
import cz.vutbr.networkemulator.model.parameters.Duplication;
import cz.vutbr.networkemulator.model.parameters.Loss;
import cz.vutbr.networkemulator.model.parameters.Parameter;
import cz.vutbr.networkemulator.model.parameters.Rate;
import cz.vutbr.networkemulator.model.parameters.Reordering;
import cz.vutbr.networkemulator.utils.NetworkEmulatorConstants;
import cz.vutbr.networkemulator.verification.IpAddressVerifier;
import cz.vutbr.networkemulator.verification.RangeVerifier;

public class TrafficClassPanel extends JPanel {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(TrafficClassPanel.class);

    private final String PROPERTY_FILTER = "filter_";
    private final String PROPERTY_PARAMETERS = "parameters_";

    private ButtonGroup ipProtocolGroup;
    private JRadioButton tcpButton;
    private JRadioButton udpButton;
    private JRadioButton icmpButton;
    private JTextField srcAddressField;
    private JComboBox<String> srcSubnetMaskBox;
    private JTextField srcPortField;
    private JTextField dstAddressField;
    private JComboBox<String> dstSubnetMaskBox;
    private JTextField dstPortField;
    private JComboBox<String> protocolsBox;
    private JTextField delayValueField;
    private JTextField jitterField;
    private JTextField delayCorrelationField;
    private JComboBox<String> distributionsBox;
    private JTextField lossValueField;
    private JTextField lossCorrelationField;
    private JTextField rateField;
    private JTextField reorderingValueField;
    private JTextField reorderingCorrelationField;
    private JTextField duplicationValueField;
    private JTextField duplicationCorrelationField;
    private JTextField corruptionField;

    private final String UNDERSCORE = "_";

    public TrafficClassPanel(String niName, String tcName) {
        setName(niName + UNDERSCORE + tcName);
        init();
    }

    public Filter getFilter() {
        Filter filter = new Filter();
        filter.setIpProtocol(getIpProtocol());
        filter.setSrcAddress(srcAddressField.getText());
        filter.setSrcSubnetMask((String) srcSubnetMaskBox.getSelectedItem());
        filter.setSrcPort(srcPortField.getText());
        filter.setDstAddress(dstAddressField.getText());
        filter.setDstSubnetMask((String) dstSubnetMaskBox.getSelectedItem());
        filter.setDstPort(dstPortField.getText());

        return filter;
    }

    public List<Parameter> getParameters() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new Delay(
                delayValueField.getText(),
                jitterField.getText(),
                delayCorrelationField.getText(),
                (String) distributionsBox.getSelectedItem()
        ));
        parameters.add(new Loss(
                lossValueField.getText(),
                lossCorrelationField.getText()
        ));
        parameters.add(new Rate(
                rateField.getText()
        ));
        parameters.add(new Reordering(
                reorderingValueField.getText(),
                reorderingCorrelationField.getText()
        ));
        parameters.add(new Duplication(
                duplicationValueField.getText(),
                duplicationCorrelationField.getText()
        ));
        parameters.add(new Corruption(
                corruptionField.getText()
        ));

        return parameters;
    }

    private void init() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(createFilterPanel());
        add(createDelayPanel());
        add(createLossPanel());
        add(createRatePanel());
        add(createReorderingPanel());
        add(createDuplicationPanel());
        add(createCorruptionPanel());
    }

    private JPanel createFilterPanel() {
        tcpButton = new JRadioButton(NetworkEmulatorConstants.TCP_PROTOCOL);
        udpButton = new JRadioButton(NetworkEmulatorConstants.UDP_PROTOCOL);
        icmpButton = new JRadioButton(NetworkEmulatorConstants.ICMP_PROTOCOL);
        tcpButton.setSelected(true);

        ipProtocolGroup = new ButtonGroup();
        ipProtocolGroup.add(tcpButton);
        ipProtocolGroup.add(udpButton);
        ipProtocolGroup.add(icmpButton);

        srcAddressField = new JTextField(20);
        srcPortField = new JTextField(7);
        dstAddressField = new JTextField(20);
        dstPortField = new JTextField(7);

        srcSubnetMaskBox = new JComboBox<>(NetworkEmulatorConstants.SUBNET_MASKS);
        dstSubnetMaskBox = new JComboBox<>(NetworkEmulatorConstants.SUBNET_MASKS);
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
        dstPortField.setInputVerifier(new RangeVerifier(0, 1024));
        srcAddressField.setInputVerifier(new IpAddressVerifier());
        srcPortField.setInputVerifier(new RangeVerifier(0, 1024));

        JPanel iPprotocolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        iPprotocolPanel.add(new JLabel(NetworkEmulatorConstants.LABEL_IP_PROTOCOL));
        iPprotocolPanel.add(tcpButton);
        iPprotocolPanel.add(udpButton);
        iPprotocolPanel.add(icmpButton);

        JPanel srcPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        srcPanel.add(new JLabel(NetworkEmulatorConstants.LABEL_SRC_ADDRESS));
        srcPanel.add(srcAddressField);
        srcPanel.add(srcSubnetMaskBox);
        srcPanel.add(new JLabel(NetworkEmulatorConstants.LABEL_SRC_PORT));
        srcPanel.add(srcPortField);

        JPanel dstPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dstPanel.add(new JLabel(NetworkEmulatorConstants.LABEL_DST_ADDRESS));
        dstPanel.add(dstAddressField);
        dstPanel.add(dstSubnetMaskBox);
        dstPanel.add(new JLabel(NetworkEmulatorConstants.LABEL_DST_PORT));
        dstPanel.add(dstPortField);
        dstPanel.add(protocolsBox);

        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.PAGE_AXIS));
        filterPanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorConstants.TITLE_FILTER_PANEL));
        filterPanel.add(iPprotocolPanel);
        filterPanel.add(srcPanel);
        filterPanel.add(dstPanel);

        return filterPanel;
    }

    private JPanel createDelayPanel() {
        delayValueField = new JTextField(8);
        jitterField = new JTextField(8);
        delayCorrelationField = new JTextField(8);
        distributionsBox = new JComboBox<>(NetworkEmulatorConstants.DISTRIBUTIONS);
        distributionsBox.setPreferredSize(new Dimension(180, delayCorrelationField.getPreferredSize().height));

        JLabel delayValueLabel = new JLabel(NetworkEmulatorConstants.LABEL_DELAY_VALUE);
        delayValueLabel.setLabelFor(delayValueField);
        JLabel jitterLabel = new JLabel(NetworkEmulatorConstants.LABEL_JITTER);
        jitterLabel.setLabelFor(jitterField);
        JLabel delayCorrelationLabel = new JLabel(NetworkEmulatorConstants.LABEL_DELAY_CORRELATION);
        delayCorrelationLabel.setLabelFor(delayCorrelationField);
        JLabel distributionsLabel = new JLabel(NetworkEmulatorConstants.LABEL_DISTRIBUTION);
        distributionsLabel.setLabelFor(distributionsBox);

        delayValueField.setInputVerifier(new RangeVerifier(0, 10000));
        jitterField.setInputVerifier(new RangeVerifier(0, 10000));
        delayCorrelationField.setInputVerifier(new RangeVerifier(0, 100));

        JPanel delayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        delayPanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorConstants.TITLE_DELAY_PANEL));
        delayPanel.add(delayValueLabel);
        delayPanel.add(delayValueField);
        delayPanel.add(jitterLabel);
        delayPanel.add(jitterField);
        delayPanel.add(delayCorrelationLabel);
        delayPanel.add(delayCorrelationField);
        delayPanel.add(distributionsLabel);
        delayPanel.add(distributionsBox);

        return delayPanel;
    }

    private JPanel createLossPanel() {
        lossValueField = new JTextField(8);
        lossCorrelationField = new JTextField(8);

        JLabel lossValueLabel = new JLabel(NetworkEmulatorConstants.LABEL_LOSS_VALUE);
        lossValueLabel.setLabelFor(lossValueField);
        JLabel lossCorrelationLabel = new JLabel(NetworkEmulatorConstants.LABEL_LOSS_CORRELATION);
        lossCorrelationLabel.setLabelFor(lossCorrelationField);

        lossValueField.setInputVerifier(new RangeVerifier(0, 100));
        lossCorrelationField.setInputVerifier(new RangeVerifier(0, 100));

        JPanel lossPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lossPanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorConstants.TITLE_LOSS_PANEL));
        lossPanel.add(lossValueLabel);
        lossPanel.add(lossValueField);
        lossPanel.add(lossCorrelationLabel);
        lossPanel.add(lossCorrelationField);

        return lossPanel;
    }

    private JPanel createRatePanel() {
        rateField = new JTextField(8);

        JLabel rateLabel = new JLabel(NetworkEmulatorConstants.LABEL_RATE);
        rateLabel.setLabelFor(rateLabel);

        rateField.setInputVerifier(new RangeVerifier(0, 100000));

        JPanel ratePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ratePanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorConstants.TITLE_RATE_PANEL));
        ratePanel.add(rateLabel);
        ratePanel.add(rateField);

        return ratePanel;
    }

    private JPanel createReorderingPanel() {
        reorderingValueField = new JTextField(8);
        reorderingCorrelationField = new JTextField(8);

        JLabel reorderingValueLabel = new JLabel(NetworkEmulatorConstants.LABEL_REORDERING_VALUE);
        reorderingValueLabel.setLabelFor(reorderingValueField);
        JLabel reorderingCorrelationLabel = new JLabel(NetworkEmulatorConstants.LABEL_REORDERING_CORRELATION);
        reorderingCorrelationLabel.setLabelFor(reorderingCorrelationField);

        reorderingValueField.setInputVerifier(new RangeVerifier(0, 100));
        reorderingCorrelationField.setInputVerifier(new RangeVerifier(0, 100));

        JPanel reorderingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        reorderingPanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorConstants.TITLE_REORDERING_PANEL));
        reorderingPanel.add(reorderingValueLabel);
        reorderingPanel.add(reorderingValueField);
        reorderingPanel.add(reorderingCorrelationLabel);
        reorderingPanel.add(reorderingCorrelationField);

        return reorderingPanel;
    }

    private JPanel createDuplicationPanel() {
        duplicationValueField = new JTextField(8);
        duplicationCorrelationField = new JTextField(8);

        JLabel duplicationValueLabel = new JLabel(NetworkEmulatorConstants.LABEL_DUPLICATION_VALUE);
        duplicationValueLabel.setLabelFor(duplicationValueField);
        JLabel duplicationCorrelationLabel = new JLabel(NetworkEmulatorConstants.LABEL_DUPLICATION_CORRELATION);
        duplicationCorrelationLabel.setLabelFor(duplicationCorrelationField);

        duplicationValueField.setInputVerifier(new RangeVerifier(0, 100));
        duplicationCorrelationField.setInputVerifier(new RangeVerifier(0, 100));

        JPanel duplicationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        duplicationPanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorConstants.TITLE_DUPLICATION_PANEL));
        duplicationPanel.add(duplicationValueLabel);
        duplicationPanel.add(duplicationValueField);
        duplicationPanel.add(duplicationCorrelationLabel);
        duplicationPanel.add(duplicationCorrelationField);

        return duplicationPanel;
    }

    private JPanel createCorruptionPanel() {
        corruptionField = new JTextField(8);

        JLabel corruptionLabel = new JLabel(NetworkEmulatorConstants.LABEL_CORRUPTION);
        corruptionLabel.setLabelFor(corruptionField);

        corruptionField.setInputVerifier(new RangeVerifier(0, 100));

        JPanel corruptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        corruptionPanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorConstants.TITLE_CORRUPTION_PANEL));
        corruptionPanel.add(corruptionLabel);
        corruptionPanel.add(corruptionField);

        return corruptionPanel;
    }

    private String getIpProtocol() {
        if (udpButton.isSelected()) {
            return NetworkEmulatorConstants.UDP_PROTOCOL;
        }
        if (icmpButton.isSelected()) {
            return NetworkEmulatorConstants.ICMP_PROTOCOL;
        }

        return NetworkEmulatorConstants.TCP_PROTOCOL;
    }

    private void setIpProtocol(String protocol) {
        switch (protocol) {
            case NetworkEmulatorConstants.UDP_PROTOCOL ->
                udpButton.setSelected(true);
            case NetworkEmulatorConstants.ICMP_PROTOCOL ->
                icmpButton.setSelected(true);
            default ->
                tcpButton.setSelected(true);
        }

    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setComponentsEnabled(this, enabled);
    }

    private void setComponentsEnabled(Container container, boolean enabled) {
        for (Component component : container.getComponents()) {
            component.setEnabled(enabled);
            if (component instanceof Container nested) {
                setComponentsEnabled(nested, enabled);
            }
        }
    }

    public void modifyTestElement(TestElement te) {
        CollectionProperty filter = new CollectionProperty(PROPERTY_FILTER + getName(), new ArrayList<>());
        filter.addItem(getIpProtocol());
        filter.addItem(srcAddressField.getText());
        filter.addItem((String) srcSubnetMaskBox.getSelectedItem());
        filter.addItem(srcPortField.getText());
        filter.addItem(dstAddressField.getText());
        filter.addItem((String) dstSubnetMaskBox.getSelectedItem());
        filter.addItem(dstPortField.getText());
        te.setProperty(filter);

        CollectionProperty parameters = new CollectionProperty(PROPERTY_PARAMETERS + getName(), new ArrayList<>());
        parameters.addItem(delayValueField.getText());
        parameters.addItem(jitterField.getText());
        parameters.addItem(delayCorrelationField.getText());
        parameters.addItem((String) distributionsBox.getSelectedItem());
        parameters.addItem(lossValueField.getText());
        parameters.addItem(lossCorrelationField.getText());
        parameters.addItem(rateField.getText());
        parameters.addItem(reorderingValueField.getText());
        parameters.addItem(reorderingCorrelationField.getText());
        parameters.addItem(duplicationValueField.getText());
        parameters.addItem(duplicationCorrelationField.getText());
        parameters.addItem(corruptionField.getText());
        te.setProperty(parameters);
    }

    void configure(TestElement te) {
        JMeterProperty filterPropery = (CollectionProperty) te.getProperty(PROPERTY_FILTER + getName());
        if (filterPropery instanceof CollectionProperty filter) {
            setIpProtocol(filter.get(0).getStringValue());
            srcAddressField.setText(filter.get(1).getStringValue());
            srcSubnetMaskBox.setSelectedItem(filter.get(2).getStringValue());
            srcPortField.setText(filter.get(3).getStringValue());
            dstAddressField.setText(filter.get(4).getStringValue());
            dstSubnetMaskBox.setSelectedItem(filter.get(5).getStringValue());
            dstPortField.setText(filter.get(6).getStringValue());
        }

        JMeterProperty parametersProperty = te.getPropertyOrNull(PROPERTY_PARAMETERS + getName());
        if (parametersProperty instanceof CollectionProperty parameters) {
            delayValueField.setText(parameters.get(0).getStringValue());
            jitterField.setText(parameters.get(1).getStringValue());
            delayCorrelationField.setText(parameters.get(2).getStringValue());
            distributionsBox.setSelectedItem(parameters.get(3).getStringValue());
            lossValueField.setText(parameters.get(4).getStringValue());
            lossCorrelationField.setText(parameters.get(5).getStringValue());
            rateField.setText(parameters.get(6).getStringValue());
            reorderingValueField.setText(parameters.get(7).getStringValue());
            reorderingCorrelationField.setText(parameters.get(8).getStringValue());
            duplicationValueField.setText(parameters.get(9).getStringValue());
            duplicationCorrelationField.setText(parameters.get(10).getStringValue());
            corruptionField.setText(parameters.get(11).getStringValue());
        }
    }
}
