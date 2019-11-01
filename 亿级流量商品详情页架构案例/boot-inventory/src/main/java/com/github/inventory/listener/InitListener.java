package com.github.inventory.listener;

import com.github.inventory.config.RedisConfig;
import com.github.inventory.thread.RequestProcessorThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


/**
 * 功能描述:  初始化监听类
 * @author: qinxuewu
 * @date: 2019/11/1 9:57
 * @since 1.0.0
 */
@Component
public class InitListener implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(InitListener.class);
    @Override
    public void run(String... args) throws Exception {
        // 初始化工作线程池和内存队列
        RequestProcessorThreadPool.init();
        log.info("【InitListener 初始化执行..........】");
    }


}
