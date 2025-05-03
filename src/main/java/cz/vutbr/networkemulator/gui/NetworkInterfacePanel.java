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
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import cz.vutbr.networkemulator.controller.NetworkEmulatorController;
import cz.vutbr.networkemulator.model.filter.Filter;
import cz.vutbr.networkemulator.model.parameters.Parameter;
import cz.vutbr.networkemulator.utils.NetworkEmulatorUtils;

public class NetworkInterfacePanel extends JPanel {

    private final NetworkEmulatorController controller;
    private final JPanel contentPanel;

    public NetworkInterfacePanel(String name) {
        setName(name);
        controller = NetworkEmulatorController.getInstance();
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 5, 5, 5));

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollPane.setPreferredSize(new Dimension(700, 100));

        add(scrollPane, BorderLayout.CENTER);
    }

    public void update(String niName) {
        contentPanel.removeAll();

        List<String> classIds = controller.getEmulationRule(niName);
        classIds.forEach(classId -> {
            Filter filter = controller.getFilter(niName, classId);
            List<Parameter> parameters = controller.getParameters(niName, classId);
            if (filter != null) {
                JLabel label = new JLabel(NetworkEmulatorUtils.getString("label_emulation_rule") + " " + classId);
                label.setAlignmentX(Component.LEFT_ALIGNMENT);
                contentPanel.add(label);

                JTable emulationRuleTable = new JTable(buildTableModel(filter, parameters));
                emulationRuleTable.setFillsViewportHeight(true);
                emulationRuleTable.setAlignmentX(Component.LEFT_ALIGNMENT);

                JPanel emulationRulePanel = new JPanel(new BorderLayout());
                emulationRulePanel.add(emulationRuleTable, BorderLayout.CENTER);
                emulationRulePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                emulationRulePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, emulationRuleTable.getPreferredSize().height));

                contentPanel.add(emulationRulePanel);
                contentPanel.add(Box.createVerticalStrut(10));
            }
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private DefaultTableModel buildTableModel(Filter filter, List<Parameter> parameters) {
        DefaultTableModel tableModel = new DefaultTableModel(new Object[] { "", "" }, 0);

        appendFilterRows(tableModel, filter);
        appendParameterRows(tableModel, parameters);

        return tableModel;
    }

    private void appendFilterRows(DefaultTableModel tableModel, Filter filter) {
        filter.appendToTable(tableModel);
    }

    private void appendParameterRows(DefaultTableModel tableModel, List<Parameter> parameters) {
        for (Parameter parameter : parameters) {
            parameter.appendToTable(tableModel);
        }
    }
}
