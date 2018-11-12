package com;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * 分布式配置中心  pring Cloud Config
 * @author qxw
 * 2017年11月3日
 */

@EnableConfigServer
@SpringBootApplication
public class Application{
	public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class).web(true).run(args);
	}

}
