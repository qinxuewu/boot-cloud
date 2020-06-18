package com.github.listener;
import com.github.listener.event.OrderStatusMsgEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;


/**
 * 功能描述: 自定义事件监听器订单事件监听：当有事件发生时，监听到事件的变更通知做进一步处理
 * @author: qinxuewu
 */

public class OrderEventListener implements ApplicationListener<OrderStatusMsgEvent>, Ordered {

    @Override
    public void onApplicationEvent(OrderStatusMsgEvent event) {
       // 监听到订单事件时  业务处理
        System.out.println(" 监听到订单事件时  业务处理~~~~~~~~~~~~~~~"+event.getOrderDTO().toString());
    }

    @Override
    public int getOrder() {
        // 最低优先级
        return Ordered.LOWEST_PRECEDENCE+10;
    }
}
