package com.liu.user.config;

import com.alibaba.nacos.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author gdLiu
 * @date 2023/5/12 08:34
 */
@Configuration
public class ProjectNameConfig implements EnvironmentAware {

    @Value("${spring.application.name}")
    private  String applicationName;

    @Override
    public void setEnvironment(Environment environment) {
        if(StringUtils.isBlank(System.getProperty("project.name"))){
            System.setProperty("project.name",applicationName);
        }
    }
}
