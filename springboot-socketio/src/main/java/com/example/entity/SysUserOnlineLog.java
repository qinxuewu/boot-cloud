package com.example.entity;

import java.util.Date;
import javax.persistence.*;
import java.io.Serializable;

/**
 * 用户每次在线时长日志
 *
 * @author qinxuewu
 * @version 1.00
 * @time 29/3/2019 下午 5:46
 * @email 870439570@qq.com
 */

@Table(name = "system_user_online_log")
@Entity
public class SysUserOnlineLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String account;                //目标用户账号

    private Date startTime;      //上线时间
    private Date endTime;        //下线时间
    private String countTime;   //在线总时长  单位秒
    private String sessionId;      //会话sessionId
    private Long mostsignbits;            //socketio-mostsignbits
    private Long leastsignbits;            //socketio-leastsignbits
    private String remoteAddress;   //远程客户端地址
    private String source;  //页面来源


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getCountTime() {
        return countTime;
    }

    public void setCountTime(String countTime) {
        this.countTime = countTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getMostsignbits() {
        return mostsignbits;
    }

    public void setMostsignbits(Long mostsignbits) {
        this.mostsignbits = mostsignbits;
    }

    public Long getLeastsignbits() {
        return leastsignbits;
    }

    public void setLeastsignbits(Long leastsignbits) {
        this.leastsignbits = leastsignbits;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
