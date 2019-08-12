package com.mybatis.core.annotation;

import java.lang.annotation.*;

/**
 * TODO
 *
 * @author zhaozhongchao
 * @date 2019/4/8
 **/
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Param {

    String value() default "";
}
