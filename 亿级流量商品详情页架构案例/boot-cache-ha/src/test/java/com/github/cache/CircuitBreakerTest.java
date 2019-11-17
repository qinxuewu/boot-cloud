package com.github.cache;

import com.github.cache.http.HttpClientUtils;

public class CircuitBreakerTest {
    /**
     * 配置一个断路器，流量要求是20，异常比例是50%，短路时间是5s
     *
     * 在command内加入一个判断，如果是productId=-1，那么就直接报错，触发异常执行
     *
     * 写一个client测试程序，写入50个请求，前20个是正常的，但是后30个是productId=-1，然后继续请求，会发现
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        for(int i = 0; i < 15; i++) {
            String response = HttpClientUtils.sendGetRequest("http://localhost:8082/getProductInfo?productId=1");
            System.out.println("第" + (i + 1) + "次请求，结果为：" + response);
        }
        for(int i = 0; i < 25; i++) {
            String response = HttpClientUtils.sendGetRequest("http://localhost:8082/getProductInfo?productId=-1");
            System.out.println("第" + (i + 1) + "次请求，结果为：" + response);
        }
        Thread.sleep(5000);
        // 等待了5s后，时间窗口统计了，发现异常比例太多，就短路了
        for(int i = 0; i < 10; i++) {
            String response = HttpClientUtils.sendGetRequest("http://localhost:8082/getProductInfo?productId=-1");
            System.out.println("第" + (i + 1) + "次请求，结果为：" + response);
        }
        // 统计单位，有一个时间窗口的，我们必须要等到那个时间窗口过了以后，才会说，hystrix看一下最近的这个时间窗口
        // 比如说，最近的10秒内，有多少条数据，其中异常的数据有没有到一定的比例
        // 如果到了一定的比例，那么才会去短路
        System.out.println("尝试等待3秒钟。。。。。。");
        Thread.sleep(5000);
        for(int i = 0; i < 10; i++) {
            String response = HttpClientUtils.sendGetRequest("http://localhost:8082/getProductInfo?productId=1");
            System.out.println("第" + (i + 1) + "次请求，结果为：" + response);
        }
    }
}
