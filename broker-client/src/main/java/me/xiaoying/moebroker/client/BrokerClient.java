package me.xiaoying.moebroker.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import me.xiaoying.moebroker.api.BrokerAddress;
import me.xiaoying.moebroker.api.message.Message;
import me.xiaoying.moebroker.api.netty.MessageDecoder;
import me.xiaoying.moebroker.api.netty.MessageEncoder;

public abstract class BrokerClient {
    private final BrokerAddress address;

    private volatile ChannelFuture future;

    private volatile boolean connected = false;

    public abstract void onStart();

    public abstract void onClose();

    public abstract void onErrorCaught();

    public BrokerClient(BrokerAddress address) {
        this.address = address;
    }

    public void run() {
        new Thread(() -> {
            EventLoopGroup group = new NioEventLoopGroup();

            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(group)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline()
                                        .addLast(new MessageEncoder())
                                        .addLast(new MessageDecoder());
                            }
                        });

                this.future = bootstrap.connect(this.address.getHost(), this.address.getPort());

                this.future.addListener((ChannelFutureListener) channelFuture -> {
                    if (!future.isSuccess())
                        return;

                    this.connected = true;
                    this.onStart();
                });

                this.future.sync();
                this.future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                group.close();
            }
        }).start();
    }

    public void sendMessage(Message message) {
        this.future.channel().writeAndFlush(message);
    }
}