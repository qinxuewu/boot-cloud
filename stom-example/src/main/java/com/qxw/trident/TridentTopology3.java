package com.qxw.trident;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.testing.FixedBatchSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

/**
 * Trident分组
 * <p>
 * shuffle (表示随机分组)
 * partitionBy(类似于Storm 原生字段分组 )
 * global (全局分组)
 * broadcast (广播分组)
 *
 * @author qxw
 * @data 2018年9月21日下午4:25:19
 */
public class TridentTopology3 {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        FixedBatchSpout spout = new FixedBatchSpout(new Fields("spoutValue"), 4,
                new Values("胖虎"),
                new Values("小夫"),
                new Values("大熊"),
                new Values("静香"),
                new Values("蓝胖子"));
        //是否轮询发送
        spout.setCycle(true);

        //创建topology
        TridentTopology topology = new TridentTopology();
        Stream input = topology.newStream("spout", spout);//设置数据源
        //分区分组
        input.partitionBy(new Fields("spoutValue")).each(new Fields("spoutValue"), new OutFunction(), new Fields()).parallelismHint(4);
        //本地模式
        Config conf = new Config();
        conf.setNumWorkers(1);
        conf.setMaxSpoutPending(20);
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("TridentTopology3", conf, topology.build());

        //集群模式
//		 StormSubmitter.submitTopology("TridentTopology3", conf, buildTopology());

    }
}
