package com.github.feign;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;


@Component
public class AccountFallback implements  AccountApi {
    @Override
    public String decrease(Long userId, BigDecimal money) {
        return "调用账户服务失败,返回默认参数 false";
    }
}
