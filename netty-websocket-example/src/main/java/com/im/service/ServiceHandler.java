package com.im.service;

import com.alibaba.fastjson.JSONObject;
import com.im.bean.UserInfo;
import com.im.utils.ConstEnum;
import com.im.utils.MessageUtil;
import com.im.utils.NettyConfig;
import com.im.utils.TimeUtil;
import io.netty.channel.SimpleChannelInboundHandler;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentMap;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * 接收/处理/响应客户端websocket请求的核心业务处理类
 * 通过添加hanlder，我们可以监听Channel的各种动作以及状态的改变，包括连接，绑定，接收消息等。
 *
 * @author qinxuewu
 * @create 18/10/13上午10:08
 * @since 1.0.0
 */


public class ServiceHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger log = LoggerFactory.getLogger(ServiceHandler.class);

    // 用于服务器端web套接字打开和关闭握手
    private WebSocketServerHandshaker handshaker;

    private static final String WEB_SOCKET_URL = "ws://localhost:8888/websocket";

    //客户端与服务端创建连接的时候调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyConfig.group.add(ctx.channel());
        log.info("客户端与服务端连接开启，客户端remoteAddress:{}", ctx.channel().remoteAddress());
    }

    //客户端与服务端断开连接的时候调用
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        UserInfo userinfo = MessageUtil.userInfos.get(ctx.channel());
        log.info("与服务端连接关闭用户id: {}", userinfo.getUserId());
        JSONObject json = new JSONObject();
        json.put("type", "login");

        NettyConfig.list.remove(userinfo.getUserId());
        log.info("目前在线用户：{}", NettyConfig.list.size());
        json.put("list", NettyConfig.list);

        NettyConfig.group.remove(ctx.channel());

        //通知客户端更新在线用户列表
        NettyConfig.group.writeAndFlush(new TextWebSocketFrame(json.toJSONString()));
    }

    /**
     * 每当从服务端收到客户端断开时，客户端的 Channel 移除 ChannelGroup 列表中，并通知列表中的其他客户端 Channel
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        log.info("Client:" + incoming.remoteAddress() + "离开");
        UserInfo userinfo = MessageUtil.userInfos.get(incoming);

        JSONObject json = new JSONObject();
        json.put("type", "login");

        NettyConfig.list.remove(userinfo.getUserId());
        log.info("目前在线用户：{}", NettyConfig.list.size());
        json.put("list", NettyConfig.list);

        NettyConfig.group.remove(ctx.channel());

        //通知客户端更新在线用户列表
        NettyConfig.group.writeAndFlush(new TextWebSocketFrame(json.toJSONString()));

        NettyConfig.group.remove(ctx.channel());

    }

    /**
     * 每当从服务端收到新的客户端连接时，客户端的 Channel 存入 ChannelGroup 列表中，并通知列表中的其他客户端 Channel
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        NettyConfig.group.add(ctx.channel());
        System.err.println("Client:" + incoming.remoteAddress() + "加入");

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
            log.info("关闭websocket的指令:{},{}", ctx.channel().id());
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
            JSONObject info = JSONObject.parseObject(((TextWebSocketFrame) frame).text());

            log.info("接收消息：info{}", info);
            System.out.println("收到客户端" + MessageUtil.parseChannelRemoteAddr(ctx.channel()) + "的消息:" + info);

            String myid = info.getString(ConstEnum.MYID.getName());

            if (StringUtils.isEmpty(myid)) {
                JSONObject j = new JSONObject();
                j.put("type", "noLogin");
                log.info("用户还未登录不能发送消息 ,channelID :{}", ctx.channel().id());
                ctx.writeAndFlush(new TextWebSocketFrame(j.toJSONString()));
                return;
            }

            //登录
            if (!info.containsKey(ConstEnum.FRIENDID.getName()) && !info.containsKey(ConstEnum.MESSAGE.getName())) {
                // 将通道加入通道管理器。
                MessageUtil.addChannel(ctx.channel(), myid, "");
                log.info("用户登录OK ,名称:{}", myid);
                JSONObject j = new JSONObject();
                j.put("type", "login");
                j.put("username", myid);

                //加入临时用户在线列表
                NettyConfig.list.add(myid);
                j.put("list", NettyConfig.list);
                //上线通知
                NettyConfig.group.writeAndFlush(new TextWebSocketFrame(j.toJSONString()));

                log.info("目前在线用户：{}", NettyConfig.list.size());
                return;
            }
            if (!NettyConfig.list.contains(myid)) {
                JSONObject j = new JSONObject();
                j.put("type", "noUser");
                log.info("该用户不存在：{}", myid);
                ctx.writeAndFlush(new TextWebSocketFrame(j.toJSONString()));
                return;

            }

            // 表示是用户之间端到端的私密通信
            if (info.containsKey(ConstEnum.FRIENDID.getName()) && !StringUtils.isEmpty(info.getString(ConstEnum.FRIENDID.getName()))) {
                String senderId = info.getString(ConstEnum.MYID.getName());
                String receiverId = info.getString(ConstEnum.FRIENDID.getName());
                String message = info.getString(ConstEnum.MESSAGE.getName());
                // 发送逻辑：在map里面寻找用户id对应的channel，使用用户的channel发送消息
                MessageUtil.sendP2PMessage(senderId, receiverId, message);
            } else {
                String senderId = info.getString(ConstEnum.MYID.getName());
                String message = info.getString(ConstEnum.MESSAGE.getName());
                JSONObject j = new JSONObject();
                j.put("senderId", senderId);
                j.put("message", message);
                j.put("type", "msg");
                // 获取当前的日期时间
                LocalDateTime currentTime = LocalDateTime.now();
                j.put("time", TimeUtil.format(currentTime, "yyyy-MM-dd HH:mm:ss"));

                //如果没有指定接收者表示群发
                log.debug("用户" + senderId + "群发了一条消息：" + message);
                NettyConfig.group.writeAndFlush(new TextWebSocketFrame(j.toJSONString()));
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