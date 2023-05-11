package com.liu.user.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.liu.common.entity.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author gdLiu
 * @date 2023/5/11 16:39
 */
@Slf4j
@Validated
@ApiSort(10)
@RestController
@Api(tags = "用户")
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/info")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "用户信息获取-ID", notes = "用户信息获取-ID")
    public CommonResult<String> infoById(@RequestParam(value = "userId")
                                         @NotBlank(message = "用户ID不能为空！")
                                         String userId) {
        JSONObject json = new JSONObject();
        json.set("userId", userId);
        json.set("userName", RandomUtil.randomChinese());
        json.set("time", LocalDateTime.now().format(DatePattern.NORM_DATETIME_FORMATTER));
        return CommonResult.success(json.toString());
    }
}
