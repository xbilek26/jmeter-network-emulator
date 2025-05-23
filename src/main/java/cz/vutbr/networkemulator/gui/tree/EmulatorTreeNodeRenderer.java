package cz.vutbr.networkemulator.gui.tree;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import cz.vutbr.networkemulator.gui.RootPanel;
import cz.vutbr.networkemulator.gui.NetworkInterfacePanel;
import cz.vutbr.networkemulator.gui.EmulationRulePanel;
import cz.vutbr.networkemulator.utils.EmulatorUtils;

public class EmulatorTreeNodeRenderer extends DefaultTreeCellRenderer {

    private final ImageIcon rootIcon;
    private final ImageIcon networkInterfaceIcon;
    private final ImageIcon emulationRuleIcon;

    public EmulatorTreeNodeRenderer() {
        this.rootIcon = EmulatorUtils.getImage("root_node.gif");
        this.networkInterfaceIcon = EmulatorUtils.getImage("network_interface.gif");
        this.emulationRuleIcon = EmulatorUtils.getImage("emulation_rule.gif");
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
            boolean leaf, int row, boolean focus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, focus);

        Object node = ((EmulatorTreeNode) value).getUserObject();
        if (node instanceof RootPanel) {
            this.setIcon(EmulatorUtils.getScaledIcon(rootIcon, 16, 16));
        } else if (node instanceof EmulationRulePanel) {
            this.setIcon(EmulatorUtils.getScaledIcon(emulationRuleIcon, 16, 16));
        } else if (node instanceof NetworkInterfacePanel) {
            this.setIcon(EmulatorUtils.getScaledIcon(networkInterfaceIcon, 16, 16));
        } else {
            this.setIcon(null);
        }

        return this;
    }
}
