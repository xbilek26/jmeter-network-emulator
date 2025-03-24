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
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import org.apache.jmeter.gui.AbstractJMeterGuiComponent;
import org.apache.jmeter.gui.GUIFactory;
import org.apache.jmeter.gui.action.ActionNames;
import org.apache.jmeter.gui.action.KeyStrokes;
import org.apache.jmeter.gui.util.JMeterToolBar;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.NetworkEmulatorTestElement;
import cz.vutbr.networkemulator.controller.NetworkEmulatorController;
import cz.vutbr.networkemulator.linux.CommandOutput;
import cz.vutbr.networkemulator.linux.CommandRunner;
import cz.vutbr.networkemulator.model.NetworkEmulator;
import cz.vutbr.networkemulator.utils.NetworkEmulatorConstants;

public class NetworkEmulatorGui extends AbstractJMeterGuiComponent {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(NetworkEmulatorGui.class);

    private NetworkEmulator networkEmulator;
    private NetworkEmulatorController controller;

    private ConfigurationPanel configurationPanel;
    private JButton btnStart;
    private JButton btnStop;
    private JLabel lblEmulatorState;

    public NetworkEmulatorGui() {
        if (networkEmulator == null) {
            networkEmulator = new NetworkEmulator();
        }

        if (controller == null) {
            controller = new NetworkEmulatorController(networkEmulator);
        }
        init();
    }

    @Override
    public TestElement makeTestElement() {
        return new NetworkEmulatorTestElement();
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.modifyTestElement(element);
        configurationPanel.modifyTestElement(element);
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        configurationPanel.configure(element);
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
        configurationPanel = new ConfigurationPanel(controller);
        return configurationPanel;
    }

    private void startEmulation() {
        configurationPanel.collectAndApplySettings();
        controller.runEmulation();
        controller.printNetworkConfiguration();

        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
        lblEmulatorState.setText(NetworkEmulatorConstants.MSG_EMULATION_RUNNING);
    }

    private void stopEmulation() {
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        lblEmulatorState.setText(NetworkEmulatorConstants.MSG_EMULATION_STOPPED);
    }

    private JPanel createStatePanel() {
        JPanel statePanel = new JPanel(new BorderLayout());

        JTextArea currentSettings = new JTextArea();
        currentSettings.setBackground(new Color(0x000000));
        currentSettings.setForeground(new Color(0xffffff));
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
        btnStart = new JButton(NetworkEmulatorConstants.BTN_START_EMULATION);
        btnStart.setIcon(startImage);
        btnStart.addActionListener(e -> startEmulation());
        btnStart.setFocusable(false);
        btnStart.setEnabled(true);

        btnStop = new JButton(NetworkEmulatorConstants.BTN_STOP_EMULATION);
        ImageIcon stopImage = JMeterUtils.getImage("toolbar/" + iconSize + "/process-stop-4.png");
        btnStop.setIcon(stopImage);
        btnStop.addActionListener(e -> stopEmulation());
        btnStop.setFocusable(false);
        btnStop.setEnabled(false);

        lblEmulatorState = new JLabel(NetworkEmulatorConstants.MSG_EMULATION_STOPPED, JLabel.CENTER);

        controlPanel.add(btnStart);
        controlPanel.add(btnStop);
        controlPanel.add(lblEmulatorState);

        return controlPanel;
    }

    @Override
    public String getLabelResource() {
        return NetworkEmulatorConstants.NETWORK_EMULATOR_LABEL_RESOURCE;
    }

    @Override
    public String getStaticLabel() {
        return NetworkEmulatorConstants.NETWORK_EMULATOR_STATIC_LABEL;
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
        JPopupMenu menu = new JPopupMenu();

        JMenuItem cut = MenuFactory.makeMenuItemRes("cut", ActionNames.CUT);
        cut.setAccelerator(KeyStrokes.CUT);
        menu.add(cut);
        JMenuItem remove = MenuFactory.makeMenuItemRes("remove", ActionNames.REMOVE);
        remove.setAccelerator(KeyStrokes.REMOVE);
        menu.add(remove);

        menu.addSeparator();

        menu.add(MenuFactory.makeMenuItemRes("open", ActionNames.OPEN));
        menu.add(MenuFactory.makeMenuItemRes("menu_merge", ActionNames.MERGE));
        menu.add(MenuFactory.makeMenuItemRes("save_as", ActionNames.SAVE_AS));
        menu.add(MenuFactory.makeMenuItemRes("save_as_test_fragment", ActionNames.SAVE_AS_TEST_FRAGMENT));

        menu.addSeparator();

        menu.add(MenuFactory.makeMenuItemRes("copy_code", ActionNames.COPY_CODE));
        JMenuItem savePicture = MenuFactory.makeMenuItemRes("save_as_image", ActionNames.SAVE_GRAPHICS);
        savePicture.setAccelerator(KeyStrokes.SAVE_GRAPHICS);
        menu.add(savePicture);
        JMenuItem savePictureAll = MenuFactory.makeMenuItemRes("save_as_image_all", ActionNames.SAVE_GRAPHICS_ALL);
        savePictureAll.setAccelerator(KeyStrokes.SAVE_GRAPHICS_ALL);
        menu.add(savePictureAll);

        return menu;
    }

    @Override
    public Collection<String> getMenuCategories() {
        return Arrays.asList(MenuFactory.NON_TEST_ELEMENTS);
    }
}
