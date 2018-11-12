package com.web;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * 服务消费
 * @author qxw
 * 2017年11月1日
 */

@RestController
public class DcController{

		@Autowired
	    DcClient dcClient;
		
	    @GetMapping("/consumer")
	    public String dc() {
	        return dcClient.consumer();
	    }
}
