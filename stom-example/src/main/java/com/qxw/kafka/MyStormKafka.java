package com.qxw.kafka;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

/**
 * kafka -storm集成
 *
 * @author qxw
 * @data 2018年9月26日下午12:25:42
 */
public class MyStormKafka {
    public static class MyBolt extends BaseBasicBolt {
        public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
            System.err.println("接受订阅kafka消息：  " + tuple.getStringByField("topic"));
            System.err.println("接受订阅kafka消息：  " + tuple.getStringByField("value"));
        }

        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        }
    }

    public static void main(String[] args) {
        TopologyBuilder tp = new TopologyBuilder();
        tp.setSpout("kafka_spout", new KafkaSpout(KafkaSpoutConfig.builder("localhost:9092", "qxw").build()), 1);
        tp.setBolt("bolt", new MyBolt()).shuffleGrouping("kafka_spout");
        Config cfg = new Config();
        cfg.setNumWorkers(1);//指定工作进程数  （jvm数量，分布式环境下可用，本地模式设置无意义）
        cfg.setDebug(true);
        LocalCluster locl = new LocalCluster();
        locl.submitTopology("kkafka-topo", cfg, tp.createTopology());

    }
}
