package cz.vutbr.networkemulator.gui.tree;

import javax.swing.tree.DefaultMutableTreeNode;

public class EmulatorTreeNode extends DefaultMutableTreeNode {

    private String name;

    public EmulatorTreeNode(Object userObject, boolean allowsChildren) {
        super(userObject, allowsChildren);
    }

    public EmulatorTreeNode(Object userObject) {
        super(userObject, true);
    }

    public EmulatorTreeNode(boolean allowsChildren) {
        super(null, allowsChildren);
    }

    public EmulatorTreeNode() {
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
