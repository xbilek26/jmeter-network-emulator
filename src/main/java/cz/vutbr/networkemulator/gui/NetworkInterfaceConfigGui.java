package cz.vutbr.networkemulator.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

public class NetworkInterfaceConfigGui extends JPanel implements ActionListener {

    private String name;

    JButton button;
    JLabel label;

    private JTextField delayField;
    private JTextField dropField;
    private JTextField rateField;
    private JTextField lossField;
    private JTextField reorderingField;
    private JTextField duplicationField;
    private JTextField corruptionField;
    private JTextField[] srcAddressFields;
    private JTextField[] destAddressFields;
    private JTextField srcPortField;
    private JTextField destPortField;
    private boolean running = false;

    public String getName() {
        return name;
    }

    public NetworkInterfaceConfigGui(String name) {
        super();
        this.name = String.format("Interface '%s'", name);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Set zero gap between panels
        //setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Source address configuration
        JPanel srcPanel = createPanel();
        JCheckBox srcCheck = new JCheckBox();
        srcPanel.add(srcCheck);
        srcPanel.add(new JLabel("Source Address (IPv4):"));
        srcAddressFields = createIPv4AddressFields(srcPanel);
        srcPortField = new JTextField(5);
        srcPortField.setEnabled(false);
        for (JTextField field : srcAddressFields) {
            field.setEnabled(false);
        }
        srcCheck.addItemListener(e -> {
            boolean enabled = e.getStateChange() == ItemEvent.SELECTED;
            for (JTextField field : srcAddressFields) {
                field.setEnabled(enabled);
            }
            srcPortField.setEnabled(enabled);
        });
        srcPanel.add(new JLabel(":"));
        srcPanel.add(srcPortField);
        add(srcPanel);

        // Destination address configuration
        JPanel destPanel = createPanel();
        JCheckBox destCheck = new JCheckBox();
        destPanel.add(destCheck);
        destPanel.add(new JLabel("Destination Address (IPv4):"));
        destAddressFields = createIPv4AddressFields(destPanel);
        destPanel.add(new JLabel(":"));
        destPortField = new JTextField(5);
        destPortField.setEnabled(false);
        for (JTextField field : destAddressFields) {
            field.setEnabled(false);
        }
        destCheck.addItemListener(e -> {
            boolean enabled = e.getStateChange() == ItemEvent.SELECTED;
            for (JTextField field : destAddressFields) {
                field.setEnabled(enabled);
            }
            destPortField.setEnabled(enabled);
        });
        destPanel.add(destPortField);
        add(destPanel);

        // Delay configuration
        JPanel delayPanel = createPanel();
        JCheckBox delayCheck = new JCheckBox();
        delayField = new JTextField(10);
        delayField.setEnabled(false);
        delayCheck.addItemListener(e -> delayField.setEnabled(e.getStateChange() == ItemEvent.SELECTED));
        delayPanel.add(delayCheck);
        delayPanel.add(new JLabel("Delay (ms):"));
        delayPanel.add(delayField);
        add(delayPanel);

        // Packet drop configuration
        JPanel dropPanel = createPanel();
        JCheckBox dropCheck = new JCheckBox();
        dropField = new JTextField(10);
        dropField.setEnabled(false);
        dropCheck.addItemListener(e -> dropField.setEnabled(e.getStateChange() == ItemEvent.SELECTED));
        dropPanel.add(dropCheck);
        dropPanel.add(new JLabel("Packet Drop (%):"));
        dropPanel.add(dropField);
        add(dropPanel);

        // Rate configuration
        JPanel ratePanel = createPanel();
        JCheckBox rateCheck = new JCheckBox();
        rateField = new JTextField(10);
        rateField.setEnabled(false);
        rateCheck.addItemListener(e -> rateField.setEnabled(e.getStateChange() == ItemEvent.SELECTED));
        ratePanel.add(rateCheck);
        ratePanel.add(new JLabel("Rate (bps):"));
        ratePanel.add(rateField);
        add(ratePanel);

        // Loss configuration
        JPanel lossPanel = createPanel();
        JCheckBox lossCheck = new JCheckBox();
        lossField = new JTextField(10);
        lossField.setEnabled(false);
        lossCheck.addItemListener(e -> lossField.setEnabled(e.getStateChange() == ItemEvent.SELECTED));
        lossPanel.add(lossCheck);
        lossPanel.add(new JLabel("Loss (%):"));
        lossPanel.add(lossField);
        add(lossPanel);

        // Reordering configuration
        JPanel reorderingPanel = createPanel();
        JCheckBox reorderingCheck = new JCheckBox();
        reorderingField = new JTextField(10);
        reorderingField.setEnabled(false);
        reorderingCheck.addItemListener(e -> reorderingField.setEnabled(e.getStateChange() == ItemEvent.SELECTED));
        reorderingPanel.add(reorderingCheck);
        reorderingPanel.add(new JLabel("Reordering (%):"));
        reorderingPanel.add(reorderingField);
        add(reorderingPanel);

        // Duplication configuration
        JPanel duplicationPanel = createPanel();
        JCheckBox duplicationCheck = new JCheckBox();
        duplicationField = new JTextField(10);
        duplicationField.setEnabled(false);
        duplicationCheck.addItemListener(e -> duplicationField.setEnabled(e.getStateChange() == ItemEvent.SELECTED));
        duplicationPanel.add(duplicationCheck);
        duplicationPanel.add(new JLabel("Duplication (%):"));
        duplicationPanel.add(duplicationField);
        add(duplicationPanel);

        // Corruption configuration
        JPanel corruptionPanel = createPanel();
        JCheckBox corruptionCheck = new JCheckBox();
        corruptionField = new JTextField(10);
        corruptionField.setEnabled(false);
        corruptionCheck.addItemListener(e -> corruptionField.setEnabled(e.getStateChange() == ItemEvent.SELECTED));
        corruptionPanel.add(corruptionCheck);
        corruptionPanel.add(new JLabel("Corruption (%):"));
        corruptionPanel.add(corruptionField);
        add(corruptionPanel);

        JLabel currentSettings = new JLabel();
        currentSettings.setText("Current settings...");
        add(currentSettings);

        button = new JButton("Start Emulation");
        button.addActionListener(this);
        button.setBackground(Color.RED);
        button.setForeground(Color.BLACK);
        button.setFocusable(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("Verdana", Font.BOLD, 18));

        label = new JLabel("Emulation is stopped.");
        label.setFont(new Font("Verdana", Font.BOLD, 15));

        add(button);
        add(label);

    }

