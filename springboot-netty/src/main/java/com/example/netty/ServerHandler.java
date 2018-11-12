package com.example.netty;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;


/**
 * @author qinxuewu
 * @version 1.00
 * @time 9/10/2018下午 5:37
 */
public class ServerHandler extends ChannelHandlerAdapter{

    /**
     * 通道刚被激活时会调用次方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server channel active... ");
    }

    /**
     * 读取消息方法
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "utf-8");
        System.out.println("Server :" + body );

        String response = "进行返回给客户端的响应：" + body ;
        ctx.writeAndFlush(Unpooled.copiedBuffer(response.getBytes()));

    }

    /**
     * 读取完毕后处理方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("读完了");
        ctx.flush();
    }


    /***
     * 这个方法会在发生异常时触发

     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) throws Exception {
        /**
         * exceptionCaught() 事件处理方法是当出现 Throwable 对象才会被调用，即当 Netty 由于 IO
         * 错误或者处理器在处理事件时抛出的异常时。在大部分情况下，捕获的异常应该被记录下来 并且把关联的 channel
         * 给关闭掉。然而这个方法的处理方式会在遇到不同异常的情况下有不 同的实现，比如你可能想在关闭连接之前发送一个错误码的响应消息。
         */
        // 出现异常就关闭
        t.printStackTrace();
        ctx.close();
    }
}
