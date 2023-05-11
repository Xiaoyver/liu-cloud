package com.liu.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gdLiu
 * @date 2023/5/5 16:18
 */
@Getter
@AllArgsConstructor
public enum ClientEnum {

    /**
     * 安卓
     */
    ANDROID("android"),
    /**
     * 苹果
     */
    IOS("ios"),
    /**
     * 小程序
     */
    MINI_PROGRAM("mini_program"),
    /**
     * 网页
     */
    WEB("web"),
    /**
     * WINDOWS
     */
    WINDOWS("windows"),
    /**
     * MAC
     */
    MAC("mac"),
    /**
     * LINUX
     */
    LINUX("linux"),
    /**
     * 树莓派
     */
    RASPBERRY_PI("raspberry_pi"),
    /**
     * 其他
     */
    OTHER("other");

    private final String client;

    private static final Map<String, ClientEnum> MAP = Arrays
            .stream(ClientEnum.values())
            .collect(Collectors.toMap(ClientEnum::getClient, type -> type));

    @JsonCreator
    public static ClientEnum getEnumByCode(String client) {
        return MAP.get(client);
    }

    @JsonValue
    public String getClient() {
        return client;
    }

}
