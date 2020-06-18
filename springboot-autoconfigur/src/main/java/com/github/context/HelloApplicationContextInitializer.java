package com.github.context;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;



/**
 * 功能描述: 功能描述: 自定义应用上下文初始化器
 * 不设置优先级
 */
public class HelloApplicationContextInitializer <C extends ConfigurableApplicationContext>
        implements ApplicationContextInitializer<C> {
    @Override
    public void initialize(C c) {
        System.out.println("不设置优先级自定义应用上下文初始化器= "+ c.getId());
    }
}
