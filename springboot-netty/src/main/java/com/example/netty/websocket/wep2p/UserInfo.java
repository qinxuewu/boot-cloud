package com.example.netty.websocket.wep2p;

import io.netty.channel.Channel;

public class UserInfo {

    private String userId;  // UID

    private String address;    // 地址

    private Channel channel;// 通道

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
}
