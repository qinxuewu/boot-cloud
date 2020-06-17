package com.github.bootstarp;

import com.github.repository.MyUserRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


/**
 * 功能描述: 启动引导类
 * @author: qinxuewu
 * @date: 2020/6/17 10:54
 * @since 1.0.0
 */
@ComponentScan(basePackages = "com.github.repository")
public class RepositoryBootstrap {

    public static void main(String[] args) {
        // 构建应用上下文  设置非web类型
        ConfigurableApplicationContext context = new SpringApplicationBuilder(RepositoryBootstrap.class)
                .web(WebApplicationType.NONE)
                .run(args);

        MyUserRepository myUserRepository= context.getBean("myUserRepository",MyUserRepository.class);
        System.out.println(myUserRepository.toString());


        // 关闭上下文
        context.close();
    }
}
