package cz.vutbr.networkemulator.gui;
import javax.swing.*;

import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jorphan.gui.JLabeledTextField;

import java.awt.*;

public class NetworkInterfaceConfigGui extends JPanel {

    private String name;

    JButton button;
    JLabel label;

    private JLabeledTextField srcAddress;
    private JLabeledTextField srcPort;
    private JLabeledTextField dstAddress;
    private JLabeledTextField dstPort;
    private JLabeledTextField delay;
    private JLabeledTextField drop;

    private JLabeledTextField rate;
    private JLabeledTextField loss;
    private JLabeledTextField reordering;
    private JLabeledTextField duplication;
    private JLabeledTextField corruption;

    private JLabeledTextField delayCorrelation;
    private JLabeledTextField dropCorrelation;
    private JLabeledTextField duplicationCorrelation;
    private JLabeledTextField reorderingCorrelation;
    private JLabeledTextField jitter;

    public String getName() {
        return name;
    }

    public NetworkInterfaceConfigGui(String name) {
        super();
        this.name = String.format("Interface '%s'", name);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Set zero gap between panels
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
        JPanel filterPanel = new JPanel(new BorderLayout());
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter"));
    
        dstAddress = new JLabeledTextField("Destination Address:", 20);
        dstPort = new JLabeledTextField("Destination Port:", 7);
        srcAddress = new JLabeledTextField("Source Address:", 20);
        srcPort = new JLabeledTextField("Source Port:", 7);
    
        JPanel addressPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        addressPanel.add(dstAddress);
        addressPanel.add(dstPort);
        addressPanel.add(srcAddress);
        addressPanel.add(srcPort);
    
        JPanel protocolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel protocolsLabel = new JLabel("Protocol:");
        String[] protocols = {"ICMP", "DNS", "FTP"};
        JComboBox<String> protocolsBox = new JComboBox<>(protocols);
        protocolsLabel.setLabelFor(protocolsBox);
        protocolPanel.add(protocolsLabel);
        protocolPanel.add(protocolsBox);
    
        JPanel fieldsPanel = new JPanel(new BorderLayout());
        fieldsPanel.add(addressPanel, BorderLayout.CENTER);
        fieldsPanel.add(protocolPanel, BorderLayout.EAST);
    
        filterPanel.add(fieldsPanel, BorderLayout.CENTER);
        return filterPanel;
    }
    
    
    private JPanel getDelayPanel() {
        delay = new JLabeledTextField("Delay (ms):", 7);
        jitter = new JLabeledTextField("Jitter (ms):", 7);
        delayCorrelation = new JLabeledTextField("Correlation (%):", 7);
        JPanel delayPanel = new HorizontalPanel();
        delayPanel.setBorder(BorderFactory.createTitledBorder("Delay"));
        delayPanel.add(delay);
        delayPanel.add(jitter);
        delayPanel.add(delayCorrelation);
        return delayPanel;
    }

    private JPanel getDropPanel() {
        drop = new JLabeledTextField("Packet Drop (%)", 7);
        dropCorrelation = new JLabeledTextField("Correlation (%)", 7);
        JPanel dropPanel = new HorizontalPanel();
        dropPanel.setBorder(BorderFactory.createTitledBorder("Packet Drop"));
        dropPanel.add(drop);
        dropPanel.add(dropCorrelation);
        return dropPanel;
    }

    private JPanel getRatePanel() {
        rate = new JLabeledTextField("Rate (bps):", 7);
        JPanel ratePanel = new HorizontalPanel();
        ratePanel.setBorder(BorderFactory.createTitledBorder("Rate"));
        ratePanel.add(rate);
        return ratePanel;
    }

    private JPanel getLossPanel() {
        loss = new JLabeledTextField("Loss (%):", 7);
        JPanel lossPanel = new HorizontalPanel();
        lossPanel.setBorder(BorderFactory.createTitledBorder("Loss"));
        lossPanel.add(loss);
        return lossPanel;
    }

    private JPanel getReorderingPanel() {
        reordering = new JLabeledTextField("Reordering (%):", 7);
        reorderingCorrelation = new JLabeledTextField("Correlation (%):", 7);
        JPanel reorderingPanel = new HorizontalPanel();
        reorderingPanel.setBorder(BorderFactory.createTitledBorder("Reordering"));
        reorderingPanel.add(reordering);
        reorderingPanel.add(reorderingCorrelation);
        return reorderingPanel;
    }

    private JPanel getDuplicationPanel() {
        duplication = new JLabeledTextField("Duplication (%):", 7);
        duplicationCorrelation = new JLabeledTextField("Correlation (%):", 7);
        JPanel duplicationPanel = new HorizontalPanel();
        duplicationPanel.setBorder(BorderFactory.createTitledBorder("Duplication"));
        duplicationPanel.add(duplication);
        duplicationPanel.add(duplicationCorrelation);
        return duplicationPanel;
    }

    private JPanel getCorruptionPanel() {
        corruption = new JLabeledTextField("Corruption (%):", 7);
        JPanel corruptionPanel = new HorizontalPanel();
        corruptionPanel.setBorder(BorderFactory.createTitledBorder("Corruption:"));
        corruptionPanel.add(corruption);
        return corruptionPanel;
    }

}