package com.im.utils;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈全局配置〉
 *
 * @author qinxuewu
 * @create 18/10/13上午9:43
 * @since 1.0.0
 */


public class NettyConfig {
    /**
     * 存储每一个客户端接入进来时的channel对象
     */
    public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    /**
     * 存储临时用户列表
     */
    public static List<String> list = new ArrayList<>();

    /**
     * 端口号
     */
    public static int port = 8888;
}
