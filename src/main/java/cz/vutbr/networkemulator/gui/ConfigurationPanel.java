package cz.vutbr.networkemulator.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.controller.NetworkEmulatorController;
import cz.vutbr.networkemulator.gui.tree.ConfigTreeNode;
import cz.vutbr.networkemulator.gui.tree.TreeNodeRenderer;
import cz.vutbr.networkemulator.model.NetworkParameters;
import cz.vutbr.networkemulator.utils.NetworkEmulatorConstants;
import cz.vutbr.networkemulator.utils.NetworkEmulatorConverter;

public class ConfigurationPanel extends JPanel {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(ConfigurationPanel.class);

    private final String PROPERTY_NETWORK_INTERFACES = "network_interfaces";
    private final String PROPERTY_TRAFFIC_CLASSES = "traffic_classes_";
    private final String PROPERTY_NETWORK_PARAMETERS = "network_parameters_";

    private final String BTN_REFRESH = "refresh";
    private final String BTN_ADD = "add";
    private final String BTN_REMOVE = "remove";

    private final String DEFAULT_ROOT_PANEL = "default_root_panel";
    private final String NETWORK_INTERFACE_PANEL = "network_interface_panel";

    private ConfigTreeNode rootNode;
    private DefaultTreeModel treeModel;
    private JTree tree;

    private JPanel buttonPanel;
    private JPanel rightPanel;

    private DefaultRootPanel defaultRootPanel;
    private NetworkInterfacePanel networkInterfacePanel;

    private final NetworkEmulatorController controller;