    private JPanel createPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    private JTextField[] createIPv4AddressFields(JPanel panel) {
        JTextField[] fields = new JTextField[4];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new JTextField(3);
            panel.add(fields[i]);
            if (i < fields.length - 1) {
                panel.add(new JLabel("."));
            }
        }
        return fields;
    }

    public String getDelay() {
        return delayField.getText();
    }

    public String getDrop() {
        return dropField.getText();
    }

    public String getRate() {
        return rateField.getText();
    }

    public String getLoss() {
        return lossField.getText();
    }

    public String getReordering() {
        return reorderingField.getText();
    }

    public String getDuplication() {
        return duplicationField.getText();
    }

    public String getCorruption() {
        return corruptionField.getText();
    }

    public String getSourceAddress() {
        return getAddressFromFields(srcAddressFields);
    }

    public String getSourcePort() {
        return srcPortField.getText();
    }

    public String getDestinationAddress() {
        return getAddressFromFields(destAddressFields);
    }

    public String getDestinationPort() {
        return destPortField.getText();
    }

    private String getAddressFromFields(JTextField[] fields) {
        StringBuilder address = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            address.append(fields[i].getText());
            if (i < fields.length - 1) {
                address.append(".");
            }
        }
        return address.toString();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        running = !running;
        if (running) {
            button.setText("Stop Emulation");
            button.setBackground(new Color(0xFFAFAF));
            label.setText("Emulation is running!");
        } else {
            button.setText("Start Emulation");
            button.setBackground(new Color(0xB3F3B3));
            label.setText("Emulation is stopped.");
        }
    }
}
