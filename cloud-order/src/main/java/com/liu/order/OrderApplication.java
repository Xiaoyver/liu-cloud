package com.liu.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 订单应用程序
 *
 * @author gdLiu
 * @date 2023/5/11 17:04
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.liu"})
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
