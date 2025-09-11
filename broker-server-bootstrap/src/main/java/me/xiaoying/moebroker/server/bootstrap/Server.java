package me.xiaoying.moebroker.server.bootstrap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import me.xiaoying.moebroker.api.Broker;
import me.xiaoying.moebroker.api.BrokerAddress;
import me.xiaoying.moebroker.api.RemoteClient;
import me.xiaoying.moebroker.api.executor.ExecutorManager;
import me.xiaoying.moebroker.api.message.heartbeat.HeartbeatPingMessage;
import me.xiaoying.moebroker.server.BrokerServer;
import me.xiaoying.moebroker.server.bootstrap.netty.SerializableDecoder;
import me.xiaoying.moebroker.server.bootstrap.netty.SerializableEncoder;
import me.xiaoying.moebroker.server.netty.ConnectionHandler;
import me.xiaoying.moebroker.server.netty.MessageHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Server extends BrokerServer {
    private final List<Runnable> start = new ArrayList<>();

    public Server(BrokerAddress address) {
        super(address);
    }

    @Override
    protected void start() {
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
                                .addLast(new MessageHandler(Server.this))
                                .addLast(new ConnectionHandler(Server.this));
                    }
                });

        this.channelFuture = bootstrap.bind(this.getAddress().getHost(), this.getAddress().getPort()).syncUninterruptibly();
        this.channelFuture.addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess())
                return;

            this.running = true;
            ExecutorManager.getExecutor("broker").execute(this::onStart);
            this.scheduledFutures.add(ExecutorManager.getScheduledExecutor("heartbeat").scheduleWithFixedDelay(() -> this.invokeSync(new HeartbeatPingMessage()), 30, 30, TimeUnit.SECONDS));
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

    @Override
    public void onStart() {
        Broker.getLogger().info("Starting MoeBroker server on {}:{}", this.getAddress().getHost(), this.getAddress().getPort());

        this.start.forEach(Runnable::run);
    }

    public BrokerServer onStart(Runnable runnable) {
        this.start.add(runnable);
        return this;
    }

    @Override
    public void onOpen(RemoteClient remote) {
        Broker.getLogger().info("Connection channel registered: {}:{}", remote.getAddress().getHost(), remote.getAddress().getPort());
    }

    @Override
    public void onClose(RemoteClient remote) {
        Broker.getLogger().info("Connection closed: {}:{}", remote.getAddress().getHost(), remote.getAddress().getPort());
    }
}