package com.github.annotation;


import org.springframework.stereotype.Repository;

import java.lang.annotation.*;

/**
 * 功能描述:   自定义模式注解  派生于@Repository
 * @author: qinxuewu
 * @date: 2020/6/17 10:51
 * @since 1.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repository
public @interface MyRepository {

    String value() default "";
}
