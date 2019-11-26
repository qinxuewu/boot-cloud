package com.github.feign;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountFallback implements  AccountApi {
    @Override
    public String decrease(Long userId, BigDecimal money) {
        return null;
    }
}
