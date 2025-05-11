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

import cz.vutbr.networkemulator.EmulatorTestElement;
import cz.vutbr.networkemulator.controller.EmulatorController;
import cz.vutbr.networkemulator.utils.EmulatorUtils;
import net.miginfocom.swing.MigLayout;

public class EmulatorGui extends AbstractJMeterGuiComponent {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(EmulatorGui.class);

    private final EmulatorController controller;
    private boolean isRunning;

    private ConfigurationPanel configurationPanel;
    private JButton btnStart;
    private JButton btnStop;
    private JSyntaxTextArea currentSettings;

    public EmulatorGui() {
        controller = EmulatorController.getInstance();
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
        JSplitPane topPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createStatePanel(), createControlPanel());
        topPanel.setResizeWeight(0.92);
        topPanel.setDividerLocation(0.92);
        topPanel.setOneTouchExpandable(true);

        return topPanel;
    }

    private JPanel createConfigurationPanel() {
        configurationPanel = new ConfigurationPanel();
        return configurationPanel;
    }

    private JPanel createStatePanel() {
        JPanel statePanel = new JPanel(new MigLayout("insets 0", "grow", "grow"));
        statePanel.setBorder(BorderFactory.createTitledBorder(EmulatorUtils.getString("title_state_panel")));

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

        statePanel.add(scrollPane, "growx, growy, gap 0, pad 0");

        return statePanel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new MigLayout("insets 5", "grow", ""));
        controlPanel.setBorder(BorderFactory.createTitledBorder(EmulatorUtils.getString("title_controls")));

        String iconSize = JMeterUtils.getPropDefault(JMeterToolBar.TOOLBAR_ICON_SIZE,
                JMeterToolBar.DEFAULT_TOOLBAR_ICON_SIZE);
        ImageIcon startImage = JMeterUtils.getImage("toolbar/" + iconSize + "/arrow-right-3.png");
        btnStart = new JButton(EmulatorUtils.getString("button_start"));
        btnStart.setIcon(startImage);
        btnStart.setFocusable(false);
        btnStart.setEnabled(true);
        btnStart.addActionListener(ActionRouter.getInstance());
        btnStart.setActionCommand("start_emulation");

        btnStop = new JButton(EmulatorUtils.getString("button_stop"));
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
        ImageIcon icon = EmulatorUtils.getImage("network_emulator.gif");
        icon = EmulatorUtils.getScaledIcon(icon, 16, 16);
        GUIFactory.registerIcon(EmulatorGui.class.getName(), icon);
    }

    @Override
    public String getLabelResource() {
        return EmulatorUtils.getString("network_emulator_label_resource");
    }

    @Override
    public String getStaticLabel() {
        return EmulatorUtils.getString("network_emulator_static_label");
    }

    @Override
    public TestElement makeTestElement() {
        EmulatorTestElement element = new EmulatorTestElement();
        configureTestElement(element);

        element.setEmulationRunning(false);
        return element;
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.modifyTestElement(element);
        configurationPanel.modifyTestElement(element);

        if (element instanceof EmulatorTestElement emulatorTestElement) {
            emulatorTestElement.setEmulationRunning(isRunning);
        }
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        configurationPanel.configure(element);

        boolean running = ((EmulatorTestElement) element).isEmulationRunning();

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
