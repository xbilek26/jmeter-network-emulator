package cz.vutbr.networkemulator.action;

import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;

import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.JMeterGUIComponent;
import org.apache.jmeter.gui.action.AbstractAction;
import org.apache.jmeter.gui.action.Command;

import com.google.auto.service.AutoService;

import cz.vutbr.networkemulator.NetworkEmulatorTestElement;
import cz.vutbr.networkemulator.controller.NetworkEmulatorController;
import cz.vutbr.networkemulator.gui.NetworkEmulatorGui;

@AutoService(Command.class)
public class StopEmulation extends AbstractAction {

    private static final Set<String> commands = new HashSet<>();

    static {
        commands.add("stop_emulation");
    }

    @Override
    public void doAction(ActionEvent e) {
        NetworkEmulatorController controller = NetworkEmulatorController.getInstance();
        controller.stopEmulation();
        JMeterGUIComponent gui = GuiPackage.getInstance().getCurrentGui();
        if (gui instanceof NetworkEmulatorGui networkEmulatorGui) {
            networkEmulatorGui.onEmulationStopped();
            ((NetworkEmulatorTestElement) GuiPackage.getInstance().getCurrentElement()).setEmulationRunning(false);
        }
    }

    @Override
    public Set<String> getActionNames() {
        return commands;
    }

}
