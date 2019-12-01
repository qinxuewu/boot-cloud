package com.example.listener;

import com.alibaba.fastjson.JSONObject;
import com.example.mapper.AccountDao;
import com.example.server.AccountService;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.omg.CORBA.UNKNOWN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 实现本地事务提交和事务回查
 *
 * @author qinxuewu
 * @create 19/12/1下午12:56
 * @since 1.0.0
 *
 */

@Component
@RocketMQTransactionListener(txProducerGroup = "producer_group_trmsg_bank1")
public class ProduccerTxmsgListener implements RocketMQLocalTransactionListener {

    @Autowired
    AccountDao accountDao;
    @Autowired
    AccountService accountService;

    /**
     * 实现本地事务提交,事务消息发送成功后的回调方法
     * @param msg
     * @param arg
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        try {
            System.out.println("【实现本地事务提交,事务消息发送成功后的回调方法。。。。。。。】");
            // 解析message
            String message=new String((byte[])msg.getPayload());
            JSONObject jsonObject=JSONObject.parseObject(message);

            Long userId=jsonObject.getLong("userId");
            double amount=jsonObject.getDouble("amount");
            String txNo=jsonObject.getString("txNo");
            // 执行本地事务，扣减金额
            accountService.decrease(userId,amount,txNo);
            // 自动向MQ发送commit消息 mq将消费的状态改为可消费
            return RocketMQLocalTransactionState.COMMIT;
        }catch (Exception e){
            e.printStackTrace();
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    /**
     * 事务回查  当网络断调时 查询bank1是否已扣减金额
     * @param msg
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        System.out.println("【事务回查  当网络断调时 查询bank1是否已扣减金额。。。。。。。】");
        // 解析message
        String message=new String((byte[])msg.getPayload());
        JSONObject jsonObject=JSONObject.parseObject(message);
        String txNo=jsonObject.getString("txNo");

         if(accountDao.isExisTx(txNo)>0){
                return RocketMQLocalTransactionState.COMMIT;
         }
         // 未知状态  可以继续回查
        return RocketMQLocalTransactionState.UNKNOWN;
    }
}
