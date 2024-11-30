package cz.vutbr.networkemulator.gui;

import net.miginfocom.swing.MigLayout;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.apache.jmeter.gui.AbstractJMeterGuiComponent;
import org.apache.jmeter.gui.GUIFactory;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.testelement.TestElement;
import com.google.auto.service.AutoService;

import cz.vutbr.networkemulator.controller.NetworkEmulatorController;
import cz.vutbr.networkemulator.model.NetworkEmulator;

@AutoService(NetworkEmulatorGui.class)
public class NetworkEmulatorGui extends AbstractJMeterGuiComponent {

    private NetworkEmulatorController networkEmulatorController;
    private NetworkEmulator networkEmulator;

    public NetworkEmulatorGui() {
        registerIcon();
        init();
        networkEmulatorController = new NetworkEmulatorController();
        networkEmulator = new NetworkEmulator();
    }

    @Override
    public String getLabelResource() {
        return "netemulator";
    }

    @Override
    public String getStaticLabel() {
        return "Network Emulator";
    }

    public static void registerIcon() {
        String iconPath = "/cz/vutbr/networkemulator/images/network_emulator.gif";
        ImageIcon icon = new ImageIcon(NetworkEmulatorGui.class.getResource(iconPath));
        GUIFactory.registerIcon(NetworkEmulatorGui.class.getName(), icon);
    }

    @Override
    public JPopupMenu createPopupMenu() {
        JPopupMenu pop = new JPopupMenu();
        pop.add(createAddInterfaceMenu());
        MenuFactory.addEditMenu(pop, true);

        return pop;
    }

    private JMenu createAddInterfaceMenu() {
        JMenu addInterfaceMenu = new JMenu("Add Interface");

        ArrayList<String> availableInterfaces = networkEmulator.getAvailableNetworkInterfaces();
        for (String networkInterface : availableInterfaces) {
            JMenuItem item = MenuFactory.makeMenuItem(networkInterface, networkInterface, "add_interface");
            addInterfaceMenu.add(item);
            //item.addActionListener(e -> modifyTestElement(networkEmulator));
        }

        return addInterfaceMenu;
    }

    @Override
    public TestElement makeTestElement() {
        networkEmulator.setAvaiableNetworkInterfaces(networkEmulatorController.getNetworkInterfaces());
        return networkEmulator;
    }

    @Override
    public void modifyTestElement(TestElement testElement) {
        //super.configureTestElement(testElement);
        //networkEmulator = (NetworkEmulator) testElement;
    }

    @Override
    public Collection<String> getMenuCategories() {
        return Arrays.asList(MenuFactory.NON_TEST_ELEMENTS);
    }

    private void init() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        Box box = Box.createVerticalBox();
        box.add(makeTitlePanel());
        add(box, BorderLayout.NORTH);

        JPanel emulatorPropsPanel = new JPanel(new MigLayout("fillx, wrap 2", "[][fill,grow]"));
        emulatorPropsPanel.setBorder(BorderFactory.createTitledBorder("Emulator parameters"));

        add(emulatorPropsPanel, BorderLayout.CENTER);
    }
}
