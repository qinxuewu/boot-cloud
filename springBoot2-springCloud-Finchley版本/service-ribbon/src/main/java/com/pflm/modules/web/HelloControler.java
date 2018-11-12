package com.pflm.modules.web;

import com.pflm.modules.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qinxuewu
 * @version 1.00
 * @time 19/7/2018下午 3:24
 */
@RestController
public class HelloControler {
    @Autowired
    HelloService helloService;

    @GetMapping(value = "/hi")
    public String hi(@RequestParam String name) {
        System.out.print("----------HelloControler-----------------------");
        return helloService.hiService( name );
    }
}
