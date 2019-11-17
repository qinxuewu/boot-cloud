package com.github.cache.zk;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 功能描述: 
 * @author: qinxuewu
 * @date: 2019/11/7 16:20
 * @since 1.0.0 
 */
public class ZooKeeperWatcher implements Watcher {

    private static final Logger logger = LoggerFactory.getLogger(ZooKeeperWatcher.class);
    @Override
    public void process(WatchedEvent event) {
        logger.info("【Watcher监听事件】={}",event.getState());
        logger.info("【监听路径为】={}",event.getPath());
        //  三种监听类型： 创建，删除，更新
        logger.info("【监听的类型为】={}",event.getType());
    }
}
