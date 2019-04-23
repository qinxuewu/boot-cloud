package com.qxw.trident;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentState;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.operation.builtin.Count;
import org.apache.storm.trident.testing.FixedBatchSpout;
import org.apache.storm.trident.testing.MemoryMapState;
import org.apache.storm.trident.testing.Split;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

/**
 * Triden 单词计数器
 *
 * @author qxw
 * @data 2018年9月21日下午5:30:30
 */
public class TridentWordCount {
    public static class MyFunction extends BaseFunction {
        private static final long serialVersionUID = 1L;

        public void execute(TridentTuple tuple, TridentCollector collector) {
            String word = tuple.getStringByField("word");
            Long count = tuple.getLongByField("count");
            System.out.println(word + "   :  " + count);
        }
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        /* 创建spout */
        FixedBatchSpout spout = new FixedBatchSpout(new Fields("sentence"), 4,
                new Values("java php asd java"),
                new Values("php css js html"),
                new Values("js php java java"),
                new Values("a a b c d"));
        //是否循环发送
        spout.setCycle(false);
        /* 创建topology */
        TridentTopology topology = new TridentTopology();
        /* 创建Stream spout1, 分词、统计 */
        topology.newStream("spout", spout)
                //先切割
                .each(new Fields("sentence"), new Split(), new Fields("word"))
                //分组
                .groupBy(new Fields("word"))
                //聚合统计
                .aggregate(new Count(), new Fields("count"))
                //输出函数
                .each(new Fields("word", "count"), new MyFunction(), new Fields())
                //设置并行度
                .parallelismHint(1);
        Config conf = new Config();
        conf.setNumWorkers(1);
        conf.setMaxSpoutPending(20);
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("TridentWordCount", conf, topology.build());

        System.out.println(2 / 0);

    }
}
