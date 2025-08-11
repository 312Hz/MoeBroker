package me.xiaoying.moebroker.server.netty;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import me.xiaoying.moebroker.api.Broker;
import me.xiaoying.moebroker.server.BrokerServer;

public class ConnectionEventHandler extends ChannelHandlerAdapter {
    private final BrokerServer server;

    public ConnectionEventHandler(BrokerServer server) {
        this.server = server;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

        String host = ctx.channel().remoteAddress().toString();

        if (host.startsWith("/"))
            host = host.substring(1);

        Broker.getLogger().info("Connection channel registered: {}", host);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        String host = ctx.channel().remoteAddress().toString();

        if (host.startsWith("/"))
            host = host.substring(1);

        Broker.getLogger().info("Connection closed: {}", host);
    }
}