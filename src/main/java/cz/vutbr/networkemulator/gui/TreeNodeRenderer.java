package cz.vutbr.networkemulator.gui;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class TreeNodeRenderer extends DefaultTreeCellRenderer {

    private final String interfaceIconPath = "/cz/vutbr/networkemulator/images/network_interface.gif";
    private final String trafficClassIconPath = "/cz/vutbr/networkemulator/images/traffic_class.gif";

    private final ImageIcon interfaceIcon = new ImageIcon(TreeNodeRenderer.class.getResource(interfaceIconPath));
    private final ImageIcon trafficClassIcon = new ImageIcon(TreeNodeRenderer.class.getResource(trafficClassIconPath));

    public TreeNodeRenderer() {
        super();
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
            boolean leaf, int row, boolean focus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, focus);
        Object obj = ((DefaultMutableTreeNode) value).getUserObject();
        if (obj instanceof String string) {
            if (string.contains(":")) {
                this.setIcon(trafficClassIcon);
            } else if (string.equals("Network Interfaces")) {
                this.setIcon(null);
            } else {
                this.setIcon(interfaceIcon);
            }
        }
        return this;
    }
}
