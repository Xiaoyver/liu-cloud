package com.liu.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 用户应用程序
 *
 * @author gdLiu
 * @date 2023/5/11 16:39
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.liu"})
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
