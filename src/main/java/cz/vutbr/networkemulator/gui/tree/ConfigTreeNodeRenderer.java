package cz.vutbr.networkemulator.gui.tree;

import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import cz.vutbr.networkemulator.gui.DefaultRootPanel;
import cz.vutbr.networkemulator.gui.NetworkInterfacePanel;
import cz.vutbr.networkemulator.gui.TrafficClassPanel;
import cz.vutbr.networkemulator.utils.NetworkEmulatorUtils;

public class ConfigTreeNodeRenderer extends DefaultTreeCellRenderer {

    private final ImageIcon networkInterfaceIcon;
    private final ImageIcon trafficClassIcon;

    public ConfigTreeNodeRenderer() {
        this.networkInterfaceIcon = NetworkEmulatorUtils.getImage("network_interface.gif");
        this.trafficClassIcon = NetworkEmulatorUtils.getImage("traffic_class.gif");
    }

    private ImageIcon getScaledIcon(ImageIcon icon) {
        Image image = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
            boolean leaf, int row, boolean focus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, focus);

        Object node = ((ConfigTreeNode) value).getUserObject();
        if (node instanceof DefaultRootPanel) {
            // this.setIcon();
        } else if (node instanceof TrafficClassPanel) {
            this.setIcon(getScaledIcon(trafficClassIcon));
        } else if (node instanceof NetworkInterfacePanel) {
            this.setIcon(getScaledIcon(networkInterfaceIcon));
        } else {
            this.setIcon(null);
        }

        return this;
    }
}
