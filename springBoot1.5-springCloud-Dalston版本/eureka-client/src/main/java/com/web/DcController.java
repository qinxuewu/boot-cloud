package com.web;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务提供方
 * 实现/dc请求处理接口，通过DiscoveryClient对象，在日志中打印出服务实例的相关内容。
 * @author qxw
 * 2017年11月1日
 */

@RestController
public class DcController {

	private Logger logger=Logger.getLogger(DcController.class);
	
	@Autowired
    DiscoveryClient discoveryClient;
	
	
	/**
	 * 服务提供方提供给消费者的接口
	 * @return
	 * 2017年11月2日
	 * @throws InterruptedException 
	 */
    @GetMapping("/dc")
    public String dc(HttpServletRequest request) throws InterruptedException {
    	String name=request.getParameter("name");
    	logger.info("name>>>>>>>>>>>>>>>>>"+name);
    	//为了触发服务降级逻辑，我们可以将服务提供者eureka-client的逻辑加一些延迟
    	Thread.sleep(5000L);
        String services = "Services: " + discoveryClient.getServices();
        System.out.println(services);
        return services;
    }
    
    
    /**
	 * 服务提供方提供给消费者的接口
	 * @return
	 * 2017年11月2日
	 * @throws InterruptedException 
	 */
    @PostMapping("/dcPost")
    public String dcPost(HttpServletRequest request) throws InterruptedException {
    	String name=request.getParameter("name");
    	logger.info("dcPost "+name);
        String services = "Services: " + discoveryClient.getServices();
        System.out.println(services);
        return services+"    "+name;
    }
}
