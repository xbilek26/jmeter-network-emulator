package cz.vutbr.networkemulator.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.jmeter.gui.util.PowerTableModel;

import cz.vutbr.networkemulator.controller.NetworkEmulatorController;
import cz.vutbr.networkemulator.model.NetworkParameters;
import cz.vutbr.networkemulator.utils.NetworkEmulatorConstants;

public class NetworkInterfacePanel extends JPanel {

    private final NetworkEmulatorController controller;
    private final JPanel contentPanel;
    private final String SPACE = " ";

    public NetworkInterfacePanel(String name) {
        setName(name);
        controller = NetworkEmulatorController.getInstance();
        setLayout(new BorderLayout());

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollPane.setPreferredSize(new Dimension(700, 400));

        add(scrollPane, BorderLayout.CENTER);
    }

    public void update(String niName) {
        contentPanel.removeAll();

        List<String> tcNames = controller.getTrafficClasses(niName);
        tcNames.forEach(tcName -> {
            NetworkParameters params = controller.getNetworkParameters(niName, tcName);
            if (params != null) {
                JLabel label = new JLabel("Traffic Class: " + tcName);
                label.setAlignmentX(Component.LEFT_ALIGNMENT);
                contentPanel.add(label);

                JTable table = new JTable(buildTableModel(params));
                table.setFillsViewportHeight(true);
                table.setAlignmentX(Component.LEFT_ALIGNMENT);

                JPanel tableContainer = new JPanel(new BorderLayout());
                tableContainer.add(table, BorderLayout.CENTER);
                tableContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
                tableContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, table.getPreferredSize().height));

                contentPanel.add(tableContainer);
                contentPanel.add(Box.createVerticalStrut(10));
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private PowerTableModel buildTableModel(NetworkParameters params) {
        PowerTableModel tableModel = new PowerTableModel(
                new String[]{NetworkEmulatorConstants.PARAMETER, NetworkEmulatorConstants.VALUE},
                new Class[]{String.class, String.class}
        );

        if (params.isIpProtocolSet()) {
            tableModel.addRow(new Object[]{
                NetworkEmulatorConstants.IP_PROTOCOL,
                params.getIpProtocol()
            });
        }
        if (params.isSrcAddressSet()) {
            tableModel.addRow(new Object[]{
                NetworkEmulatorConstants.SRC_ADDRESS,
                params.getSrcAddress() + params.getSrcSubnetMask()
            });
        }
        if (params.isSrcPortSet()) {
            tableModel.addRow(new Object[]{
                NetworkEmulatorConstants.SRC_PORT,
                params.getSrcPort()
            });
        }
        if (params.isDstAddressSet()) {
            tableModel.addRow(new Object[]{
                NetworkEmulatorConstants.DST_ADDRESS,
                params.getDstAddress() + params.getDstSubnetMask()
            });
        }
        if (params.isDstPortSet()) {
            tableModel.addRow(new Object[]{
                NetworkEmulatorConstants.DST_PORT,
                params.getDstPort()
            });
        }
        if (params.isDelayValueSet()) {
            tableModel.addRow(new Object[]{
                NetworkEmulatorConstants.DELAY_VALUE,
                params.getDelayValue() + SPACE + NetworkEmulatorConstants.DELAY_UNIT
            });
        }
        if (params.isJitterSet()) {
            tableModel.addRow(new Object[]{
                NetworkEmulatorConstants.JITTER,
                params.getJitter() + SPACE + NetworkEmulatorConstants.JITTER_UNIT
            });
        }
        if (params.isDelayCorrelationSet()) {
            tableModel.addRow(new Object[]{
                NetworkEmulatorConstants.DELAY_CORRELATION,
                params.getDelayCorrelation() + SPACE + NetworkEmulatorConstants.PERCENT
            });
        }
        if (params.isDistributionSet()) {
            tableModel.addRow(new Object[]{
                NetworkEmulatorConstants.DISTRIBUTION,
                params.getDistribution()
            });
        }
        if (params.isLossValueSet()) {
            tableModel.addRow(new Object[]{
                NetworkEmulatorConstants.LOSS_VALUE,
                params.getLossValue() + SPACE + NetworkEmulatorConstants.PERCENT
            });
        }
        if (params.isLossCorrelationSet()) {
            tableModel.addRow(new Object[]{
                NetworkEmulatorConstants.LOSS_CORRELATION,
                params.getLossCorrelation() + SPACE + NetworkEmulatorConstants.PERCENT
            });
        }
        if (params.isRateSet()) {
            tableModel.addRow(new Object[]{
                NetworkEmulatorConstants.RATE,
                params.getRate() + SPACE + NetworkEmulatorConstants.RATE_UNIT
            });
        }
        if (params.isReorderingValueSet()) {
            tableModel.addRow(new Object[]{
                NetworkEmulatorConstants.REORDERING_VALUE,
                params.getReorderingValue() + SPACE + NetworkEmulatorConstants.PERCENT
            });
        }
        if (params.isReorderingCorrelationSet()) {
            tableModel.addRow(new Object[]{
                NetworkEmulatorConstants.REORDERING_CORRELATION,
                params.getReorderingCorrelation() + SPACE + NetworkEmulatorConstants.PERCENT
            });
        }
        if (params.isDuplicationValueSet()) {
            tableModel.addRow(new Object[]{
                NetworkEmulatorConstants.DUPLICATION_VALUE,
                params.getDuplicationValue() + SPACE + NetworkEmulatorConstants.PERCENT
            });
        }
        if (params.isDuplicationCorrelationSet()) {
            tableModel.addRow(new Object[]{
                NetworkEmulatorConstants.DUPLICATION_CORRELATION,
                params.getDuplicationCorrelation() + SPACE + NetworkEmulatorConstants.PERCENT
            });
        }
        if (params.isCorruptionSet()) {
            tableModel.addRow(new Object[]{
                NetworkEmulatorConstants.CORRUPTION,
                params.getCorruption() + SPACE + NetworkEmulatorConstants.PERCENT
            });
        }

        return tableModel;
    }
}
