package cz.vutbr.networkemulator.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import cz.vutbr.networkemulator.controller.NetworkEmulatorController;
import cz.vutbr.networkemulator.model.NetworkParameters;

public class NetworkInterfacePanel extends JPanel {

    private final NetworkEmulatorController controller;
    private final JPanel contentPanel;

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
                table.setEnabled(false);
                table.setAlignmentX(Component.LEFT_ALIGNMENT);

                JPanel tableContainer = new JPanel(new BorderLayout());
                tableContainer.add(table.getTableHeader(), BorderLayout.NORTH);
                tableContainer.add(table, BorderLayout.CENTER);
                tableContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
                tableContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, table.getPreferredSize().height + table.getTableHeader().getPreferredSize().height));

                contentPanel.add(tableContainer);
                contentPanel.add(Box.createVerticalStrut(10));
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private DefaultTableModel buildTableModel(NetworkParameters params) {
        String[] columnNames = {"Parameter", "Value"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        Map<String, Integer> setParams = params.getSetParameters();
        setParams.forEach((key, value) -> model.addRow(new Object[]{key, value}));

        return model;
    }
}
