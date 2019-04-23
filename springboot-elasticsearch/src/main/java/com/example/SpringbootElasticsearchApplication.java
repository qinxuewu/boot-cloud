package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author qinxuewu
 * @version 1.00
 * @time 10/4/2019 下午 8:02
 * @email 870439570@qq.com
 */
@EnableScheduling
@SpringBootApplication
public class SpringbootElasticsearchApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpringbootElasticsearchApplication.class, args);
    }
}
