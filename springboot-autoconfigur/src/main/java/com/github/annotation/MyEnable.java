package com.github.annotation;


import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;

import java.lang.annotation.*;

/**
 * 功能描述: 注解模块,  所谓“模块”是指具备相同领域的功能组件集合， 组合所形成一个独立
 * 的单元。
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(HelloWorldImportSelector.class)
public @interface MyEnable {
}
