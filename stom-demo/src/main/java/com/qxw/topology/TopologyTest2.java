package com.qxw.topology;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

import com.qxw.bolt.OutBolt;
import com.qxw.bolt.OutBolt2;
import com.qxw.spout.DataSource;

/**
 * 拓扑的并行性
 * @author qxw
 * @data 2018年9月17日下午2:49:09
 */
public class TopologyTest2 {

	public static void main(String[] args) throws Exception {
		//配置
		Config cfg = new Config();
		cfg.setNumWorkers(2);//指定工作进程数  （jvm数量，分布式环境下可用，本地模式设置无意义）
		cfg.setDebug(false);
		
		//构造拓扑流程图
		TopologyBuilder builder = new TopologyBuilder();
		//设置数据源（产生2个执行器和俩个任务）
		builder.setSpout("dataSource", new DataSource(),2).setNumTasks(2);
		//设置数据建流处理组件（产生2个执行器和4个任务）
		builder.setBolt("out-bolt", new OutBolt(),2).shuffleGrouping("dataSource").setNumTasks(4); //随机分组
		//设置bolt的并行度和任务数:（产生6个执行器和6个任务）
//		builder.setBolt("out-bol2", new OutBolt2(),6).shuffleGrouping("out-bolt").setNumTasks(6); //随机分组
		
		//设置字段分组（产生8个执行器和8个任务）字段分组 
		builder.setBolt("out-bol2", new OutBolt2(),8).fieldsGrouping("out-bolt", new Fields("outdata")).setNumTasks(8);
		//设置广播分组
		//builder.setBolt("write-bolt", new OutBolt2(), 4).allGrouping("print-bolt");
		//设置全局分组
		//builder.setBolt("write-bolt", new OutBolt2(), 4).globalGrouping("print-bolt");
		
		//1 本地模式
		LocalCluster cluster = new LocalCluster();
		
		//提交拓扑图  会一直轮询执行
		cluster.submitTopology("topo", cfg, builder.createTopology());

		
		//2 集群模式
//		StormSubmitter.submitTopology("topo", cfg, builder.createTopology());
		
	}
}
