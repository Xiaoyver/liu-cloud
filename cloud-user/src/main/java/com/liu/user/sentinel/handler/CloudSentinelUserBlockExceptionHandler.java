package com.liu.user.sentinel.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author gdLiu
 * @date 2023/5/12 08:29
 */
@Slf4j
@Primary
@Component
public class CloudSentinelUserBlockExceptionHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpStatus.OK.value());
        PrintWriter out = response.getWriter();
        out.write(buildErrorResult(e));
        out.flush();
        out.close();
    }

    private String buildErrorResult(BlockException e) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", HttpStatus.TOO_MANY_REQUESTS.value());
        jsonObject.put("data", "用户限流");
        if (e instanceof FlowException) {
            jsonObject.put("message", "接口限流");
        } else if (e instanceof DegradeException) {
            jsonObject.put("message", "服务降级");
        } else if (e instanceof ParamFlowException) {
            jsonObject.put("message", "热点参数限流");
        } else if (e instanceof AuthorityException) {
            jsonObject.put("message", "授权规则不通过");
        } else if (e instanceof SystemBlockException) {
            jsonObject.put("message", "系统阻塞不同通过");
        } else {
            jsonObject.put("message", "sentinel限制,异常捕获未定义");
        }
        return jsonObject.toJSONString();
    }
}