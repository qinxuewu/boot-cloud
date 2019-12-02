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
     * 增加ban2余额
     * @param userId 用户id
     * @param money 金额
     */
    @Override
    @Transactional
    public void decrease(Long userId, double money,String txNo) {
        // 幂等判断
        if(accountDao.isExisTx(txNo)>0){
            return;
        }
        accountDao.decrease(userId,money);

        // 模拟增加账户余额异常
        // 模拟扣减后，，其它业务处理失败异常
        if(money>=200 ){
            throw new RuntimeException("人工模拟扣减账户金额 异常触发...........");
        }
        // 增加事务日志 防止幂等
        accountDao.addTx(txNo);
    }



}
