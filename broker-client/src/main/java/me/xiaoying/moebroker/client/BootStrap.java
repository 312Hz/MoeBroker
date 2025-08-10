package me.xiaoying.moebroker.client;

import me.xiaoying.moebroker.api.Broker;
import me.xiaoying.moebroker.api.BrokerAddress;

public class BootStrap {
    public static void main(String[] args) {
        BrokerClient brokerClient = new BrokerClient(new BrokerAddress("0.0.0.0", 22332));
        brokerClient.run();
    }
}