package com.example.netty.websocket.wep2p;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * 接收/处理/响应客户端websocket请求的核心业务处理类
 *通过添加hanlder，我们可以监听Channel的各种动作以及状态的改变，包括连接，绑定，接收消息等。
 * @author liuyazhuang
 */
public class MyWebSocketHandler extends SimpleChannelInboundHandler<Object> {

    // 用于服务器端web套接字打开和关闭握手
    private WebSocketServerHandshaker handshaker;

    private static final String WEB_SOCKET_URL = "ws://localhost:8888/websocket";

    //客户端与服务端创建连接的时候调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyConfig.group.add(ctx.channel());
        System.out.println("客户端与服务端连接开启，客户端remoteAddress：" + ctx.channel().remoteAddress());
    }

    //客户端与服务端断开连接的时候调用
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyConfig.group.remove(ctx.channel());
        System.out.println("客户端与服务端连接关闭...");
    }

    //服务端接收客户端发送过来的数据结束之后调用
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    //工程出现异常的时候调用
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    //服务端处理客户端websocket请求的核心方法
    @Override
    protected void messageReceived(ChannelHandlerContext context, Object msg) throws Exception {

        /* 传统的HTTP接入（采用http处理方式）
         * 第一次握手请求消息由HTTP协议承载，所以它是一个HTTP消息，
         * 握手成功后，数据就直接从 TCP 通道传输，与 HTTP 无关了。
         * 执行handleHttpRequest方法来处理WebSocket握手请求。
         */

        // FullHttpRequest是完整的 HTTP请求，协议头和Form数据是在一起的，不用分开读
        if (msg instanceof FullHttpRequest) {
            handHttpRequest(context, (FullHttpRequest) msg);
        }
        /**
         *  WebSocket接入（采用socket处理方式）
         *  提交请求消息给服务端，
         *  WebSocketServerHandler接收到的是已经解码后的WebSocketFrame消息。
         */
        else if (msg instanceof WebSocketFrame) {
            handWebsocketFrame(context, (WebSocketFrame) msg);
        }
        /**
         * Websocket的数据传输是frame形式传输的，比如会将一条消息分为几个frame，按照先后顺序传输出去。这样做会有几个好处：
         *
         * 1）大数据的传输可以分片传输，不用考虑到数据大小导致的长度标志位不足够的情况。
         *
         * 2）和http的chunk一样，可以边生成数据边传递消息，即提高传输效率。
         */
    }

    /**
     * 处理客户端与服务端之前的websocket业务
     *
     * @param ctx
     * @param frame
     */
    private void handWebsocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {

        //判断是否是关闭websocket的指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
        }

        //判断是否是ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        

       // NettyConfig.group.find(ctx.channel().id()).write(tws);
        
        if (frame instanceof TextWebSocketFrame) {
            // 返回应答消息
            String requestMsg = ((TextWebSocketFrame) frame).text();
            System.out.println("收到客户端" + UserInfoManager.parseChannelRemoteAddr(ctx.channel()) + "的消息==》"+requestMsg);
            String[] array= requestMsg.split(",");
            // 将通道加入通道管理器。array[0]是发送者，1是接收者，2是具体消息内容
            UserInfoManager.addChannel(ctx.channel(), array[0]);
            if (array.length == 3) {
                // 表示是用户之间端到端的私密通信
                String senderId = array[0];
                String receiverId = array[1];
                String message = array[2];
                // 发送逻辑：在map里面寻找用户id对应的channel，使用用户的channel发送消息
                UserInfoManager.sendP2PMessage(senderId, receiverId, message);
            } else if (array.length == 2) {
                //如果没有指定接收者表示群发array.length() = 2
                System.out.println("用户" + array[0] + "群发了一条消息：" + requestMsg);
                NettyConfig.group.writeAndFlush(new TextWebSocketFrame("用户" + array[0] + "群发了一条消息：" + requestMsg));
            } else {
                //如果没有指定发送者与接收者表示向服务端发送array.length() = 1
                System.out.println("服务端接收用户" + ctx.channel().remoteAddress() + "消息，不再发送出去");
            }
        }
    }


    /**
     * 处理客户端向服务端发起http握手请求的业务
     *
     * @param ctx
     * @param req
     */
    private void handHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {

        // 如果不是WebSocket握手请求消息，那么就返回 HTTP 400 BAD REQUEST 响应给客户端。
        if (!req.decoderResult().isSuccess() || !("websocket".equals(req.headers().get("Upgrade")))) {
                sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }



        //如果是握手请求，那么就进行握手
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(WEB_SOCKET_URL, null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            // 通过它构造握手响应消息返回给客户端，
            // 同时将WebSocket相关的编码和解码类动态添加到ChannelPipeline中，用于WebSocket消息的编解码，
            // 添加WebSocketEncoder和WebSocketDecoder之后，服务端就可以自动对WebSocket消息进行编解码了
            handshaker.handshake(ctx.channel(), req);
        }
    }

    /**
     * 服务端向客户端响应消息
     *
     * @param ctx
     * @param req
     * @param res
     */
    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req,
                                  DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
