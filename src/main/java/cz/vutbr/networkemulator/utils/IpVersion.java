package cz.vutbr.networkemulator.utils;

public enum IpVersion {
    IPv4("ipv4", "ip"),
    IPv6("ipv6", "ip6");

    private final String name;
    private final String tcCommand;

    private IpVersion(String name, String tcCommand) {
        this.name = name;
        this.tcCommand = tcCommand;
    }

    public String getName() {
        return name;
    }

    public String getTcCommand() {
        return tcCommand;
    }
}
