package me.xiaoying.moebroker.client;

import me.xiaoying.moebroker.api.BrokerAddress;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class BrokerClient {
    private final BrokerAddress address;

    public BrokerClient(BrokerAddress address) {
        this.address = address;
    }

    public void run() {
        try (Socket socket = new Socket(this.address.getHost(), this.address.getPort())) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject("Hello World");
            objectOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}