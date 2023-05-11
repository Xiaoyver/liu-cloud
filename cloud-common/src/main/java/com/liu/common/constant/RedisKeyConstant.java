package com.liu.common.constant;

/**
 * Redis-Key常量
 *
 * @author gdLiu
 * @date 2022/10/21 15:34
 */
public final class RedisKeyConstant {

    /**
     * 一分钟过期键
     */
    public static final String ONE_MINUTE_EXPIRED = "minute";
    /**
     * 一小时过期键
     */
    public static final String ONE_HOUR_EXPIRED = "hour";
    /**
     * 一天过期键
     */
    public static final String ONE_DAY_EXPIRED = "day";
    /**
     * 一周过期键
     */
    public static final String ONE_WEEK_EXPIRED = "week";
    /**
     * 1月过期Key
     */
    public static final String ONE_MONTH_EXPIRED = "month";

    private RedisKeyConstant() {
        throw new IllegalArgumentException("RedisKeyConstant类不能被实例化");
    }
}
