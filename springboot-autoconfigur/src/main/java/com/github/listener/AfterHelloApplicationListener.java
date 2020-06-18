package com.github.listener;


import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;

/**
 * 功能描述: 自定义应用事件监听器
 *
 * 监听ContextRefreshedEvent 事件  当应用上下文刷新后收到监听
 *
 * @author: qinxuewu
 */
public class AfterHelloApplicationListener  implements ApplicationListener<ContextRefreshedEvent>, Ordered {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("AfterHelloApplicationListener : " + event.getApplicationContext().getId()
                + " , timestamp : " + event.getTimestamp());
    }

    @Override
    public int getOrder() {
        // 最低优先级
        return Ordered.LOWEST_PRECEDENCE;
    }
}
