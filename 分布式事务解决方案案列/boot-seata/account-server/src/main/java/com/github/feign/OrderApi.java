package com.github.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;


/**
 * 功能描述: 订单API 远程调用
 * @author: qinxuewu
 * @date: 2019/11/25 17:31
 * @since 1.0.0
 */
@FeignClient(value = "order-server")
public interface OrderApi {

    /**
     * 修改订单金额
     * @param userId
     * @param money
     * @param status
     * @return
     */
    @RequestMapping("/order/update")
    String update(@RequestParam("userId") Long userId, @RequestParam("money") BigDecimal money, @RequestParam("status") Integer status);
}
