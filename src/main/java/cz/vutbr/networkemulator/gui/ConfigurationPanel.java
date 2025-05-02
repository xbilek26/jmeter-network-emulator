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
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.controller.NetworkEmulatorController;
import cz.vutbr.networkemulator.gui.tree.ConfigTree;
import cz.vutbr.networkemulator.gui.tree.ConfigTreeNode;
import cz.vutbr.networkemulator.gui.tree.ConfigTreeNodeRenderer;
import cz.vutbr.networkemulator.utils.NetworkEmulatorConverter;
import cz.vutbr.networkemulator.utils.NetworkEmulatorUtils;

public class ConfigurationPanel extends JPanel {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(ConfigurationPanel.class);

    private final String PROPERTY_NETWORK_INTERFACES = "network_interfaces";
    private final String PROPERTY_TRAFFIC_CLASSES = "traffic_classes_";
    private final String PROPERTY_EXPANDED_PATHS = "expanded_paths";
    private final String PROPERTY_SELECTED_PATH = "selected_path";

    private final String BTN_REFRESH = "refresh";
    private final String BTN_ADD = "add";
    private final String BTN_REMOVE = "remove";

    private final String DEFAULT_ROOT_PANEL = "default_root_panel";
    private final String NETWORK_INTERFACE_PANEL = "network_interface_panel";

    private ConfigTreeNode rootNode;
    private DefaultTreeModel treeModel;
    private ConfigTree tree;

