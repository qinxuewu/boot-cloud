package com.github.listener.event;
// 抽象事件发布
public interface EventPublishService <T>{

    void publishEvent(T event);
}
