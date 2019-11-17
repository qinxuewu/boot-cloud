package com.github.cache;

import com.github.cache.http.HttpClientUtils;

public class RequestCollapserTest {
    public static void main(String[] args) throws Exception {
        HttpClientUtils.sendGetRequest("http://localhost:8081/getProductInfos?productIds=1,1,2,2,3,4");
    }
}
