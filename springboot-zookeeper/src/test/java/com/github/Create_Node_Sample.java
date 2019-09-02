package com.github;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;


/**
 * 创建节点
 *
 * @author qinxuewu
 * @create 19/9/2下午1:08
 * @since 1.0.0
 */


public class Create_Node_Sample {
    public static void main(String[] args) throws Exception {
        String path = "/zk-book/c1";
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                .sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
        client.start();
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, "init".getBytes());
        System.out.println("success create znode: " + path);
    }
}
