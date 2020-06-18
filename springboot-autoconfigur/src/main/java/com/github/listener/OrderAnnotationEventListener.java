package com.github.listener;
import com.github.listener.event.OrderStatusMsgEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


/**
 * 功能描述: 自定义事件监听器订单事件监听：通过注解
 * @author: qinxuewu
 */

@Component
public class OrderAnnotationEventListener {

    /**
     * 参数为Object类型时，所有事件都会监听到
     * 参数为指定类型事件时，该参数类型事件或者其子事件（子类）都可以接收到
     */
    @EventListener
    public void event(OrderStatusMsgEvent event){
        System.out.println(" 监听到订单事件时  注解的方式~~~~~~~~~~~~~~~"+event.getOrderDTO().toString());
    }
}
