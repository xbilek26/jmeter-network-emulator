package cz.vutbr.networkemulator.gui.action;

import java.util.HashSet;
import java.util.Set;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.tree.TreePath;

import org.apache.jmeter.gui.action.AddToTree;

import java.awt.event.ActionEvent;

import org.apache.jmeter.exceptions.IllegalUserActionException;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.action.Command;
import org.apache.jmeter.gui.tree.JMeterTreeModel;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.testelement.TestElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.auto.service.AutoService;

import cz.vutbr.networkemulator.gui.NetworkInterfaceGui;
import cz.vutbr.networkemulator.model.NetworkInterface;

@AutoService(Command.class)
public class AddNetworkInterface extends AddToTree {

    private static final Logger log = LoggerFactory.getLogger(AddNetworkInterface.class);

    private static final Set<String> commands = new HashSet<>();

    static {
        commands.add("add_interface");
    }

    @Override
    public void doAction(ActionEvent event) {
        GuiPackage guiPackage = GuiPackage.getInstance();
        String networkInterfaceName = ((JComponent) event.getSource()).getName();
        
        guiPackage.updateCurrentNode();
        TestElement testElement = guiPackage.createTestElement(NetworkInterfaceGui.class.getName());
        testElement.setName(networkInterfaceName);
        JMeterTreeNode parentNode = guiPackage.getCurrentNode();
        JMeterTreeModel model = guiPackage.getTreeModel();

        List<JMeterTreeNode> networkInterfaceNodes = model.getNodesOfType(NetworkInterface.class);

        boolean exists = networkInterfaceNodes.stream().anyMatch(node -> node.getTestElement().equals(testElement));

        if (exists) {
            log.error("Network interface already exists with the same TestElement");
            return;
        }
        
        try {
            JMeterTreeNode addedNode = model.addComponent(testElement, parentNode);
            guiPackage.getNamingPolicy().nameOnCreation(addedNode);
            guiPackage.getMainFrame().getTree().setSelectionPath(new TreePath(addedNode.getPath()));
            log.info("Added network interface to tree: " + networkInterfaceName);
        } catch (IllegalUserActionException error) {
            log.error("Error adding network interface: " + error.getMessage(), error);
        }
    }

    @Override
    public Set<String> getActionNames() {
        return commands;
    }
    
}
