package me.xiaoying.moebroker.server.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.xiaoying.moebroker.api.Broker;
import me.xiaoying.moebroker.api.message.ObjectMessage;

public class ServerHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Object message) throws Exception {
        Broker.getLogger().info("接收到一个消息: {}", ((ObjectMessage) message).getObject().toString());
    }
}