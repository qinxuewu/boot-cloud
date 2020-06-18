package com.github.listener.bean;
import lombok.Data;

import java.util.Date;

/**
 * 功能描述: 首先定义事件源对象，对象中包含需要发送的基本信息
 * @author: qinxuewu
 */
@Data
public class OrderDTO {
    private int orderId;
    private String name;


    public OrderDTO(int orderId,String name){
        this.orderId=orderId;
        this.name=name;
    }
}
