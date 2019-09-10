package com.github;
import com.alipay.demo.trade.config.Configs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 功能描述: 
 * @author: qinxuewu
 * @date: 2019/9/10 11:36
 * @since 1.0.0 
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		// 基于官方SDK DEMO方式初始化配置文件
		Configs.init("zfbinfo.properties");
		SpringApplication.run(Application.class, args);

	}

}
