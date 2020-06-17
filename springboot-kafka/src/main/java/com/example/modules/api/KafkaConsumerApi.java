package com.example.modules.api;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 消费者
 * kafka api接口调用   不集成spring-kafka
 *
 * @author qxw
 * @data 2018年8月8日下午5:53:10
 */
public class KafkaConsumerApi {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerApi.class);
    private static Properties props = null;

    static {
        props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        //消费者组 #在kafka/config文件的consumer.properties中有配置
        props.put("group.id", "test-consumer-group");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        logger.info("初始kafka配置参数完成.....................");
    }

    public static void consumer(String... topic) {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        //订阅消费主题
        consumer.subscribe(Arrays.asList(topic));
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                // 处理消息
                for (ConsumerRecord<String, String> record : records){
                    System.out.printf("消费者----offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                }
                // 使用异步提交规避阻塞
                consumer.commitSync();
            }
        }catch (Exception e){
            // 处理异常
                e.printStackTrace();
        }finally {
            try {
                consumer.commitSync(); // 最后一次提交使用同步阻塞式提交
            } finally {
                // 关闭消费者
                consumer.close();
            }
        }

    }



    /**
     *  如何每处理 100 条消息就提交一次位移
     */
  private Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
  public  void consumer2(String... topic) {
      KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
      //订阅消费主题
      consumer.subscribe(Arrays.asList(topic));
      int count = 0;
      while (true) {
          ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
          for (ConsumerRecord<String, String> record: records) {

              System.out.printf("消费者----offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());

              offsets.put(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset() + 1));
              if(count % 100 == 0){
                  // 回调处理逻辑是 null
                  consumer.commitAsync(offsets, null);
              }

              count++;
          }
      }
  }





    public static void main(String[] args) {
        consumer("qxw", "testtete");


    }
}
