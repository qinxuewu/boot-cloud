package com.github.api;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.List;

/**
 *
 * ZkApi
 * @author qinxuewu
 * @create 19/9/2下午1:49
 * @since 1.0.0
 *
 */

@Component
public class ZkApi {

    private static final Logger logger = LoggerFactory.getLogger(ZkApi.class);

    @Autowired
    private ZooKeeper zkClient;


    /**
     * 判断指定节点是否存在
     * @param path
     * @param needWatch
     * @return
     */
    public Stat exists(String path, boolean needWatch){
        try {
            return zkClient.exists(path,needWatch);
        } catch (Exception e) {
            logger.error("【断指定节点是否存在异常】{},{}",path,e);
            return null;
        }
    }

    /**
     * 创建持久化节点
     * @param path
     * @param data
     */
    public boolean createNode(String path, String data){
        try {
            zkClient.create(path,data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            return true;
        } catch (Exception e) {
            logger.error("【创建持久化节点异常】{},{},{}",path,data,e);
            return false;
        }
    }


    /**
     * 修改持久化节点
     * @param path
     * @param data
     */
    public boolean updateNode(String path, String data){
        try {
            //zk的数据版本是从0开始计数的。如果客户端传入的是-1，则表示zk服务器需要基于最新的数据进行更新。如果对zk的数据节点的更新操作没有原子性要求则可以使用-1.
            //version参数指定要更新的数据的版本, 如果version和真实的版本不同, 更新操作将失败. 指定version为-1则忽略版本检查
            zkClient.setData(path,data.getBytes(),-1);
            return true;
        } catch (Exception e) {
            logger.error("【修改持久化节点异常】{},{},{}",path,data,e);
            return false;
        }
    }

    /**
     * 删除持久化节点
     * @param path
     */
    public boolean deleteNode(String path){
        try {
            //version参数指定要更新的数据的版本, 如果version和真实的版本不同, 更新操作将失败. 指定version为-1则忽略版本检查
            zkClient.delete(path,-1);
            return true;
        } catch (Exception e) {
            logger.error("【删除持久化节点异常】{},{}",path,e);
            return false;
        }
    }

    /**
      * 获取当前节点的子节点(不包含孙子节点)
      * @param path 父节点path
      */
    public List<String> getChildren(String path) throws KeeperException, InterruptedException{
        List<String> list = zkClient.getChildren(path, false);
        return list;
    }

    /**
     * 获取指定节点的值
     * @param path
     * @return
     */
    public  String getData(String path){
        try {
            Stat stat=new Stat();
            byte[] bytes=zkClient.getData(path,null,stat);
            return  new String(bytes);
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
    }


    /**
     * 测试方法  初始化
     */
    @PostConstruct
    public  void init(){
        logger.info("【执行初始化测试方法。。。。。。。。。。。。】");
        createNode("/zk-test","测试");
    }

}
