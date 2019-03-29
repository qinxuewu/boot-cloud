package com.example.entity;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * 客户端信息，用来存放客户端的sessionid
 *
 * 
 * @author qinxuewu
 * @version 1.00
 * @time  29/3/2019 下午 4:48
 * @email 870439570@qq.com
 */
@Table(name = "system_client_info")
@Entity
public class SystemClientInfo implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long	id;
    private String	account;				//目标用户账号
    private Short	connected;				//0:断开连接1:连接成功
    private Long	mostsignbits;			//socketio-mostsignbits
    private Long	leastsignbits;			//socketio-leastsignbits
    private Date   lastconnecteddate;		//最后一次连接时间

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
    public Short getConnected() {
        return connected;
    }
    public void setConnected(Short connected) {
        this.connected = connected;
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
    public Date getLastconnecteddate() {
        return lastconnecteddate;
    }
    public void setLastconnecteddate(Date lastconnecteddate) {
        this.lastconnecteddate = lastconnecteddate;
    }
}
