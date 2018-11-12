package com.example.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qinxuewu
 * @create 18/8/5上午12:02
 * @since 1.0.0
 */

@RestController
@RequestMapping("/kafka")
public class SendController {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @RequestMapping(value = "/send")
    public String send(String msg) {
        kafkaTemplate.send("test", msg);
        return "success";
    }
}
