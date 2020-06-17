package com.github.service;


import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("Java7")    // 系统条件装配的注解
@Service
public class Java7CalculateService implements CalculateService {

    @Override
    public Integer sum(Integer... values) {
        System.out.println("Java 7 for 循环实现 ");
        int sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += values[i];
        }
        return sum;
    }


}
