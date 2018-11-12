package com.im.bean;


import io.netty.channel.Channel;

/**
 * 用户信息实体类
 * @author qinxuewu
 * @create 18/10/13上午9:44
 * @since 1.0.0
 *
 */
public class UserInfo {
    private String userId;
    private String name;
    private String address;
    private Channel channel;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
