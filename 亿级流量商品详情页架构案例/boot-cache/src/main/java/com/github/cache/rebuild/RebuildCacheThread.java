package com.github.cache.rebuild;
import com.github.cache.model.ProductInfo;
import com.github.cache.service.CacheService;
import com.github.cache.spring.SpringContextHolder;
import com.github.cache.zk.ZooKeeperLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 功能描述: 缓存重建线程  被动重建
 *   直接读取源头数据，直接返回给nginx，同时推送一条消息到一个队列，后台线程异步消费
 *   后台现线程负责先获取分布式锁，然后才能更新redis，同时要比较时间版本
 * @author: qinxuewu
 * @date: 2019/11/7 16:36
 * @since 1.0.0
 */
public class RebuildCacheThread implements  Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RebuildCacheThread.class);



    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public void run() {
        RebuildCacheQueue rebuildCacheQueue=RebuildCacheQueue.getInstance();
        // 在线程中为了线程安全，是防注入
        CacheService cacheService = (CacheService) SpringContextHolder.getApplicationContext().getBean("cacheService");
        ZooKeeperLock zooKeeperLock=(ZooKeeperLock) SpringContextHolder.getApplicationContext().getBean("zooKeeperLock");
        while (true){
            // 不断循环获取队列中的缓存重建消息
            ProductInfo productInfo=rebuildCacheQueue.takeProductInfo();
            //先获取当前产品ID的分布式锁
            zooKeeperLock.acquireDistributedLock(productInfo.getId());
            // 本地ehcache缓存中获取商品信息
            ProductInfo existedProductInfo =   cacheService.getProductInfoFromLocalCache(productInfo.getId());

            if(existedProductInfo !=null){
                // 比较当前数据的时间版本比已有数据的时间版本是新还是旧
                try {
                    Date date = sdf.parse(productInfo.getModifiedTime());
                    Date existedDate = sdf.parse(existedProductInfo.getModifiedTime());
                    //当前队列的产品更新时间 在缓存中的商品时间之前 则跳出循环
                    if(date.before(existedDate)) {
                        logger.info("【 缓存重建线程-> 当前队列的产品更新时间 在缓存中的商品时间之前 则跳出循环。。。。。】");
                        System.out.println("current date[" + productInfo.getModifiedTime() + "] is before existed date[" + existedProductInfo.getModifiedTime() + "]");
                        continue;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //当前队列的产品更新时间 在缓存中的商品时间之后 可以更新队列中的信息 到缓存中
                logger.info("【 缓存重建线程-> 当前队列的产品更新时间 在缓存中的商品时间之后 可以更新队列中的信息 到缓存中....】");
                System.out.println("current date[" + productInfo.getModifiedTime() + "] is after existed date[" + existedProductInfo.getModifiedTime() + "]");
            }else {
                logger.info("【 缓存重建线程-> 本地ehcache缓存中获取商品信息为空....】");
            }
            // 将商品信息保存到本地缓存中
            cacheService.saveProductInfo2LocalCache(productInfo);
            // 将商品信息保存到redis中
            cacheService.saveProductInfo2ReidsCache(productInfo);

            // 释放锁
            zooKeeperLock.releaseDistributedLock(productInfo.getId());
        }
    }
}
