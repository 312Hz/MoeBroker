package me.xiaoying.moebroker.server;

import me.xiaoying.moebroker.api.Broker;
import me.xiaoying.moebroker.api.BrokerAddress;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;

public class BrokerServer {
    private final BrokerAddress address;

    public BrokerServer(BrokerAddress address) {
        this.address = address;
    }

    public void run() {
        long start = System.currentTimeMillis();

        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(this.address.getHost(), this.address.getPort()));
            Broker.getLogger().info("Starting MoeBroker server on {}:{}", this.address.getPort());
            Broker.getLogger().info("Done({}s)! For help, type \"help\"", new DecimalFormat("0.000").format((double) (System.currentTimeMillis() - start) / 1000));

            while (true) {
                Socket accept = serverSocket.accept();
                ObjectInputStream inputStream = new ObjectInputStream(accept.getInputStream());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}