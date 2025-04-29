package cz.vutbr.networkemulator.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
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

import org.apache.jmeter.gui.AbstractJMeterGuiComponent;
import org.apache.jmeter.gui.GUIFactory;
import org.apache.jmeter.gui.action.ActionNames;
import org.apache.jmeter.gui.action.KeyStrokes;
import org.apache.jmeter.gui.util.JMeterToolBar;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.NetworkEmulatorTestElement;
import cz.vutbr.networkemulator.controller.NetworkEmulatorController;
import cz.vutbr.networkemulator.utils.NetworkEmulatorUtils;

public class NetworkEmulatorGui extends AbstractJMeterGuiComponent {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(NetworkEmulatorGui.class);

    private final NetworkEmulatorController controller;

    private ConfigurationPanel configurationPanel;
    private JButton btnStart;
    private JButton btnStop;
    private JLabel emulatorState;
    private RSyntaxTextArea currentSettings;

    public NetworkEmulatorGui() {
        controller = NetworkEmulatorController.getInstance();
        controller.initialize();
        init();
    }

    private void init() {
        registerIcon();
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, createTopPanel(), createConfigurationPanel());
        add(splitPane, BorderLayout.CENTER);
    }

    private JSplitPane createTopPanel() {
        JSplitPane topPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createSettingsPanel(), createControlPanel());
        topPanel.setResizeWeight(0.98);
        topPanel.setDividerLocation(0.98);
        topPanel.setOneTouchExpandable(true);

        return topPanel;
    }

    private JPanel createConfigurationPanel() {
        configurationPanel = new ConfigurationPanel();
        return configurationPanel;
    }

    private JPanel createSettingsPanel() {
        JPanel settingsPanel = new JPanel(new BorderLayout());
        settingsPanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorUtils.getString("title_network_settings")));

        currentSettings = new RSyntaxTextArea();
        currentSettings.setEditable(false);
        currentSettings.setSyntaxEditingStyle("text/css");

        JScrollPane scrollPane = new JScrollPane(currentSettings);
        scrollPane.setPreferredSize(scrollPane.getMinimumSize());
        scrollPane.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        currentSettings.setText(controller.getNetworkConfiguration());

        settingsPanel.add(scrollPane, BorderLayout.CENTER);

        return settingsPanel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        controlPanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorUtils.getString("title_controls")));

        String iconSize = JMeterUtils.getPropDefault(JMeterToolBar.TOOLBAR_ICON_SIZE,
                JMeterToolBar.DEFAULT_TOOLBAR_ICON_SIZE);
        ImageIcon startImage = JMeterUtils.getImage("toolbar/" + iconSize + "/arrow-right-3.png");
        btnStart = new JButton(NetworkEmulatorUtils.getString("button_start"));
        btnStart.setIcon(startImage);
        btnStart.setFocusable(false);
        btnStart.setEnabled(true);
        btnStart.addActionListener(this::toggleEmulator);

        btnStop = new JButton(NetworkEmulatorUtils.getString("button_stop"));
        ImageIcon stopImage = JMeterUtils.getImage("toolbar/" + iconSize + "/process-stop-4.png");
        btnStop.setIcon(stopImage);
        btnStop.setFocusable(false);
        btnStop.setEnabled(false);
        btnStop.addActionListener(this::toggleEmulator);

        emulatorState = new JLabel(NetworkEmulatorUtils.getString("msg_emulation_stopped"), JLabel.CENTER);

        controlPanel.add(btnStart);
        controlPanel.add(btnStop);
        controlPanel.add(emulatorState);

        return controlPanel;
    }

    private void toggleEmulator(ActionEvent evt) {
        final Object source = evt.getSource();
        if (source == btnStart) {
            configurationPanel.applySettingsToController();
            controller.runEmulation();
            btnStart.setEnabled(false);
            btnStop.setEnabled(true);
            configurationPanel.setEditable(false);
            currentSettings.setText(controller.getNetworkConfiguration());
            emulatorState.setText(NetworkEmulatorUtils.getString("msg_emulation_running"));
        } else if (source == btnStop) {
            controller.restoreNetworkConfiguration();
            btnStart.setEnabled(true);
            btnStop.setEnabled(false);
            configurationPanel.setEditable(true);
            currentSettings.setText(controller.getNetworkConfiguration());
            emulatorState.setText(NetworkEmulatorUtils.getString("msg_emulation_stopped"));
        }
    }

    public static void registerIcon() {
        String iconPath = "/cz/vutbr/networkemulator/images/network_emulator.gif";
        ImageIcon icon = new ImageIcon(NetworkEmulatorGui.class.getResource(iconPath));
        GUIFactory.registerIcon(NetworkEmulatorGui.class.getName(), icon);
    }

    @Override
    public String getLabelResource() {
        return NetworkEmulatorUtils.getString("network_emulator_label_resource");
    }

    @Override
    public String getStaticLabel() {
        return NetworkEmulatorUtils.getString("network_emulator_static_label");
    }

    @Override
    public TestElement makeTestElement() {
        return new NetworkEmulatorTestElement();
    }

    @Override
    public void clearGui() {
        super.clearGui();
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        configurationPanel.setEditable(true);
        currentSettings.setText(controller.getNetworkConfiguration());
        emulatorState.setText(NetworkEmulatorUtils.getString("msg_emulation_stopped"));
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.modifyTestElement(element);
        configurationPanel.modifyTestElement(element);

        if (element instanceof NetworkEmulatorTestElement networkEmulatorTestElement) {
            networkEmulatorTestElement.setEmulationRunning(controller.isEmulationRunning());
        }
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        configurationPanel.configure(element);

        NetworkEmulatorTestElement networkEmulatorTestElement = (NetworkEmulatorTestElement) element;
        boolean running = networkEmulatorTestElement.isEmulationRunning();
        btnStart.setEnabled(!running);
        btnStop.setEnabled(running);
        configurationPanel.setEditable(!running);
        currentSettings.setText(controller.getNetworkConfiguration());
        emulatorState.setText(running
                ? NetworkEmulatorUtils.getString("msg_emulation_running")
                : NetworkEmulatorUtils.getString("msg_emulation_stopped"));

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
