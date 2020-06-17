package com.github.annotation;


import com.github.service.HelloServiceA;
import com.github.service.HelloServiceB;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 ImportSelector介绍：
     1.用于指定需要注册为bean的Class名称
     2.使用@Import引入了一个ImportSelector实现类后，会把实现类中返回的Class名称都定义为bea
 */
public class HelloWorldImportSelector  implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[] {HelloServiceA.class.getName(), HelloServiceB.class.getName()};
    }
}
