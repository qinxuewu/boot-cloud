package com.example.modules.interceptor;


import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.Map;

/**
 * 功能描述: kafka消费者拦截器
 * @author: qinxuewu
 * @date: 2020/4/22 11:33
 * @since 1.0.0
 */
public class AvgLatencyConsumerInterceptor  implements ConsumerInterceptor<String, String> {
    @Override
    public ConsumerRecords<String, String> onConsume(ConsumerRecords<String, String> consumerRecords) {
        long lantency = 0L;
        for (ConsumerRecord<String, String> record : consumerRecords) {
            lantency += (System.currentTimeMillis() - record.timestamp());
        }

        System.out.println("avgLatency : "+lantency);
        return consumerRecords;
    }

    @Override
    public void onCommit(Map<TopicPartition, OffsetAndMetadata> map) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
