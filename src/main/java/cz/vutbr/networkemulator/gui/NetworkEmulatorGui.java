package cz.vutbr.networkemulator.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
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
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import org.apache.jmeter.gui.AbstractJMeterGuiComponent;
import org.apache.jmeter.gui.GUIFactory;
import org.apache.jmeter.gui.util.JMeterToolBar;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;

import com.google.auto.service.AutoService;

import cz.vutbr.networkemulator.linux.CommandOutput;
import cz.vutbr.networkemulator.linux.CommandRunner;
import cz.vutbr.networkemulator.model.NetworkEmulator;
import net.sf.saxon.functions.ConstantFunction.False;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(NetworkEmulatorGui.class)
public class NetworkEmulatorGui extends AbstractJMeterGuiComponent implements ActionListener {

    private static final Logger log = LoggerFactory.getLogger(NetworkEmulatorGui.class);

    private NetworkEmulator networkEmulator = new NetworkEmulator();

    private boolean running = false;
    JButton startButton;
    JButton stopButton;
    JLabel emulatorState;

    public NetworkEmulatorGui() {
        registerIcon();
        registerDisabledIcon();
        init();
        log.info("Network Emulator Successfully initialized.");
    }

    @Override
    public TestElement makeTestElement() {
        return networkEmulator;
    }

    @Override
    public void modifyTestElement(TestElement testElement) {
        super.modifyTestElement(testElement);
    }

    private void init() {
        setLayout(new BorderLayout(0, 5));
        setBorder(BorderFactory.createEmptyBorder());

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(makeBorder());

        JSplitPane mainPanel = createMainPanel();

        JTabbedPane interfaces = createInterfacesTabs();
        interfaces.setBorder(BorderFactory.createTitledBorder("Interfaces"));

        Container titlePanel = makeTitlePanel();
        JSplitPane emulatorPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mainPanel, interfaces);
        emulatorPanel.setBorder(BorderFactory.createEmptyBorder());
        emulatorPanel.setResizeWeight(0.6);
        emulatorPanel.setOneTouchExpandable(true);
        emulatorPanel.setDividerLocation(0.6);

        wrapper.add(titlePanel, BorderLayout.NORTH);
        wrapper.add(emulatorPanel, BorderLayout.CENTER);
        add(wrapper);
    }

    protected JTabbedPane createInterfacesTabs() {
        JTabbedPane tabbedPane = new JTabbedPane();
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
        JPanel statePanel = new JPanel(new BorderLayout());
        statePanel.setBorder(BorderFactory.createTitledBorder("Current Settings"));

        JTextArea currentSettings = new JTextArea();
        currentSettings.setBackground(new Color(0x000000));
        currentSettings.setForeground(new Color(0x00ff00));
        currentSettings.setFont(new Font("", Font.PLAIN, 14));
        currentSettings.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(currentSettings);
        currentSettings.setLineWrap(true);
        currentSettings.setWrapStyleWord(true);

        CommandRunner runner = new CommandRunner();
        CommandOutput output = runner.runCommand("tc qdisc");
        currentSettings.setText(output.getOutputString());

        statePanel.add(scrollPane, BorderLayout.CENTER);

        return statePanel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Controls"));

        String iconSize = JMeterUtils.getPropDefault(JMeterToolBar.TOOLBAR_ICON_SIZE,
                JMeterToolBar.DEFAULT_TOOLBAR_ICON_SIZE);
        ImageIcon startImage = JMeterUtils.getImage("toolbar/" + iconSize + "/arrow-right-3.png");
        startButton = new JButton("Start");
        startButton.setIcon(startImage);
        startButton.addActionListener(this);
        startButton.setFocusable(false);
        startButton.setEnabled(true);

        stopButton = new JButton("Stop");
        ImageIcon stopImage = JMeterUtils.getImage("toolbar/" + iconSize + "/process-stop-4.png");
        stopButton.setIcon(stopImage);
        stopButton.addActionListener(this);
        stopButton.setFocusable(false);
        stopButton.setEnabled(false);

        emulatorState = new JLabel("Emulation is stopped.", JLabel.CENTER);

        controlPanel.add(startButton);
        controlPanel.add(stopButton);
        controlPanel.add(emulatorState);

        return controlPanel;
    }

    private JSplitPane createMainPanel() {
        JSplitPane mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createStatePanel(), createControlPanel());
        mainPanel.setResizeWeight(0.98);
        mainPanel.setDividerLocation(0.98);
        mainPanel.setOneTouchExpandable(false);
        mainPanel.setDividerSize(0);

        mainPanel.setBorder(BorderFactory.createTitledBorder("Main Panel"));

        return mainPanel;
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

    public static void registerDisabledIcon() {
        String iconPath = "/cz/vutbr/networkemulator/images/network_emulator_disabled.gif";
        ImageIcon icon = new ImageIcon(NetworkEmulatorGui.class.getResource(iconPath));
        GUIFactory.registerDisabledIcon(NetworkEmulatorGui.class.getName(), icon);
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
            emulatorState.setText("Emulation is running!");
        } else {
            emulatorState.setText("Emulation is stopped.");
        }
        startButton.setEnabled(!running);
        stopButton.setEnabled(running);
    }

}