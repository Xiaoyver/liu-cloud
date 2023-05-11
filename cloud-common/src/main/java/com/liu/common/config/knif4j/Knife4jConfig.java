package com.liu.common.config.knif4j;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static com.liu.common.constant.HttpHeaderConstant.*;


/**
 * knife4j配置
 *
 * @author gdLiu
 * @date 22022/10/21 15:34
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
public class Knife4jConfig {

    /**
     * C端的接口文档配置
     *
     * @return springfox.documentation.spring.web.plugins.Docket
     * @author gdLiu
     * @date 2022/10/21 15:34
     */
    @Bean
    public Docket webRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("小小猫儿鱼-cloud文档-C端")
                .apiInfo(apiInfo())
                .pathMapping("/")
                .useDefaultResponseMessages(false)
                .select()
                //此包路径下的类，才生成接口文档
                .apis(RequestHandlerSelectors.basePackage("com.liu"))
                //加了ApiOperation注解的类，才生成接口文档
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .globalRequestParameters(globalRequestParameters())
                .globalResponses(HttpMethod.GET, globalResponse())
                .globalResponses(HttpMethod.POST, globalResponse())
                .globalResponses(HttpMethod.PUT, globalResponse())
                .globalResponses(HttpMethod.DELETE, globalResponse());
    }

    /**
     * 管理端的接口文档配置
     *
     * @return springfox.documentation.spring.web.plugins.Docket
     * @author gdLiu
     * @date 2022/10/21 15:34
     */
    @Bean
    public Docket adminRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("小小猫儿鱼-cloud文档-管理端")
                .apiInfo(apiInfo())
                .pathMapping("/")
                .useDefaultResponseMessages(false)
                .select()
                //此包路径下的类，才生成接口文档
                .apis(RequestHandlerSelectors.basePackage("com.liu"))
                //加了ApiOperation注解的类，才生成接口文档
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.ant("/admin/**"))
                .build()
                .globalRequestParameters(globalRequestParameters())
                .globalResponses(HttpMethod.GET, globalResponse())
                .globalResponses(HttpMethod.POST, globalResponse())
                .globalResponses(HttpMethod.PUT, globalResponse())
                .globalResponses(HttpMethod.DELETE, globalResponse());

    }

    /**
     * 管理端的接口文档配置
     *
     * @return springfox.documentation.spring.web.plugins.Docket
     * @author gdLiu
     * @date 2022/10/21 15:34
     */
    @Bean
    public Docket pubRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("小小猫儿鱼-cloud文档-公共")
                .apiInfo(apiInfo())
                .pathMapping("/")
                .useDefaultResponseMessages(false)
                .select()
                //此包路径下的类，才生成接口文档
                .apis(RequestHandlerSelectors.basePackage("com.liu"))
                //加了ApiOperation注解的类，才生成接口文档
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.ant("/pub/**"))
                .build()
                .globalResponses(HttpMethod.GET, globalResponse())
                .globalResponses(HttpMethod.POST, globalResponse())
                .globalResponses(HttpMethod.PUT, globalResponse())
                .globalResponses(HttpMethod.DELETE, globalResponse());
    }

    /**
     * 注册枚举值属性建造器，用于增强swagger对枚举显示描述的支持
     */
    @Bean
    public SwaggerEnumPlugin swaggerEnumPlugin() {
        return new SwaggerEnumPlugin();
    }

    /**
     * 生成请求头需要的公共参数
     * <p>
     * 其他请求体里面的公共参数也可接着写
     *
     * @return java.util.List<springfox.documentation.service.RequestParameter>
     * @author gdLiu
     * @date 2022/10/21 15:34
     */
    List<RequestParameter> globalRequestParameters() {
        return List.of(
                new RequestParameterBuilder()
                        .name(TOKEN)
                        .description("令牌")
                        .in(ParameterType.HEADER)
                        .required(true)
                        .parameterIndex(1)
                        .example(new ExampleBuilder().value("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9").build())
                        .query(q -> q.model(t -> t.scalarModel(ScalarType.STRING)))
                        .build(),
                new RequestParameterBuilder()
                        .name(CLIENT)
                        .description("客户端(IOS,ANDROID,PC,WEB)")
                        .in(ParameterType.HEADER)
                        .required(true)
                        .parameterIndex(3)
                        .example(new ExampleBuilder().value("IOS").build())
                        .query(q -> q.model(t -> t.scalarModel(ScalarType.STRING)))
                        .build(),
                new RequestParameterBuilder()
                        .name(TIMESTAMP)
                        .description("时间戳")
                        .in(ParameterType.HEADER)
                        .required(false)
                        .parameterIndex(4)
                        .example(new ExampleBuilder().value("1666666666666").build())
                        .query(q -> q.model(t -> t.scalarModel(ScalarType.LONG)))
                        .build()
        );
    }

    /**
     * 全局的响应
     *
     * @return java.util.List<springfox.documentation.service.Response>
     * @author gdLiu
     * @date 2022/10/21 15:34
     */
    private static List<Response> globalResponse() {
        return List.of(
                new ResponseBuilder().code("200").description("成功").build(),
                new ResponseBuilder().code("3XX").description("重定向异常").build(),
                new ResponseBuilder().code("4XX").description("失败,根据响应码和信息做出判断").build(),
                new ResponseBuilder().code("5XX").description("服务器内部异常").build(),
                new ResponseBuilder().code("10XX").description("自定义异常").build()
        );
    }

    /**
     * 文档主页信息
     *
     * @return springfox.documentation.service.ApiInfo
     * @author gdLiu
     * @date 2022/10/21 15:34
     */
    private static ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("小小猫儿鱼-cloud在线API接口文档")
                .description("小小猫儿鱼-cloud文档在线API接口文档,基于knife4j")
                .contact(new Contact("gdLiu", "http://xiaoyver.top", "iloveyou0116@163.com"))
                .version("v1.0.0")
                .license("The MIT License")
                .licenseUrl("https://mit-license.org")
                .termsOfServiceUrl("http://xiaoyver.top")
                .build();
    }
}
