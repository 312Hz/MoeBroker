package me.xiaoying.moebroker.api;

public class RemoteClient {
    private final BrokerAddress address;

    public RemoteClient(BrokerAddress address) {
        this.address = address;
    }

    public BrokerAddress getAddress() {
        return this.address;
    }
}