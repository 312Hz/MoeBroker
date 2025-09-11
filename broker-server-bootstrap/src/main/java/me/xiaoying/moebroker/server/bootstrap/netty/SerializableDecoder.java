package me.xiaoying.moebroker.server.bootstrap.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import me.xiaoying.moebroker.api.utils.SerializationUtil;
import me.xiaoying.moebroker.server.bootstrap.api.BCore;
import me.xiaoying.moebroker.server.bootstrap.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class SerializableDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4)
            return;

        in.markReaderIndex();

        int length = in.readInt();

        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }

        byte[] bytes = new byte[length];
        in.readBytes(bytes);

        List<ClassLoader> classloaders = new ArrayList<>();
        for (Plugin plugin : BCore.getPluginManager().getPlugins())
            classloaders.add(plugin.getClass().getClassLoader());

        Object obj = SerializationUtil.deserialize(bytes, classloaders);
        out.add(obj);
    }
}