package cz.vutbr.networkemulator.verification;

import java.util.regex.Pattern;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.utils.NetworkEmulatorConstants;

public class IpAddressVerifier extends InputVerifier {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(IpAddressVerifier.class);

    private static final Pattern IPV4_PATTERN = createIpv4Pattern();

    public static boolean isValid(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        return IPV4_PATTERN.matcher(text.trim()).matches();
    }

    @Override
    public boolean verify(JComponent input) {
        if (input instanceof JTextField field) {
            String text = field.getText().trim();

            if (text.isEmpty()) {
                return true;
            }

            boolean isValidIpv4Address = IPV4_PATTERN.matcher(text).matches();

            if (isValidIpv4Address) {
                return true;
            } else {
                JOptionPane.showMessageDialog(
                        null, NetworkEmulatorConstants.MSG_ENTER_VALID_IP_ADDRESS,
                        NetworkEmulatorConstants.BAD_ADDRESS,
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private static Pattern createIpv4Pattern() {
        StringBuilder ipv4Regex = new StringBuilder();
        ipv4Regex.append("((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}");
        ipv4Regex.append("(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])");

        return Pattern.compile(ipv4Regex.toString());
    }
}
