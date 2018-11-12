package com.web.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.HystrixObservable;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
@Service
public class ConsumerService {

	
	 @Autowired
     RestTemplate restTemplate;
     
     /**
      * @HystrixCommand注解来指定服务降级方法
      * @return
      * 2017年11月3日
      */
     @HystrixCommand(fallbackMethod = "fallback")
     public String consumer() {
         return restTemplate.getForObject("http://eureka-client/dc", String.class);
     }
     public String fallback() {
         return "fallback";
     }
     
}
