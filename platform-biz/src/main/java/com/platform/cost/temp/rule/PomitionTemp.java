package com.platform.cost.temp.rule;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author caozj
 * @version 1.0
 * @ClassName: 优惠模糊计算规则
 * @Description: 类功能的描述
 * @date 2023-06-11  10:58
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface PomitionTemp {


    @AliasFor("tempCode")
    String value() default "";

    @AliasFor("value")
    String tempCode() default "";


}
