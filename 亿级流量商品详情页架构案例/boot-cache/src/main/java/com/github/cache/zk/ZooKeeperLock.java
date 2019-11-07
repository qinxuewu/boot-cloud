package com.github.cache.zk;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 功能描述: 基于zk实现的分布式锁
 * @author: qinxuewu
 * @date: 2019/11/7 16:09
 * @since 1.0.0
 */

@Component("zooKeeperLock")
public class ZooKeeperLock {
    private static final Logger logger = LoggerFactory.getLogger(ZooKeeperLock.class);

    @Autowired
    private ZooKeeper zkClient;


    /**
     *  获取分布式锁
     * @param productId  商品ID
     */
    public  void  acquireDistributedLock(Long productId){
        String path = "/product-lock-" + productId;
        try {
            zkClient.create(path,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            logger.info("【获取zk分布式锁成功】 product[id=" + productId + "]");
        }catch (Exception e){
            e.printStackTrace();
            // 如果那个商品对应的锁的node，已经存在了，就是已经被别人加锁了，那么就这里就会报错
            int count=0;
            while (true){
                try {
                    // 休眠一会在尝试获取锁
                    Thread.sleep(20);
                    zkClient.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                }catch (Exception e2){
                    e2.printStackTrace();
                    count++;
                    continue;
                }
                logger.info("success to acquire lock for product[id=" + productId + "] after " + count + " times try..");
                break;
            }
        }
    }

    /**
     * 释放掉一个分布式锁
     * @param productId
     */
    public void releaseDistributedLock(Long productId) {
        String path = "/product-lock-" + productId;
        try {
            zkClient.delete(path, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
