package com.example.netty.websocket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * https://blog.csdn.net/weixin_39168678/article/details/79453585(点对点发消息)
 * https://blog.csdn.net/xzknet/article/details/78281195
 * https://github.com/waylau/essential-netty-in-action
 * @author qinxuewu
 * @version 1.00
 * @time 12/10/2018下午 5:54
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler {
    /**
     * 通道列表
     */
    public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServerHandler.class.getName());

    private WebSocketServerHandshaker handshaker;

    /**
     * 服务端监听到客户端活动
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        System.err.println("Client:"+incoming.remoteAddress()+"在线");
    }

    /**
     * 服务端监听到客户端不活动
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        System.err.println("Client:"+incoming.remoteAddress()+"掉线不活动");
    }

    /**
     * 每当从服务端收到客户端断开时，客户端的 Channel 移除 ChannelGroup 列表中，并通知列表中的其他客户端 Channel
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : group) {
            channel.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + incoming.remoteAddress() + " 离开"));
        }
        System.err.println("Client:"+incoming.remoteAddress() +"离开");
        group.remove(ctx.channel());

    }

    /**
     * 每当从服务端收到新的客户端连接时，客户端的 Channel 存入 ChannelGroup 列表中，并通知列表中的其他客户端 Channel
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : group) {
            channel.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + incoming.remoteAddress() + " 加入"));
        }
        group.add(ctx.channel());
        System.err.println("Client:"+incoming.remoteAddress() +"加入");

    }

    /**
     * 异常处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        Channel incoming = ctx.channel();
        System.err.println("Client:"+incoming.remoteAddress()+"异常");

        cause.printStackTrace();
        ctx.close();
    }

//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        logger.info("channelReadComplete..........");
//        ctx.flush();
//    }

    /*
     * 功能：读空闲时移除Channel
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent evnet = (IdleStateEvent) evt;
            // 判断Channel是否读空闲, 读空闲时移除Channel
            if (evnet.state().equals(IdleState.READER_IDLE)) {
//                UserInfoManager.removeChannel(ctx.channel());
            }
        }

    }

    /**
     * 每当从服务端读到客户端写入信息时，将信息转发给其他客户端的 Channel。其中如果你使用的是 Netty 5.x 版本时，需要把 channelRead0() 重命名为messageReceived()
     * @param ctx
     * @param msg
     */
    @Override
    public void messageReceived(ChannelHandlerContext ctx, Object msg){
        try {
            logger.info("收到客户端消息：{}",msg);

            if (msg instanceof FullHttpRequest) {
                // 如果是HTTP请求，进行HTTP操作
                handleHttpRequest(ctx, (FullHttpRequest) msg);
            }else if (msg instanceof WebSocketFrame) {
                // 如果是Websocket请求，则进行websocket操作
                handleWebSocketFrame(ctx, (WebSocketFrame) msg);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    /**
     * http协议请求处理方法
     * @param ctx
     * @throws Exception
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        logger.info("http协议请求处理方法...........");
        // 如果HTTP解码失败，返回HHTP异常
        if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
            return;
        }

        // 构造握手响应返回，本机测试
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://localhost:8080/websocket", null, false);
        handshaker = wsFactory.newHandshaker(req);

        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {

            handshaker.handshake(ctx.channel(), req);
        }
    }

    /**
     * WebSocket消息处理方法
     * @param ctx
     * @param frame
     */
    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        logger.info("WebSocket消息处理方法...........");
        // 判断是否是关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            logger.info("客户端关闭........");
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            group.remove(ctx.channel());
            return;
        }
        // 判断是否是Ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 本例程仅支持文本消息，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass().getName()));
        }


        // 返回应答消息
        String request = ((TextWebSocketFrame) frame).text();



        //群发
        group.writeAndFlush(new TextWebSocketFrame(request + " , 现在时刻：" + new java.util.Date().toString()));
        //谁发的发给谁
//        ctx.channel().writeAndFlush(new TextWebSocketFrame(request + " , 欢迎使用Netty WebSocket服务，现在时刻：" + new java.util.Date().toString()));



    }

    /**
     * 发送消息
     * @param ctx
     * @param req
     * @param res
     */
    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        logger.info("sendHttpResponse处理方法...........");
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            HttpHeaderUtil.setContentLength(res, res.content().readableBytes());
        }

        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpHeaderUtil.isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }




}
