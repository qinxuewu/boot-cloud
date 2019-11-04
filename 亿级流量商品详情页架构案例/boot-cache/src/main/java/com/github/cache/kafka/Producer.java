package com.github.cache.kafka;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.PostConstruct;
import java.util.Date;
/**
 * 功能描述:
 * @author: qinxuewu
 * @date: 2019/11/4 11:53
 * @since 1.0.0
 */

@Component
public class Producer {
    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);
    @Autowired
    private KafkaTemplate kafkaTemplate;


    @PostConstruct
    public void  productInfo() throws InterruptedException {
        Thread.sleep(2000);
        JSONObject obj = new JSONObject();
        obj.put("productId", "1");
        //这个 topic 在 Java 程序中是不需要提前在 Kafka 中设置的，因为它会在发送的时候自动创建你设置的 topic
        kafkaTemplate.send("productInfo-cache", obj.toString());
        logger.info("【发送更新商品信息缓存请求到kafka...........】");
    }

    @PostConstruct
    public void  shopInfo() throws InterruptedException {
        Thread.sleep(4000);
        JSONObject obj = new JSONObject();
        obj.put("shopId", "1");
        //这个 topic 在 Java 程序中是不需要提前在 Kafka 中设置的，因为它会在发送的时候自动创建你设置的 topic
        kafkaTemplate.send("shopInfo-cache", obj.toString());
        logger.info("【发送更新店铺信息缓存请求到kafka...........。。。。。。。。。】");
    }
}
