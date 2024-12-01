package cz.vutbr.networkemulator.gui.control;

import javax.swing.JPanel;

public class NetworkEmulatorConfigGui extends JPanel {

    private String networkInterfaceName; 

    public String getNetworkInterfaceName() {
        return networkInterfaceName;
    }

    public NetworkEmulatorConfigGui(String networkInterfaceName) {
        super();
        this.networkInterfaceName = networkInterfaceName;
    }

}
