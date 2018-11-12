package com.example.netty;

import io.netty.channel.EventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author qinxuewu
 * @version 1.00
 * @time 9/10/2018下午 5:33
 */
public class Client {
    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(new ClientHandler()); //收到服务端发送过来的消息处理类
                    }
                });

        ChannelFuture cf1 = null;
        try {
            cf1 = b.connect("127.0.0.1", 8765).sync();
            //发送消息
            cf1.channel().writeAndFlush(Unpooled.copiedBuffer("777".getBytes()));
            cf1.channel().writeAndFlush(Unpooled.copiedBuffer("666".getBytes()));
            cf1.channel().writeAndFlush(Unpooled.copiedBuffer("888".getBytes()));

            //关闭通道
            cf1.channel().closeFuture().sync();
            group.shutdownGracefully();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
