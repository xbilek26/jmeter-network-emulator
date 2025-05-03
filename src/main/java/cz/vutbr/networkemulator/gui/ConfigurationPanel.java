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
import cz.vutbr.networkemulator.gui.tree.EmulatorTree;
import cz.vutbr.networkemulator.gui.tree.EmulatorTreeNode;
import cz.vutbr.networkemulator.gui.tree.EmulatorTreeNodeRenderer;
import cz.vutbr.networkemulator.utils.EmulatorConverter;
import cz.vutbr.networkemulator.utils.EmulatorUtils;

public class ConfigurationPanel extends JPanel {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(ConfigurationPanel.class);

    private final String PROPERTY_NETWORK_INTERFACES = "network_interfaces";
    private final String PROPERTY_EMULATION_RULES = "emulation_rules_";
    private final String PROPERTY_EXPANDED_PATHS = "expanded_paths";
    private final String PROPERTY_SELECTED_PATH = "selected_path";

    private final String BTN_REFRESH = "refresh";
    private final String BTN_ADD = "add";
    private final String BTN_REMOVE = "remove";

    private final String ROOT_PANEL = "root_panel";
    private final String NETWORK_INTERFACE_PANEL = "network_interface_panel";

    private EmulatorTreeNode rootNode;
    private DefaultTreeModel treeModel;
    private EmulatorTree tree;

    private JPanel buttonPanel;
    private JButton btnRefresh;
    private JButton btnAdd;
    private JButton btnRemove;

    private JPanel rightPanel;

    private RootPanel rootPanel;
    private NetworkInterfacePanel networkInterfacePanel;

    private final NetworkEmulatorController controller;

    public ConfigurationPanel() {
        controller = NetworkEmulatorController.getInstance();
        init();
    }

