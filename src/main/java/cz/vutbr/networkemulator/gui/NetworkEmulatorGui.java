package cz.vutbr.networkemulator.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import org.apache.jmeter.gui.AbstractJMeterGuiComponent;
import org.apache.jmeter.gui.GUIFactory;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.testelement.TestElement;

import com.google.auto.service.AutoService;

import cz.vutbr.networkemulator.linux.CommandOutput;
import cz.vutbr.networkemulator.linux.CommandRunner;
import cz.vutbr.networkemulator.model.NetworkEmulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(NetworkEmulatorGui.class)
public class NetworkEmulatorGui extends AbstractJMeterGuiComponent implements ActionListener {

    private static final Logger log = LoggerFactory.getLogger(NetworkEmulatorGui.class);

    private NetworkEmulator networkEmulator = new NetworkEmulator();

    private boolean running = false;
    JButton button;
    JLabel state;

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

        JPanel statePanel = createStatePanel();
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, wrapper, tabbedPane);

        // JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, wrapper, subPlane);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setOneTouchExpandable(true);
        add(splitPane);
    }
    
    protected JTabbedPane createTabbedConfigPane() {
        final JTabbedPane tabbedPane = new JTabbedPane();
    
        ArrayList<NetworkInterfaceGui> guis = createNetworkInterfaceConfigGuis();
    
        for (NetworkInterfaceGui gui : guis) {
            tabbedPane.add(gui.getName(), gui);
        }
    
        return tabbedPane;
    }
    
    protected ArrayList<NetworkInterfaceGui> createNetworkInterfaceConfigGuis() {
        
        ArrayList<NetworkInterfaceGui> guis = new ArrayList<>();
        ArrayList<String> availableInterfaces = networkEmulator.getAvailableNetworkInterfaces();
    
        for (String networkInterface : availableInterfaces) {
            NetworkInterfaceGui gui = new NetworkInterfaceGui(networkInterface);
            gui.setBorder(makeBorder());
            guis.add(gui);
        }
        return guis;
    }

    private JPanel createStatePanel() {
        JPanel statePanel = new JPanel();
        JTextArea currentSettings = new JTextArea();
        CommandRunner runner = new CommandRunner();
        CommandOutput output = runner.runCommand("tc qdisc");
        currentSettings.setText("Current settings..." + output.getOutputString());

        button = new JButton("Start Emulation");
        button.addActionListener(this);
        button.setFocusable(false);

        state = new JLabel("Emulation is stopped.");

        statePanel.add(currentSettings);
        statePanel.add(button);
        statePanel.add(state);

        return statePanel;

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

    @Override
    public void actionPerformed(ActionEvent e) {
        running = !running;
        if (running) {
            button.setText("Stop Emulation");
            state.setText("Emulation is running!");
        } else {
            button.setText("Start Emulation");
            state.setText("Emulation is stopped.");
        }
    }

}
