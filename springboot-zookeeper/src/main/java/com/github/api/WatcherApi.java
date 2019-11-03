package com.github.api;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  实现Watcher监听
 *
 * @author qinxuewu
 * @create 19/9/2下午9:10
 * @since 1.0.0
 */
public class WatcherApi implements Watcher {

    private static final Logger logger = LoggerFactory.getLogger(WatcherApi.class);
    @Override
    public void process(WatchedEvent event) {
        logger.info("【Watcher监听事件】={}",event.getState());
        logger.info("【监听路径为】={}",event.getPath());
        //  三种监听类型： 创建，删除，更新
        logger.info("【监听的类型为】={}",event.getType());

    }
}
