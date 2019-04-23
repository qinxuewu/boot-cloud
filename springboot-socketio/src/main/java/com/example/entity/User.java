package com.example.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户类
 *
 * @author qinxuewu
 * @version 1.00
 * @time 26/3/2019 下午 1:55
 * @email 870439570@qq.com
 */
@Entity
@Table(name = "sys_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * @ID：指定的类的属性，用于识别（一个表中的主键）
     * @GeneratedValue：指定如何标识属性可以被初始化，例如自动、手动、或从序列表中获得的值
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @Column有如下属性： name： 字段名
     * unique： 是否唯一
     * nullable： 是否可以为空
     * insertable：是否允许插入
     * updatable： 是否允许更新
     * columnDefinition： 定义建表时创建此列的DDL
     * table：
     * length：  长度
     * precision：
     * scale：
     */

    private String userName;
    private String passWord;
    private Date createTime;
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", createTime=" + createTime +
                ", status=" + status +
                '}';
    }
}
