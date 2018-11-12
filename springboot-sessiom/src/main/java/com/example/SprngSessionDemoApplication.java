package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import javax.servlet.http.HttpSession;

@SpringBootApplication
public class SprngSessionDemoApplication {
    public static void main(String[] args) {

        SpringApplication.run(SprngSessionDemoApplication.class, args);


    }
}
