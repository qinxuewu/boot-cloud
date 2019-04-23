package com.qxw.wordCount;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

public class WordTopology {

    public static void main(String[] args) throws InterruptedException {

        // 组建拓扑，并使用流分组
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("WordSpout", new WordSpout());
        builder.setBolt("WordSplitBolt", new WordSplitBolt(), 5).shuffleGrouping("WordSpout");
        builder.setBolt("WordCountBolt", new WordCountBolt(), 5).fieldsGrouping("WordSplitBolt", new Fields("word"));
        builder.setBolt("WordReportBolt", new WordReportBolt(), 10).globalGrouping("WordCountBolt");


        //配置
        Config cfg = new Config();
        cfg.setDebug(false);
        LocalCluster cluster = new LocalCluster();

        //提交拓扑图  会一直轮询执行
        cluster.submitTopology("wordcount-topo", cfg, builder.createTopology());
    }
}
