package me.xiaoying.moebroker.client.netty;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import me.xiaoying.moebroker.client.BrokerClient;

public class ConnectionHandler extends ChannelHandlerAdapter {
    private final BrokerClient client;

    public ConnectionHandler(final BrokerClient client) {
        this.client = client;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        this.client.onError(cause);
    }
}