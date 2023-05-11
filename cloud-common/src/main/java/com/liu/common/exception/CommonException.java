package com.liu.common.exception;

import com.liu.common.enums.CommonResultCodeEnum;
import lombok.Getter;

/**
 * 公共异常
 *
 * @author gdLiu
 * @date 2022/10/21 15:34
 */
@Getter
public class CommonException extends RuntimeException {

    private static final long serialVersionUID = -8233600088047683504L;

    /**
     * 代码
     */
    private final Integer code;

    public CommonException(CommonResultCodeEnum errorCode, String msg) {
        super(msg);
        this.code = errorCode.getCode();
    }

    public CommonException(CommonResultCodeEnum resultCodeEnum, Throwable cause) {
        super(resultCodeEnum.getMessage(), cause);
        this.code = resultCodeEnum.getCode();
    }

    public CommonException(String msg, Throwable cause) {
        super(msg, cause);
        this.code = CommonResultCodeEnum.SYS_ERROR.getCode();
    }

    public CommonException(Integer errorCode, String msg) {
        super(msg);
        this.code = errorCode;
    }

    public CommonException(CommonResultCodeEnum errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public CommonException(String msg) {
        super(msg);
        this.code = CommonResultCodeEnum.FAIL.getCode();
    }

}
