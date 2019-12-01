package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class Ban1ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(Ban1ServerApplication.class, args);
	}

}
