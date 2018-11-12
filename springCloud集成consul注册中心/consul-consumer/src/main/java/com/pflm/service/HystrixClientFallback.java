package com.pflm.service;

import org.springframework.stereotype.Component;

@Component
public class HystrixClientFallback implements FeinClient{

	@Override
	public String sayhello() {
		return "fallback： 熔断器打开";
	}

}
