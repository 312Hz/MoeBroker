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
import me.xiaoying.moebroker.api.Broker;
import me.xiaoying.moebroker.api.BrokerAddress;

import java.net.InetSocketAddress;
import java.text.DecimalFormat;

public class BrokerServer {
    private final BrokerAddress address;

    public BrokerServer(BrokerAddress address) {
        this.address = address;
    }

    public void run() {
        long start = System.currentTimeMillis();

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();

        try (EventLoopGroup bossGroup = new NioEventLoopGroup()) {
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new StringDecoder())
                                    .addLast(new StringEncoder())
                                    .addLast();
                        }
                    });

            ChannelFuture future = bootstrap.bind(new InetSocketAddress(this.address.getHost(), this.address.getPort()));

            future.addListener((ChannelFutureListener) channelFuture -> {
                if (!future.isSuccess())
                    return;

                Broker.getLogger().info("Starting MoeBroker server on {}:{}", BrokerServer.this.address.getHost(), BrokerServer.this.address.getPort());
                Broker.getLogger().info("Done({}s)! For help, type \"help\"", new DecimalFormat("0.000").format((double) (System.currentTimeMillis() - start) / 1000));
            });

            future.sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}