package com.github.cache;

import com.github.cache.http.HttpClientUtils;

public class TimeoutTest {

    // 让一个command执行timeout，然后看是否会调用fallback降级
    public static void main(String[] args) throws Exception {
        HttpClientUtils.sendGetRequest("http://localhost:8082/getProductInfo?productId=-3");
    }
}
