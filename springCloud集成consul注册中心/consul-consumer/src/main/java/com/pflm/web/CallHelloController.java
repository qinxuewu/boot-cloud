package com.pflm.web;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.pflm.service.FeinClient;

@RestController
public class CallHelloController {
		@Autowired
	    private LoadBalancerClient loadBalancer;

		@Autowired
		FeinClient  feinClient;
		
		@RequestMapping("/call")
		public String call() {
			ServiceInstance serviceInstance = loadBalancer.choose("consul-producer");
			System.out.println("服务地址：" + serviceInstance.getUri());
			System.out.println("服务名称：" + serviceInstance.getServiceId());
			String callServiceResult = new RestTemplate().getForObject(serviceInstance.getUri().toString() + "/hello", String.class);
			System.out.println(callServiceResult);
			return callServiceResult;
		}
		
		/**
		 * Feign客户端调用方法
		 * @return
		 */
		@RequestMapping("/hello")
		public String hello() {
			return feinClient.sayhello();
		}

}
