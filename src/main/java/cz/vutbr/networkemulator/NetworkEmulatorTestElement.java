package cz.vutbr.networkemulator;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

import org.apache.jmeter.exceptions.IllegalUserActionException;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.action.ActionRouter;
import org.apache.jmeter.gui.action.Copy;
import org.apache.jmeter.gui.action.Cut;
import org.apache.jmeter.gui.action.Paste;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkEmulatorTestElement extends AbstractTestElement {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(NetworkEmulatorTestElement.class);

    private static final String NETWORK_EMULATOR_TEST_ELEMENT_NAME = NetworkEmulatorTestElement.class.getSimpleName();

    public NetworkEmulatorTestElement() {
        // registerCopyListeners();
        // registerCutListeners();
        // registerPasteListeners();
    }

    private void registerPasteListeners() {
        AtomicBoolean clipboardContainsNetworkEmulator = new AtomicBoolean();

        ActionRouter.getInstance().addPreActionListener(Paste.class, (ActionEvent e) -> {
            JMeterTreeNode[] copiedNodesBefore = Copy.getCopiedNodes();
            JMeterTreeNode[] copiedNodesAfter = Arrays.stream(copiedNodesBefore)
                    .filter(node -> !node.getTestElement().getClass().getSimpleName().equals(NETWORK_EMULATOR_TEST_ELEMENT_NAME))
                    .toArray(JMeterTreeNode[]::new);

            clipboardContainsNetworkEmulator.set(copiedNodesBefore.length > copiedNodesAfter.length);

            Copy.setCopiedNodes(copiedNodesAfter);
        });

        ActionRouter.getInstance().addPostActionListener(Paste.class, (ActionEvent e) -> {
            if (clipboardContainsNetworkEmulator.get()) {
                log.warn("Only one instance of Network Emulator node is allowed in JMeter tree.");
            }
        });
    }

    private void registerCopyListeners() {
        AtomicBoolean clipboardContainsNetworkEmulator = new AtomicBoolean();
        ActionRouter.getInstance().addPostActionListener(Copy.class, (ActionEvent e) -> {
            JMeterTreeNode[] copiedNodesBefore = Copy.getCopiedNodes();
            JMeterTreeNode[] copiedNodesAfter = Arrays.stream(copiedNodesBefore)
                    .filter(node -> !node.getTestElement().getClass().getSimpleName().equals(NETWORK_EMULATOR_TEST_ELEMENT_NAME))
                    .toArray(JMeterTreeNode[]::new);

            clipboardContainsNetworkEmulator.set(copiedNodesBefore.length > copiedNodesAfter.length);

            Copy.setCopiedNodes(copiedNodesAfter);

            if (clipboardContainsNetworkEmulator.get()) {
                log.warn("Only one instance of Network Emulator node is allowed in JMeter tree.");
            }
        });
    }

    private void registerCutListeners() {
        ActionRouter.getInstance().addPostActionListener(Cut.class, (ActionEvent e) -> {

            JMeterTreeNode[] copiedNodes = Copy.getCopiedNodes();

            Optional<JMeterTreeNode> networkEmulatorNodeOptional = Arrays.stream(copiedNodes)
                    .filter(node -> node.getTestElement().getClass().getSimpleName().equals(NETWORK_EMULATOR_TEST_ELEMENT_NAME))
                    .findFirst();

            if (networkEmulatorNodeOptional.isPresent()) {
                GuiPackage guiPack = GuiPackage.getInstance();
                JMeterTreeNode networkEmulatorNode = networkEmulatorNodeOptional.get();

                JMeterTreeNode testPlanNode = (JMeterTreeNode) ((JMeterTreeNode) guiPack.getTreeModel().getRoot()).getChildAt(0);

                try {
                    JMeterTreeNode node = guiPack.getTreeModel().addComponent(networkEmulatorNode.getTestElement(), testPlanNode);
                    guiPack.getNamingPolicy().nameOnCreation(node);
                    guiPack.getMainFrame().getTree().setSelectionPath(new TreePath(node.getPath()));
                    JOptionPane.showMessageDialog(null, "TODO", "TODO", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalUserActionException iuae) {
                    log.error("Illegal user action while adding a tree node.", iuae);
                }
            }
        });
    }
}
