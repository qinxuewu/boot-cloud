package com.github;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;


/**
 * 分布式锁
 *
 * @author qinxuewu
 * @create 19/9/2下午1:10
 * @since 1.0.0
 */

public class Recipes_Lock {
    static String lock_path = "/curator_recipes_lock_path";
    static CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
            .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

    public static void main(String[] args) throws Exception {
        client.start();
        final InterProcessMutex lock = new InterProcessMutex(client, lock_path);
        final CountDownLatch down = new CountDownLatch(1);
        for (int i = 0; i < 30; i++) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        down.await();
                        lock.acquire();
                    } catch (Exception e) {
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
                    String orderNo = sdf.format(new Date());
                    System.err.println("生成的订单号是 : " + orderNo);
                    try {
                        lock.release();
                    } catch (Exception e) {
                    }
                }
            }).start();
        }
        down.countDown();
    }
}
