package me.xiaoying.moebroker.server.netty;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import me.xiaoying.moebroker.api.BrokerAddress;
import me.xiaoying.moebroker.api.RemoteClient;
import me.xiaoying.moebroker.api.executor.ExecutorManager;
import me.xiaoying.moebroker.server.BrokerServer;

public class ConnectionHandler extends ChannelHandlerAdapter {
    private final BrokerServer server;

    public ConnectionHandler(BrokerServer server) {
        this.server = server;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);

        ExecutorManager.getExecutor("broker").execute(() -> {
            RemoteClient remoteClient = this.getRemoteClient(ctx);
            this.server.captureClient(remoteClient);
            this.server.onOpen(remoteClient);
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        this.server.onError(this.getRemoteClient(ctx), cause);
    }

    private RemoteClient getRemoteClient(ChannelHandlerContext ctx) {
        String host = ctx.channel().remoteAddress().toString();

        if (host.startsWith("/"))
            host = host.substring(1);

        String[] split = host.split(":");

        return new RemoteClient(new BrokerAddress(split[0], Integer.parseInt(split[1])), ctx.channel());
    }
}