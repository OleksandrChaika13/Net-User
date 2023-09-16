package org.example.app.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.app.utils.Constants;

import java.util.ArrayList;
import java.util.List;

@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    static final List<Channel> channels = new ArrayList<>();

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        System.out.println(Constants.USER_JOIN_MSG + ctx);
        channels.add(ctx.channel());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        System.out.println(Constants.MSG_RECEIVE_MSG + msg);
        for (Channel c : channels) {
            c.writeAndFlush(Constants.SERVER_HELLO_MSG + msg + Constants.SERVER_HELLO2_MSG + '\n');
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println(Constants.SERVER_CLOSE_MSG + ctx);
        ctx.close();
    }
}