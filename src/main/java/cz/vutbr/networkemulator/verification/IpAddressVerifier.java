package cz.vutbr.networkemulator.verification;

import java.util.regex.Pattern;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.networkemulator.utils.EmulatorUtils;
import cz.vutbr.networkemulator.utils.enums.IpVersion;

public class IpAddressVerifier extends InputVerifier {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(IpAddressVerifier.class);

    private final IpVersion version;

    private static final Pattern IPV4_PATTERN = createIpv4Pattern();
    private static final Pattern IPV6_PATTERN = createIpv6Pattern();

    public IpAddressVerifier(IpVersion version) {
        this.version = version;
    }

    @Override
    public boolean verify(JComponent input) {
        if (input instanceof JTextField field) {
            String text = field.getText().trim();

            if (text.isEmpty()) {
                return true;
            }

            if (version == IpVersion.IPv4) {
                if (IPV4_PATTERN.matcher(text).matches()) {
                    return true;
                } else {
                    JOptionPane.showMessageDialog(
                            null, EmulatorUtils.getString("msg_enter_valid_ipv4_address"),
                            EmulatorUtils.getString("msg_bad_ipv4_address"),
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            if (version == IpVersion.IPv6) {
                if (IPV6_PATTERN.matcher(text).matches()) {
                    return true;
                } else {
                    JOptionPane.showMessageDialog(
                            null, EmulatorUtils.getString("msg_enter_valid_ipv6_address"),
                            EmulatorUtils.getString("msg_bad_ipv6_address"),
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
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

    private static Pattern createIpv6Pattern() {
        StringBuilder ipv6Regex = new StringBuilder();
        ipv6Regex.append("(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|"); // 1:2:3:4:5:6:7:8
        ipv6Regex.append("([0-9a-fA-F]{1,4}:){1,7}:|"); // 1:: 1:2:3:4:5:6:7::
        ipv6Regex.append("([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|"); // 1::8 1:2:3:4:5:6::8 1:2:3:4:5:6::8
        ipv6Regex.append("([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|"); // 1::7:8 1:2:3:4:5::7:8 1:2:3:4:5::8
        ipv6Regex.append("([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|"); // 1::6:7:8 1:2:3:4::6:7:8 1:2:3:4::8
        ipv6Regex.append("([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|"); // 1::5:6:7:8 1:2:3::5:6:7:8 1:2:3::8
        ipv6Regex.append("([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|"); // 1::4:5:6:7:8 1:2::4:5:6:7:8 1:2::8
        ipv6Regex.append("[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|"); // 1::3:4:5:6:7:8 1::3:4:5:6:7:8 1::8
        ipv6Regex.append(":((:[0-9a-fA-F]{1,4}){1,7}|:)|"); // ::2:3:4:5:6:7:8 ::2:3:4:5:6:7:8 ::8 ::
        ipv6Regex.append("fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|"); // fe80::7:8%eth0 fe80::7:8%1
        ipv6Regex.append("::(ffff(:0{1,4}){0,1}:){0,1}");
        ipv6Regex.append("((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}");
        ipv6Regex.append("(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|"); // ::255.255.255.255 ::ffff:255.255.255.255
        // ::ffff:0:255.255.255.255
        ipv6Regex.append("([0-9a-fA-F]{1,4}:){1,4}:");
        ipv6Regex.append("((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}");
        ipv6Regex.append("(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))"); // 2001:db8:3:4::192.0.2.33 64:ff9b::192.0.2.33

        return Pattern.compile(ipv6Regex.toString());
    }
}
