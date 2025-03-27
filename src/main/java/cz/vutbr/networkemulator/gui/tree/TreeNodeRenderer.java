package cz.vutbr.networkemulator.gui.tree;

import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import cz.vutbr.networkemulator.gui.DefaultRootPanel;
import cz.vutbr.networkemulator.gui.NetworkInterfacePanel;
import cz.vutbr.networkemulator.gui.TrafficClassPanel;

public class TreeNodeRenderer extends DefaultTreeCellRenderer {

    private static final String INTERFACE_ICON_PATH = "/cz/vutbr/networkemulator/images/network_interface.gif";
    private static final String TRAFFIC_CLASS_ICON_PATH = "/cz/vutbr/networkemulator/images/traffic_class.gif";
    private static final int ICON_MARGIN = 3;

    private final ImageIcon interfaceIcon;
    private final ImageIcon trafficClassIcon;

    public TreeNodeRenderer() {
        this.interfaceIcon = loadIcon(INTERFACE_ICON_PATH);
        this.trafficClassIcon = loadIcon(TRAFFIC_CLASS_ICON_PATH);
    }

    private ImageIcon loadIcon(String path) {
        return new ImageIcon(TreeNodeRenderer.class.getResource(path));
    }

    private ImageIcon getScaledIcon(ImageIcon icon, int size) {
        int scaledSize = Math.max(size - ICON_MARGIN * 2, 1);
        Image image = icon.getImage().getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
            boolean leaf, int row, boolean focus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, focus);

        int rowHeight = tree.getRowHeight();
        Object object = ((ConfigTreeNode) value).getUserObject();
        if (object instanceof DefaultRootPanel) {
            setIcon(null);
        } else if (object instanceof TrafficClassPanel) {
            setIcon(getScaledIcon(trafficClassIcon, rowHeight));
        } else if (object instanceof NetworkInterfacePanel) {
            setIcon(getScaledIcon(interfaceIcon, rowHeight));
        } else {
            setIcon(null);
        }

        return this;
    }
}
