package com.github.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;


/**
 * 功能描述: 调用账户服务 扣钱
 * @author: qinxuewu
 * @date: 2019/11/25 17:10
 * @since 1.0.0
 */
@FeignClient(value = "account-server",fallback =AccountFallback.class )
public interface AccountApi {


    /**
     * 扣减账户余额
     * @param userId 用户id
     * @param money 金额
     * @return
     */
    @RequestMapping("/account/decrease")
    String decrease(@RequestParam("userId") Long userId, @RequestParam("money") BigDecimal money);
}
