package me.xiaoying.moebroker.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import me.xiaoying.moebroker.api.BrokerAddress;
import me.xiaoying.moebroker.api.executor.ExecutorManager;
import me.xiaoying.moebroker.api.netty.MessageDecoder;
import me.xiaoying.moebroker.api.netty.MessageEncoder;
import me.xiaoying.moebroker.client.netty.ConnectionHandler;

public abstract class BrokerClient {
    private final BrokerAddress address;

    private ChannelFuture channelFuture;

    private EventLoopGroup group;

    public BrokerClient(final BrokerAddress address) {
        this.address = address;
    }

    public void run() {
        ExecutorManager.getExecutor("broker").execute(this::start);
    }

    private void start() {
        this.group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(this.group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new MessageEncoder())
                                    .addLast(new MessageDecoder())
                                    .addLast(new ConnectionHandler(BrokerClient.this));
                        }
                    });

            this.channelFuture = bootstrap.connect(this.address.getHost(), this.address.getPort());
            this.onStart();

            this.channelFuture.addListener((ChannelFuture future) -> {
                if (!future.isSuccess()) {
                    this.onErrorCaught(future.cause());
                    return;
                }

                this.onOpen();
            });

            this.channelFuture.channel().closeFuture().addListener((ChannelFuture future) -> {
                this.onClose();
            });

            this.channelFuture.sync();
            this.channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.group.close();
        }
    }

    public abstract void onStart();

    public abstract void onOpen();

    public abstract void onClose();

    public abstract void onErrorCaught(Throwable cause);
}