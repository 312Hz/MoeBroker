package me.xiaoying.moebroker.client.bootstrap;

import me.xiaoying.logger.LoggerFactory;
import me.xiaoying.logger.event.EventHandle;
import me.xiaoying.moebroker.api.Broker;
import me.xiaoying.moebroker.api.BrokerAddress;
import me.xiaoying.moebroker.client.bootstrap.logger.LoggerListener;

public class BootStrap {
    public static void main(String[] args) {
        EventHandle.registerEvent(new LoggerListener().initialize());

        Broker.setLogger(LoggerFactory.getLogger());

        Client client = new Client(new BrokerAddress("0.0.0.0", 22332));
        client.run();
    }
}