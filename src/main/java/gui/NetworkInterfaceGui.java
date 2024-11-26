package gui;

import org.apache.jmeter.gui.AbstractJMeterGuiComponent;
import org.apache.jmeter.gui.GUIFactory;
import org.apache.jmeter.gui.action.ActionNames;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;

import model.NetworkInterface;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

public class NetworkInterfaceGui extends AbstractJMeterGuiComponent {

    public NetworkInterfaceGui() {
        init();
        registerIcon();
    }

    @Override
    public String getStaticLabel() {
        return "Network Interface";
    }

    public static void registerIcon() {
        String jmeterHome = JMeterUtils.getJMeterHome();
        GUIFactory.registerIcon(NetworkInterfaceGui.class.getName(), new ImageIcon(jmeterHome + "/lib/ext/networkemulator/network_interface.png"));
    }

    @Override
    public String getLabelResource() {
        return null; // Pokud nepoužíváte překlad
    }

    @Override
    public TestElement makeTestElement() {
        NetworkInterface networkInterface = new NetworkInterface();
        modifyTestElement(networkInterface);
        return networkInterface;
    }

    @Override
    public void modifyTestElement(TestElement element) {
        super.configureTestElement(element);
    }

    private void init() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        Box box = Box.createVerticalBox();
        box.add(makeTitlePanel());
        add(box, BorderLayout.NORTH);

        JPanel threadPropsPanel = new JPanel(new MigLayout("fillx, wrap 2", "[][fill,grow]"));
        threadPropsPanel.setBorder(BorderFactory.createTitledBorder("Emulation parameters"));

        add(threadPropsPanel, BorderLayout.CENTER);
    }

    @Override
    public JPopupMenu createPopupMenu() {
        JPopupMenu pop = new JPopupMenu();
        pop.add(MenuFactory.makeMenuItem("Toggle", "toggle", ActionNames.TOGGLE));
        pop.add(MenuFactory.makeMenuItem("Remove", "remove", "remove_interface"));

        return pop;
    }

    @Override
    public Collection<String> getMenuCategories() {
        return null;
    }
}
