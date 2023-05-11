package com.liu.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liu.common.enums.CommonResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 公共返回类
 *
 * @author gdLiu
 * @date 2023/5/4 10:36
 */
@Data
@ApiModel(value = "公共返回类")
public class CommonResult<T> {

    /**
     * 返回代码
     */
    @ApiModelProperty(value = "返回码,200代表接口调用成功", required = true, example = "200")
    private Integer code;
    /**
     * 返回处理消息
     */
    @ApiModelProperty(value = "返回信息", required = true, example = "操作成功")
    private String message;
    /**
     * 返回数据对象
     */
    @ApiModelProperty(value = "返回数据", required = true)
    private T data;

    public static <T> CommonResult<T> success() {
        return success(null);
    }

    public static <T> CommonResult<T> success(T data) {
        CommonResult<T> commonResult = new CommonResult<>();
        commonResult.setCode(CommonResultCodeEnum.SUCCESS.getCode());
        commonResult.setMessage(CommonResultCodeEnum.SUCCESS.getMessage());
        commonResult.setData(data);
        return commonResult;
    }

    public static <T> CommonResult<T> success(String message, T data) {
        CommonResult<T> commonResult = new CommonResult<>();
        commonResult.setCode(CommonResultCodeEnum.SUCCESS.getCode());
        commonResult.setMessage(StringUtils.isNotEmpty(message) ? message : CommonResultCodeEnum.SUCCESS.getMessage());
        commonResult.setData(data);
        return commonResult;
    }

    public static <T> CommonResult<T> successWithoutData(String message) {
        CommonResult<T> commonResult = new CommonResult<>();
        commonResult.setCode(CommonResultCodeEnum.SUCCESS.getCode());
        commonResult.setMessage(message);
        return commonResult;
    }

    public static <T> CommonResult<T> fail(CommonResultCodeEnum resultCodeEnum) {
        CommonResult<T> commonResult = new CommonResult<>();
        commonResult.setCode(resultCodeEnum.getCode());
        commonResult.setMessage(resultCodeEnum.getMessage());
        return commonResult;
    }

    public static <T> CommonResult<T> fail(CommonResultCodeEnum resultCodeEnum, String message) {
        CommonResult<T> commonResult = new CommonResult<>();
        commonResult.setCode(resultCodeEnum.getCode());
        commonResult.setMessage(message);
        return commonResult;
    }

    public static <T> CommonResult<T> fail(Integer code, String message) {
        CommonResult<T> commonResult = new CommonResult<>();
        commonResult.setCode(code);
        commonResult.setMessage(message);
        return commonResult;
    }

    public static <T> CommonResult<T> fail(String message) {
        CommonResult<T> commonResult = new CommonResult<>();
        commonResult.setCode(CommonResultCodeEnum.FAIL.getCode());
        commonResult.setMessage(message);
        return commonResult;
    }

    public static <T> CommonResult<T> custom(Integer code, String message, T data) {
        CommonResult<T> commonResult = new CommonResult<>();
        commonResult.setCode(code);
        commonResult.setMessage(message);
        commonResult.setData(data);
        return commonResult;
    }

    public static <T> CommonResult<T> convertWithOutData(CommonResult<?> message) {
        CommonResult<T> commonResult = new CommonResult<>();
        commonResult.setCode(message.code);
        commonResult.setMessage(message.message);
        return commonResult;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return CommonResultCodeEnum.SUCCESS.getCode().equals(code);
    }

    @JsonIgnore
    public boolean isFail() {
        return !isSuccess();
    }

}
