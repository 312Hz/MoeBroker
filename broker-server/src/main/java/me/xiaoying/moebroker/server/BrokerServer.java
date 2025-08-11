package me.xiaoying.moebroker.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import me.xiaoying.moebroker.api.BrokerAddress;
import me.xiaoying.moebroker.api.netty.MessageDecoder;
import me.xiaoying.moebroker.api.netty.MessageEncoder;
import me.xiaoying.moebroker.server.netty.ConnectionEventHandler;
import me.xiaoying.moebroker.server.netty.ServerHandler;

import java.net.InetSocketAddress;

public abstract class BrokerServer {
    private final BrokerAddress address;
    private volatile boolean running = false;

    public BrokerServer(BrokerAddress address) {
        this.address = address;
    }

    /**
     * 当 server 启动时会调用此方法
     */
    public abstract void onStart();

    /**
     * 获取新请求时会调用此方法
     */
    public abstract void onOpen();

    /**
     * 连接关闭时会调用此方法
     */
    public abstract void onClose();

    /**
     * 接收消息时会调用此方法
     */
    public abstract void onMessage();

    /**
     * 报错触发方法
     */
    public abstract void onErrorCaught();

    public void run() {
        new Thread(() -> {
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            ServerBootstrap bootstrap = new ServerBootstrap();

            try (EventLoopGroup bossGroup = new NioEventLoopGroup()) {
                bootstrap.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                socketChannel.pipeline()
                                        .addLast(new MessageEncoder())
                                        .addLast(new MessageDecoder())
                                        .addLast(new ServerHandler())
                                        .addLast(new ConnectionEventHandler(BrokerServer.this));
                            }
                        });

                ChannelFuture future = bootstrap.bind(new InetSocketAddress(this.address.getHost(), this.address.getPort()));

                future.addListener((ChannelFutureListener) channelFuture -> {
                    if (!future.isSuccess())
                        return;

                    this.running = true;
                    this.onStart();
                });

                future.sync();
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
            }
        }).start();
    }

    public BrokerAddress getAddress() {
        return this.address;
    }

    public boolean isRunning() {
        return this.running;
    }
}