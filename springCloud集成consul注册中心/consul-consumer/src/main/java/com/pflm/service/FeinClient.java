package com.pflm.service;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author qinxuewu
 * @version 1.00
 * @time 24/7/2018上午 10:22
 */


@FeignClient(value = "consul-producer", fallback = HystrixClientFallback.class)
public interface FeinClient {
	
	@RequestMapping(value = "/hello",method = RequestMethod.GET)
	String sayhello();
	

}

