package com.qxw.trident;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.operation.BaseFilter;
import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.testing.FixedBatchSpout;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

/**
 * TridentTopology海量数据过滤
 * @author qxw
 * @data 2018年9月21日上午10:57:21
 */
public class TridentTopology2 {
	
	/**
	 * 可以海量数据进行过滤，需要继承BaseFilter，重写isKeep方法
	 * @author qxw
	 * @data 2018年9月21日上午10:57:00
	 */
	public static  class MyFilter extends BaseFilter {
		private static final long serialVersionUID = 1L;
		public boolean isKeep(TridentTuple tuple) {
				//能够被2对第1个和第2个值进行相加.然后除2，为0则发射，不为零则不发射射
			   	return tuple.getInteger(1) % 2 == 0;
		   }
	}
	
	/**
	 * 类似原生storm bolt数据流处理组件
	 * @author qxw
	 * @data 2018年9月21日下午3:31:12
	 */
   public static class MyFunction extends BaseFunction{
	private static final long serialVersionUID = 1L;

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		//获取tuple输入内容
		Integer a = tuple.getIntegerByField("a");
		Integer b = tuple.getIntegerByField("b");
		Integer c = tuple.getIntegerByField("c");
		Integer d = tuple.getIntegerByField("d");
		System.out.println("a: "+ a + ", b: " + b + ", c: " + c + ", d: " + d);

	}
	   
   }
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		//固定批处理数据源（类似storm原生的spout） 声明a,b,c,d四个字段
		FixedBatchSpout spout =new FixedBatchSpout(new Fields("a","b","c","d"),4,//设置批处理大小
						new Values(1,4,7,10),
						new Values(2,3,5,7),
						new Values(6,9,7,2),
						new Values(9,1,6,8)  //设置数据内容
		 );
		 //是否循环发送
		 spout.setCycle(false);

		//创建topology
		 TridentTopology topology =new TridentTopology();
		//指定数据源
		 Stream input=topology.newStream("spout", spout);
		//要实现storm原生spolt--bolt的模式在Trident中用each实现 (随机分组)
		 input.shuffle().each(new Fields("a","b","c","d"),new MyFilter()).each(new Fields("a","b","c","d"), new MyFunction(),new Fields()); 
		 //本地模式
		 Config conf = new Config();
		 conf.setNumWorkers(1);
		 conf.setMaxSpoutPending(20);
		 LocalCluster cluster = new LocalCluster();
		 cluster.submitTopology("TridentTopology2", conf, topology.build());		
		 
		 //集群模式
//		 StormSubmitter.submitTopology("TridentTopology1", conf, buildTopology());
	}
}
