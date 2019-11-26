package com.github.controller;
import com.github.entity.Order;
import com.github.server.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;

/**
 * 功能描述: 订单 API
 * @author: qinxuewu
 * @date: 2019/11/25 17:28
 * @since 1.0.0
 */
@RestController
@RequestMapping(value = "/order")
public class OrderController {
    @Autowired
    private OrderService orderService;


    /**
     * 创建订单
     * @param order
     * @return
     */
    @GetMapping("/create")
    public String create(){
        Order order=new Order();
        order.setUserId(1L);
        order.setProductId(1L);
        order.setCount(2);
        BigDecimal bigDecimal=new BigDecimal("10");
        order.setMoney(bigDecimal);

        orderService.create(order);
        return "Create order success";
    }

    /**
     * 修改订单状态
     * @param userId
     * @param money
     * @param status
     * @return
     */
    @RequestMapping("update")
    String update(@RequestParam("userId") Long userId, @RequestParam("money") BigDecimal money, @RequestParam("status") Integer status){
        orderService.update(userId,money,status);
        return "订单状态修改成功";
    }
}
