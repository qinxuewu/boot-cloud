package com.github.server;
import com.github.feign.OrderApi;
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

    @Autowired
    private OrderApi  orderApi;

    /**
     * 扣减账户余额
     * @param userId 用户id
     * @param money 金额
     */
    @Override
    public void decrease(Long userId, BigDecimal money) {
        LOGGER.info("------->扣减账户开始account中");
        //模拟处理超时异常，导致订单服务请求失败
//        try {
//            Thread.sleep(30*1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        accountDao.decrease(userId,money);
        LOGGER.info("------->扣减账户结束account中");

        //修改订单状态
        String mes = orderApi.update(userId, money.multiply(new BigDecimal("0.09")),0);
        LOGGER.info("调用订单服务  修改订单状态结束：{}",mes);
    }
}
