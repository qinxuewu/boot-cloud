package com.im.service;

import com.im.utils.NettyConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 服务启动类  测试使用
 *
 * @author qinxuewu
 * @create 18/10/13上午10:04
 * @since 1.0.0
 */


public class ServiceStart {

    public static void main(String[] args) {
        // 启用两个Reactor线程池【netty是基于NIO的，基于线程处理的】
        // 用于接收Client端连接的
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 进行网络通信读写
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            // 创建一个辅助类Bootstrap，就是对我们的Server进行一系列的配置
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup);
            // 指定使用NioServerSocketChannel这种类型的通道
            b.channel(NioServerSocketChannel.class);
            /*
             *  使用 childHandler 去初始化服务器
             *  添加handler，用来监听已经连接的客户端的Channel的动作和状态。
             *
             *  被绑定的MyWebSocketChannelHandler()里面设置了服务端初始化参数以及
             *
             */
            b.childHandler(new SocketChannelHandler());

            System.out.println("服务端开启等待客户端连接....");
            Channel ch = b.bind(NettyConfig.port).sync().channel();
            ch.closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //优雅的退出程序
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
