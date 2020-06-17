package com.github.bootstarp;

import com.github.annotation.MyEnable;
import com.github.service.HelloService;
import com.github.service.HelloServiceA;
import com.github.service.HelloServiceB;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;



/**
 * 功能描述: 模块装配启动类测试
 */
@MyEnable
public class MyEnableBootstrap {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(MyEnableBootstrap.class)
                .web(WebApplicationType.NONE)
                .run(args);

        HelloService helloServiceA=context.getBean(HelloServiceA.class);
        System.out.println("helloServiceA:"+helloServiceA.toString());

        HelloService helloServiceB=context.getBean(HelloServiceB.class);
        System.out.println("helloServiceB:"+helloServiceB.toString());

        // 关闭上下文
        context.close();
    }
}
