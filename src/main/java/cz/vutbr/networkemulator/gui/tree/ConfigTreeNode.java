package cz.vutbr.networkemulator.gui.tree;

import javax.swing.tree.DefaultMutableTreeNode;

public class ConfigTreeNode extends DefaultMutableTreeNode {

    private String name;

    public ConfigTreeNode(Object userObject, boolean allowsChildren) {
        super(userObject, allowsChildren);
    }

    public ConfigTreeNode(Object userObject) {
        super(userObject, true);
    }

    public ConfigTreeNode() {
        super();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
