package com.github.service;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import java.util.stream.Stream;

@Profile("Java8")  // // 系统条件装配的注解
@Service
public class Java8CalculateService  implements CalculateService {

    @Override
    public Integer sum(Integer... values) {
        System.out.println("Java 8 Lambda 实现");
        int sum = Stream.of(values).reduce(0, Integer::sum);
        return sum;
    }


}
