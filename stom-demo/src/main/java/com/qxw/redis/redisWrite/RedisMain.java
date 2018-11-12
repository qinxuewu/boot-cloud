package com.qxw.redis.redisWrite;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;

import org.apache.storm.redis.bolt.RedisStoreBolt;
import org.apache.storm.redis.common.config.JedisPoolConfig;
import org.apache.storm.redis.common.mapper.RedisStoreMapper;
import org.apache.storm.topology.TopologyBuilder;
 
public class RedisMain {
	public static void main(String[] args) throws Exception {

		JedisPoolConfig poolConfig = new JedisPoolConfig.Builder().setHost("127.0.0.1").setPort(6379).build();
		RedisStoreMapper storeMapper = new RedisWriteMapper();
		RedisStoreBolt storeBolt = new RedisStoreBolt(poolConfig, storeMapper);
		
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("RedisWriteSpout", new RedisWriteSpout(), 2);
		builder.setBolt("to-save", storeBolt, 1).shuffleGrouping("RedisWriteSpout");
		
		Config conf = new Config();
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("test", conf, builder.createTopology());
		System.err.println("写入完成!!!!!");
		try {
			Thread.sleep(10000);
			 //等待6s之后关闭集群
		     cluster.killTopology("test");
		     //关闭集群
		     cluster.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	} 
	
    
}
