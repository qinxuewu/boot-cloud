package com.github.condition;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * 系统属性条件判断

 */
public class OnSystemPropertyCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        Map<String, Object> attributes =
                metadata.getAnnotationAttributes(ConditionalOnSystemProperty.class.getName());

        String propertyName = String.valueOf(attributes.get("name"));

        String propertyValue = String.valueOf(attributes.get("value"));

        //   user.name= 获取当前系统用户名
        String javaPropertyValue = System.getProperty(propertyName);
        System.out.println("propertyName:  "+propertyName);
        System.out.println("javaPropertyValue:  "+javaPropertyValue);
        return propertyValue.equals(javaPropertyValue);
    }
}
