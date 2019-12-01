package com.example.listener;
import com.alibaba.fastjson.JSONObject;
import com.example.mapper.AccountDao;
import com.example.server.AccountService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 *  监听事务消息
 */

@Component
@RocketMQMessageListener(consumerGroup = "consumer_group_trmsg_bank2",topic = "topic_txmsg")
public class ConsumerTxmsgListener implements RocketMQListener<String> {

    @Autowired
    AccountService accountService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onMessage(String message) {
        System.out.println("ban2服务 监听事务消息。。。。  ");
        // 解析message
        JSONObject jsonObject=JSONObject.parseObject(message);
        // 转账发起方
        Long userId=jsonObject.getLong("userId");
        // 转账接收方
        Long toUserId=jsonObject.getLong("toUserId");
        double amount=jsonObject.getDouble("amount");
        String txNo=jsonObject.getString("txNo");

        // 更新ban2服务账户 增加金额
        accountService.decrease(toUserId,amount,txNo);
    }
}
