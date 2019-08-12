package com.platform.annotation;

/**
 * TODO
 *
 * @author zhaozhongchao
 * @date 2019/4/9
 **/

import java.lang.annotation.*;

/**
 * Annotation:见http://blog.csdn.net/sodino/article/details/7987888
 */
@Target(ElementType.TYPE)//ElementType.TYPE用于标识类、接口(包括内注自身)、枚举
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited //子类继承父类上配置的注释,当子类也配置了注释，会覆盖父类注解属性;不添加改注释则子类无法继承到父类的注解,注解为null
public @interface Author {
    /**
     * 修饰符仅可为public, protected, private & static的组合
     *
     * @author zhaozhongchao
     * @date 2019/4/9
     */
    enum AppEnum {
        /**
         * web
         */
        Web,
        /**
         * Client
         */
        Client,
        /**
         * Service
         */
        Service,
        UnDesignated
    }

    //public & abstract的组合或默认
    AppEnum type() default AppEnum.UnDesignated;

    String name() default "unknown";

    String webSite() default "N/A";
}


