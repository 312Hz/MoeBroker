package me.xiaoying.moebroker.client.bootstrap;

import me.xiaoying.moebroker.api.Broker;
import me.xiaoying.moebroker.api.BrokerAddress;
import me.xiaoying.moebroker.client.BrokerClient;

public class Client extends BrokerClient {
    public Client(BrokerAddress address) {
        super(address);
    }

    @Override
    public void onStart() {
//        this.sendMessage(new ObjectMessage("Hello World"));
    }

    @Override
    public void onOpen() {
        Broker.getLogger().info("Connected to MoeBroker server");
    }

    @Override
    public void onClose() {

    }

    @Override
    public void onErrorCaught(Throwable cause) {

    }
}