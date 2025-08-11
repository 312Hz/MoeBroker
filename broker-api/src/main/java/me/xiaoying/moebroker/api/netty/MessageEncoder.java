package me.xiaoying.moebroker.api.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import me.xiaoying.moebroker.api.message.Message;
import me.xiaoying.moebroker.api.utils.SerializationUtil;

public class MessageEncoder extends MessageToByteEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        byte[] bytes = SerializationUtil.serialize(msg);

        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}