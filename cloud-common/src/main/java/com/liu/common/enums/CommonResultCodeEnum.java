package com.liu.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author gdLiu
 * @date 2023/4/29 15:32
 */
@Getter
@AllArgsConstructor
public enum CommonResultCodeEnum {

    /**
     * 成功
     */
    SUCCESS(200, "成功"),
    /**
     * 服务器内部异常
     */
    FAIL(500, "服务器内部异常"),

    PARAMETER_ERROR(100_001, "参数缺失"),
    HEADER_PARAMETER_ERROR(100_002, "请求头参数缺失"),
    PARAMETER_VALIDATION_ERROR(100_200, "参数验证错误"),

    TIMESTAMP_PARAMETER_OUT_ERROR(100_300, "请求时间超时"),

    /**
     * 登录权限相关
     */
    NOT_LOGIN_ERROR(200_000, "未登录"),
    LOGIN_TYPE_ERROR(200_001, "登录类型错误"),

    /**
     * 兜底异常
     */
    SYS_ERROR(999_999, "系统异常");
    /**
     * 代码
     */
    private final Integer code;

    /**
     * 消息
     */
    private final String message;

}
