package com.github;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import java.util.concurrent.CountDownLatch;

/**
 *  java连接 ZooKeeper
 *
 * @author qinxuewu
 * @create 19/9/2上午10:54
 * @since 1.0.0
 */


public class ZkTest1 {



    public static void main(String[] args) {
        try {

            final CountDownLatch countDownLatch = new CountDownLatch(1);
            //连接成功后，会回调watcher监听，此连接操作是异步的，执行完new语句后，直接调用后续代码
            //  可指定多台服务地址 127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183
            ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 4000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {

                    if(Event.KeeperState.SyncConnected==event.getState()){
                            //如果收到了服务端的响应事件,连接成功
                        countDownLatch.countDown();
                    }
                }
            });
            countDownLatch.await();
            //CONNECTING
            System.err.println("------------获取连接状态-------------");
            System.out.println(zooKeeper.getState());

            System.err.println("------------创建节点-------------");
            zooKeeper.create("/zk-qxw",
                        "6666".getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE,  //节点的权限  完全开放
                        CreateMode.PERSISTENT);   //节点的类型  持久节点
            Thread.sleep(1000);
            Stat stat=new Stat();

            //得到当前节点的值
            System.err.println("------------得到当前节点的值-------------");
            byte[] bytes=zooKeeper.getData("/zk-qxw",null,stat);
            System.out.println(new String(bytes));

            System.err.println("------------修改节点值-------------");
            zooKeeper.setData("/zk-qxw","7777777".getBytes(),stat.getVersion());

            //得到当前节点的值
            System.err.println("------------得到修改后节点的值-------------");
            byte[] bytes1=zooKeeper.getData("/zk-qxw",null,stat);
            System.out.println(new String(bytes1));

            zooKeeper.delete("/zk-qxw",stat.getVersion());

          zooKeeper.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
