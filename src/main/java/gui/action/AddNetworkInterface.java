package gui.action;

import gui.NetworkInterfaceGui;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.tree.TreePath;

import org.apache.jmeter.gui.action.AddToTree;

import java.awt.event.ActionEvent;

import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.action.Command;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.testelement.TestElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.auto.service.AutoService;

@AutoService(Command.class)
public class AddNetworkInterface extends AddToTree {

    private static final Logger log = LoggerFactory.getLogger(RemoveNetworkInterface.class);

    private static final Set<String> commands = new HashSet<>();

    static {
        commands.add("add_interface");
    }

    @Override
    public void doAction(ActionEvent e) {
        GuiPackage guiPackage = GuiPackage.getInstance();
        try {
            guiPackage.updateCurrentNode();
            TestElement testElement = guiPackage.createTestElement(NetworkInterfaceGui.class.getName());
            testElement.setName(((JComponent) e.getSource()).getName());
            JMeterTreeNode parentNode = guiPackage.getCurrentNode();
            JMeterTreeNode node = guiPackage.getTreeModel().addComponent(testElement, parentNode);
            guiPackage.getNamingPolicy().nameOnCreation(node);
            guiPackage.getMainFrame().getTree().setSelectionPath(new TreePath(node.getPath()));
        } catch (Exception err) {
            
        }
        log.info("Add Network Interface");

    }

    @Override
    public Set<String> getActionNames() {
        return commands;
    }
    
}
