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
    private final String PROPERTY_EXPANDED_PATHS = "expanded_paths";
    private final String PROPERTY_SELECTED_PATH = "selected_path";

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
        defaultRootPanel = new DefaultRootPanel(DEFAULT_ROOT_PANEL);
        rootNode.setUserObject(defaultRootPanel);
        rightPanel.add(defaultRootPanel, defaultRootPanel.getName());
        networkInterfacePanel = new NetworkInterfacePanel(NETWORK_INTERFACE_PANEL);
        rightPanel.add(networkInterfacePanel, networkInterfacePanel.getName());

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
        int newTcNodeNumber = 1;
        if (niNode.getChildCount() > 0) {
            newTcNodeNumber = Integer.parseInt(((ConfigTreeNode) niNode.getLastChild()).getName().substring(2)) + 1;
        }
        String tcName = String.format("1:%s", newTcNodeNumber);

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
                rightPanelCards.show(rightPanel, rootPanel.getName());
            }
            case NetworkInterfacePanel niPanel -> {
                buttonPanelCards.show(buttonPanel, BTN_ADD);
                niPanel.update(name);
                rightPanelCards.show(rightPanel, niPanel.getName());
            }
            case TrafficClassPanel tcPanel -> {
                buttonPanelCards.show(buttonPanel, BTN_REMOVE);
                rightPanelCards.show(rightPanel, tcPanel.getName());
            }
            default -> {
            }
        }
    }

    private List<String> getExpandedPaths(JTree tree) {
        List<String> expanded = new ArrayList<>();
        TreePath rootPath = new TreePath(rootNode);

        if (tree.isExpanded(rootPath)) {
            expanded.add(rootNode.getName());
        }

        int interfaceCount = treeModel.getChildCount(rootNode);
        for (int i = 0; i < interfaceCount; i++) {
            Object iface = treeModel.getChild(rootNode, i);
            TreePath ifacePath = rootPath.pathByAddingChild(iface);

            if (tree.isExpanded(ifacePath)) {
                expanded.add(iface.toString());
            }

            int classCount = treeModel.getChildCount(iface);
            for (int j = 0; j < classCount; j++) {
                Object trafficClass = treeModel.getChild(iface, j);
                TreePath classPath = ifacePath.pathByAddingChild(trafficClass);

                if (tree.isExpanded(classPath)) {
                    expanded.add(iface.toString() + "/" + trafficClass.toString());
                }
            }
        }
        return expanded;
    }

    private void restoreExpandedPaths(JTree tree, List<String> pathsToExpand) {
        TreePath rootPath = new TreePath(rootNode);

        if (pathsToExpand.contains(rootNode.getName())) {
            tree.expandPath(rootPath);
        }

        int interfaceCount = treeModel.getChildCount(rootNode);
        for (int i = 0; i < interfaceCount; i++) {
            Object iface = treeModel.getChild(rootNode, i);
            TreePath ifacePath = rootPath.pathByAddingChild(iface);

            if (pathsToExpand.contains(iface.toString())) {
                tree.expandPath(ifacePath);
            }

            int classCount = treeModel.getChildCount(iface);
            for (int j = 0; j < classCount; j++) {
                Object trafficClass = treeModel.getChild(iface, j);
                String pathStr = iface.toString() + "/" + trafficClass.toString();

                if (pathsToExpand.contains(pathStr)) {
                    TreePath classPath = ifacePath.pathByAddingChild(trafficClass);
                    tree.expandPath(classPath);
                }
            }
        }
    }

    private String getSelectedPath() {
        TreePath selectedPath = tree.getSelectionPath();
        Object[] pathComponents = selectedPath.getPath();
        StringBuilder pathBuilder = new StringBuilder();
        for (Object pathComponent : pathComponents) {
            ConfigTreeNode node = (ConfigTreeNode) pathComponent;
            pathBuilder.append(node.getName());
            pathBuilder.append("/");
        }

        return pathBuilder.toString();
    }

    private void selectPath(String path) {
        String[] pathComponents = path.split("/");

        TreePath pathToSelect = new TreePath(rootNode);
        ConfigTreeNode currentNode = rootNode;

        for (String pathComponent : pathComponents) {
            for (int i = 0; i < currentNode.getChildCount(); i++) {
                ConfigTreeNode child = (ConfigTreeNode) currentNode.getChildAt(i);
                if (pathComponent.equals(child.getName())) {
                    pathToSelect = pathToSelect.pathByAddingChild(child);
                    currentNode = child;
                    break;
                }
            }
        }

        tree.setSelectionPath(pathToSelect);
        tree.scrollPathToVisible(pathToSelect);
    }

    public void modifyTestElement(TestElement te) {
        CollectionProperty niNames = new CollectionProperty(PROPERTY_NETWORK_INTERFACES, new ArrayList<>());

        for (int i = 0; i < rootNode.getChildCount(); i++) {
            ConfigTreeNode niNode = (ConfigTreeNode) rootNode.getChildAt(i);
            String niName = niNode.getName();
            niNames.addItem(niName);

            CollectionProperty tcNames = new CollectionProperty(PROPERTY_TRAFFIC_CLASSES + niName, new ArrayList<>());
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

        List<String> expanded = getExpandedPaths(tree);
        CollectionProperty expandedPaths = new CollectionProperty(PROPERTY_EXPANDED_PATHS, new ArrayList<>());
        for (String path : expanded) {
            expandedPaths.addItem(path);
        }
        te.setProperty(expandedPaths);

        String selectedPath = getSelectedPath();
        te.setProperty(PROPERTY_SELECTED_PATH, selectedPath);
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

        rightPanel.add(defaultRootPanel, defaultRootPanel.getName());
        rightPanel.add(networkInterfacePanel, networkInterfacePanel.getName());

        treeModel.reload();

        List<String> expPaths = new ArrayList<>();
        JMeterProperty expandedPathsProp = te.getProperty(PROPERTY_EXPANDED_PATHS);
        if (expandedPathsProp instanceof CollectionProperty expandedPaths) {
            for (int i = 0; i < expandedPaths.size(); i++) {
                expPaths.add(expandedPaths.get(i).getStringValue());
            }
        }
        restoreExpandedPaths(tree, expPaths);

        JMeterProperty selectedPathProp = te.getProperty(PROPERTY_SELECTED_PATH);
        selectPath(selectedPathProp.getStringValue());
    }

    public void collectSettings() {
        controller.clearNetworkInterfaces();
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
