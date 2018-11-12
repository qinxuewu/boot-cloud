package com.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.service.ConsumerService;


/**
 * 服务消费  
 * @author qxw
 * 2017年11月1日
 */

@RestController
public class DcController{


		@Autowired
	    ConsumerService consumerService;
		
		
	    @GetMapping("/consumer")
	    public String dc() {
	    	System.out.println("=========演示服务降级处理==========");
	        return consumerService.consumer();
	    }

	    
	    
	   
}
