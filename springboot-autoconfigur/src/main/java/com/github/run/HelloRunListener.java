package com.github.run;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;


/**
 * 功能描述: SpringApplicationRunListener接口的作用:
 *      主要就是在Spring Boot 启动初始化的过程中可以通过SpringApplicationRunListener接口
 *      回调来让用户在启动的各个流程中可以加入自己的逻辑。
 *
 *   Spring Boot启动过程的关键事件（按照触发顺序）包括：
 *          开始启动
 *          Environment构建完成
 *          ApplicationContext构建完成
 *          ApplicationContext完成加载
 *          ApplicationContext完成刷新并启动
 *          启动完成
 *          启动失败
 */

public class HelloRunListener  implements SpringApplicationRunListener {

    public HelloRunListener(SpringApplication application, String[]  args){
        System.out.println("HelloRunListener   constructor");
    }
    // 开始启动
    @Override
    public void starting() {
        System.out.println("HelloRunListener->>>>>>>>>>>>>>>>>>>>>>>>>> 开始启动.starting()...");
    }

    //  Environment构建完成
    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {

    }

    //   ApplicationContext构建完成
    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {

    }

    //  ApplicationContext完成加载
    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {

    }

    //   ApplicationContext完成刷新并启动
    @Override
    public void started(ConfigurableApplicationContext context) {

    }

    //    启动完成
    @Override
    public void running(ConfigurableApplicationContext context) {

    }

    // 启动失败
    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {

    }
}
