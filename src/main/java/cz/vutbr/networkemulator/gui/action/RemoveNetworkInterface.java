package cz.vutbr.networkemulator.gui.action;

import java.util.HashSet;
import java.util.Set;

import java.awt.event.ActionEvent;

import org.apache.jmeter.gui.action.Remove;
import org.apache.jmeter.gui.action.Command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.auto.service.AutoService;

@AutoService(Command.class)
public class RemoveNetworkInterface extends Remove {

    private static final Logger log = LoggerFactory.getLogger(RemoveNetworkInterface.class);

    private static final Set<String> commands = new HashSet<>();

    static {
        commands.add("remove_interface");
    }

    @Override
    public void doAction(ActionEvent event) {
        super.doAction(event);
        log.info("Removed network interface from tree");
    }

    @Override
    public Set<String> getActionNames() {
        return commands;
    }

}
