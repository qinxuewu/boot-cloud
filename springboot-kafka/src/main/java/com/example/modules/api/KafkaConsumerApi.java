package com.example.modules.api;
import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 消费者
 * kafka api接口调用   不集成spring-kafka
 * @author qxw
 * @data 2018年8月8日下午5:53:10
 */
public class KafkaConsumerApi {
	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerApi.class);
	private static Properties props =null;
	static {
			props=new Properties();
			props.put("bootstrap.servers", "localhost:9092");
			//消费者组 #在kafka/config文件的consumer.properties中有配置
		    props.put("group.id", "test-consumer-group");
		    props.put("enable.auto.commit", "true");
		    props.put("auto.commit.interval.ms", "1000");
		    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		    logger.info("初始kafka配置参数完成.....................");
	}

	public static void consumer(String...topic){
		 KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		 //订阅消费主题
		 consumer.subscribe(Arrays.asList(topic));
	     while (true) {
	    	 ConsumerRecords<String, String> records = consumer.poll(100);
	         for (ConsumerRecord<String, String> record : records)
	             System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
	     }
	}
	
	
	public static void main(String[] args) {
		consumer("qxw","testtete");
	}
}
