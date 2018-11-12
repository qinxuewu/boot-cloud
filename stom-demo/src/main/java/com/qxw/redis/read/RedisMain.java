package com.qxw.redis.read;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.redis.bolt.RedisLookupBolt;
import org.apache.storm.redis.common.config.JedisPoolConfig;
import org.apache.storm.redis.common.mapper.RedisLookupMapper;
import org.apache.storm.topology.TopologyBuilder;
 
public class RedisMain {
	public static void main(String[] args) throws Exception {
		JedisPoolConfig poolConfig = new JedisPoolConfig.Builder() .setHost("127.0.0.1").setPort(6379).build();
		RedisLookupMapper lookupMapper = new RedisReadMapper();
		RedisLookupBolt lookupBolt = new RedisLookupBolt(poolConfig, lookupMapper);
		
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("RedisReadSpout-reader", new RedisReadSpout(), 2);
		builder.setBolt("to-lookupBolt", lookupBolt, 1).shuffleGrouping("RedisReadSpout-reader");
		builder.setBolt("to-out",new RedisOutBolt(), 1).shuffleGrouping("to-lookupBolt");
		Config conf = new Config();
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("test", conf, builder.createTopology());
		try {
			Thread.sleep(100000);
			 //等待6s之后关闭集群
		     cluster.killTopology("test");
		     //关闭集群
		     cluster.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
    
}
