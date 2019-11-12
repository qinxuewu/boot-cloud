package com.github.cache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 功能描述:  基于hystrix的高可用缓存服务架构
 * @author: qinxuewu
 * @date: 2019/11/12 11:29
 * @since 1.0.0 
 */
@SpringBootApplication
public class BootCacheHaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootCacheHaApplication.class, args);
	}

}
