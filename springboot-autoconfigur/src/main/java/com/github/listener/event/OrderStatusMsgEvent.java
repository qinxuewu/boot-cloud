package com.github.listener.event;

import com.github.listener.bean.OrderDTO;
import lombok.Data;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;


/**
 * 功能描述: 定义发生的事件, 订单状态变更事件
 * @author: qinxuewu
 *
 * 在Spring框架中实现事件监听的流程如下：
 *      1.自定义事件，继承org.springframework.context.ApplicationEvent抽象类
 *      2.定义事件监听器，实现org.springframework.context.ApplicationListener接口
 *      3.在Spring容器中发布事件
 */

@Getter
public class OrderStatusMsgEvent extends ApplicationEvent {
    private OrderDTO orderDTO;
    /**
     * 重写构造函数
     * @param source   发生事件的对象
     * @param orderDTO 注册用户对象
     */
    public OrderStatusMsgEvent(Object source, OrderDTO orderDTO) {
        super(source);
        this.orderDTO = orderDTO;
    }



}