    private void init() {
        setLayout(new BorderLayout());

        JSplitPane splitPane;
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createLeftPanel(), createRightPanel());
        splitPane.setBorder(BorderFactory.createTitledBorder(EmulatorUtils.getString("title_configuration")));
        splitPane.setOneTouchExpandable(true);
        add(splitPane);
    }

    private JPanel createLeftPanel() {
        rootNode = new EmulatorTreeNode();
        rootNode.setName(EmulatorUtils.getString("root_node_name"));
        treeModel = new DefaultTreeModel(rootNode);
        tree = new EmulatorTree(treeModel);
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setCellRenderer(new EmulatorTreeNodeRenderer());
        tree.addTreeSelectionListener(this::updateView);

        btnRefresh = new JButton(EmulatorUtils.getString("button_refresh"));
        btnRefresh.addActionListener(_ -> refreshNetworkInterfaces());
        btnAdd = new JButton(EmulatorUtils.getString("button_add"));
        btnAdd.addActionListener(_ -> addEmulationRule());
        btnRemove = new JButton(EmulatorUtils.getString("button_remove"));
        btnRemove.addActionListener(_ -> removeEmulationRule());

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
        rootPanel = new RootPanel(ROOT_PANEL);
        rootNode.setUserObject(rootPanel);
        rightPanel.add(rootPanel, rootPanel.getName());
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
        List<EmulatorTreeNode> nodesToRemove = new ArrayList<>();

        controller.refreshNetworkInterfaces();
        Set<String> phyNetworkInterfaces = controller.getNetworkInterfaces();

        for (int i = 0; i < rootNode.getChildCount(); i++) {
            EmulatorTreeNode niNode = (EmulatorTreeNode) rootNode.getChildAt(i);
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
                    EmulatorTreeNode niNode = new EmulatorTreeNode();
                    niNode.setUserObject(networkInterfacePanel);
                    niNode.setName(niName);
                    return niNode;
                })
                .forEach(rootNode::add);

        treeModel.reload();
        tree.setSelectionPath(new TreePath(rootNode));
    }

    private void addEmulationRule() {
        Enumeration<TreePath> expandedEnum = tree.getExpandedDescendants(new TreePath(rootNode));
        List<TreePath> expandedPaths = Collections.list(expandedEnum);

        TreePath path = tree.getSelectionPath();
        if (path == null) {
            return;
        }

        EmulatorTreeNode niNode = (EmulatorTreeNode) path.getLastPathComponent();
        String niName = niNode.getName();
        int newClassNumber = 10;
        if (niNode.getChildCount() > 0) {
            newClassNumber = Integer.parseInt(((EmulatorTreeNode) niNode.getLastChild()).getName().substring(2)) + 1;
        }
        String classId = String.format("1:%s", newClassNumber);

        EmulationRulePanel tcPanel = new EmulationRulePanel(niName, classId);
        JScrollPane tcScrollPane = new JScrollPane(tcPanel);
        tcScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        tcScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tcScrollPane.setPreferredSize(new Dimension(rightPanel.getWidth(), rightPanel.getHeight()));
        rightPanel.add(tcScrollPane, tcPanel.getName());

        SwingUtilities.invokeLater(() -> {
            rightPanel.revalidate();
            rightPanel.repaint();
        });

        EmulatorTreeNode ruleNode = new EmulatorTreeNode(false);
        ruleNode.setUserObject(tcPanel);
        ruleNode.setName(classId);
        niNode.add(ruleNode);

        treeModel.reload();
        expandedPaths.stream().forEach(tree::expandPath);
        tree.setSelectionPath(new TreePath(treeModel.getPathToRoot(ruleNode)));
    }

    private void removeEmulationRule() {
        Enumeration<TreePath> expandedEnum = tree.getExpandedDescendants(new TreePath(tree.getModel().getRoot()));
        List<TreePath> expandedPaths = Collections.list(expandedEnum);

        TreePath path = tree.getSelectionPath();
        if (path == null) {
            return;
        }

        EmulatorTreeNode ruleNode = (EmulatorTreeNode) path.getLastPathComponent();
        EmulatorTreeNode niNode = (EmulatorTreeNode) ruleNode.getParent();
        EmulatorTreeNode ruleNodePrev = (EmulatorTreeNode) ruleNode.getPreviousNode();

        niNode.remove(ruleNode);

        EmulationRulePanel tcPanel = (EmulationRulePanel) ruleNode.getUserObject();
        rightPanel.remove(tcPanel);
        SwingUtilities.invokeLater(() -> {
            rightPanel.revalidate();
            rightPanel.repaint();
        });

        treeModel.reload();
        expandedPaths.stream().forEach(tree::expandPath);
        tree.setSelectionPath(new TreePath(treeModel.getPathToRoot(ruleNodePrev)));
    }

    private void updateView(TreeSelectionEvent tse) {
        CardLayout buttonPanelCards = (CardLayout) buttonPanel.getLayout();
        CardLayout rightPanelCards = (CardLayout) rightPanel.getLayout();

        TreePath path = tse.getNewLeadSelectionPath();
        if (path == null) {
            return;
        }

        Object object = ((EmulatorTreeNode) path.getLastPathComponent()).getUserObject();
        String name = ((EmulatorTreeNode) path.getLastPathComponent()).getName();

        switch (object) {
            case RootPanel rtPanel -> {
                buttonPanelCards.show(buttonPanel, BTN_REFRESH);
                rightPanelCards.show(rightPanel, rtPanel.getName());
                rtPanel.update();
            }
            case NetworkInterfacePanel niPanel -> {
                applySettings();
                buttonPanelCards.show(buttonPanel, BTN_ADD);
                rightPanelCards.show(rightPanel, niPanel.getName());
                niPanel.update(name);
            }
            case EmulationRulePanel tcPanel -> {
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
            EmulatorTreeNode niNode = (EmulatorTreeNode) rootNode.getChildAt(i);
            String niName = niNode.getName();
            controller.addNetworkInterface(niName);

            for (int j = 0; j < niNode.getChildCount(); j++) {
                EmulatorTreeNode ruleNode = (EmulatorTreeNode) niNode.getChildAt(j);
                String classId = ruleNode.getName();

                controller.addEmulationRule(niName, classId);

                EmulationRulePanel tcPanel = (EmulationRulePanel) ruleNode.getUserObject();
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
            EmulatorTreeNode niNode = (EmulatorTreeNode) rootNode.getChildAt(i);
            String niName = niNode.getName();
            niNames.addItem(niName);

            CollectionProperty classIds = new CollectionProperty(PROPERTY_EMULATION_RULES + niName, new ArrayList<>());
            for (int j = 0; j < niNode.getChildCount(); j++) {
                EmulatorTreeNode ruleNode = (EmulatorTreeNode) niNode.getChildAt(j);
                EmulationRulePanel tcPanel = (EmulationRulePanel) ruleNode.getUserObject();
                String classId = ruleNode.getName();
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
                EmulatorTreeNode niNode = new EmulatorTreeNode();
                niNode.setUserObject(networkInterfacePanel);
                niNode.setName(niName);
                rootNode.add(niNode);

                JMeterProperty classIdsPropery = te.getProperty(PROPERTY_EMULATION_RULES + niName);
                if (classIdsPropery instanceof CollectionProperty classIds) {
                    for (int j = 0; j < classIds.size(); j++) {
                        String classId = classIds.get(j).getStringValue();
                        EmulationRulePanel tcPanel = new EmulationRulePanel(niName, classId);
                        JScrollPane tcScrollPane = new JScrollPane(tcPanel);
                        tcScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                        tcScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                        tcScrollPane.setPreferredSize(new Dimension(rightPanel.getWidth(), rightPanel.getHeight()));
                        rightPanel.add(tcScrollPane, tcPanel.getName());
                        EmulatorTreeNode ruleNode = new EmulatorTreeNode(false);
                        ruleNode.setUserObject(tcPanel);
                        ruleNode.setName(classId);
                        niNode.add(ruleNode);

                        tcPanel.configure(te);
                    }

                }
            }
        }

        rightPanel.add(rootPanel, rootPanel.getName());
        rightPanel.add(networkInterfacePanel, networkInterfacePanel.getName());

        SwingUtilities.invokeLater(() -> {
            rightPanel.revalidate();
            rightPanel.repaint();
        });

        treeModel.reload();

        CollectionProperty expandedPaths = (CollectionProperty) te.getPropertyOrNull(PROPERTY_EXPANDED_PATHS);
        tree.expandPaths(EmulatorConverter.convertToList(expandedPaths));

        StringProperty selectedPath = (StringProperty) te.getPropertyOrNull(PROPERTY_SELECTED_PATH);
        tree.selectPath(EmulatorConverter.convertToString(selectedPath));

        applySettings();
    }
}
