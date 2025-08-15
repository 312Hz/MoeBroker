package me.xiaoying.moebroker.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import me.xiaoying.moebroker.api.BrokerAddress;
import me.xiaoying.moebroker.api.RemoteClient;
import me.xiaoying.moebroker.api.executor.ExecutorManager;
import me.xiaoying.moebroker.api.netty.SerializableDecoder;
import me.xiaoying.moebroker.api.netty.SerializableEncoder;
import me.xiaoying.moebroker.api.processor.ProcessorManager;
import me.xiaoying.moebroker.server.netty.ConnectionHandler;
import me.xiaoying.moebroker.server.netty.MessageHandler;

public abstract class BrokerServer {
    private final BrokerAddress address;

    private ChannelFuture channelFuture;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private ProcessorManager processorManager;

    public BrokerServer(BrokerAddress address) {
        this.address = address;

        this.processorManager = new ProcessorManager();
    }

    public void run() {
        ExecutorManager.getExecutor("broker").execute(this::start);
    }

    private void start() {
        this.workerGroup = new NioEventLoopGroup();
        this.bossGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

         bootstrap.group(this.bossGroup, this.workerGroup)
                 .channel(NioServerSocketChannel.class)
                 .childHandler(new ChannelInitializer<SocketChannel>() {
                     @Override
                     protected void initChannel(SocketChannel ch) throws Exception {
                         ch.pipeline()
                                 .addLast(new SerializableEncoder())
                                 .addLast(new SerializableDecoder())
                                 .addLast(new MessageHandler(BrokerServer.this))
                                 .addLast(new ConnectionHandler(BrokerServer.this));
                     }
                 });

         this.channelFuture = bootstrap.bind(this.address.getHost(), this.address.getPort()).syncUninterruptibly();
         this.channelFuture.addListener((ChannelFutureListener) future -> {
             if (!future.isSuccess())
                 return;

             BrokerServer.this.onStart();
         });

        try {
            this.channelFuture.sync();
            this.channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            this.bossGroup.shutdownGracefully();
            this.workerGroup.shutdownGracefully();
        }
    }

    public BrokerAddress getAddress() {
        return this.address;
    }

    public ProcessorManager getProcessorManager() {
        return this.processorManager;
    }

    /**
     * 当 server 启动时会调用此方法
     */
    public abstract void onStart();

    /**
     * 获取新请求时会调用此方法
     */
    public abstract void onOpen(RemoteClient remote);

    /**
     * 连接关闭时会调用此方法
     */
    public abstract void onClose(RemoteClient remote);

    /**
     * 接收消息时会调用此方法
     */
    public abstract void onMessage();

    /**
     * 报错触发方法
     */
    public void onError(RemoteClient remote, Throwable cause) {
        if (!remote.getChannel().isActive()) {
            this.onClose(remote);
            return;
        }

        cause.printStackTrace();
    }

//    public abstract void onError(RemoteClient remote, Throwable cause);
}