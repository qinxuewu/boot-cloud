package com.github;


import com.github.listener.OrderEventListener;
import com.github.listener.bean.OrderDTO;
import com.github.listener.event.OrderStatusMsgEvent;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * * https://www.cnblogs.com/ashleyboy/p/9566579.html
 * 功能描述: 自定义订单监听器和发布事件 启动类
 * @author: qinxuewu
 *

 *
 *  在Spring框架中实现事件监听的流程如下：
 *      1.自定义事件，继承org.springframework.context.ApplicationEvent抽象类
 *      2.定义事件监听器，实现org.springframework.context.ApplicationListener接口
 *      3.在Spring容器中发布事件
 *
 *  在Spring框架中提供了两种事件监听的方式：
 *      1.编程式：通过实现ApplicationListener接口来监听指定类型的事件
 *      2.注解式：通过在方法上加@EventListener注解的方式监听指定参数类型的事件，写该类需要托管到Spring容器中
 *   在SpringBoot应用中还可以通过配置的方式实现监听：
 *          3.通过application.properties中配置context.listener.classes属性指定监听器
 *
 *   SpringBoot进行事件监听有四种方式：
 *          1.手工向ApplicationContext中添加监听器
 *          2.将监听器装载入spring容器
 *          3.在application.properties中配置监听器
 *          4.通过@EventListener注解实现事件监听
 */
@SpringBootApplication
public class OrderEventBootstrap {
    public static void main(String[] args) {

        ConfigurableApplicationContext context = new SpringApplicationBuilder(OrderEventBootstrap.class)
//                .web(WebApplicationType.NONE)
                .run(args);

//        //需要把监听器加入到spring容器中 配置文件中 配置 不需添加了
        context.addApplicationListener(new OrderEventListener());

        //发布订单事件
        context.publishEvent(new OrderStatusMsgEvent(new Object(),new OrderDTO(1,"订单名")));

        // 关闭上下文
        context.close();
    }


}
