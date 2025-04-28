package cz.vutbr.networkemulator.utils;

public enum Protocol {
    TCP("tcp", 6),
    UDP("udp", 17),
    ICMP("icmp", 1);

    private final String name;
    private final int number;

    private Protocol(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }
}
