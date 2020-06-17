package com.github.server;
import com.github.entity.Order;
import com.github.feign.AccountApi;
import com.github.mapper.OrderDao;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

/**
 * 功能描述: 订单服务
 * @author: qinxuewu
 * @date: 2019/11/25 17:29
 * @since 1.0.0
 */
@Service("orderService")
public class OrderServiceImpl implements  OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private AccountApi accountApi;

//    /**
//     * 创建订单
//     * @param order
//     * @return
//     * 测试结果：
//     * 1.添加本地事务：仅仅扣减库存
//     * 2.不添加本地事务：创建订单，扣减库存
//     */

    /***
     *  流程：订单服务下单，远程请求账户服务扣减金额
     * @param order
     */
    @Override
    @Transactional
    @GlobalTransactional(name = "fsp-create-order",rollbackFor = Exception.class,timeoutMills = 60000)
    public void create(Order order) {
        LOGGER.info("------->交易开始  开始下单");
        //本地方法
        orderDao.create(order);
        //openfeign远程调用账户服务 扣减账户余额
        LOGGER.info("------->下单完成  开始远程调用账户服务 扣减账户余额");
        String result= accountApi.decrease(order.getUserId(),order.getMoney());
        LOGGER.info("------->远程调用账户服返回结果："+result);
    }




    /**
     * 修改订单状态
     */
    @Override
    public void update(Long userId, BigDecimal money, Integer status) {
        LOGGER.info("修改订单状态，入参为：userId={},money={},status={}",userId,money,status);
        orderDao.update(userId,money,status);
    }
}
