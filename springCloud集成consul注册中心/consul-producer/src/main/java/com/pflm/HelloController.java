package com.pflm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	@Value("${server.port}")
	String port;
	@Value("${spring.application.name}")
	 String name;

	@RequestMapping("/hello")
	public String hello() {

		return "hello world ! I'm :" + name + ":" + port;
	}
}
