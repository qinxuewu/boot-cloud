package com.example.netty.tcp;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.ReferenceCountUtil;

/**
 * tcp粘包 拆包  客户端
 * @author qinxuewu
 * @version 1.00
 * @time 11/10/2018下午 2:11
 */
public class Client {
    //消息响应处理
    public static  class ClientHander extends ChannelHandlerAdapter{

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            try {
                String response = (String)msg;
                System.out.println("客户端收到消息: " + response);
            } finally {
                // 抛弃收到的数据
                ReferenceCountUtil.release(msg);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
        }
    }


    public static void main(String[] args) throws  Exception{
        EventLoopGroup group=new NioEventLoopGroup();
        Bootstrap b=new Bootstrap();
        b.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                //消息响应处理
//                ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes());
//                socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf));
                //设置消息长度 解决粘包
                socketChannel.pipeline().addLast(new FixedLengthFrameDecoder(6));
                socketChannel.pipeline().addLast(new StringDecoder());
                socketChannel.pipeline().addLast(new ClientHander());
            }
        });

        ChannelFuture cf = b.connect("127.0.0.1", 8888).sync();
        //尾部加入分隔符
        cf.channel().writeAndFlush(Unpooled.wrappedBuffer("bbbb$_".getBytes()));
        cf.channel().writeAndFlush(Unpooled.wrappedBuffer("cccc$_".getBytes()));


        //等待客户端端口关闭
        cf.channel().closeFuture().sync();
        group.shutdownGracefully();
    }
}
