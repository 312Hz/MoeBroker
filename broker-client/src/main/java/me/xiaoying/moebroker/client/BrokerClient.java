package me.xiaoying.moebroker.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import me.xiaoying.moebroker.api.BrokerAddress;
import me.xiaoying.moebroker.api.executor.ExecutorManager;
import me.xiaoying.moebroker.api.message.MessageHelper;
import me.xiaoying.moebroker.api.message.RequestMessage;
import me.xiaoying.moebroker.api.netty.SerializableDecoder;
import me.xiaoying.moebroker.api.netty.SerializableEncoder;
import me.xiaoying.moebroker.api.processor.ProcessorManager;
import me.xiaoying.moebroker.client.netty.ClientMessageHandler;
import me.xiaoying.moebroker.client.netty.ConnectionHandler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class BrokerClient {
    private final ProcessorManager processorManager;

    private final BrokerAddress address;

    private ChannelFuture channelFuture;

    private EventLoopGroup bossGroup;

    private DefaultEventExecutorGroup workerGroup;

    public BrokerClient(final BrokerAddress address) {
        this.address = address;
        this.processorManager = new ProcessorManager();
    }

    public void run() {
        ExecutorManager.getExecutor("broker").execute(this::start);
    }

    private void start() {
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new DefaultEventExecutorGroup(8);

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(this.bossGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(BrokerClient.this.workerGroup, new SerializableEncoder())
                                    .addLast(BrokerClient.this.workerGroup, new SerializableDecoder())
                                    .addLast(BrokerClient.this.workerGroup, new ClientMessageHandler(BrokerClient.this))
                                    .addLast(BrokerClient.this.workerGroup, new ConnectionHandler(BrokerClient.this));
                        }
                    });

            this.channelFuture = bootstrap.connect(this.address.getHost(), this.address.getPort());
            ExecutorManager.getExecutor("broker").execute(this::onStart);

            this.channelFuture.addListener((ChannelFuture future) -> {
                if (!future.isSuccess())
                    return;

                ExecutorManager.getExecutor("broker").execute(this::onOpen);
            });

            this.channelFuture.channel().closeFuture().addListener((ChannelFuture future) -> ExecutorManager.getExecutor("broker").execute(this::onClose));

            this.channelFuture.sync();
            this.channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.bossGroup.close();
        }
    }

    public void oneway(Object object) {
        this.channelFuture.channel().writeAndFlush(new RequestMessage(object));
    }

    public Object invokeSync(Object object) {
        CompletableFuture<Object> future = new CompletableFuture<>();

        RequestMessage message = new RequestMessage(object)
                .setChannel(this.channelFuture.channel())
                .setFuture(future)
                .setNeedResponse(true);

        MessageHelper.captureMessage(message, message.getChannel());

        this.channelFuture.channel().writeAndFlush(message);

        try {
            return future.get(5000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    public ProcessorManager getProcessorManager() {
        return this.processorManager;
    }

    public abstract void onStart();

    public abstract void onOpen();

    public abstract void onClose();

    public abstract void onError(Throwable cause);
}