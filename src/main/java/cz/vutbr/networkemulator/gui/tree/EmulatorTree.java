package cz.vutbr.networkemulator.gui.tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import cz.vutbr.networkemulator.gui.EmulationRulePanel;

public class EmulatorTree extends JTree {

    public EmulatorTree(TreeModel model) {
        super(model);
    }

    public List<String> getExpandedPaths() {
        List<String> expanded = new ArrayList<>();
        EmulatorTreeNode rootNode = (EmulatorTreeNode) getModel().getRoot();
        TreePath rootPath = new TreePath(rootNode);

        if (isExpanded(rootPath)) {
            expanded.add(rootNode.getName());
        }

        int niCount = treeModel.getChildCount(rootNode);
        for (int i = 0; i < niCount; i++) {
            Object networkInterface = treeModel.getChild(rootNode, i);
            TreePath niPath = rootPath.pathByAddingChild(networkInterface);

            if (isExpanded(niPath)) {
                expanded.add(networkInterface.toString());
            }

            int classCount = treeModel.getChildCount(networkInterface);
            for (int j = 0; j < classCount; j++) {
                Object emulationRule = treeModel.getChild(networkInterface, j);
                TreePath classPath = niPath.pathByAddingChild(emulationRule);

                if (isExpanded(classPath)) {
                    expanded.add(networkInterface.toString() + "/" + emulationRule.toString());
                }
            }
        }

        return expanded;
    }

    public void expandPaths(List<String> pathsToExpand) {
        EmulatorTreeNode rootNode = (EmulatorTreeNode) getModel().getRoot();
        TreePath rootPath = new TreePath(rootNode);

        if (pathsToExpand.contains(rootNode.getName())) {
            expandPath(rootPath);
        }

        int niCount = treeModel.getChildCount(rootNode);
        for (int i = 0; i < niCount; i++) {
            Object ni = treeModel.getChild(rootNode, i);
            TreePath niPath = rootPath.pathByAddingChild(ni);

            if (pathsToExpand.contains(ni.toString())) {
                expandPath(niPath);
            }

            int tcCount = treeModel.getChildCount(ni);
            for (int j = 0; j < tcCount; j++) {
                Object tc = treeModel.getChild(ni, j);
                String pathStr = ni.toString() + "/" + tc.toString();

                if (pathsToExpand.contains(pathStr)) {
                    TreePath tcPath = niPath.pathByAddingChild(tc);
                    expandPath(tcPath);
                }
            }
        }
    }

    public String getSelectedPath() {
        TreePath selectedPath = getSelectionPath();
        Object[] pathComponents = selectedPath.getPath();
        StringBuilder pathBuilder = new StringBuilder();
        for (Object pathComponent : pathComponents) {
            EmulatorTreeNode node = (EmulatorTreeNode) pathComponent;
            pathBuilder.append(node.getName());
            pathBuilder.append("/");
        }

        return pathBuilder.toString();
    }

    public void selectPath(String pathToSelect) {
        EmulatorTreeNode rootNode = (EmulatorTreeNode) getModel().getRoot();
        String[] pathComponents = pathToSelect.split("/");

        TreePath path = new TreePath(rootNode);
        EmulatorTreeNode currentNode = rootNode;

        for (String pathComponent : pathComponents) {
            for (int i = 0; i < currentNode.getChildCount(); i++) {
                EmulatorTreeNode child = (EmulatorTreeNode) currentNode.getChildAt(i);
                if (pathComponent.equals(child.getName())) {
                    path = path.pathByAddingChild(child);
                    currentNode = child;
                    break;
                }
            }
        }

        setSelectionPath(path);
        scrollPathToVisible(path);
    }

    public Set<EmulationRulePanel> getTcPanels() {
        Set<EmulationRulePanel> tcPanels = new HashSet<>();
        EmulatorTreeNode rootNode = (EmulatorTreeNode) getModel().getRoot();
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            EmulatorTreeNode niNode = (EmulatorTreeNode) rootNode.getChildAt(i);
            for (int j = 0; j < niNode.getChildCount(); j++) {
                tcPanels.add((EmulationRulePanel) ((EmulatorTreeNode) niNode.getChildAt(j)).getUserObject());
            }
        }

        return tcPanels;
    }

}
