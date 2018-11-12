package com.example.utils;

import java.util.*;

/**
 * 负载均衡算法
 *
 *
 * 负载均衡的几种简单实现：
 * (1)轮询法（Round Robin）
 * (2)随机法（Random）
 * (3)源地址Hash法（Hash）
 * (4)加权轮询法（Weight Round Robin）
 * (5)加权随机法（Weight Random）
 * (6)最小连接数法（Least Connections）
 *
 * @author qinxuewu
 * @version 1.00
 * @time 22/8/2018上午 10:21
 */
public class LoadBalancingUtil {
    public static HashMap<String, Integer> serverWeightMap = new HashMap<String, Integer>();
    private static Integer pos=0;
    static {
        //第一个参数是IP地址,第二个是权重.
        serverWeightMap.put("192.168.1.100", 1);
        serverWeightMap.put("192.168.1.101", 2);
        serverWeightMap.put("192.168.1.102", 3);
        serverWeightMap.put("192.168.1.103", 4);
        serverWeightMap.put("192.168.1.104", 3);
        serverWeightMap.put("192.168.1.105", 2);
        serverWeightMap.put("192.168.1.106", 1);
        serverWeightMap.put("192.168.1.107", 2);
        serverWeightMap.put("192.168.1.108", 1);
        serverWeightMap.put("192.168.1.109", 4);
    }

    public static void main(String[] args) {
        String result = null;

        for(int i=0; i<10; i++){
            result = hash();
            System.out.println("load balance 的地址是:" + result);
        }
    }
    /**
     * 轮询法
     * 轮询法的优点在于：试图做到请求转移的绝对均衡。
     *
     * 轮询法的缺点在于：为了做到请求转移的绝对均衡，必须付出相当大的代价，因为为了保证pos变量修改的互斥性，
     * 需要引入重量级的悲观锁synchronized，这将会导致该段轮询代码的并发吞吐量发生明显的下降。
     */
    public static String roundRobin(){
        //取的IP地址的Set
        Set<String> ips = serverWeightMap.keySet();

        List<String> iplist = new ArrayList<>();
        iplist.addAll(ips);

        String server = null;
        synchronized (pos){
            if(pos > iplist.size())
                pos=0;
            server = iplist.get(pos);

            pos++;
        }
        return server;
    }

    /**
     * 随机法
     * 基于概率统计的理论，吞吐量越大，随机算法的效果越接近于轮询算法的效果。
     * @return
     */
    public  static  String random(){
        //取的IP地址的Set
        Set<String> ips = serverWeightMap.keySet();

        List<String> iplist = new ArrayList<String>();
        iplist.addAll(ips);

        String server = null;

        //获取IP的策略
        Random random = new Random();
        int pos = random.nextInt(iplist.size());
        return iplist.get(pos);
    }

    /**
     * 源地址哈希法
     * 优点在于：保证了相同客户端IP地址将会被哈希到同一台后端服务器，直到后端服务器列表变更。根据此特性可以在服务消费者与服务提供者之间建立有状态的session会话。
     * 缺点在于：除非集群中服务器的非常稳定，基本不会上下线，否则一旦有服务器上线、下线，那么通过源地址哈希算法路由到的服务器是服务器上线、
     * 下线前路由到的服务器的概率非常低，如果是session则取不到session，如果是缓存则可能引发”雪崩”。
     * @return
     */
    public  static  String hash(){
        //取的IP地址的Set
        Set<String> ips = serverWeightMap.keySet();

        List<String> iplist = new ArrayList<String>();
        iplist.addAll(ips);

        //获取IP的策略
        //获取远端请求的IP地址
        String remoteIP = "127.0.0.11";
        int hashCode = remoteIP.hashCode();
        hashCode = Math.abs(hashCode);//确保hash值是正数. 如果hash值是负数
        int ipSize = iplist.size();
        int pos = hashCode % ipSize;

        return iplist.get(pos);
    }

    /**
     * 加权轮询法
     * 与轮询法类似，只是在获取服务器地址之前增加了一段权重计算的代码，根据权重的大小，将地址重复地增加到服务器地址列表中，权重越大，该服务器每轮所获得的请求数量越多。
     * @return
     */
    public  static String weightRoundRobin(){
        //取的IP地址的Set
        Set<String> ips = serverWeightMap.keySet();
        Iterator<String> iterator = ips.iterator();

        List<String> iplist = new ArrayList<String>();
        while (iterator.hasNext()){
            String server = iterator.next();
            int weight = serverWeightMap.get(server);
            //按照权重来添加比例.
            for(int i=0; i<weight; i++){
                iplist.add(server);
            }
        }

        String server=null;
        synchronized (pos){
            if(pos > iplist.size())
                pos=0;

            server = iplist.get(pos);

            pos++;
        }
        return server;
    }

    /**
     * 加权随机
     * @return
     */
    public  static String weightRandom(){
        //取的IP地址的Set
        Set<String> ips = serverWeightMap.keySet();
        Iterator<String> iterator = ips.iterator();

        List<String> iplist = new ArrayList<String>();
        while (iterator.hasNext()){
            String server = iterator.next();
            int weight = serverWeightMap.get(server);
            //按照权重来添加比例.
            for(int i=0; i<weight; i++){
                iplist.add(server);
            }
        }

        //获取IP的策略
        Random random = new Random();
        int pos = random.nextInt(iplist.size());
        return iplist.get(pos);
    }
}
