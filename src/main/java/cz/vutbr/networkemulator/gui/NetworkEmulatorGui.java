package cz.vutbr.networkemulator.gui;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.apache.jmeter.gui.AbstractJMeterGuiComponent;
import org.apache.jmeter.gui.GUIFactory;
import org.apache.jmeter.gui.action.ActionNames;
import org.apache.jmeter.gui.action.ActionRouter;
import org.apache.jmeter.gui.action.KeyStrokes;
import org.apache.jmeter.gui.util.JMeterToolBar;
import org.apache.jmeter.gui.util.JSyntaxTextArea;
import org.apache.jmeter.gui.util.JTextScrollPane;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.NetworkEmulatorTestElement;
import cz.vutbr.networkemulator.controller.NetworkEmulatorController;
import cz.vutbr.networkemulator.utils.NetworkEmulatorUtils;
import net.miginfocom.swing.MigLayout;

public class NetworkEmulatorGui extends AbstractJMeterGuiComponent {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(NetworkEmulatorGui.class);

    private final NetworkEmulatorController controller;
    private boolean isRunning;

    private ConfigurationPanel configurationPanel;
    private JButton btnStart;
    private JButton btnStop;
    // private JLabel emulatorState;
    private JSyntaxTextArea currentSettings;

    public NetworkEmulatorGui() {
        controller = NetworkEmulatorController.getInstance();
        controller.initialize();
        isRunning = false;
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
        topPanel.setResizeWeight(0.92);
        topPanel.setDividerLocation(0.92);
        topPanel.setOneTouchExpandable(true);

        return topPanel;
    }

    private JPanel createConfigurationPanel() {
        configurationPanel = new ConfigurationPanel();
        return configurationPanel;
    }

    private JPanel createSettingsPanel() {
        JPanel settingsPanel = new JPanel(new MigLayout("insets 0", "grow", "grow"));
        settingsPanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorUtils.getString("title_network_settings")));

        currentSettings = JSyntaxTextArea.getInstance(80, 30, true);
        currentSettings.setEditable(false);
        currentSettings.setLineWrap(true);
        currentSettings.setWrapStyleWord(true);
        currentSettings.setSyntaxEditingStyle("text/css");
        currentSettings.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        JScrollPane scrollPane = JTextScrollPane.getInstance(currentSettings);

        scrollPane.setPreferredSize(scrollPane.getMinimumSize());
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        currentSettings.setText(controller.getNetworkConfiguration());

        settingsPanel.add(scrollPane, "growx, growy, gap 0, pad 0");

        return settingsPanel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new MigLayout("insets 10", "grow", ""));
        controlPanel.setBorder(BorderFactory.createTitledBorder(NetworkEmulatorUtils.getString("title_controls")));

        String iconSize = JMeterUtils.getPropDefault(JMeterToolBar.TOOLBAR_ICON_SIZE,
                JMeterToolBar.DEFAULT_TOOLBAR_ICON_SIZE);
        ImageIcon startImage = JMeterUtils.getImage("toolbar/" + iconSize + "/arrow-right-3.png");
        btnStart = new JButton(NetworkEmulatorUtils.getString("button_start"));
        btnStart.setIcon(startImage);
        btnStart.setFocusable(false);
        btnStart.setEnabled(true);
        btnStart.addActionListener(ActionRouter.getInstance());
        btnStart.setActionCommand("start_emulation");

        btnStop = new JButton(NetworkEmulatorUtils.getString("button_stop"));
        ImageIcon stopImage = JMeterUtils.getImage("toolbar/" + iconSize + "/process-stop-4.png");
        btnStop.setIcon(stopImage);
        btnStop.setFocusable(false);
        btnStop.setEnabled(false);
        btnStop.addActionListener(ActionRouter.getInstance());
        btnStop.setActionCommand("stop_emulation");

        controlPanel.add(btnStart, "growx, wrap");
        controlPanel.add(btnStop, "growx");

        return controlPanel;
    }

    public void onEmulationStarted() {
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
        currentSettings.setText(controller.getNetworkConfiguration());
        configurationPanel.setEditable(false);
        isRunning = true;
    }

    public void onEmulationStopped() {
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        currentSettings.setText(controller.getNetworkConfiguration());
        configurationPanel.setEditable(true);
        isRunning = false;
    }

    public ConfigurationPanel getConfigurationPanel() {
        return configurationPanel;
    }

    public static void registerIcon() {
        ImageIcon icon = NetworkEmulatorUtils.getImage("network_emulator.gif");
        icon = NetworkEmulatorUtils.getScaledIcon(icon, 16, 16);
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
        NetworkEmulatorTestElement element = new NetworkEmulatorTestElement();
        configureTestElement(element);

        element.setEmulationRunning(false);
        return element;
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.modifyTestElement(element);
        configurationPanel.modifyTestElement(element);

        if (element instanceof NetworkEmulatorTestElement networkEmulatorTestElement) {
            networkEmulatorTestElement.setEmulationRunning(isRunning);
        }
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        configurationPanel.configure(element);

        boolean running = ((NetworkEmulatorTestElement) element).isEmulationRunning();

        btnStart.setEnabled(!running);
        btnStop.setEnabled(running);
        configurationPanel.setEditable(!running);
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
