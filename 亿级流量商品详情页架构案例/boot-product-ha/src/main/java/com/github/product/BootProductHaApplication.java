package com.github.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 功能描述: 基于hystrix的高可用商品服务
 * @author: qinxuewu
 * @date: 2019/11/12 11:47
 * @since 1.0.0 
 */
@SpringBootApplication
public class BootProductHaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootProductHaApplication.class, args);
	}

}
