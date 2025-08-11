package me.xiaoying.moebroker.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import me.xiaoying.moebroker.api.BrokerAddress;

public class BrokerClient {
    private final BrokerAddress address;

    public BrokerClient(BrokerAddress address) {
        this.address = address;
    }

    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new StringEncoder())
                                    .addLast(new StringDecoder());
                        }
                    });

            ChannelFuture future = bootstrap.connect(this.address.getHost(), this.address.getPort()).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.close();
        }



//        try (Socket socket = new Socket(this.address.getHost(), this.address.getPort())) {
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//            objectOutputStream.writeObject("Hello World");
//            objectOutputStream.flush();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}