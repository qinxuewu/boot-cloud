package com.example.modules.api;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * kafka api接口调用   不集成spring-kafka
 * @author qxw
 * @data 2018年8月8日下午5:53:10
 */
public class KafkaClientApi {
	private static final Logger logger = LoggerFactory.getLogger(KafkaClientApi.class);
	private static Properties props;
	static {
		 props=new Properties();
		 //kafka地址 broker地址
		 props.put("bootstrap.servers","localhost:9092");
		 //判断求是否为完整的条件（就是是判断是不是成功发送了）。我们指定了“all”将会阻塞消息，这种设置性能最低，但是是最可靠的。
		 props.put("acks", "all");
		 //发送失败重试次数
		 props.put("retries", 0);
		 // 缓存的大小
		 props.put("batch.size", 16384);
		 props.put("linger.ms", 1);
		 //控制生产者可用的缓存总量
		 props.put("buffer.memory", 33554432);
		 //序列化
		 props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		 props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		 logger.info("初始kafka配置参数完成.....................");
	
	}
	/**
	 * 生产者发送消息
	 * @param topic 
	 */
	public static void send(String topic,String value){
		Producer<String, Object> producer=new KafkaProducer<>(props);;
		try {
		      producer.send(new ProducerRecord<String, Object>(topic,value));
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			producer.close();
		}
	}
	
	

	
	public static void main(String[] args) {
		send("qxw","dadasfasasfsafd");
		send("qxw","213213");
		send("qxw","dadasfasas3213213fsafd");
		send("qxw","dadasfasas321312312321fsafd");
	
	}
}
