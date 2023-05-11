package com.liu.common.exception;

import com.liu.common.constant.SymbolConstant;
import com.liu.common.entity.CommonResult;
import com.liu.common.enums.CommonResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;

import static com.liu.common.enums.CommonResultCodeEnum.*;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

/**
 * 全局的异常处理器
 *
 * @author gdLiu
 * @date 2022/10/21 15:34
 */
@Slf4j
@RestControllerAdvice
@Order(value = LOWEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public CommonResult<String> exceptionHandler(Exception e) {
        log.error("系统异常:{} , {}", e.getMessage(), e);
        return CommonResult.fail(CommonResultCodeEnum.FAIL, "系统异常:" + e.getMessage());
    }

    @ExceptionHandler(CommonException.class)
    public CommonResult<String> commonExceptionHandler(CommonException e) {
        log.warn("公共异常:{}", e.getMessage(), e);
        return CommonResult.fail(e.getCode(), e.getMessage());
    }

    /**
     * 处理绑定异常
     * <p>
     * Valid注解触发的参数校验异常
     *
     * @param e 异常
     * @return {@link CommonResult}<{@link Object}>
     * @author gdLiu
     * @date 2022/8/6 11:07
     */
    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
    public CommonResult<Object> handleBindException(Exception e) {
        log.warn("Valid注解触发自定义异常(参数绑定校验异常)", e);
        if (e instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            // getFieldError获取的是第一个不合法的参数(P.S.如果有多个参数不合法的话)
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                return CommonResult.fail(PARAMETER_VALIDATION_ERROR, fieldError.getDefaultMessage() + "(" + fieldError.getField() + ")");
            }
        }
        if (e instanceof BindException) {
            // getFieldError获取的是第一个不合法的参数(P.S.如果有多个参数不合法的话)
            FieldError fieldError = ((BindException) e).getFieldError();
            if (fieldError != null) {
                return CommonResult.fail(PARAMETER_VALIDATION_ERROR, fieldError.getDefaultMessage() + "(" + fieldError.getField() + ")");
            }
        }
        return CommonResult.fail(PARAMETER_VALIDATION_ERROR);
    }

    @ExceptionHandler(value = {ValidationException.class})
    public CommonResult<Object> handleValidationException(Exception e) {
        ValidationException validationException = (ValidationException) e;
        String message = validationException.getMessage();
        if (StringUtils.hasLength(message) && message.contains(SymbolConstant.COLON)) {
            return CommonResult.fail(PARAMETER_VALIDATION_ERROR, message.substring(message.indexOf(SymbolConstant.COLON)));
        }
        return CommonResult.fail(PARAMETER_VALIDATION_ERROR);
    }

    /**
     * 处理http消息不可读例外
     *
     * @param e 异常信息
     * @return {@link CommonResult}<{@link Object}>
     * @author gdLiu
     * @date 2022/8/9 17:08
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public CommonResult<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        if (StringUtils.hasLength(e.getMessage()) && e.getMessage().contains(SymbolConstant.COLON)) {
            String message = e.getMessage();
            if (message.contains("JSON parse error") && message.contains(SymbolConstant.SEMICOLON)) {
                return CommonResult.fail(message.substring(message.indexOf(SymbolConstant.COLON), message.indexOf(SymbolConstant.SEMICOLON)).trim());
            }
            return CommonResult.fail(e.getMessage().substring(0, e.getMessage().indexOf(SymbolConstant.COLON)));
        }
        return CommonResult.fail(e.getMessage());
    }

    /**
     * http请求方法不支持异常
     *
     * @param e 异常
     * @return {@link CommonResult}<{@link Object}>
     * @author gdLiu
     * @date 2022/8/25 15:57
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public CommonResult<Object> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        StringBuilder sb = new StringBuilder();
        sb.append("不支持");
        sb.append(e.getMethod());
        sb.append("请求方法，");
        sb.append("支持以下");
        String[] methods = e.getSupportedMethods();
        if (methods != null) {
            for (String str : methods) {
                sb.append(str);
                sb.append("、");
            }
        }
        log.warn(sb.toString(), e);
        return CommonResult.fail(sb.toString());
    }

    /**
     * servlet请求绑定异常
     *
     * @param e 异常
     * @return com.liu.common.entity.CommonResult<java.lang.Object>
     * @author gdLiu
     * @date 2022/09/21 11:40:00
     */
    @ExceptionHandler(ServletRequestBindingException.class)
    public CommonResult<Object> servletRequestBindingException(ServletRequestBindingException e) {
        String parameterMiss = "参数缺失:";
        if (e instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException exception = (MissingServletRequestParameterException) e;
            return CommonResult.fail(PARAMETER_ERROR, parameterMiss + exception.getParameterName());
        }
        if (e instanceof MissingPathVariableException) {
            MissingPathVariableException exception = (MissingPathVariableException) e;
            return CommonResult.fail(PARAMETER_ERROR, parameterMiss + exception.getVariableName());
        }
        if (e instanceof MissingMatrixVariableException) {
            MissingMatrixVariableException exception = (MissingMatrixVariableException) e;
            return CommonResult.fail(PARAMETER_ERROR, parameterMiss + exception.getVariableName());
        }
        if (e instanceof MissingRequestHeaderException) {
            MissingRequestHeaderException exception = (MissingRequestHeaderException) e;
            return CommonResult.fail(HEADER_PARAMETER_ERROR, HEADER_PARAMETER_ERROR.getMessage() + ":" + exception.getHeaderName());
        }
        return CommonResult.fail(PARAMETER_ERROR, e.getMessage());
    }

}
