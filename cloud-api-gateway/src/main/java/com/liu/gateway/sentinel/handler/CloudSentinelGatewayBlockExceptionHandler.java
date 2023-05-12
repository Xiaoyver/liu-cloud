package com.liu.gateway.sentinel.handler;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author gdLiu
 * @date 2023/5/12 08:29
 */
@Slf4j
@Primary
@Component
public class CloudSentinelGatewayBlockExceptionHandler implements BlockRequestHandler {

    @Override
    public Mono<ServerResponse> handleRequest(ServerWebExchange exchange, Throwable ex) {
        if (acceptsHtml(exchange)) {
            return htmlErrorResponse(ex);
        }
        // JSON result by default.
        return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(buildErrorResult(ex));
    }

    private Mono<ServerResponse> htmlErrorResponse(Throwable ex) {
        return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(buildErrorResult(ex));
    }

    private String buildErrorResult(Throwable e) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", HttpStatus.TOO_MANY_REQUESTS.value());
        jsonObject.put("data", "网关限流");
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

    /**
     * Reference from {@code DefaultErrorWebExceptionHandler} of Spring Boot.
     */
    private boolean acceptsHtml(ServerWebExchange exchange) {
        try {
            List<MediaType> acceptedMediaTypes = exchange.getRequest().getHeaders().getAccept();
            acceptedMediaTypes.remove(MediaType.ALL);
            MediaType.sortBySpecificityAndQuality(acceptedMediaTypes);
            return acceptedMediaTypes.stream()
                    .anyMatch(MediaType.TEXT_HTML::isCompatibleWith);
        } catch (InvalidMediaTypeException ex) {
            return false;
        }
    }
}