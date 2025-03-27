package cz.vutbr.networkemulator.gui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.controller.NetworkEmulatorController;

public class NetworkInterfacePanel extends JPanel {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(NetworkInterfacePanel.class);

    private final JLabel label;
    @SuppressWarnings("unused")
    private final NetworkEmulatorController controller;

    public NetworkInterfacePanel() {
        controller = NetworkEmulatorController.getInstance();

        setLayout(new BorderLayout());
        label = new JLabel("Vyber uzel");
        add(label, BorderLayout.CENTER);
    }

    public void update(String niName) {
        label.setText("Vybraný uzel: " + niName);
        revalidate();
        repaint();
    }
}
