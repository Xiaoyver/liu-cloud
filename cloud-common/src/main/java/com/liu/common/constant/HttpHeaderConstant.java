package com.liu.common.constant;

/**
 * http请求头
 *
 * @author gdLiu
 * @date 2023/5/4 11:01
 */
public class HttpHeaderConstant {

    /**
     * 请求头：token
     */
    public static final String TOKEN = "token";

    /**
     * 请求头：时间戳
     */
    public static final String TIMESTAMP = "timestamp";

    /**
     * 请求头：客户端
     */
    public static final String CLIENT = "client";

    private HttpHeaderConstant() {
        throw new IllegalArgumentException("HttpHeaderConstant类不允许被实例化");
    }
}
