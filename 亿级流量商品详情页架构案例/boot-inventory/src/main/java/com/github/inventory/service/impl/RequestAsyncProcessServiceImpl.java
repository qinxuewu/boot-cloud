package com.github.inventory.service.impl;

import com.github.inventory.request.Request;
import com.github.inventory.request.RequestQueue;
import com.github.inventory.service.RequestAsyncProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;


/**
 * 功能描述: 请求异步处理的service实现
 * @author: qinxuewu
 * @date: 2019/11/1 10:55
 * @since 1.0.0 
 */
@Service("requestAsyncProcessService")
public class RequestAsyncProcessServiceImpl   implements RequestAsyncProcessService {
    private static final Logger log = LoggerFactory.getLogger(ProductInventoryServiceImpl.class);


    /**
     * 根据商品ID计算路由下标 放入指定的队列
     * @param request
     */
    @Override
    public void process(Request request) {
        try {
            // 做请求的路由，根据每个请求的商品id，路由到对应的内存队列中去
            ArrayBlockingQueue<Request> queue = getRoutingQueue(request.getProductId());
            // 将请求放入对应的队列中，完成路由操作
            queue.put(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取路由到的内存队列
     * @param productId 商品id
     * @return 内存队列
     */
    private ArrayBlockingQueue<Request> getRoutingQueue(Integer productId) {
        RequestQueue requestQueue = RequestQueue.getInstance();

        // 先获取productId的hash值
        String key = String.valueOf(productId);
        int h;
        int hash = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);

        // 对hash值取模，将hash值路由到指定的内存队列中，比如内存队列大小8
        // 用内存队列的数量对hash值取模之后，结果一定是在0~7之间
        // 所以任何一个商品id都会被固定路由到同样的一个内存队列中去的
        int index = (requestQueue.queueSize() - 1) & hash;

        log.info("路由内存队列，商品id=" + productId + ", 队列索引=" + index);

        return requestQueue.getQueue(index);
    }
}
