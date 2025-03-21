package cz.vutbr.networkemulator.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.controller.NetworkEmulatorController;
import cz.vutbr.networkemulator.model.NetworkParameters;
import cz.vutbr.networkemulator.utils.NetworkEmulatorConstants;
import cz.vutbr.networkemulator.utils.NetworkEmulatorConverter;

public class ConfigurationPanel extends JPanel {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(ConfigurationPanel.class);

    private DefaultMutableTreeNode rootNode;
    private DefaultTreeModel treeModel;
    private JTree tree;

    private JPanel buttonPanel;
    private JPanel rightPanel;
    private Map<String, TrafficClassPanel> tcPanels = new HashMap<>();

    private NetworkEmulatorController controller;

    public ConfigurationPanel(NetworkEmulatorController networkEmulatorController) {
        this.controller = networkEmulatorController;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JSplitPane splitPane;
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createLeftPanel(), createRightPanel());
        splitPane.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorConstants.TITLE_CONFIG_PANEL));
        splitPane.setOneTouchExpandable(true);
        add(splitPane);
    }

    private JPanel createLeftPanel() {
        rootNode = new DefaultMutableTreeNode(NetworkEmulatorConstants.ROOT_NODE_NAME);
        treeModel = new DefaultTreeModel(rootNode);
        tree = new JTree(treeModel);
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(e -> updateRightPanel());
        tree.addTreeSelectionListener(e -> updateButtonPanel());

        JButton btnRefreshNetworkInterfaces = new JButton(NetworkEmulatorConstants.BTN_REFRESH_INTERFACES);
        btnRefreshNetworkInterfaces.addActionListener(e -> refreshInterfaces());
        JButton btnAddTrafficClass = new JButton(NetworkEmulatorConstants.BTN_ADD_TRAFFIC_CLASS);
        btnAddTrafficClass.addActionListener(e -> addTrafficClass());
        JButton btnRemoveTrafficClass = new JButton(NetworkEmulatorConstants.BTN_REMOVE_TRAFFIC_CLASS);
        btnRemoveTrafficClass.addActionListener(e -> removeTrafficClass());

        buttonPanel = new JPanel(new CardLayout());
        buttonPanel.add(btnRefreshNetworkInterfaces, "refresh");
        buttonPanel.add(btnAddTrafficClass, "add");
        buttonPanel.add(btnRemoveTrafficClass, "remove");
        updateButtonPanel();

        JScrollPane treeScrollPane = new JScrollPane(tree);
        treeScrollPane.setPreferredSize(new Dimension(150, 0));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(treeScrollPane, BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        return leftPanel;
    }

    private JPanel createRightPanel() {
        rightPanel = new JPanel(new CardLayout());

        NetworkInterfaceStatePanel networkInterfaceStatePanel = new NetworkInterfaceStatePanel();

        rightPanel.add(networkInterfaceStatePanel, "network_interface_state");
        rightPanel.add(new JPanel(), "default_empty"); // for now
        updateRightPanel();

        return rightPanel;
    }

    private void refreshInterfaces() {
        controller.refreshInterfaces();
        rootNode.removeAllChildren();
        rootNode.add(new DefaultMutableTreeNode("ens33"));
        rootNode.add(new DefaultMutableTreeNode("eth0"));

        treeModel.reload();
    }

    private void addTrafficClass() {
        Enumeration<TreePath> expandedEnum = tree.getExpandedDescendants(new TreePath(tree.getModel().getRoot()));
        List<TreePath> expandedPaths = Collections.list(expandedEnum);

        TreePath path = tree.getSelectionPath();
        if (path == null)
            return;

        DefaultMutableTreeNode interfaceNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        String interfaceName = interfaceNode.toString();
        String tcName = String.format("1:%s", interfaceNode.getChildCount() + 1);

        TrafficClassPanel tcPanel = new TrafficClassPanel();
        String tcPanelName = String.format("tc_%s_%s", interfaceName, tcName);
        rightPanel.add(tcPanel, tcPanelName);
        tcPanels.put(tcPanelName, tcPanel);

        DefaultMutableTreeNode tcNode = new DefaultMutableTreeNode(tcName);
        interfaceNode.add(tcNode);

        treeModel.reload();
        expandedPaths.stream().forEach(tree::expandPath);
        tree.setSelectionPath(new TreePath(treeModel.getPathToRoot(tcNode)));
    }

    private void removeTrafficClass() {
        Enumeration<TreePath> expandedEnum = tree.getExpandedDescendants(new TreePath(tree.getModel().getRoot()));
        List<TreePath> expandedPaths = Collections.list(expandedEnum);

        TreePath path = tree.getSelectionPath();
        if (path == null)
            return;

        DefaultMutableTreeNode tcNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        DefaultMutableTreeNode interfaceNode = (DefaultMutableTreeNode) tcNode.getParent();
        DefaultMutableTreeNode tcNodePrevious = tcNode.getPreviousNode();

        interfaceNode.remove(tcNode);

        String interfaceName = interfaceNode.toString();
        String tcName = tcNode.toString();
        String tcPanelName = String.format("tc_%s_%s", interfaceName, tcName);

        TrafficClassPanel panelToRemove = tcPanels.get(tcPanelName);
        if (panelToRemove != null) {
            rightPanel.remove(panelToRemove);
            tcPanels.remove(tcPanelName);
        }

        rightPanel.revalidate();
        rightPanel.repaint();

        treeModel.reload();
        expandedPaths.stream().forEach(tree::expandPath);
        tree.setSelectionPath(new TreePath(treeModel.getPathToRoot(tcNodePrevious)));
    }

    private void updateButtonPanel() {
        CardLayout cardLayout = (CardLayout) buttonPanel.getLayout();
        TreePath path = tree.getSelectionPath();

        // nothing is selected
        if (path == null) {
            cardLayout.show(buttonPanel, "refresh");
            return;
        }

        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (selectedNode.getParent() == rootNode) {
            cardLayout.show(buttonPanel, "add");
        } else if (selectedNode.getParent() == null) {
            cardLayout.show(buttonPanel, "refresh");
        } else {
            cardLayout.show(buttonPanel, "remove");
        }
    }

    private void updateRightPanel() {
        CardLayout cardLayout = (CardLayout) rightPanel.getLayout();
        TreePath path = tree.getSelectionPath();

        // nothing is selected
        if (path == null) {
            cardLayout.show(rightPanel, "default_empty");
            return;
        }

        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (selectedNode == rootNode) {
            cardLayout.show(rightPanel, "default_empty");
        } else if (selectedNode.getParent() == rootNode) {
            cardLayout.show(rightPanel, "network_interface_state");
        } else {
            DefaultMutableTreeNode interfaceNode = (DefaultMutableTreeNode) selectedNode.getParent();
            String networkInterfaceName = interfaceNode.getUserObject().toString();
            String tcName = selectedNode.getUserObject().toString();

            String tcPanelName = String.format("tc_%s_%s", networkInterfaceName, tcName);
            cardLayout.show(rightPanel, tcPanelName);
        }
    }

    public void modifyTestElement(TestElement testElement) {
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            DefaultMutableTreeNode interfaceNode = (DefaultMutableTreeNode) rootNode.getChildAt(i);
            String interfaceName = interfaceNode.getUserObject().toString();
            testElement.setProperty("interface_" + i, interfaceName);

            StringBuilder tcNames = new StringBuilder();
            for (int j = 0; j < interfaceNode.getChildCount(); j++) {
                DefaultMutableTreeNode tcNode = (DefaultMutableTreeNode) interfaceNode.getChildAt(j);
                String tcName = tcNode.getUserObject().toString();
                tcNames.append(tcName).append(",");

                String tcPanelName = String.format("tc_%s_%s", interfaceName, tcName);
                TrafficClassPanel tcPanel = tcPanels.get(tcPanelName);

                CollectionProperty parameters = tcPanel.getNetworkParameters();
                parameters.setName(tcPanelName);
                testElement.setProperty(parameters);
            }
            testElement.setProperty("classes_" + i, tcNames.toString());
        }
    }

    public void configure(TestElement testElement) {
        rootNode.removeAllChildren();
        tcPanels.clear();
        rightPanel.removeAll();

        int interfaceIndex = 0;
        while (true) {
            String interfaceName = testElement.getPropertyAsString("interface_" + interfaceIndex);
            if (interfaceName == null || interfaceName.isEmpty()) {
                break;
            }

            DefaultMutableTreeNode interfaceNode = new DefaultMutableTreeNode(interfaceName);
            rootNode.add(interfaceNode);

            String trafficClasses = testElement.getPropertyAsString("classes_" + interfaceIndex);
            if (trafficClasses != null && !trafficClasses.isEmpty()) {
                String[] tcNames = trafficClasses.split(",");
                for (String tcName : tcNames) {

                    DefaultMutableTreeNode tcNode = new DefaultMutableTreeNode(tcName);
                    interfaceNode.add(tcNode);

                    String tcPanelName = String.format("tc_%s_%s", interfaceName, tcName);
                    TrafficClassPanel tcPanel = tcPanels.computeIfAbsent(tcPanelName,
                            key -> new TrafficClassPanel());

                    JMeterProperty parameters = testElement.getProperty(tcPanelName);
                    if (parameters instanceof CollectionProperty) {
                        tcPanel.setNetworkParameters((CollectionProperty) parameters);
                    } else {
                        tcPanel.setNetworkParameters(new CollectionProperty());
                    }

                    rightPanel.add(tcPanel, tcPanelName);
                }
            }

            interfaceIndex++;
        }

        rightPanel.add(new NetworkInterfaceStatePanel(), "network_interface_state");
        rightPanel.add(new JPanel(), "default_empty");

        treeModel.reload();
        tree.setSelectionPath(new TreePath(rootNode));
    }

    public void collectAndApplySettings() {
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            DefaultMutableTreeNode interfaceNode = (DefaultMutableTreeNode) rootNode.getChildAt(i);
            String interfaceName = interfaceNode.getUserObject().toString();
            controller.addNetworkInterface(interfaceName);

            for (int j = 0; j < interfaceNode.getChildCount(); j++) {
                DefaultMutableTreeNode tcNode = (DefaultMutableTreeNode) interfaceNode.getChildAt(j);
                String tcName = tcNode.getUserObject().toString();

                controller.addTrafficClassToInterface(interfaceName, tcName);

                String tcPanelName = String.format("tc_%s_%s", interfaceName, tcName);
                TrafficClassPanel tcPanel = tcPanels.get(tcPanelName);

                CollectionProperty propertyParameters = tcPanel.getNetworkParameters();
                NetworkParameters networkParameters = NetworkEmulatorConverter.convertToNetworkParameters(propertyParameters);
                controller.setNetworkParameters(interfaceName, tcName, networkParameters);
            }
        }
    }

}