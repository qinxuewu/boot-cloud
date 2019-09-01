package com.qxw.trident;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.testing.FixedBatchSpout;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

public class TridentTopology1 {

    /**
     * 接受一组输入字段并发出零个或多个元组作为输出 （类似storm bolt数据流处理组件）
     *
     * @author qxw
     * @data 2018年9月19日下午6:17:14
     */
    public static class MyFunction extends BaseFunction {
        private static final long serialVersionUID = 1L;

        public void execute(TridentTuple tuple, TridentCollector collector) {
            int a = tuple.getIntegerByField("a");
            int b = tuple.getIntegerByField("b");
//			 System.out.println("a:  "+tuple.getIntegerByField("a"));
//			 System.out.println("b:  "+tuple.getIntegerByField("b"));
//			 System.out.println("c:  "+tuple.getIntegerByField("c"));
//			 System.out.println("d:  "+tuple.getIntegerByField("d"));
            int sum = a + b;
            //发射数据
            collector.emit(new Values(sum));

        }
    }

    public static class Result extends BaseFunction {
        private static final long serialVersionUID = 1L;

        @Override
        public void execute(TridentTuple tuple, TridentCollector collector) {
            //获取tuple输入内容
            System.out.println();
            Integer a = tuple.getIntegerByField("a");
            Integer b = tuple.getIntegerByField("b");
            Integer c = tuple.getIntegerByField("c");
            Integer d = tuple.getIntegerByField("d");
            System.out.println("a: " + a + ", b: " + b + ", c: " + c + ", d: " + d);
            Integer sum = tuple.getIntegerByField("sum");
            System.out.println("sum: " + sum);
        }
    }


    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        //固定批处理数据源（类似storm原生的spout） 声明a,b,c,d四个字段
        FixedBatchSpout spout = new FixedBatchSpout(new Fields("a", "b", "c", "d"), 4,//设置批处理大小
                new Values(1, 4, 7, 10),
                new Values(2, 3, 5, 7),
                new Values(6, 9, 7, 2),
                new Values(9, 1, 6, 8)  //设置数据内容
        );
        //是否循环发送
        spout.setCycle(false);

        //创建topology
        TridentTopology topology = new TridentTopology();
        //指定数据源
        Stream input = topology.newStream("spout", spout);
        //要实现storm原生spolt--bolt的模式在Trident中用each实现 (随机分组)
        input.shuffle().each(new Fields("a", "b", "c", "d"),
                new MyFunction(),//执行函数 类似bolt
                new Fields("sum") //为空不向下发送

                //向下继续发送  new Fields()为空表示不继续向下输出
        ).each(new Fields("a", "b", "c", "d", "sum"), new Result(), new Fields()).parallelismHint(2);//设置并行度为2

        Config conf = new Config();
        conf.setNumWorkers(1);
        conf.setMaxSpoutPending(20);
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("TridentTopology1", conf, topology.build());

//	    StormSubmitter.submitTopology("TridentTopology1", conf, buildTopology());
    }
}
