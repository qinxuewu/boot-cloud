package com.pflm.modules.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 服务消费者
 * @author qinxuewu
 * @version 1.00
 * @time 19/7/2018下午 3:12
 */
@Service
public class HelloService {

    @Autowired
    RestTemplate restTemplate;

    /**
     * HystrixCommand：断路器 （Hystric 是5秒20次请求失败） 断路器将会被打开
     * @param name
     * @return
     */
    @HystrixCommand(fallbackMethod = "hiError")
    public String hiService(String name) {
        //程序名替代了具体的url地址 在ribbon中它会根据服务名来选择具体的服务实例，根据服务实例在请求的时候会用具体的url替换掉服务名
        return restTemplate.getForObject("http://szq-api/hi?name="+name,String.class);
    }

    public String hiError(String name) {
        return "hi,"+name+",sorry,error!";
    }

}
