package com.github.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;


/**
 * 功能描述: 自定义应用事件监听器
 *
 * 监听ContextRefreshedEvent 事件  当应用上下文刷新后收到监听
 *
 * @author: qinxuewu
 */
@Order(Ordered.HIGHEST_PRECEDENCE)  // 设置最高优先级
public class HelloApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("HelloApplicationListener : " + event.getApplicationContext().getId()
                + " , timestamp : " + event.getTimestamp());
    }
}
