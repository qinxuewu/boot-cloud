package com.github.server;
import com.github.mapper.AccountDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private AccountDao accountDao;

    /**
     * 扣减账户余额
     * @param userId 用户id
     * @param money 金额
     */
    @Override
    public void decrease(Long userId, BigDecimal money) {
        LOGGER.info("------->扣减账户开始account中");
        //模拟超时异常，全局事务回滚
//        try {
//            Thread.sleep(30*1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        accountDao.decrease(userId,money);
        LOGGER.info("------->扣减账户结束account中");

        //修改订单状态，此调用会导致调用成环
//        LOGGER.info("修改订单状态开始");
//        String mes = orderApi.update(userId, money.multiply(new BigDecimal("0.09")),0);
//        LOGGER.info("修改订单状态结束：{}",mes);
    }
}
