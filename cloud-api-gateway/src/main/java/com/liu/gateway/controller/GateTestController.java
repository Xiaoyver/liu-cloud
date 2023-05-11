package com.liu.gateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 *
 * @author gdLiu
 * @date 2023/5/11 14:39
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/pub/gateway")
public class GateTestController {

    @Value("${config.name}")
    private String name;


    @GetMapping("/test")
    public String test() {
        log.info("gateway test name is {}!", name);
        return name;
    }

}
