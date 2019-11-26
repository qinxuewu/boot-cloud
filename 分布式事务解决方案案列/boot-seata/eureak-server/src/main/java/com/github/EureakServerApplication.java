package com.github;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


/**
 * 功能描述: 注册中心
 * @author: qinxuewu
 * @date: 2019/11/25 17:12
 * @since 1.0.0
 */
@EnableEurekaServer
@SpringBootApplication
public class EureakServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EureakServerApplication.class, args);
    }

}
