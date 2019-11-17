package com.github.cache.listener;

import com.github.cache.rebuild.RebuildCacheThread;
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
    public void run(String... args){
        new Thread(new RebuildCacheThread()).start();
        log.info("【InitListener 初始化执行..........】");
    }


}
