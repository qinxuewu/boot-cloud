package com.github.context;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;


/**
 * 功能描述: 自定义应用上下文初始化器
 */
public class AfterHelloApplicationContextInitializer implements ApplicationContextInitializer, Ordered {
    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        System.out.println("自定义应用上下文初始化器After后执行 = " + configurableApplicationContext.getId());
    }


    @Override
    public int getOrder() {
        // 最低优先级  设置此标志 这个初始化器 会在排序的调转的末尾
        return Ordered.LOWEST_PRECEDENCE;
    }
}
