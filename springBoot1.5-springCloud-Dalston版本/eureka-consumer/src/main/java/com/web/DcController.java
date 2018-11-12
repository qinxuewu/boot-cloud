package com.web;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


/**
 * 服务消费
 * @author qxw
 * 2017年11月1日
 */

@RestController
public class DcController{

		@Autowired
	    LoadBalancerClient loadBalancerClient;
	    @Autowired
	    RestTemplate restTemplate;
	    
	    
	    /**
	     * 获取服务提供方的实例
	     * 消费服务提供者的接口
	     * @return
	     * 2017年11月2日
	     */
	    @GetMapping("/consumer")
	    public String dc() {
	    	//LoadBalancerClient接口来获取某个服务的具体实例，并根据实例信息来发起服务接口消费请求
	        ServiceInstance serviceInstance = loadBalancerClient.choose("eureka-client");
	        String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/dc";
	        System.out.println(url);
	        return restTemplate.getForObject(url, String.class);
	    }
}
