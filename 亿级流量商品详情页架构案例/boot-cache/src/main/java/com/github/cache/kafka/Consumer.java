package com.github.cache.kafka;
import com.alibaba.fastjson.JSONObject;
import com.github.cache.model.ProductInfo;
import com.github.cache.model.ShopInfo;
import com.github.cache.service.CacheService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import java.util.Optional;


/**
 * 功能描述: 消费者
 * @author: qinxuewu
 * @date: 2019/11/4 11:51
 * @since 1.0.0
 */

@Component
public class Consumer {
    @Autowired
    private CacheService cacheService;
    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    /**
     *监听topic 的消息了，可同时监听多个topic
     * @param record
     * @throws Exception
     */
    @KafkaListener(topics = {"productInfo-cache"})
    public void productInfoListen(ConsumerRecord<?, ?> record, Acknowledgment ack){
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            // 首先将message转换成json对象
            JSONObject info = JSONObject.parseObject(message.toString());
            // 从这里提取出消息对应的服务的标识
            processProductInfoMessage(info);
            // 手动提交偏移量
            ack.acknowledge();
        }
    }

    /**
     *监听topic 的消息了，可同时监听多个topic
     * @param record
     * @throws Exception
     */
    @KafkaListener(topics = {"shopInfo-cache"})
    public void shopInfoListen(ConsumerRecord<?, ?> record, Acknowledgment ack){
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            // 首先将message转换成json对象
            JSONObject info = JSONObject.parseObject(message.toString());
            // 从这里提取出消息对应的服务的标识
            processShopInfoChangeMessage(info);
            // 手动提交偏移量
            ack.acknowledge();
        }
    }


    /**
     * 处理商品信息变更的消息
     * @param message
     */
    private  void  processProductInfoMessage(JSONObject message){
        logger.info("【Kafka消费者，处理商品信息变更的消息.....】：={}",message);
        //获取商品ID
        Long productId=message.getLong("productId");

        // 调用商品信息服务的接口
        // 直接用注释模拟：getProductInfo?productId=1，传递过去
        // 商品信息服务，一般来说就会去查询数据库，去获取productId=1的商品信息，然后返回回来

        // 模拟查询获取到最新的商品信息
        String productInfoJSON = "{\"id\": 1, \"name\": \"iphone7手机\", \"price\": 5599, \"pictureList\":\"a.jpg,b.jpg\", \"specification\": \"iphone7的规格\", \"service\": \"iphone7的售后服务\", \"color\": \"红色,白色,黑色\", \"size\": \"5.5\", \"shopId\": 1}";
        ProductInfo productInfo = JSONObject.parseObject(productInfoJSON, ProductInfo.class);
        //更新本地缓存
        cacheService.saveProductInfo2LocalCache(productInfo);
        logger.info("【获取刚保存到本地缓存的商品信息】：={}",cacheService.getProductInfoFromLocalCache(productId).toString());
        // 更新redis缓存
        cacheService.saveProductInfo2ReidsCache(productInfo);
    }

    /**
     * 处理店铺信息变更的消息
     * @param message
     */
    private void processShopInfoChangeMessage(JSONObject message) {
        logger.info("【Kafka消费者，处理店铺信息变更的消息.....】：={}",message);
        Long shopId = message.getLong("shopId");

        // 调用店铺信息服务的接口
        // 模拟查询获取到最新的商品信息
        String shopInfoJSON = "{\"id\": 1, \"name\": \"小王的手机店\", \"level\": 5, \"goodCommentRate\":0.99}";
        ShopInfo shopInfo = JSONObject.parseObject(shopInfoJSON, ShopInfo.class);
        //更新本地缓存
        cacheService.saveShopInfo2LocalCache(shopInfo);
        logger.info("【获取刚保存到本地缓存的商品信息】：={}",cacheService.getShopInfoFromLocalCache(shopId).toString());
        // 更新redis缓存
        cacheService.saveShopInfo2ReidsCache(shopInfo);
    }
}
