package com.example.netty.serial;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * netty入门之编解码
 * @author qinxuewu
 * @version 1.00
 * @time 11/10/2018下午 6:03
 */
public class Server {
    public static  class ServerHandler extends ChannelHandlerAdapter{
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                ctx.close();
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println(msg);
            Req req = (Req)msg;
            //将客户端传过来的信息进行打印，然后将数据传入返回的实体类Resp，将实体类Resp返回给客户端
            System.out.println("Server : " + req.getId() + ", " + req.getName() + ", " + req.getRequestMessage());

            Resp resp = new Resp();
            resp.setId(req.getId());
            resp.setName("resp" + req.getId());
            resp.setResponseMessage("响应内容" + req.getId());
            ctx.writeAndFlush(resp);//.addListener(ChannelFutureListener.CLOSE);
        }
    }

    public static void main(String[] args) {

        try {
            EventLoopGroup pGroup = new NioEventLoopGroup();
            EventLoopGroup cGroup = new NioEventLoopGroup();

            ServerBootstrap b = new ServerBootstrap();
            b.group(pGroup, cGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel sc) throws Exception {
                            sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                            sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                            sc.pipeline().addLast(new ServerHandler());
                        }
                    });

            ChannelFuture cf = b.bind(8889).sync();

            cf.channel().closeFuture().sync();
            pGroup.shutdownGracefully();
            cGroup.shutdownGracefully();
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
