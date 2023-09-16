package org.example.app.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.example.app.utils.Constants;

import java.util.Scanner;

public final class NettyClient {

    public static void runClient() throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new StringDecoder());
                            p.addLast(new StringEncoder());
                            p.addLast(new ClientHandler());
                        }
                    });

            ChannelFuture f = b.connect(Constants.HOST, Constants.PORT).sync();

            Scanner scanner = new Scanner(System.in);
            System.out.print("Hey! Please enter your username: ");
            String input = scanner.nextLine();
            Channel channel = f.sync().channel();
            channel.writeAndFlush(input);
            channel.flush();

            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}