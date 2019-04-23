package com.qxw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * Hystrix监控面板
 * <p>
 * 为应用主类加上@EnableHystrixDashboard，启用Hystrix Dashboard功能。
 *
 * @author qxw
 * 2017年11月6日
 */

@EnableHystrixDashboard
@SpringBootApplication
public class HystrixDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(HystrixDashboardApplication.class, args);
    }

}
