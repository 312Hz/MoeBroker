package me.xiaoying.moebroker.client.bootstrap;

import me.xiaoying.moebroker.api.BrokerAddress;

public class BootStrap {
    public static void main(String[] args) {
        Client client = new Client(new BrokerAddress("0.0.0.0", 22332));
        client.run();
    }
}