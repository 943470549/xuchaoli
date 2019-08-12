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
@Target({ElementType.METHOD})
public @interface Insert {

    String value() default "";
}
