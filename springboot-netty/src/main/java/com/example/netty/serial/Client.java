package com.example.netty.serial;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;

/**
 * netty入门之编解码
 * @author qinxuewu
 * @version 1.00
 * @time 11/10/2018下午 6:03
 */
public class Client {
    public static class ClientHandler extends ChannelHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            try {
                Resp resp = (Resp)msg;
                System.out.println("客户端 : " + resp.getId() + ", " + resp.getName() + ", " + resp.getResponseMessage());
            } finally {
                ReferenceCountUtil.release(msg);
            }
        }
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
        }
    }

    public static void main(String[] args) {
        try {
            EventLoopGroup group = new NioEventLoopGroup();
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                            sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                            sc.pipeline().addLast(new ClientHandler());
                        }
                    });

            ChannelFuture cf = b.connect("127.0.0.1", 8889).sync();

            //循环5次 发送req对象
            for(int i = 0; i < 5; i++ ){
                Req req = new Req();
                req.setId("" + i);
                req.setName("pro" + i);
                req.setRequestMessage("数据信息" + i);
                cf.channel().writeAndFlush(req);
            }
            cf.channel().closeFuture().sync();
            group.shutdownGracefully();
        }catch (Exception e){

        }

    }
}
