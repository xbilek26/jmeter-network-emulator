package cz.vutbr.networkemulator;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.action.ActionRouter;
import org.apache.jmeter.gui.action.AddToTree;
import org.apache.jmeter.gui.action.Duplicate;
import org.apache.jmeter.gui.action.Paste;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.utils.NetworkEmulatorConstants;

/**
 * Represents a Network Emulator test element in JMeter. This class ensures that
 * only one instance of the Network Emulator exists in the test plan by
 * listening to add, paste, and duplicate actions. If multiple instances are
 * detected, the additional ones are removed, and a warning message is
 * displayed.
 *
 * @author Frantisek Bilek (xbilek26)
 */
public class NetworkEmulatorTestElement extends AbstractTestElement {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(NetworkEmulatorTestElement.class);

    public NetworkEmulatorTestElement() {
        registerAddToTreeListener();
        registerDuplicateListener();
        registerPasteListener();
    }

    private void registerAddToTreeListener() {
        ActionRouter.getInstance().addPostActionListener(AddToTree.class, (ActionEvent e) -> {
            GuiPackage guiPack = GuiPackage.getInstance();
            List<JMeterTreeNode> networkEmulatorNodes = guiPack.getTreeModel().getNodesOfType(NetworkEmulatorTestElement.class);
            if (networkEmulatorNodes.size() > 1) {
                JMeterTreeNode networkEmulator = networkEmulatorNodes.getLast();
                TestElement testElement = networkEmulator.getTestElement();
                guiPack.getTreeModel().removeNodeFromParent(networkEmulator);
                guiPack.removeNode(testElement);
                testElement.removed();
                guiPack.updateCurrentGui();
                JOptionPane.showMessageDialog(
                        null,
                        NetworkEmulatorConstants.MSG_ONE_INSTANCE_ALLOWED,
                        NetworkEmulatorConstants.MSG_GENERAL_ERROR,
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    private void registerDuplicateListener() {
        ActionRouter.getInstance().addPostActionListener(Duplicate.class, (ActionEvent e) -> {
            GuiPackage guiPack = GuiPackage.getInstance();
            List<JMeterTreeNode> networkEmulatorNodes = guiPack.getTreeModel().getNodesOfType(NetworkEmulatorTestElement.class);
            if (networkEmulatorNodes.size() > 1) {
                JMeterTreeNode networkEmulator = networkEmulatorNodes.getLast();
                TestElement testElement = networkEmulator.getTestElement();
                guiPack.getTreeModel().removeNodeFromParent(networkEmulator);
                guiPack.removeNode(testElement);
                testElement.removed();
                guiPack.updateCurrentGui();
                JOptionPane.showMessageDialog(
                        null,
                        NetworkEmulatorConstants.MSG_ONE_INSTANCE_ALLOWED,
                        NetworkEmulatorConstants.MSG_GENERAL_ERROR,
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    private void registerPasteListener() {
        ActionRouter.getInstance().addPostActionListener(Paste.class, (ActionEvent e) -> {
            GuiPackage guiPack = GuiPackage.getInstance();
            List<JMeterTreeNode> networkEmulatorNodes = guiPack.getTreeModel().getNodesOfType(NetworkEmulatorTestElement.class);
            if (networkEmulatorNodes.size() > 1) {
                JMeterTreeNode networkEmulator = networkEmulatorNodes.getLast();
                TestElement testElement = networkEmulator.getTestElement();
                guiPack.getTreeModel().removeNodeFromParent(networkEmulator);
                guiPack.removeNode(testElement);
                testElement.removed();
                guiPack.updateCurrentGui();
                JOptionPane.showMessageDialog(
                        null,
                        NetworkEmulatorConstants.MSG_ONE_INSTANCE_ALLOWED,
                        NetworkEmulatorConstants.MSG_GENERAL_ERROR,
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}
