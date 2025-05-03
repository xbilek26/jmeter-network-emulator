package cz.vutbr.networkemulator.gui.tree;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import cz.vutbr.networkemulator.gui.DefaultRootPanel;
import cz.vutbr.networkemulator.gui.NetworkInterfacePanel;
import cz.vutbr.networkemulator.gui.EmulationRulePanel;
import cz.vutbr.networkemulator.utils.NetworkEmulatorUtils;

public class ConfigTreeNodeRenderer extends DefaultTreeCellRenderer {

    private final ImageIcon defaultRootIcon;
    private final ImageIcon networkInterfaceIcon;
    private final ImageIcon emulationRuleIcon;

    public ConfigTreeNodeRenderer() {
        this.defaultRootIcon = NetworkEmulatorUtils.getImage("root_node.gif");
        this.networkInterfaceIcon = NetworkEmulatorUtils.getImage("network_interface.gif");
        this.emulationRuleIcon = NetworkEmulatorUtils.getImage("emulation_rule.gif");
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
            boolean leaf, int row, boolean focus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, focus);

        Object node = ((ConfigTreeNode) value).getUserObject();
        if (node instanceof DefaultRootPanel) {
            this.setIcon(NetworkEmulatorUtils.getScaledIcon(defaultRootIcon, 16, 16));
        } else if (node instanceof EmulationRulePanel) {
            this.setIcon(NetworkEmulatorUtils.getScaledIcon(emulationRuleIcon, 16, 16));
        } else if (node instanceof NetworkInterfacePanel) {
            this.setIcon(NetworkEmulatorUtils.getScaledIcon(networkInterfaceIcon, 16, 16));
        } else {
            this.setIcon(null);
        }

        return this;
    }
}
