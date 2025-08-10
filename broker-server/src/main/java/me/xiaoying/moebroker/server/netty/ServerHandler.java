package me.xiaoying.moebroker.server.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<ClassProtocol> {
    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, ClassProtocol classProtocol) throws Exception {

    }
}