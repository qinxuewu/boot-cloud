package com.example.netty.websocket.wep2p;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.net.InetSocketAddress;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class UserInfoManager {

    private static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(true);

    // 一个通道对应一个用户信息
    private static ConcurrentMap<Channel, UserInfo> userInfos = new ConcurrentHashMap<>();

    /**
     * 登录注册 channel
     */
    public static void addChannel(Channel channel, String uid) {
        String remoteAddr = UserInfoManager.parseChannelRemoteAddr(channel);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(uid);
        userInfo.setAddress(remoteAddr);
        userInfo.setChannel(channel);
        userInfos.put(channel, userInfo);
    }

    /**
     * 发送点对点消息
     *
     * @param message
     */
    public static void sendP2PMessage(String senderId, String receiverId, String message) {

        /**
         * A给B发消息，应该是B收到消息，并在B的对话框并输出消息
         */
        boolean hasReceiverId = false;
        Set<Channel> keySet = userInfos.keySet();
        if (!UserInfoManager.isBlank(message)) {
            try {
                rwLock.readLock().lock();
                // 取出所有的channel,然后遍历，寻找sender对应的channel

                for (Channel ch : keySet) {
                    UserInfo userInfo = userInfos.get(ch);
                    // 当前通道不是接收者的话，重新遍历
                    if (!userInfo.getUserId().equals(receiverId) )
                        continue;
                    // 当前通道是接收者的
                    String backMessage = senderId + "发来消息：" + message;
                    ch.writeAndFlush(new TextWebSocketFrame(backMessage));
                    hasReceiverId = true;
                    break;
                }
                if (hasReceiverId == false) {
                    // 对方不在线
                    for (Channel ch : keySet) {
                        UserInfo userInfo = userInfos.get(ch);
                        if (!userInfo.getUserId().equals(senderId) )
                            continue;
                        String backMessage = receiverId + "不在线";
                        ch.writeAndFlush(new TextWebSocketFrame(backMessage));
                        break;
                    }
                }
            } finally {
                rwLock.readLock().unlock();
            }
        }
    }

    public static boolean isBlank(String message) {
        if (message.length() == 0) {
            return true;
        } else if (message == null) {
            return true;
        } else {
            return false;
        }
    }

    public static String parseChannelRemoteAddr(Channel channel) {
        InetSocketAddress insocket = (InetSocketAddress) channel.remoteAddress();
        return insocket.getAddress().getHostAddress();
    }
}
