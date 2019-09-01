package com.im.utils;

import com.im.bean.UserInfo;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * 消息发送工具类
 *
 * @author qinxuewu
 * @create 18/10/13上午9:48
 * @since 1.0.0
 */


public class MessageUtil {
    private static final Logger log = LoggerFactory.getLogger(MessageUtil.class);

    /**
     * 读写锁  后面换成redissesion 分布锁
     */
    private static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(true);


    /**
     * 存储通道对应的用户信息，支持并发的map集合， 分布式用rediss替代
     */
    public static ConcurrentMap<Channel, UserInfo> userInfos = new ConcurrentHashMap<>();

    /**
     * 用户登录上线时， 存储通道信息
     */
    public static void addChannel(Channel channel, String uid, String name) {
        log.info("用户登录后存储对应Channel：{},{}", uid, name);
        String remoteAddr = MessageUtil.parseChannelRemoteAddr(channel);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(uid);
        userInfo.setName(name);
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
        log.info("发送点对点消息：{},{},{}", senderId, receiverId, message);
        boolean hasReceiverId = false;
        Set<Channel> keySet = userInfos.keySet();
        if (!MessageUtil.isBlank(message)) {
            try {
                rwLock.readLock().lock();
                // 取出所有的channel,然后遍历，寻找sender对应的channel
                for (Channel ch : keySet) {
                    UserInfo userInfo = userInfos.get(ch);
                    // 当前通道不是接收者的话，重新遍历
                    if (!userInfo.getUserId().equals(receiverId)) {
                        continue;
                    }
                    // 当前通道是接收者的
                    String backMessage = senderId + "发来消息：" + message;
                    ch.writeAndFlush(new TextWebSocketFrame(backMessage));
                    hasReceiverId = true;
                    break;
                }
                if (hasReceiverId == false) {
                    // 对方不在线  可保存到数据库实现离线消息
                    for (Channel ch : keySet) {
                        UserInfo userInfo = userInfos.get(ch);
                        if (!userInfo.getUserId().equals(senderId))
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
