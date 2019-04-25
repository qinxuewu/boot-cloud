package com.sun.shard.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource(value = {"classpath:jdbc.properties"})
@Component
public class JdbcConfig {
private String className1;

	@Value("${jdbc.url1}")
	private String url1;
	
	@Value("${jdbc.user1}")
	private String user1;
	
	@Value("${jdbc.password1}")
	private String password1;
	
	@Value("${jdbc.className2}")
	private String className2;
	
	@Value("${jdbc.url2}")
	private String url2;
	
	@Value("${jdbc.user2}")
	private String user2;
	
	@Value("${jdbc.password2}")
	private String password2;
	

	public String getClassName1() {
		return className1;
	}


	public void setClassName1(String className1) {
		this.className1 = className1;
	}


	public String getUrl1() {
		return url1;
	}


	public void setUrl1(String url1) {
		this.url1 = url1;
	}


	public String getUser1() {
		return user1;
	}


	public void setUser1(String user1) {
		this.user1 = user1;
	}


	public String getPassword1() {
		return password1;
	}


	public void setPassword1(String password1) {
		this.password1 = password1;
	}


	public String getClassName2() {
		return className2;
	}


	public void setClassName2(String className2) {
		this.className2 = className2;
	}


	public String getUrl2() {
		return url2;
	}


	public void setUrl2(String url2) {
		this.url2 = url2;
	}


	public String getUser2() {
		return user2;
	}


	public void setUser2(String user2) {
		this.user2 = user2;
	}


	public String getPassword2() {
		return password2;
	}


	public void setPassword2(String password2) {
		this.password2 = password2;
	}
    
}
