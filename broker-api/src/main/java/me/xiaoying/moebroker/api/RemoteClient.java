package me.xiaoying.moebroker.api;

import io.netty.channel.Channel;

public class RemoteClient {
    private final BrokerAddress address;

    private final Channel channel;

    public RemoteClient(BrokerAddress address, Channel channel) {
        this.address = address;
        this.channel = channel;
    }

    public BrokerAddress getAddress() {
        return this.address;
    }

    public Channel getChannel() {
        return this.channel;
    }
}