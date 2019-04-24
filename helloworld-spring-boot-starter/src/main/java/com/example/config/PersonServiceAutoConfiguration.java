package com.example.config;
import com.example.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 自动配置类
 * @author qinxuewu
 * @version 1.00
 * @time  24/4/2019 下午 5:58
 * @email 870439570@qq.com
 */
@Configuration
@EnableConfigurationProperties(PersonProperties.class)
@ConditionalOnClass(PersonService.class)
@ConditionalOnProperty(prefix = "spring.person", value = "enabled", matchIfMissing = true)
public class PersonServiceAutoConfiguration {

    @Autowired
    private PersonProperties properties;

    /**
     *  当容器中没有指定Bean的情况下，自动配置PersonService类
     *
     * ConditionalOnClass：当类路径classpath下有指定的类的情况下进行自动配置
     * ConditionalOnMissingBean:当容器(Spring Context)中没有指定Bean的情况下进行自动配置
     * ConditionalOnProperty(prefix = “example.service”, value = “enabled”, matchIfMissing = true)，当配置文件中example.service.enabled=true时进行自动配置，如果没有设置此值就默认使用matchIfMissing对应的值
     * ConditionalOnMissingBean，当Spring Context中不存在该Bean时。
     * ConditionalOnBean:当容器(Spring Context)中有指定的Bean的条件下
     * ConditionalOnMissingClass:当类路径下没有指定的类的条件
     * ConditionalOnExpression:基于SpEL表达式作为判断条件
     * ConditionalOnJava:基于JVM版本作为判断条件
     * ConditionalOnJndi:在JNDI存在的条件下查找指定的位置
     * ConditionalOnNotWebApplication:当前项目不是Web项目的条件下
     * ConditionalOnWebApplication:当前项目是Web项目的条件下
     * ConditionalOnResource:类路径下是否有指定的资源
     * ConditionalOnSingleCandidate:当指定的Bean在容器中只有一个，或者在有多个Bean的情况下，用来指定首选的Bean
     *
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(PersonService.class)
    public PersonService personService(){
        PersonService personService = new PersonService(properties);
        return personService;
    }

}
