package cz.vutbr.networkemulator.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.HashMap;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.model.utils.NetworkEmulatorConstants;

public class ConfigurationPanel extends JPanel {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(ConfigurationPanel.class);

    private DefaultMutableTreeNode rootNode;
    private DefaultTreeModel treeModel;
    private JTree tree;

    private Map<String, DefaultMutableTreeNode> interfacesMap = new HashMap<>();

    private JPanel buttonPanelCards;
    private JPanel rightPanelCards;

    public ConfigurationPanel() {
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JSplitPane splitPane;
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createLeftPanel(), createRightPanel());
        splitPane.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorConstants.TITLE_CONFIG));
        splitPane.setOneTouchExpandable(true);
        add(splitPane);
    }

    private JPanel createLeftPanel() {
        rootNode = new DefaultMutableTreeNode("Network Interfaces");
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

        buttonPanelCards = new JPanel(new CardLayout());
        buttonPanelCards.add(btnRefreshNetworkInterfaces, "refresh_network_interfaces_card");
        buttonPanelCards.add(btnAddTrafficClass, "add_traffic_class_card");
        buttonPanelCards.add(btnRemoveTrafficClass, "remove_traffic_class_card");
        updateButtonPanel();

        JScrollPane treeScrollPane = new JScrollPane(tree);
        treeScrollPane.setPreferredSize(new Dimension(150, 0));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(treeScrollPane, BorderLayout.CENTER);
        leftPanel.add(buttonPanelCards, BorderLayout.SOUTH);

        return leftPanel;
    }

    private JPanel createRightPanel() {
        JPanel parametersPanelCard = new ParametersPanel();
        JPanel networkInterfaceStatePanelCard = new InterfaceStatePanel();

        rightPanelCards = new JPanel(new CardLayout());
        rightPanelCards.add(parametersPanelCard, "parameters_panel_card");
        rightPanelCards.add(networkInterfaceStatePanelCard, "network_interface_state_panel_card");
        rightPanelCards.add(new JPanel(), "default_empty_card"); // for now
        updateRightPanel();

        return rightPanelCards;
    }

    private void refreshInterfaces() {
        rootNode.removeAllChildren();
        interfacesMap.clear();
        addNetworkInterface("ens33");
        addNetworkInterface("eth0");

        treeModel.reload();
    }

    private void addNetworkInterface(String name) {
        DefaultMutableTreeNode networkInterfaceNode = new DefaultMutableTreeNode(name);
        rootNode.add(networkInterfaceNode);
        treeModel.reload();
    }

    private void addTrafficClass() {
        TreePath path = tree.getSelectionPath();
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        String trafficClassName = String.format("1:%s", selectedNode.getChildCount() + 1);
        if (trafficClassName != null && !trafficClassName.trim().isEmpty()) {
            DefaultMutableTreeNode trafficClassNode = new DefaultMutableTreeNode(trafficClassName);
            selectedNode.add(trafficClassNode);
            treeModel.reload();
            tree.setSelectionPath(new TreePath(treeModel.getPathToRoot(trafficClassNode)));
        }
    }

    private void removeTrafficClass() {
        TreePath path = tree.getSelectionPath();
        if (path == null)
            return;

        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (selectedNode.getParent() != rootNode) {
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode.getParent();
            parent.remove(selectedNode);
            treeModel.reload();
        }
    }

    private void updateButtonPanel() {
        CardLayout cardLayout = (CardLayout) buttonPanelCards.getLayout();
        TreePath path = tree.getSelectionPath();
        if (path == null) {
            cardLayout.show(buttonPanelCards, "refresh_network_interfaces_card");
            return;
        }
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (selectedNode.getParent() == rootNode) {
            cardLayout.show(buttonPanelCards, "add_traffic_class_card");
        } else if (selectedNode.getParent() == null) {
            cardLayout.show(buttonPanelCards, "refresh_network_interfaces_card");
        } else {
            cardLayout.show(buttonPanelCards, "remove_traffic_class_card");
        }
    }

    private void updateRightPanel() {
        CardLayout cardLayout = (CardLayout) rightPanelCards.getLayout();
        TreePath path = tree.getSelectionPath();
        if (path == null) {
            cardLayout.show(rightPanelCards, "default_empty_card");
            return;
        }
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (selectedNode.getParent() == rootNode) {
            cardLayout.show(rightPanelCards, "network_interface_state_panel_card");
        } else if (selectedNode.getParent() == null) {
            cardLayout.show(rightPanelCards, "default_empty_card");
        } else {
            cardLayout.show(rightPanelCards, "parameters_panel_card");
        }
    }

    public void modifyTestElement(TestElement testElement) {
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            DefaultMutableTreeNode interfaceNode = (DefaultMutableTreeNode) rootNode.getChildAt(i);
            String interfaceName = interfaceNode.getUserObject().toString();

            StringBuilder trafficClasses = new StringBuilder();
            for (int j = 0; j < interfaceNode.getChildCount(); j++) {
                DefaultMutableTreeNode trafficNode = (DefaultMutableTreeNode) interfaceNode.getChildAt(j);
                trafficClasses.append(trafficNode.getUserObject().toString()).append(";");
            }

            testElement.setProperty("interface_" + i, interfaceName);
            testElement.setProperty("traffic_classes_" + i, trafficClasses.toString());
        }
    }

    public void configure(TestElement testElement) {
        rootNode.removeAllChildren();
        interfacesMap.clear();

        int index = 0;
        while (true) {
            String interfaceName = testElement.getPropertyAsString("interface_" + index);
            if (interfaceName == null || interfaceName.isEmpty()) {
                break;
            }

            DefaultMutableTreeNode interfaceNode = new DefaultMutableTreeNode(interfaceName);
            rootNode.add(interfaceNode);
            interfacesMap.put(interfaceName, interfaceNode);

            String trafficClassesStr = testElement.getPropertyAsString("traffic_classes_" + index);
            if (trafficClassesStr != null && !trafficClassesStr.isEmpty()) {
                String[] trafficClasses = trafficClassesStr.split(";");
                for (String trafficClass : trafficClasses) {
                    interfaceNode.add(new DefaultMutableTreeNode(trafficClass));
                }
            }

            index++;
        }

        treeModel.reload();
    }

}
