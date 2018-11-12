package com.pflm;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * eureka开启验证后无法连接注册中心?
 * spring Cloud 2.0 以上）的security默认启用了csrf检验，要在eurekaServer端配置security的csrf检验为false
 * @author qxw
 * @data 2018年7月24日下午1:58:31
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	 @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http.csrf().disable();
	        super.configure(http);
	    }
}
