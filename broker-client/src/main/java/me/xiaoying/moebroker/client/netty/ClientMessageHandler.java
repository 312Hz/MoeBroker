package me.xiaoying.moebroker.client.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.xiaoying.moebroker.api.Broker;
import me.xiaoying.moebroker.api.BrokerAddress;
import me.xiaoying.moebroker.api.RemoteClient;
import me.xiaoying.moebroker.api.message.Message;
import me.xiaoying.moebroker.api.message.MessageHelper;
import me.xiaoying.moebroker.api.message.RequestMessage;
import me.xiaoying.moebroker.api.message.ResponseMessage;
import me.xiaoying.moebroker.api.processor.AbstractProcessor;
import me.xiaoying.moebroker.api.processor.AsyncProcessor;
import me.xiaoying.moebroker.client.BrokerClient;

import java.io.Serializable;

public class ClientMessageHandler extends SimpleChannelInboundHandler<Serializable> {
    private final BrokerClient client;

    public ClientMessageHandler(final BrokerClient client) {
        this.client = client;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Serializable msg) throws Exception {
        if (!(msg instanceof Message))
            return;

        Message message = (Message) msg;

        if (message instanceof RequestMessage) {
            this.handleRequestMessage(ctx, (RequestMessage) message);
            return;
        }

        this.handleResponseMessage(ctx, (ResponseMessage) message);
    }

    @SuppressWarnings("unchecked")
    private void handleRequestMessage(ChannelHandlerContext ctx, RequestMessage message) {
        Object object = message.getObject();

        if (object == null)
            return;

        AbstractProcessor processor = this.client.getProcessorManager().getProcessor(object.getClass());

        try {
            if (processor instanceof AsyncProcessor) {
                processor.asyncRequest(this.getRemoteClient(ctx), object);
                return;
            }

            Object result = processor.syncRequest(this.getRemoteClient(ctx), object);

            if (!message.isNeedResponse())
                return;

            MessageHelper.receiveMessage(new ResponseMessage().setTarget(message.getUuid()).setObject(result));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleResponseMessage(ChannelHandlerContext ctx, ResponseMessage message) {
        Object object = message.getObject();

        if (object == null)
            return;

        Message msg = MessageHelper.getMessage(message.getTarget());

        if (!(msg instanceof RequestMessage))
            return;

        RequestMessage requestMessage = (RequestMessage) msg;

        requestMessage.getFuture().complete(object);
    }

    private RemoteClient getRemoteClient(ChannelHandlerContext ctx) {
        String host = ctx.channel().remoteAddress().toString();

        if (host.startsWith("/"))
            host = host.substring(1);

        String[] split = host.split(":");

        return new RemoteClient(new BrokerAddress(split[0], Integer.parseInt(split[1])), ctx.channel());
    }
}