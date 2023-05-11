package com.liu.common.config.knif4j;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.liu.common.annotation.SwaggerDisplayEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.PropertySpecificationBuilder;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.spi.service.ExpandedParameterBuilderPlugin;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.ParameterContext;
import springfox.documentation.spi.service.contexts.ParameterExpansionContext;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 枚举插件
 * <p>
 * <a href="https://blog.gelu.me/2021/Knife4j-Swagger%E8%87%AA%E5%AE%9A%E4%B9%89%E6%9E%9A%E4%B8%BE%E7%B1%BB%E5%9E%8B/">参考网址</a>
 * <p>
 * ModelPropertyBuilderPlugin 适用于 application/json 提交的请求参数 及 json返回值 注释
 * <p>
 * ParameterBuilderPlugin、OperationBuilderPlugin 适用于请求方法上的直接枚举参数
 * <p>
 * ExpandedParameterBuilderPlugin 适用于POJO/VO内的枚举参数（网络上搜索到前几篇内容未实现的部分）
 *
 * @author gdLiu
 * @date 2022/11/4 13:34
 */
@Slf4j
public class SwaggerEnumPlugin implements ModelPropertyBuilderPlugin,
        ParameterBuilderPlugin, OperationBuilderPlugin, ExpandedParameterBuilderPlugin {

    private static final Joiner JOINER = Joiner.on(",");
    private static final String DESCRIPTION = "description";
    public static final String ERROR_MSG = "自定义Knife4j插件异常(可忽略),异常内容:{}";

    @Override
    public void apply(ModelPropertyContext context) {
        try {
            Optional<BeanPropertyDefinition> optional = context.getBeanPropertyDefinition();
            if (optional.isEmpty()) {
                return;
            }
            Class<?> fieldType = optional.get().getField().getRawType();
            addDescForEnum(context, fieldType);
        } catch (Exception e) {
            log.warn(ERROR_MSG, e.getMessage());
        }

    }

    /**
     * 为枚举添加desc
     *
     * @param context   上下文
     * @param fieldType 字段类型
     * @author gdLiu
     * @date 2022/11/05 09:35:10
     */
    private void addDescForEnum(ModelPropertyContext context, Class<?> fieldType) {
        //补充枚举
        supplementEnum(context, fieldType);
    }

    /**
     * 补充枚举
     *
     * @param context   上下文
     * @param fieldType 字段类型
     * @author gdLiu
     * @date 2022/11/05 09:34:38
     */
    private void supplementEnum(ModelPropertyContext context, Class<?> fieldType) {
        if (Enum.class.isAssignableFrom(fieldType)) {
            SwaggerDisplayEnum anno = AnnotationUtils.findAnnotation(fieldType, SwaggerDisplayEnum.class);
            if (anno != null) {
                Object[] enumConstants = fieldType.getEnumConstants();
                List<String> displayValues = getDisplayValues(anno, enumConstants);
                PropertySpecificationBuilder pb = context.getSpecificationBuilder();
                Field descField = ReflectionUtils.findField(pb.getClass(), DESCRIPTION);
                assert descField != null;
                ReflectionUtils.makeAccessible(descField);

                String joinText = (ReflectionUtils.getField(descField, pb) == null ? "" :
                        (ReflectionUtils.getField(descField, pb) + ":[ ")) + JOINER.join(displayValues) + " ] ";
                pb.description(joinText);
            }
        }
    }

    @Override
    public void apply(OperationContext context) {
        Map<String, List<String>> map = new HashMap<>(16);
        List<ResolvedMethodParameter> parameters = context.getParameters();
        try {
            parameters.forEach(parameter -> {
                ResolvedType parameterType = parameter.getParameterType();
                Class<?> clazz = parameterType.getErasedType();
                if (Enum.class.isAssignableFrom(clazz)) {
                    SwaggerDisplayEnum annotation = AnnotationUtils.findAnnotation(clazz, SwaggerDisplayEnum.class);
                    if (annotation != null) {
                        Object[] enumConstants = clazz.getEnumConstants();
                        List<String> displayValues = getDisplayValues(annotation, enumConstants);
                        map.put(parameter.defaultName().orElse(""), displayValues);

                        OperationBuilder operationBuilder = context.operationBuilder();
                        Field parametersField = ReflectionUtils.findField(operationBuilder.getClass(), "parameters");
                        assert parametersField != null;
                        ReflectionUtils.makeAccessible(parametersField);
                        List<Parameter> list = (List<Parameter>) ReflectionUtils.getField(parametersField, operationBuilder);
                        assert list != null;
                        map.forEach((k, v) -> {
                            for (Parameter currentParameter : list) {
                                if (StringUtils.equals(currentParameter.getName(), k)) {
                                    Field description = ReflectionUtils.findField(currentParameter.getClass(), DESCRIPTION);
                                    assert description != null;
                                    ReflectionUtils.makeAccessible(description);
                                    Object field = ReflectionUtils.getField(description, currentParameter);
                                    ReflectionUtils.setField(description, currentParameter, field + ":[ " + JOINER.join(v) + " ]");
                                    break;
                                }
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            log.warn(ERROR_MSG, e.getMessage());
        }
    }

    @Override
    public void apply(ParameterContext context) {
        try {
            Class<?> type = context.resolvedMethodParameter().getParameterType().getErasedType();
            RequestParameterBuilder parameterBuilder = context.requestParameterBuilder();
            setAvailableValue(parameterBuilder, type);
        } catch (Exception e) {
            log.warn(ERROR_MSG, e.getMessage());
        }
    }

    @Override
    public void apply(ParameterExpansionContext context) {

        try {
            Class<?> type = context.getFieldType().getErasedType();
            RequestParameterBuilder parameterBuilder = context.getRequestParameterBuilder();
            setAvailableValue(parameterBuilder, type);
        } catch (Exception e) {
            log.warn(ERROR_MSG, e.getMessage());
        }
    }


    private void setAvailableValue(RequestParameterBuilder parameterBuilder, Class<?> type) {
        if (Enum.class.isAssignableFrom(type)) {
            SwaggerDisplayEnum annotation = AnnotationUtils.findAnnotation(type, SwaggerDisplayEnum.class);
            if (annotation != null) {
                String code = annotation.code();
                Object[] enumConstants = type.getEnumConstants();
                List<String> displayValues = Arrays.stream(enumConstants).filter(Objects::nonNull).map(item -> {
                    Class<?> currentClass = item.getClass();

                    Field codeField = ReflectionUtils.findField(currentClass, code);
                    assert codeField != null;
                    ReflectionUtils.makeAccessible(codeField);
                    Object codeStr = ReflectionUtils.getField(codeField, item);
                    assert codeStr != null;
                    return codeStr.toString();

                }).collect(Collectors.toList());

                // 设置可用值
                Field description = ReflectionUtils.findField(parameterBuilder.getClass(), DESCRIPTION);
                assert description != null;
                String joinText = (ReflectionUtils.getField(description, parameterBuilder) == null ? "" :
                        (ReflectionUtils.getField(description, parameterBuilder) + ":[ ")) + JOINER.join(displayValues) + " ] ";
                parameterBuilder.description(joinText);
            }
        }
    }

    private List<String> getDisplayValues(SwaggerDisplayEnum annotation, Object[] enumConstants) {
        if (annotation == null) {
            return Lists.newArrayList();
        }
        String code = annotation.code();
        String value = annotation.value();
        return Arrays.stream(enumConstants).filter(Objects::nonNull).map(
                item -> {
                    Class<?> currentClass = item.getClass();
                    Field codeField = ReflectionUtils.findField(currentClass, code);
                    assert codeField != null;
                    ReflectionUtils.makeAccessible(codeField);
                    Object codeStr = ReflectionUtils.getField(codeField, item);

                    Field descField = ReflectionUtils.findField(currentClass, value);
                    assert descField != null;
                    ReflectionUtils.makeAccessible(descField);
                    Object descStr = ReflectionUtils.getField(descField, item);
                    return codeStr + ":" + descStr;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }
}