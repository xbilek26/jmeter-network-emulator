package gui;

import model.NetworkEmulator;
import controller.NetworkEmulatorController;

import net.miginfocom.swing.MigLayout;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
import org.apache.jmeter.util.JMeterUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.auto.service.AutoService;

@AutoService(NetworkEmulatorGui.class)
public class NetworkEmulatorGui extends AbstractJMeterGuiComponent {

    private static final Logger log = LoggerFactory.getLogger(NetworkEmulatorGui.class);

    private NetworkEmulatorController networkEmulatorController;  // Controller pro řízení interakce

    public NetworkEmulatorGui() {
        registerIcon();
        init();
        networkEmulatorController = new NetworkEmulatorController();  // Inicializace Controlleru
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
        String jmeterHome = JMeterUtils.getJMeterHome();
        GUIFactory.registerIcon(NetworkEmulatorGui.class.getName(), new ImageIcon(jmeterHome + "/lib/ext/networkemulator/network_emulator.png"));
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

        List<String> availableInterfaces = networkEmulatorController.getAvailableInterfaces();
        for (String networkInterface : availableInterfaces) {
            JMenuItem item = MenuFactory.makeMenuItem(networkInterface, networkInterface, "add_interface");
            item.addActionListener(e -> handleAddInterface(networkInterface)); // Přidání akce
            addInterfaceMenu.add(item);
        }

        return addInterfaceMenu;
    }

    private void handleAddInterface(String interfaceName) {
        networkEmulatorController.addInterface(interfaceName); // Přidá rozhraní do seznamu přidaných
        log.info("Added interface: " + interfaceName);
        System.out.println("!!Added interface: " + interfaceName);
    }

    @Override
    public TestElement makeTestElement() {
        NetworkEmulator networkEmulator = new NetworkEmulator();
        networkEmulator.setNetworkInterfaces(networkEmulatorController.getNetworkInterfaces());
        return networkEmulator;
    }

    @Override
    public void modifyTestElement(TestElement testElement) {
        // Můžete implementovat úpravy test elementu
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

        JPanel threadPropsPanel = new JPanel(new MigLayout("fillx, wrap 2", "[][fill,grow]"));
        threadPropsPanel.setBorder(BorderFactory.createTitledBorder("Emulator parameters"));

        add(threadPropsPanel, BorderLayout.CENTER);
    }
}
