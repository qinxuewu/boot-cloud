package com.web;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


/**
 * 服务消费  使用Spring Cloud Ribbon
 * @author qxw
 * 2017年11月1日
 */

@RestController
public class DcController{

	private Logger logger=Logger.getLogger(DcController.class);
	    @Autowired
	    RestTemplate restTemplate;
	    
	    
	    /**
	     * 占位符传参
	     * 获取服务提供方的实例
	     * 消费服务提供者的接口
	     * @return
	     * 2017年11月2日
	     */
	    @GetMapping("/consumer")
	    public String dc() {
	    	System.out.println("使用Spring Cloud Ribbon 请求服务提供者接口>>>>>>>>>>>>>>>");
	    	return restTemplate.getForObject("http://eureka-client/dc?name={1}", String.class,"qxwdasdsad");
	    }
	    
	    
	    /**
	     * Map类型传参
	     * 获取服务提供方的实例
	     * 消费服务提供者的接口
	     * @return
	     * 2017年11月2日
	     */
	   
	    @RequestMapping("/consumer2")
	    public String dc2() {
	    	logger.info("consumer2>>>>>>>>>>>>>>>>>");
//	    	Map<String, String> map=new HashMap<String, String>();
//	    	map.put("name", "秦学武");
//	    	return restTemplate.getForObject("http://eureka-client/dc?name={name}", String.class,map);
	    	
	    	Map<String, String> map=new HashMap<String, String>();
	    	map.put("name", "消费者发送post请求服务提供者方接口");
	    	return restTemplate.postForObject("http://eureka-client/dcPost", map, String.class);
	  
	    }
	    
	    

	
	    
}
