package com.qxw.controller;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 模拟的共享资源， 这个资源期望只能单线程的访问，否则会有并发问题。
 *
 * @author qxw
 * @data 2018年8月15日下午3:54:48
 */
public class FakeLimitedResource {
    private final AtomicBoolean inUse = new AtomicBoolean(false);

    // 模拟只能单线程操作的资源
    public void use() throws InterruptedException {
        if (!inUse.compareAndSet(false, true)) {
            // 在正确使用锁的情况下，此异常不可能抛出
            throw new IllegalStateException("Needs to be used by one client at a time");
        }
        try {
            Thread.sleep((long) (3 * Math.random()));
        } finally {
            inUse.set(false);
        }
    }
}