    public ConfigurationPanel() {
        controller = NetworkEmulatorController.getInstance();
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
        rootNode = new ConfigTreeNode();
        rootNode.setName(NetworkEmulatorConstants.ROOT_NODE_NAME);
        treeModel = new DefaultTreeModel(rootNode);
        tree = new JTree(treeModel);
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setCellRenderer(new TreeNodeRenderer());
        tree.addTreeSelectionListener(this::updateView);

        JButton btnRefresh = new JButton(NetworkEmulatorConstants.BTN_REFRESH_INTERFACES);
        btnRefresh.addActionListener(_ -> refreshNetworkInterfaces());
        JButton btnAdd = new JButton(NetworkEmulatorConstants.BTN_ADD_TRAFFIC_CLASS);
        btnAdd.addActionListener(_ -> addTrafficClass());
        JButton btnRemove = new JButton(NetworkEmulatorConstants.BTN_REMOVE_TRAFFIC_CLASS);
        btnRemove.addActionListener(_ -> removeTrafficClass());

        buttonPanel = new JPanel(new CardLayout());
        buttonPanel.add(btnRefresh, BTN_REFRESH);
        buttonPanel.add(btnAdd, BTN_ADD);
        buttonPanel.add(btnRemove, BTN_REMOVE);

        JScrollPane treeScrollPane = new JScrollPane(tree);
        treeScrollPane.setPreferredSize(new Dimension(150, 0));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(treeScrollPane, BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        return leftPanel;
    }

    private JPanel createRightPanel() {
        rightPanel = new JPanel(new CardLayout());
        defaultRootPanel = new DefaultRootPanel();
        rootNode.setUserObject(defaultRootPanel);
        rightPanel.add(defaultRootPanel, DEFAULT_ROOT_PANEL);
        networkInterfacePanel = new NetworkInterfacePanel();
        rightPanel.add(networkInterfacePanel, NETWORK_INTERFACE_PANEL);

        return rightPanel;
    }

    private void refreshNetworkInterfaces() {
        Set<String> currentNetworkInterfaces = new HashSet<>();
        List<ConfigTreeNode> nodesToRemove = new ArrayList<>();

        controller.refreshNetworkInterfaces();
        Set<String> phyNetworkInterfaces = controller.getNetworkInterfaces();

        for (int i = 0; i < rootNode.getChildCount(); i++) {
            ConfigTreeNode niNode = (ConfigTreeNode) rootNode.getChildAt(i);
            String niName = niNode.getName();
            currentNetworkInterfaces.add(niName);
            if (!phyNetworkInterfaces.contains(niName)) {
                nodesToRemove.add(niNode);
            }
        }
        nodesToRemove.forEach(rootNode::remove);

        Set<String> toAdd = new HashSet<>(phyNetworkInterfaces);
        toAdd.removeAll(currentNetworkInterfaces);
        toAdd.stream()
                .map(niName -> {
                    ConfigTreeNode niNode = new ConfigTreeNode();
                    niNode.setUserObject(networkInterfacePanel);
                    niNode.setName(niName);
                    return niNode;
                })
                .forEach(rootNode::add);

        treeModel.reload();
        tree.setSelectionPath(new TreePath(rootNode));
    }

    private void addTrafficClass() {
        Enumeration<TreePath> expandedEnum = tree.getExpandedDescendants(new TreePath(rootNode));
        List<TreePath> expandedPaths = Collections.list(expandedEnum);

        TreePath path = tree.getSelectionPath();
        if (path == null) {
            return;
        }

        ConfigTreeNode niNode = (ConfigTreeNode) path.getLastPathComponent();
        String niName = niNode.getName();
        String tcName = String.format("1:%s", niNode.getChildCount() + 1);

        TrafficClassPanel tcPanel = new TrafficClassPanel(niName, tcName);
        rightPanel.add(tcPanel, tcPanel.getName());

        ConfigTreeNode tcNode = new ConfigTreeNode(false);
        tcNode.setUserObject(tcPanel);
        tcNode.setName(tcName);
        niNode.add(tcNode);

        treeModel.reload();
        expandedPaths.stream().forEach(tree::expandPath);
        tree.setSelectionPath(new TreePath(treeModel.getPathToRoot(tcNode)));
    }

    private void removeTrafficClass() {
        Enumeration<TreePath> expandedEnum = tree.getExpandedDescendants(new TreePath(tree.getModel().getRoot()));
        List<TreePath> expandedPaths = Collections.list(expandedEnum);

        TreePath path = tree.getSelectionPath();
        if (path == null) {
            return;
        }

        ConfigTreeNode tcNode = (ConfigTreeNode) path.getLastPathComponent();
        ConfigTreeNode niNode = (ConfigTreeNode) tcNode.getParent();
        ConfigTreeNode tcNodePrev = (ConfigTreeNode) tcNode.getPreviousNode();

        niNode.remove(tcNode);

        TrafficClassPanel tcPanel = (TrafficClassPanel) tcNode.getUserObject();
        rightPanel.remove(tcPanel);
        rightPanel.revalidate();
        rightPanel.repaint();

        treeModel.reload();
        expandedPaths.stream().forEach(tree::expandPath);
        tree.setSelectionPath(new TreePath(treeModel.getPathToRoot(tcNodePrev)));
    }

    private void updateView(TreeSelectionEvent tse) {
        CardLayout buttonPanelCards = (CardLayout) buttonPanel.getLayout();
        CardLayout rightPanelCards = (CardLayout) rightPanel.getLayout();

        TreePath path = tse.getNewLeadSelectionPath();
        if (path == null) {
            return;
        }

        Object object = ((ConfigTreeNode) path.getLastPathComponent()).getUserObject();
        String name = ((ConfigTreeNode) path.getLastPathComponent()).getName();

        switch (object) {
            case DefaultRootPanel rootPanel -> {
                buttonPanelCards.show(buttonPanel, BTN_REFRESH);
                rootPanel.update();
                rightPanelCards.show(rightPanel, DEFAULT_ROOT_PANEL);
            }
            case NetworkInterfacePanel niPanel -> {
                buttonPanelCards.show(buttonPanel, BTN_ADD);
                niPanel.update(name);
                rightPanelCards.show(rightPanel, NETWORK_INTERFACE_PANEL);
            }
            case TrafficClassPanel tcPanel -> {
                buttonPanelCards.show(buttonPanel, BTN_REMOVE);
                rightPanelCards.show(rightPanel, tcPanel.getName());
            }
            default -> {
            }
        }
    }

    public void modifyTestElement(TestElement te) {
        CollectionProperty niNames = new CollectionProperty();
        niNames.setName(PROPERTY_NETWORK_INTERFACES);
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            ConfigTreeNode niNode = (ConfigTreeNode) rootNode.getChildAt(i);
            String niName = niNode.getName();
            niNames.addItem(niName);

            CollectionProperty tcNames = new CollectionProperty();
            tcNames.setName(PROPERTY_TRAFFIC_CLASSES + niNode.getName());
            for (int j = 0; j < niNode.getChildCount(); j++) {
                ConfigTreeNode tcNode = (ConfigTreeNode) niNode.getChildAt(j);
                TrafficClassPanel tcPanel = (TrafficClassPanel) tcNode.getUserObject();
                String tcName = tcNode.getName();
                tcNames.addItem(tcName);

                CollectionProperty parameters = tcPanel.getNetworkParameters();
                parameters.setName(PROPERTY_NETWORK_PARAMETERS + tcPanel.getName());
                te.setProperty(parameters);
            }
            te.setProperty(tcNames);
        }
        te.setProperty(niNames);
    }

