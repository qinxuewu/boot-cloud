package com.github.cache;

import com.github.cache.http.HttpClientUtils;

public class RejectTest {

    public static void main(String[] args) throws Exception {
        for(int i = 0; i < 35; i++) {
            new TestThread(i).start();
        }
    }

    /**
     *
     假设，一个线程池，大小是15个，队列大小是12个，timeout时长设置的长一些，5s
     模拟发送请求，然后写死代码，在command内部做一个sleep，比如每次sleep 1s，10个请求发送过去以后，直接被hang死，线程池占满
     再发送请求，就会堵塞在缓冲队列，queue，10个，20个，10个，后10个应该就直接reject，fallback逻辑
     */
    private static class TestThread extends Thread {

        private int index;

        public TestThread(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            String response = HttpClientUtils.sendGetRequest("http://localhost:8082/getProductInfo?productId=-2");
            System.out.println("第" + (index + 1) + "次请求，结果为：" + response);
        }

    }
}
