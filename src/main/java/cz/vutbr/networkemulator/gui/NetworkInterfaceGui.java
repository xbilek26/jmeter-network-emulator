package cz.vutbr.networkemulator.gui;

import org.apache.jmeter.gui.AbstractJMeterGuiComponent;
import org.apache.jmeter.gui.GUIFactory;
import org.apache.jmeter.gui.action.ActionNames;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.testelement.TestElement;

import cz.vutbr.networkemulator.model.NetworkInterface;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

public class NetworkInterfaceGui extends AbstractJMeterGuiComponent {

    public NetworkInterfaceGui() {
        init();
        registerIcon();
    }

    @Override
    public String getStaticLabel() {
        return "Network Interface";
    }

    public static void registerIcon() {
        String iconPath = "/cz/vutbr/networkemulator/images/network_interface.gif";
        ImageIcon icon = new ImageIcon(NetworkInterfaceGui.class.getResource(iconPath));
        GUIFactory.registerIcon(NetworkInterfaceGui.class.getName(), icon);
    }

    @Override
    public String getLabelResource() {
        return null;
    }

    @Override
    public TestElement makeTestElement() {
        NetworkInterface networkInterface = new NetworkInterface();
        modifyTestElement(networkInterface);
        return networkInterface;
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.configureTestElement(element);
    }

    private void init() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        Box box = Box.createVerticalBox();
        add(box, BorderLayout.NORTH);

        JPanel threadPropsPanel = new JPanel(new MigLayout("fillx, wrap 2", "[][fill,grow]"));
        threadPropsPanel.setBorder(BorderFactory.createTitledBorder("Emulation parameters"));

        String[] actions = {"Delay", "Loss", "Rate", "Duplicate", "Corrupt", "Reorder"};

        for (String action : actions) {
            JCheckBox actionCheckBox = new JCheckBox(action);
            JTextField actionTextField = new JTextField();
    
            threadPropsPanel.add(actionCheckBox, "span 1");
            threadPropsPanel.add(actionTextField, "span 1, growx, wmin 150");
        }

        add(threadPropsPanel, BorderLayout.CENTER);
    }

    @Override
    public JPopupMenu createPopupMenu() {
        JPopupMenu pop = new JPopupMenu();
        pop.add(MenuFactory.makeMenuItem("Toggle", "toggle", ActionNames.TOGGLE));
        pop.add(MenuFactory.makeMenuItem("Remove", "remove", "remove_interface"));

        return pop;
    }

    @Override
    public Collection<String> getMenuCategories() {
        return null;
    }
}
