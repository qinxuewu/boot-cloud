package com.qxw.redis;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;

public class Topology {
    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException, AuthorizationException {

        TopologyBuilder builder = new TopologyBuilder();

        //设置数据源
        builder.setSpout("spout", new SampleSpout(), 2);
        //数据流处理组件 并行度为2
        builder.setBolt("bolt", new StormRedisBolt("127.0.0.1", 6379), 2).shuffleGrouping("spout");
        Config conf = new Config();
        conf.setDebug(true);
        //本地模式
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("StormRedisTopology", conf, builder.createTopology());
//		try {
//			Thread.sleep(10000);
//		} catch (Exception exception) {
//			System.out.println("Thread interrupted exception : " + exception);
//		}
//		cluster.killTopology("StormRedisTopology");
//		cluster.shutdown();


//		//2 集群模式
//		StormSubmitter.submitTopology("StormRedisTopology", conf, builder.createTopology());
    }
}