    public void configure(TestElement te) {
        rootNode.removeAllChildren();
        rightPanel.removeAll();

        JMeterProperty niNamesProp = te.getProperty(PROPERTY_NETWORK_INTERFACES);
        if (niNamesProp instanceof CollectionProperty niNames) {
            for (int i = 0; i < niNames.size(); i++) {
                String niName = niNames.get(i).getStringValue();
                ConfigTreeNode niNode = new ConfigTreeNode();
                niNode.setUserObject(networkInterfacePanel);
                niNode.setName(niName);
                rootNode.add(niNode);

                JMeterProperty tcNamesProp = te.getProperty(PROPERTY_TRAFFIC_CLASSES + niName);
                if (tcNamesProp instanceof CollectionProperty tcNames) {
                    for (int j = 0; j < tcNames.size(); j++) {
                        String tcName = tcNames.get(j).getStringValue();
                        TrafficClassPanel tcPanel = new TrafficClassPanel(niName, tcName);
                        rightPanel.add(tcPanel, tcPanel.getName());
                        ConfigTreeNode tcNode = new ConfigTreeNode(false);
                        tcNode.setUserObject(tcPanel);
                        tcNode.setName(tcName);
                        niNode.add(tcNode);

                        JMeterProperty parametersProp = te.getProperty(PROPERTY_NETWORK_PARAMETERS + tcPanel.getName());
                        if (parametersProp instanceof CollectionProperty parameters) {
                            tcPanel.setNetworkParameters(parameters);
                        }
                    }
                }
            }
        }
        rightPanel.add(defaultRootPanel, DEFAULT_ROOT_PANEL);
        rightPanel.add(networkInterfacePanel, NETWORK_INTERFACE_PANEL);
        treeModel.reload();
        tree.setSelectionPath(new TreePath(rootNode));
    }

    public void collectSettings() {
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            ConfigTreeNode niNode = (ConfigTreeNode) rootNode.getChildAt(i);
            String niName = niNode.getName();
            controller.addNetworkInterface(niName);

            for (int j = 0; j < niNode.getChildCount(); j++) {
                ConfigTreeNode tcNode = (ConfigTreeNode) niNode.getChildAt(j);
                String tcName = tcNode.getName();

                controller.addTrafficClass(niName, tcName);

                TrafficClassPanel tcPanel = (TrafficClassPanel) tcNode.getUserObject();
                CollectionProperty propertyParameters = tcPanel.getNetworkParameters();
                NetworkParameters networkParameters = NetworkEmulatorConverter.convertToNetworkParameters(propertyParameters);
                controller.setNetworkParameters(niName, tcName, networkParameters);
            }
        }
    }
}
