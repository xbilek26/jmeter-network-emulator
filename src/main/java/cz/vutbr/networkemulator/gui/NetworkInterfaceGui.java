package cz.vutbr.networkemulator.gui;

import javax.swing.*;

import cz.vutbr.networkemulator.gui.util.ProtocolPortMapper;
import cz.vutbr.networkemulator.verification.*;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkInterfaceGui extends JPanel implements ItemListener {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(NetworkInterfaceGui.class);

    private String name;

    JButton button;
    JLabel label;

    private JTextField srcAddress;
    private JTextField srcPort;
    private JTextField dstAddress;
    private JTextField dstPort;
    private JTextField delayValue;
    private JTextField dropValue;

    private JTextField rateValue;
    private JTextField lossValue;
    private JTextField reorderingValue;
    private JTextField duplicationValue;
    private JTextField corruptionValue;

    private JTextField delayCorrelation;
    private JTextField dropCorrelation;
    private JTextField duplicationCorrelation;
    private JTextField reorderingCorrelation;
    private JTextField jitterValue;

    public String getName() {
        return name;
    }

    public NetworkInterfaceGui(String name) {
        super();
        this.name = String.format("%s", name);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel filterPanel = getFilterPanel();
        add(filterPanel);

        JPanel delayPanel = getDelayPanel();
        add(delayPanel);

        JPanel dropPanel = getDropPanel();
        add(dropPanel);

        JPanel ratePanel = getRatePanel();
        add(ratePanel);

        JPanel lossPanel = getLossPanel();
        add(lossPanel);

        JPanel reorderingPanel = getReorderingPanel();
        add(reorderingPanel);

        JPanel duplicationPanel = getDuplicationPanel();
        add(duplicationPanel);

        JPanel corruptionPanel = getCorruptionPanel();
        add(corruptionPanel);
    }

    private JPanel getFilterPanel() {
        dstAddress = new JTextField(20);
        dstPort = new JTextField(7);
        srcAddress = new JTextField(20);
        srcPort = new JTextField(7);

        JComboBox<String> protocolsBox = new JComboBox<>(ProtocolPortMapper.getProtocolsNames());
        protocolsBox.addItemListener(this);

        JLabel srcAddressLabel = new JLabel("Src Address:");
        srcAddressLabel.setLabelFor(srcAddress);
        JLabel srcPortLabel = new JLabel("Src Port:");
        srcPortLabel.setLabelFor(srcPort);
        JLabel dstAddressLabel = new JLabel("Dst Address:");
        dstAddressLabel.setLabelFor(dstAddress);
        JLabel dstPortLabel = new JLabel("Dst Port:");
        dstPortLabel.setLabelFor(dstPort);
        JLabel protocolLabel = new JLabel("Protocol:");
        protocolLabel.setLabelFor(protocolsBox);

        dstAddress.setInputVerifier(new IpAddressVerifier());
        dstPort.setInputVerifier(new RangeVerifier(0, 1024));
        srcAddress.setInputVerifier(new IpAddressVerifier());
        srcPort.setInputVerifier(new RangeVerifier(0, 1024));

        JPanel srcPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        srcPanel.add(srcAddressLabel);
        srcPanel.add(srcAddress);
        srcPanel.add(srcPortLabel);
        srcPanel.add(srcPort);

        JPanel dstPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dstPanel.add(dstAddressLabel);
        dstPanel.add(dstAddress);
        dstPanel.add(dstPortLabel);
        dstPanel.add(dstPort);
        dstPanel.add(protocolLabel);
        dstPanel.add(protocolsBox);

        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.PAGE_AXIS));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter"));
        filterPanel.add(srcPanel);
        filterPanel.add(dstPanel);

        return filterPanel;
    }

    private JPanel getDelayPanel() {
        delayValue = new JTextField(10);
        jitterValue = new JTextField(10);
        delayCorrelation = new JTextField(10);

        String[] distributions = { "Uniform", "Normal", "Pareto Normal" };
        JComboBox<String> distributionsBox = new JComboBox<>(distributions);

        JLabel delayValueLabel = new JLabel("Startup Delay (ms):");
        delayValueLabel.setLabelFor(delayValue);
        JLabel jitterValueLabel = new JLabel("Jitter (ms):");
        jitterValueLabel.setLabelFor(jitterValue);
        JLabel delayCorrelationLabel = new JLabel("Correlation (%):");
        delayCorrelationLabel.setLabelFor(delayCorrelation);
        JLabel distributionsLabel = new JLabel("Distribution:");
        distributionsLabel.setLabelFor(distributionsBox);

        delayValue.setInputVerifier(new RangeVerifier(0, 10000));
        jitterValue.setInputVerifier(new RangeVerifier(0, 10000));
        delayCorrelation.setInputVerifier(new RangeVerifier(0, 100));

        JPanel delayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        delayPanel.setBorder(BorderFactory.createTitledBorder("Delay"));
        delayPanel.add(delayValueLabel);
        delayPanel.add(delayValue);
        delayPanel.add(jitterValueLabel);
        delayPanel.add(jitterValue);
        delayPanel.add(delayCorrelationLabel);
        delayPanel.add(delayCorrelation);
        delayPanel.add(distributionsLabel);
        delayPanel.add(distributionsBox);

        return delayPanel;
    }

    private JPanel getDropPanel() {
        dropValue = new JTextField(10);
        dropCorrelation = new JTextField(10);

        JLabel dropValueLabel = new JLabel("Packet Drop (%):");
        dropValueLabel.setLabelFor(dropValue);
        JLabel dropCorrelationLabel = new JLabel("Correlation (%):");
        dropCorrelationLabel.setLabelFor(dropCorrelation);

        dropValue.setInputVerifier(new RangeVerifier(0, 100));
        dropCorrelation.setInputVerifier(new RangeVerifier(0, 100));

        JPanel dropPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dropPanel.setBorder(BorderFactory.createTitledBorder("Packet Drop"));
        dropPanel.add(dropValueLabel);
        dropPanel.add(dropValue);
        dropPanel.add(dropCorrelationLabel);
        dropPanel.add(dropCorrelation);

        return dropPanel;
    }

    private JPanel getRatePanel() {
        rateValue = new JTextField(10);

        JLabel rateLabel = new JLabel("Rate (bps):");
        rateLabel.setLabelFor(rateLabel);

        rateValue.setInputVerifier(new RangeVerifier(0, 100000));

        JPanel ratePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ratePanel.setBorder(BorderFactory.createTitledBorder("Rate"));
        ratePanel.add(rateLabel);
        ratePanel.add(rateValue);

        return ratePanel;
    }

    private JPanel getLossPanel() {
        lossValue = new JTextField(10);

        JLabel lossLabel = new JLabel("Loss (%):");
        lossLabel.setLabelFor(lossValue);

        lossValue.setInputVerifier(new RangeVerifier(0, 100));

        JPanel lossPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lossPanel.setBorder(BorderFactory.createTitledBorder("Loss"));
        lossPanel.add(lossLabel);
        lossPanel.add(lossValue);

        return lossPanel;
    }

    private JPanel getReorderingPanel() {
        reorderingValue = new JTextField(10);
        reorderingCorrelation = new JTextField(10);

        JLabel reorderingValueLabel = new JLabel("Reordering (%):");
        reorderingValueLabel.setLabelFor(reorderingValue);
        JLabel reorderingCorrelationLabel = new JLabel("Correlation (%):");
        reorderingCorrelationLabel.setLabelFor(reorderingCorrelation);

        reorderingValue.setInputVerifier(new RangeVerifier(0, 100));
        reorderingCorrelation.setInputVerifier(new RangeVerifier(0, 100));

        JPanel reorderingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        reorderingPanel.setBorder(BorderFactory.createTitledBorder("Reordering"));
        reorderingPanel.add(reorderingValueLabel);
        reorderingPanel.add(reorderingValue);
        reorderingPanel.add(reorderingCorrelationLabel);
        reorderingPanel.add(reorderingCorrelation);

        return reorderingPanel;
    }

    private JPanel getDuplicationPanel() {
        duplicationValue = new JTextField(10);
        duplicationCorrelation = new JTextField(10);

        JLabel duplicationValueLabel = new JLabel("Duplication (%):");
        duplicationValueLabel.setLabelFor(duplicationValue);
        JLabel duplicationCorrelationLabel = new JLabel("Correlation (%):");
        duplicationCorrelationLabel.setLabelFor(duplicationCorrelation);

        duplicationValue.setInputVerifier(new RangeVerifier(0, 100));
        duplicationCorrelation.setInputVerifier(new RangeVerifier(0, 100));

        JPanel duplicationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        duplicationPanel.setBorder(BorderFactory.createTitledBorder("Duplication"));
        duplicationPanel.add(duplicationValueLabel);
        duplicationPanel.add(duplicationValue);
        duplicationPanel.add(duplicationCorrelationLabel);
        duplicationPanel.add(duplicationCorrelation);

        return duplicationPanel;
    }

    private JPanel getCorruptionPanel() {
        corruptionValue = new JTextField(10);

        JLabel corruptionValueLabel = new JLabel("Corruption (%):");
        corruptionValueLabel.setLabelFor(corruptionValue);

        corruptionValue.setInputVerifier(new RangeVerifier(0, 100));

        JPanel corruptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        corruptionPanel.setBorder(BorderFactory.createTitledBorder("Corruption:"));
        corruptionPanel.add(corruptionValueLabel);
        corruptionPanel.add(corruptionValue);

        return corruptionPanel;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void itemStateChanged(ItemEvent item) {
        Object source = item.getSource();
        String value = "";

        if (source instanceof JComboBox) {
            value = (String) ((JComboBox) source).getSelectedItem();
        }

        Integer port = ProtocolPortMapper.getPort(value);
        if (port != null) {
            dstPort.setText(port.toString());
        } else {
            dstPort.setText("Invalid Protocol");
        }
    }

}