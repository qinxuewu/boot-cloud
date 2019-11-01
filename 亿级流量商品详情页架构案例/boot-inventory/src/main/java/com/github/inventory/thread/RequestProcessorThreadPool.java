package com.github.inventory.thread;


import com.github.inventory.request.Request;
import com.github.inventory.request.RequestQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 功能描述: 请求处理的线程池
 * @author: qinxuewu
 * @date: 2019/11/1 10:27
 * @since 1.0.0
 */
public class RequestProcessorThreadPool {

    private static final Logger log = LoggerFactory.getLogger(RequestProcessorThread.class);
    /**
     * 线程池
     */
    private ExecutorService threadPool = Executors.newFixedThreadPool(10);

    /**
     * 初始化
     */
    public  RequestProcessorThreadPool(){
        RequestQueue requestQueue=RequestQueue.getInstance();
        for (int i = 0; i <10 ; i++) {
            // 每个队列大小为100的阻塞队列
            ArrayBlockingQueue<Request> queue=new ArrayBlockingQueue<Request>(100);
            // 添加到请求队列集合中
            requestQueue.addQueue(queue);
            // 提交线程池处理
            threadPool.submit(new RequestProcessorThread(queue));
        }
        log.info("【初始哈线程池和请求内存队列完毕】 size={}",requestQueue.queueSize());
    }

    private static class Singleton {
        private static RequestProcessorThreadPool instance;
        static {
            instance = new RequestProcessorThreadPool();
        }
        public static RequestProcessorThreadPool getInstance() {
            return instance;
        }

    }
    /**
     * jvm的机制去保证多线程并发安全
     *
     * 内部类的初始化，一定只会发生一次，不管多少个线程并发去初始化
     *
     * @return
     */
    public static RequestProcessorThreadPool getInstance() {
        return Singleton.getInstance();
    }

    /**
     * 初始化的便捷方法
     */
    public static void init() {
        getInstance();
    }

}
