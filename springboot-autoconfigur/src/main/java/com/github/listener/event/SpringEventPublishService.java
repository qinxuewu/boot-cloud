package com.github.listener.event;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;


/**
 * 功能描述:   Spring 实现的事件发布公共组件
 * @author: qinxuewu
 */

@Component("springEventPublishService")
public class SpringEventPublishService implements EventPublishService<ApplicationEvent>, ApplicationContextAware {
    private ApplicationContext applicationContext;
    @Override
    public void publishEvent(ApplicationEvent event) {
        applicationContext.publishEvent(event);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
