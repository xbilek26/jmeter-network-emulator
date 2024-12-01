package cz.vutbr.networkemulator.gui;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import org.apache.jmeter.gui.AbstractJMeterGuiComponent;
import org.apache.jmeter.gui.GUIFactory;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.testelement.TestElement;

import com.google.auto.service.AutoService;

import cz.vutbr.networkemulator.model.NetworkEmulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(NetworkEmulatorGui.class)
public class NetworkEmulatorGui extends AbstractJMeterGuiComponent {

    private static final Logger log = LoggerFactory.getLogger(NetworkEmulatorGui.class);

    private NetworkEmulator networkEmulator = new NetworkEmulator();

    public NetworkEmulatorGui() {
        registerIcon();
        init();
        log.info("Network Emulator Successfully initialized.");
    }

    @Override
    public TestElement makeTestElement() {
        return networkEmulator;
    }

    @Override
    public void modifyTestElement(TestElement testElement) {
    }

    private void init() {
        setLayout(new BorderLayout(0, 5));
        setBorder(BorderFactory.createEmptyBorder());

        JTabbedPane tabbedPane = createTabbedConfigPane();

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(makeBorder());
        wrapper.add(makeTitlePanel(), BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, wrapper, tabbedPane);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setOneTouchExpandable(true);
        add(splitPane);
    }
    
    protected JTabbedPane createTabbedConfigPane() {
        final JTabbedPane tabbedPane = new JTabbedPane();
    
        ArrayList<NetworkInterfaceConfigGui> guis = createNetworkInterfaceConfigGuis();
    
        for (NetworkInterfaceConfigGui gui : guis) {
            tabbedPane.add(gui.getName(), gui);
        }
    
        return tabbedPane;
    }
    
    protected ArrayList<NetworkInterfaceConfigGui> createNetworkInterfaceConfigGuis() {
        
        ArrayList<NetworkInterfaceConfigGui> guis = new ArrayList<>();
        ArrayList<String> availableInterfaces = networkEmulator.getAvailableNetworkInterfaces();
    
        for (String networkInterface : availableInterfaces) {
            NetworkInterfaceConfigGui gui = new NetworkInterfaceConfigGui(networkInterface);
            gui.setBorder(makeBorder());
            guis.add(gui);
        }
        return guis;
    }
    

    @Override
    public Collection<String> getMenuCategories() {
        return Arrays.asList(MenuFactory.NON_TEST_ELEMENTS);
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
        MenuFactory.addEditMenu(pop, true);

        return pop;
    }

}
