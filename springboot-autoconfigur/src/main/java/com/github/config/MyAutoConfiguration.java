package com.github.config;


import com.github.annotation.MyEnable;
import com.github.condition.ConditionalOnSystemProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * 功能描述:  实现自动装配
 * @author: qinxuewu
 */
@Configuration // Spring 模式注解装配
@MyEnable // Spring @Enable 模块装配
@ConditionalOnSystemProperty(name = "user.name", value = "carisok") // 条件装配
public class MyAutoConfiguration {


    @Bean
    public String helloWorld() { // 方法名即 Bean 名称
        return "Hello,World 2020";
    }
}
