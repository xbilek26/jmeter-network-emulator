package cz.vutbr.networkemulator;

import java.awt.event.ActionEvent;
import java.util.List;

import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.action.ActionRouter;
import org.apache.jmeter.gui.action.AddToTree;
import org.apache.jmeter.gui.action.Close;
import org.apache.jmeter.gui.action.Duplicate;
import org.apache.jmeter.gui.action.Paste;
import org.apache.jmeter.gui.action.Remove;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.utils.NetworkEmulatorUtils;

/**
 * Represents a Network Emulator test element in JMeter. This class ensures that
 * only one instance of the Network Emulator exists in the test plan and also
 * stops emulation when needed.
 *
 * @author Frantisek Bilek (xbilek26)
 */
public class NetworkEmulatorTestElement extends AbstractTestElement {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(NetworkEmulatorTestElement.class);

    private static final String PROP_EMULATION_RUNNING = "NetworkEmulator.emulationRunning";

    public NetworkEmulatorTestElement() {
        System.out.println("creating network emulator test element");
        registerAddToTreeListener();
        registerDuplicateListener();
        registerPasteListener();
        registerRemoveListener();
        registerCloseListener();
    }

    public void setEmulationRunning(boolean running) {
        setProperty(PROP_EMULATION_RUNNING, running);
    }

    public boolean isEmulationRunning() {
        return getPropertyAsBoolean(PROP_EMULATION_RUNNING, false);
    }

    private void registerAddToTreeListener() {
        ActionRouter.getInstance().addPostActionListener(AddToTree.class, (ActionEvent _) -> {
            setEmulationRunning(false);
            ActionRouter.getInstance().actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "stop_emulation"));
            GuiPackage guiPack = GuiPackage.getInstance();
            List<JMeterTreeNode> networkEmulatorNodes = guiPack.getTreeModel().getNodesOfType(NetworkEmulatorTestElement.class);
            if (networkEmulatorNodes.size() > 1) {
                JMeterTreeNode networkEmulator = networkEmulatorNodes.getLast();
                TestElement testElement = networkEmulator.getTestElement();
                guiPack.getTreeModel().removeNodeFromParent(networkEmulator);
                guiPack.removeNode(testElement);
                testElement.removed();
                guiPack.updateCurrentGui();
                JMeterUtils.reportInfoToUser(
                        NetworkEmulatorUtils.getString("msg_one_instance_allowed"),
                        NetworkEmulatorUtils.getString("msg_unsupported_action"));
            }
        });
    }

    private void registerDuplicateListener() {
        ActionRouter.getInstance().addPostActionListener(Duplicate.class, (ActionEvent _) -> {
            setEmulationRunning(false);
            ActionRouter.getInstance().actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "stop_emulation"));
            GuiPackage guiPack = GuiPackage.getInstance();
            List<JMeterTreeNode> networkEmulatorNodes = guiPack.getTreeModel().getNodesOfType(NetworkEmulatorTestElement.class);
            if (networkEmulatorNodes.size() > 1) {
                JMeterTreeNode networkEmulator = networkEmulatorNodes.getLast();
                TestElement testElement = networkEmulator.getTestElement();
                guiPack.getTreeModel().removeNodeFromParent(networkEmulator);
                guiPack.removeNode(testElement);
                testElement.removed();
                guiPack.updateCurrentGui();
                JMeterUtils.reportInfoToUser(
                        NetworkEmulatorUtils.getString("msg_one_instance_allowed"),
                        NetworkEmulatorUtils.getString("msg_unsupported_action"));
            }
        });
    }

    private void registerPasteListener() {
        ActionRouter.getInstance().addPostActionListener(Paste.class, (ActionEvent _) -> {
            setEmulationRunning(false);
            ActionRouter.getInstance().actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "stop_emulation"));
            GuiPackage guiPack = GuiPackage.getInstance();
            List<JMeterTreeNode> networkEmulatorNodes = guiPack.getTreeModel().getNodesOfType(NetworkEmulatorTestElement.class);
            if (networkEmulatorNodes.size() > 1) {
                JMeterTreeNode networkEmulator = networkEmulatorNodes.getLast();
                TestElement testElement = networkEmulator.getTestElement();
                guiPack.getTreeModel().removeNodeFromParent(networkEmulator);
                guiPack.removeNode(testElement);
                testElement.removed();
                guiPack.updateCurrentGui();
                JMeterUtils.reportInfoToUser(
                        NetworkEmulatorUtils.getString("msg_one_instance_allowed"),
                        NetworkEmulatorUtils.getString("msg_unsupported_action"));
            }
        });
    }

    private void registerRemoveListener() {
        ActionRouter.getInstance().addPreActionListener(Remove.class, (ActionEvent _) -> {
            setEmulationRunning(false);
            ActionRouter.getInstance().actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "stop_emulation"));
        });
    }

    private void registerCloseListener() {
        ActionRouter.getInstance().addPreActionListener(Close.class, (ActionEvent _) -> {
            setEmulationRunning(false);
            ActionRouter.getInstance().actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "stop_emulation"));
        });
    }
}
