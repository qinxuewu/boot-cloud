package com.example.modules;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.util.Date;
/**
 * 〈生产者〉
 * @author qinxuewu
 * @create 18/8/4下午11:56
 * @since 1.0.0
 */
@Component
public class Producer {
    @Autowired
    private KafkaTemplate kafkaTemplate;
    //发送消息方法
    public void send() {
        JSONObject obj=new JSONObject();
        obj.put("id",System.currentTimeMillis());
        obj.put("name","生产者发送消息");
        obj.put("date",new Date());
        //这个 topic 在 Java 程序中是不需要提前在 Kafka 中设置的，因为它会在发送的时候自动创建你设置的 topic
        kafkaTemplate.send("qxw",obj.toString());
    }
}
