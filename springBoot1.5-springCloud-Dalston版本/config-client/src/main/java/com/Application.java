package com;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


/**
 * 构建客户端
 * @author qxw
 * 2017年11月3日
 */
@SpringBootApplication
public class Application{
	public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class).web(true).run(args);
	}

}
