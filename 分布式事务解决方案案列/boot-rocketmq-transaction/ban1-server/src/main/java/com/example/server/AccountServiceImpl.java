package com.example.server;
import com.alibaba.fastjson.JSONObject;
import com.example.mapper.AccountDao;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


/**
 * 功能描述: 
 * @author: qinxuewu
 * @date: 2019/11/25 17:24
 * @since 1.0.0 
 */
@Service("accountService")
public class AccountServiceImpl  implements  AccountService{

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    AccountDao accountDao;
    @Autowired
    RocketMQTemplate rocketMQTemplate;


    /**
     * 扣减账户余额
     * @param userId 用户id
     * @param money 金额
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void decrease(Long userId, double money,String txNo) {
        // 幂等判断
        if(accountDao.isExisTx(txNo)>0){
            return ;
        }
        accountDao.decrease(userId,money);
        // 增加事务日志 防止幂等
        accountDao.addTx(txNo);

    }

    /**
     * 发送MQ转账消息
     * @param userId  账号
     * @param  toUserId 接收方账户
     * @param amount  变动金额
     * @param txNo  事务号
     */
    @Override
    public void sendUpdateAccontChange(Long userId,Long toUserId, double amount, String txNo) {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("userId",userId);
        jsonObject.put("toUserId",toUserId);
        jsonObject.put("amount",amount);
        jsonObject.put("txNo",txNo);
        String str=jsonObject.toJSONString();
        Message<String> bulid= MessageBuilder.withPayload(str).build();

        // 发送一条事务消息
        rocketMQTemplate.sendMessageInTransaction("producer_group_trmsg_bank1","topic_txmsg",bulid,null);


    }

}
