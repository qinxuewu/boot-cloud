package com.example;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 *
 *  度量指标: http://localhost:8081/actuator/prometheus
 * @author qinxuewu
 * @version 1.00
 * @time  2/4/2019 下午 1:16
 * @email 870439570@qq.com
 */

@SpringBootApplication
public class FreemarkerApplication {
    @Value("${spring.application.name}")
    private  String application;

    public static void main(String[] args) {
        SpringApplication.run(FreemarkerApplication.class, args);
    }



    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer() {
        return (registry) -> registry.config().commonTags("application", application);
    }
}
