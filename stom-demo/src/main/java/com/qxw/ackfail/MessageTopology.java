package com.qxw.ackfail;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
public class MessageTopology {

	public static void main(String[] args) throws InterruptedException {
		
		// 组建拓扑，并使用流分组
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("MessageSpout", new MessageSpout());
        builder.setBolt("PringBolt", new PringBolt()).shuffleGrouping("MessageSpout");
        builder.setBolt("WriteBolt", new WriteBolt()).shuffleGrouping("PringBolt");
        
		//配置
		Config cfg = new Config();
		cfg.setDebug(false);
        LocalCluster cluster = new LocalCluster();
      		
      //提交拓扑图  会一直轮询执行
       cluster.submitTopology("message-topo", cfg, builder.createTopology());
	}
}
