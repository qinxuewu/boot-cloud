package com.qxw.topology;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import com.qxw.bolt.OutBolt;
import com.qxw.bolt.OutBolt2;
import com.qxw.spout.DataSource;

public class TopologyTest {

    public static void main(String[] args) throws Exception {
        //配置
        Config cfg = new Config();
        cfg.setNumWorkers(2);//指定工作进程数  （jvm数量，分布式环境下可用，本地模式设置无意义）
        cfg.setDebug(true);

        //构造拓扑流程图
        TopologyBuilder builder = new TopologyBuilder();
        //设置数据源
        builder.setSpout("dataSource", new DataSource());
        //设置数据建流处理组件
        builder.setBolt("out-bolt", new OutBolt()).shuffleGrouping("dataSource"); //随机分组
        builder.setBolt("out-bol2", new OutBolt2()).shuffleGrouping("out-bolt"); //随机分组


        //1 本地模式
        LocalCluster cluster = new LocalCluster();

        //提交拓扑图  会一直轮询执行
        cluster.submitTopology("topo", cfg, builder.createTopology());


        //2 集群模式
//		StormSubmitter.submitTopology("topo", cfg, builder.createTopology());

    }
}
