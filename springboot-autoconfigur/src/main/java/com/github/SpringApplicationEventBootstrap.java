package com.github;


import com.github.listener.OrderEventListener;
import com.github.listener.bean.OrderDTO;
import com.github.listener.event.OrderStatusMsgEvent;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;

/**
 * 功能描述: 自定义监听器和发布事件 启动类
 * @author: qinxuewu
 * @date: 2020/6/18 17:00
 * @since 1.0.0
 */
@SpringBootApplication
public class SpringApplicationEventBootstrap {
    public static void main(String[] args) {
        // 创建上下文
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        // 注册应用事件监听器
        context.addApplicationListener(event -> {
            System.out.println("监听到事件: " + event);
        });

        // 启动上下文
        context.refresh();
        // 发送事件
        context.publishEvent("HelloWorld");
        //发布订单事件

        context.publishEvent(new ApplicationEvent("小马哥") {

        });

        // 关闭上下文
        context.close();
    }


}
