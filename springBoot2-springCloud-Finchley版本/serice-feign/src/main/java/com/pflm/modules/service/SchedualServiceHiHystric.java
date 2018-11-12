package com.pflm.modules.service;

import org.springframework.stereotype.Component;

/** 熔断器打开后处理类
 * @author qinxuewu
 * @version 1.00
 * @time 19/7/2018下午 4:11
 */
@Component
public class SchedualServiceHiHystric implements SchedualServiceHi{
    @Override
    public String sayHiFromClientOne(String name) {
        return "熔断器打开  sorry  "+name;
    }
}
