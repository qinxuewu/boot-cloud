package com.github.inventory.request;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能描述: 请求内存队列
 * @author: qinxuewu
 * @date: 2019/11/1 10:07
 * @since 1.0.0
 */
public class RequestQueue {

    private List<ArrayBlockingQueue> queueList=new ArrayList<ArrayBlockingQueue>();

    /**
     * 标识位map
     */
    private Map<Integer,Boolean> flagMap=new ConcurrentHashMap<Integer, Boolean>();

    /**
     * 静态内部类单例模式
     */
    private static  class  Singleton{
        private  static  RequestQueue instance;
        static {
            instance=new RequestQueue();
        }
        public  static  RequestQueue getInstance(){
            return instance;
        }
    }

    /**
     * jvm的机制去保证多线程并发安全
     * 内部类的初始化，一定只会发生一次，不管多少个线程并发去初始化
     * @return
     */
    public  static  RequestQueue getInstance(){
        return  Singleton.getInstance();
    }

    /**
     * 添加一个队列
     * @param queue
     */
    public  void  addQueue(ArrayBlockingQueue<Request> queue){
        this.queueList.add(queue);
    }

    /**
     * 获取内存队列的数量
     * @return
     */
    public int queueSize(){
        return queueList.size();
    }

    /**
     * 获取指定位置的内存队列
     * @param index
     * @return
     */
    public  ArrayBlockingQueue<Request> getQueue(int index){
        return  queueList.get(index);
    }

    public Map<Integer,Boolean> getFlagMap(){
        return  flagMap;
    }

}