    private JPanel buttonPanel;
    private JButton btnRefresh;
    private JButton btnAdd;
    private JButton btnRemove;

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
        splitPane.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorUtils.getString("title_configuration")));
        splitPane.setOneTouchExpandable(true);
        add(splitPane);
    }

    private JPanel createLeftPanel() {
        rootNode = new ConfigTreeNode();
        rootNode.setName(NetworkEmulatorUtils.getString("root_node_name"));
        treeModel = new DefaultTreeModel(rootNode);
        tree = new ConfigTree(treeModel);
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setCellRenderer(new ConfigTreeNodeRenderer());
        tree.addTreeSelectionListener(this::updateView);

        btnRefresh = new JButton(NetworkEmulatorUtils.getString("button_refresh"));
        btnRefresh.addActionListener(_ -> refreshNetworkInterfaces());
        btnAdd = new JButton(NetworkEmulatorUtils.getString("button_add"));
        btnAdd.addActionListener(_ -> addTrafficClass());
        btnRemove = new JButton(NetworkEmulatorUtils.getString("button_remove"));
        btnRemove.addActionListener(_ -> removeTrafficClass());

        buttonPanel = new JPanel(new CardLayout());
        buttonPanel.add(btnRefresh, BTN_REFRESH);
        buttonPanel.add(btnAdd, BTN_ADD);
        buttonPanel.add(btnRemove, BTN_REMOVE);

        JScrollPane treeScrollPane = new JScrollPane(tree);
        treeScrollPane.setPreferredSize(new Dimension(200, 0));

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

        JScrollPane niScrollPane = new JScrollPane(networkInterfacePanel);
        niScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        niScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        niScrollPane.setPreferredSize(new Dimension(rightPanel.getWidth(), rightPanel.getHeight()));
        rightPanel.add(niScrollPane, networkInterfacePanel.getName());

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
        int newTcNodeNumber = 10;
        if (niNode.getChildCount() > 0) {
            newTcNodeNumber = Integer.parseInt(((ConfigTreeNode) niNode.getLastChild()).getName().substring(2)) + 1;
        }
        String classId = String.format("1:%s", newTcNodeNumber);

        TrafficClassPanel tcPanel = new TrafficClassPanel(niName, classId);
        JScrollPane tcScrollPane = new JScrollPane(tcPanel);
        tcScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        tcScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tcScrollPane.setPreferredSize(new Dimension(rightPanel.getWidth(), rightPanel.getHeight()));
        rightPanel.add(tcScrollPane, tcPanel.getName());

        SwingUtilities.invokeLater(() -> {
            rightPanel.revalidate();
            rightPanel.repaint();
        });

        ConfigTreeNode tcNode = new ConfigTreeNode(false);
        tcNode.setUserObject(tcPanel);
        tcNode.setName(classId);
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
        SwingUtilities.invokeLater(() -> {
            rightPanel.revalidate();
            rightPanel.repaint();
        });

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
                rightPanelCards.show(rightPanel, rootPanel.getName());
                rootPanel.update();
            }
            case NetworkInterfacePanel niPanel -> {
                applySettings();
                buttonPanelCards.show(buttonPanel, BTN_ADD);
                rightPanelCards.show(rightPanel, niPanel.getName());
                niPanel.update(name);
            }
            case TrafficClassPanel tcPanel -> {
                buttonPanelCards.show(buttonPanel, BTN_REMOVE);
                rightPanelCards.show(rightPanel, tcPanel.getName());
            }
            default -> {
            }
        }
    }

    public void applySettings() {
        controller.clearNetworkInterfaces();
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            ConfigTreeNode niNode = (ConfigTreeNode) rootNode.getChildAt(i);
            String niName = niNode.getName();
            controller.addNetworkInterface(niName);

            for (int j = 0; j < niNode.getChildCount(); j++) {
                ConfigTreeNode tcNode = (ConfigTreeNode) niNode.getChildAt(j);
                String classId = tcNode.getName();

                controller.addTrafficClass(niName, classId);

                TrafficClassPanel tcPanel = (TrafficClassPanel) tcNode.getUserObject();
                controller.setFilter(niName, classId, tcPanel.getFilter());
                controller.setParameters(niName, classId, tcPanel.getParameters());
            }
        }
    }

    public void setEditable(boolean enabled) {
        btnRefresh.setEnabled(enabled);
        btnAdd.setEnabled(enabled);
        btnRemove.setEnabled(enabled);
        tree.getTcPanels().stream().forEach(tcPanel -> tcPanel.setEnabled(enabled));
    }

    public void modifyTestElement(TestElement te) {

        CollectionProperty niNames = new CollectionProperty(PROPERTY_NETWORK_INTERFACES, new ArrayList<>());

        for (int i = 0; i < rootNode.getChildCount(); i++) {
            ConfigTreeNode niNode = (ConfigTreeNode) rootNode.getChildAt(i);
            String niName = niNode.getName();
            niNames.addItem(niName);

            CollectionProperty classIds = new CollectionProperty(PROPERTY_TRAFFIC_CLASSES + niName, new ArrayList<>());
            for (int j = 0; j < niNode.getChildCount(); j++) {
                ConfigTreeNode tcNode = (ConfigTreeNode) niNode.getChildAt(j);
                TrafficClassPanel tcPanel = (TrafficClassPanel) tcNode.getUserObject();
                String classId = tcNode.getName();
                classIds.addItem(classId);

                tcPanel.modifyTestElement(te);
            }
            te.setProperty(classIds);
        }
        te.setProperty(niNames);

        CollectionProperty expandedPaths = new CollectionProperty(PROPERTY_EXPANDED_PATHS, tree.getExpandedPaths());
        te.setProperty(expandedPaths);

        StringProperty selectedPath = new StringProperty(PROPERTY_SELECTED_PATH, tree.getSelectedPath());
        te.setProperty(selectedPath);
    }

    public void configure(TestElement te) {
        rootNode.removeAllChildren();
        rightPanel.removeAll();

        JMeterProperty niNamesProperty = te.getProperty(PROPERTY_NETWORK_INTERFACES);
        if (niNamesProperty instanceof CollectionProperty niNames) {
            for (int i = 0; i < niNames.size(); i++) {
                String niName = niNames.get(i).getStringValue();
                ConfigTreeNode niNode = new ConfigTreeNode();
                niNode.setUserObject(networkInterfacePanel);
                niNode.setName(niName);
                rootNode.add(niNode);

                JMeterProperty classIdsPropery = te.getProperty(PROPERTY_TRAFFIC_CLASSES + niName);
                if (classIdsPropery instanceof CollectionProperty classIds) {
                    for (int j = 0; j < classIds.size(); j++) {
                        String classId = classIds.get(j).getStringValue();
                        TrafficClassPanel tcPanel = new TrafficClassPanel(niName, classId);
                        JScrollPane tcScrollPane = new JScrollPane(tcPanel);
                        tcScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                        tcScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                        tcScrollPane.setPreferredSize(new Dimension(rightPanel.getWidth(), rightPanel.getHeight()));
                        rightPanel.add(tcScrollPane, tcPanel.getName());
                        ConfigTreeNode tcNode = new ConfigTreeNode(false);
                        tcNode.setUserObject(tcPanel);
                        tcNode.setName(classId);
                        niNode.add(tcNode);

                        tcPanel.configure(te);
                    }

                }
            }
        }

        rightPanel.add(defaultRootPanel, defaultRootPanel.getName());
        rightPanel.add(networkInterfacePanel, networkInterfacePanel.getName());

        SwingUtilities.invokeLater(() -> {
            rightPanel.revalidate();
            rightPanel.repaint();
        });

        treeModel.reload();

        CollectionProperty expandedPaths = (CollectionProperty) te.getPropertyOrNull(PROPERTY_EXPANDED_PATHS);
        tree.expandPaths(NetworkEmulatorConverter.convertToList(expandedPaths));

        StringProperty selectedPath = (StringProperty) te.getPropertyOrNull(PROPERTY_SELECTED_PATH);
        tree.selectPath(NetworkEmulatorConverter.convertToString(selectedPath));

        applySettings();
    }
}
