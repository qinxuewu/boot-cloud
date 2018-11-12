package com.pflm.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qinxuewu
 * @version 1.00
 * @time 24/7/2018上午 9:57
 */
@RestController
public class ServiceController {

    @Autowired
    private LoadBalancerClient loadBalancer;
    @Autowired
    private DiscoveryClient discoveryClient;
    //获取所有服务
    @RequestMapping("/services")
    public Object services() {
        return discoveryClient.getInstances("consul-producer");
    }
    //从所有服务中选择一个服务（轮询）
    @RequestMapping("/discover")
    public Object discover() {
        return loadBalancer.choose("consul-producer").getUri().toString();
    }
}
