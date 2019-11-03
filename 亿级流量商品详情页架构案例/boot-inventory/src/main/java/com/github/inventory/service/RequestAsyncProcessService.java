package com.github.inventory.service;

import com.github.inventory.request.Request;


/**
 * 功能描述: 请求异步执行的service
 * @author: qinxuewu
 * @date: 2019/11/1 10:47
 * @since 1.0.0 
 */
public interface RequestAsyncProcessService {

    void process(Request request);
}
