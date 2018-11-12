package com.example.netty.tcp;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author qinxuewu
 * @version 1.00
 * @time 11/10/2018下午 2:44
 */
public class Server {
    public static  class ServerHander extends ChannelHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            String request = (String)msg;
            System.out.println("服务端收到的消息 :" + msg);
            String response = msg + "$_";
            ctx.writeAndFlush(Unpooled.copiedBuffer(response.getBytes()));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
        }
    }
    public static void main(String[] args) throws Exception {
        //1 创建2个线程，一个是负责接收客户端的连接。一个是负责进行数据传输的
        EventLoopGroup pGroup = new NioEventLoopGroup();
        EventLoopGroup cGroup = new NioEventLoopGroup();
        //2 创建辅助工具类，用于服务器通道的一系列配置
        ServerBootstrap b = new ServerBootstrap();
        b.group(pGroup, cGroup)		//绑定俩个线程组
                .channel(NioServerSocketChannel.class)		//指定NIO的模式
                .option(ChannelOption.SO_BACKLOG, 1024)		//设置tcp缓冲区
                .option(ChannelOption.SO_SNDBUF, 32*1024)	//设置发送缓冲大小
                .option(ChannelOption.SO_RCVBUF, 32*1024)	//这是接收缓冲大小
                .option(ChannelOption.SO_KEEPALIVE, true)	//保持连接
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
//                        //设置特殊分隔符
//                        ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes());
//                        sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf));
                        //设置消息长度 解决粘包
                        sc.pipeline().addLast(new FixedLengthFrameDecoder(6));
                        //设置字符串形式的解码
                        sc.pipeline().addLast(new StringDecoder());
                        sc.pipeline().addLast(new ServerHander());
                    }
                });

        //4 进行绑定
        ChannelFuture cf1 = b.bind(8888).sync();
        //5 等待关闭
        cf1.channel().closeFuture().sync();
        pGroup.shutdownGracefully();
        cGroup.shutdownGracefully();

    }
}
