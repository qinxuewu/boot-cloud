package com.example.modules.interceptor;


import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * 功能描述: kafak生产者拦截器
 * @author: qinxuewu
 * @date: 2020/4/22 11:33
 * @since 1.0.0
 */
public class AvgLatencyProducerInterceptor implements ProducerInterceptor<String, String> {

    /**
     * 该方法会在消息发送之前被调用
     * @param producerRecord
     * @return
     */
    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> producerRecord) {
        System.out.println("消息发送之前被调用...................");
        return producerRecord;
    }

    /**
     * 该方法会在消息成功提交或发送失败之后被调用 调用要早于 callback 的调用
     * @param recordMetadata
     * @param e
     */
    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
