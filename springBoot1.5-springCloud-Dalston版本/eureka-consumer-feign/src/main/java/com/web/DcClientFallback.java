package com.web;

import org.springframework.stereotype.Component;

/**
 * Hystrix服务降级
 *
 * @author qxw
 * 2017年11月6日
 */

@Component
public class DcClientFallback implements DcClient {


    public String consumer() {
        return "5秒超时  Hystrix服务降级";
    }


}
