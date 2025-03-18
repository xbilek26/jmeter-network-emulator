package cz.vutbr.networkemulator.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import org.apache.jmeter.gui.AbstractJMeterGuiComponent;
import org.apache.jmeter.gui.GUIFactory;
import org.apache.jmeter.gui.util.JMeterToolBar;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;

import cz.vutbr.networkemulator.linux.CommandOutput;
import cz.vutbr.networkemulator.linux.CommandRunner;
import cz.vutbr.networkemulator.model.NetworkEmulator;
import cz.vutbr.networkemulator.model.utils.NetworkEmulatorConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkEmulatorGui extends AbstractJMeterGuiComponent {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(NetworkEmulatorGui.class);

    NetworkEmulator networkEmulator;

    private ConfigurationPanel configPanel;
    private JButton startButton;
    private JButton stopButton;
    private JLabel emulatorState;

    public NetworkEmulatorGui() {
        init();
    }

    @Override
    public TestElement makeTestElement() {
        networkEmulator = new NetworkEmulator();
        modifyTestElement(networkEmulator);
        return networkEmulator;
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.modifyTestElement(element);
        configPanel.modifyTestElement(element);
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        configPanel.configure(element);
    }

    private void init() {
        registerIcon();
        registerDisabledIcon();
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

        add(makeTitlePanel(), BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, createMainPanel(), createConfigurationPanel());

        add(splitPane, BorderLayout.CENTER);
    }

    private JSplitPane createMainPanel() {
        JSplitPane mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createStatePanel(), createControlPanel());
        mainPanel.setResizeWeight(0.98);
        mainPanel.setDividerLocation(0.98);
        mainPanel.setOneTouchExpandable(true);
        mainPanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorConstants.TITLE_MAIN_PANEL));

        return mainPanel;
    }

    private ConfigurationPanel createConfigurationPanel() {
        if (configPanel == null) {
            configPanel = new ConfigurationPanel();
        }

        return configPanel;
    }

    private void startEmulation() {
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        emulatorState.setText(NetworkEmulatorConstants.MSG_EMULATION_RUNNING);
    }

    private void stopEmulation() {
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        emulatorState.setText(NetworkEmulatorConstants.MSG_EMULATION_STOPPED);
    }

    private JPanel createStatePanel() {
        JPanel statePanel = new JPanel(new BorderLayout());

        JTextArea currentSettings = new JTextArea();
        currentSettings.setBackground(new Color(0x000000));
        currentSettings.setForeground(new Color(0x00ff00));
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

        String iconSize = JMeterUtils.getPropDefault(JMeterToolBar.TOOLBAR_ICON_SIZE,
                JMeterToolBar.DEFAULT_TOOLBAR_ICON_SIZE);
        ImageIcon startImage = JMeterUtils.getImage("toolbar/" + iconSize + "/arrow-right-3.png");
        startButton = new JButton("Start");
        startButton.setIcon(startImage);
        startButton.addActionListener(e -> startEmulation());
        startButton.setFocusable(false);
        startButton.setEnabled(true);

        stopButton = new JButton("Stop");
        ImageIcon stopImage = JMeterUtils.getImage("toolbar/" + iconSize + "/process-stop-4.png");
        stopButton.setIcon(stopImage);
        stopButton.addActionListener(e -> stopEmulation());
        stopButton.setFocusable(false);
        stopButton.setEnabled(false);

        emulatorState = new JLabel(NetworkEmulatorConstants.MSG_EMULATION_STOPPED, JLabel.CENTER);

        controlPanel.add(startButton);
        controlPanel.add(stopButton);
        controlPanel.add(emulatorState);

        return controlPanel;
    }

    @Override
    public String getLabelResource() {
        return "network_emulator_title";
    }

    @Override
    public String getStaticLabel() {
        return NetworkEmulatorConstants.EMULATOR_NAME;
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
    public Collection<String> getMenuCategories() {
        return Arrays.asList(MenuFactory.NON_TEST_ELEMENTS);
    }
}
