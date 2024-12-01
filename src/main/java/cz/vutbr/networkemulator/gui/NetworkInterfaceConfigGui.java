package cz.vutbr.networkemulator.gui;

import javax.swing.JPanel;

public class NetworkInterfaceConfigGui extends JPanel {

    private String name; 

    public String getName() {
        return name;
    }

    public NetworkInterfaceConfigGui(String name) {
        super();
        this.name = String.format("Interface '%s'", name);
    }

}
