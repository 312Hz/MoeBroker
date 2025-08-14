package me.xiaoying.moebroker.server.bootstrap;

import me.xiaoying.moebroker.api.Broker;
import me.xiaoying.moebroker.api.BrokerAddress;
import me.xiaoying.moebroker.api.RemoteClient;
import me.xiaoying.moebroker.server.BrokerServer;

import java.util.ArrayList;
import java.util.List;

public class Server extends BrokerServer {
    private final List<Runnable> start = new ArrayList<>();

    public Server(BrokerAddress address) {
        super(address);
    }

    @Override
    public void onStart() {
        Broker.getLogger().info("Starting MoeBroker server on {}:{}", this.getAddress().getHost(), this.getAddress().getPort());

        this.start.forEach(Runnable::run);
    }

    public BrokerServer onStart(Runnable runnable) {
        this.start.add(runnable);
        return this;
    }

    @Override
    public void onOpen(RemoteClient remote) {
        Broker.getLogger().info("Connection channel registered: {}:{}", remote.getAddress().getHost(), remote.getAddress().getPort());
    }

    @Override
    public void onClose() {

    }

    @Override
    public void onMessage() {

    }

    @Override
    public void onError(RemoteClient remote, Throwable cause) {

    }
}