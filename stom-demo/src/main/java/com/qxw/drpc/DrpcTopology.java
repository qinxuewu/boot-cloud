package com.qxw.drpc;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.LocalDRPC;
import org.apache.storm.StormSubmitter;
import org.apache.storm.drpc.LinearDRPCTopologyBuilder;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class DrpcTopology {
	public static class ExclaimBolt extends BaseBasicBolt {
	    public void execute(Tuple tuple, BasicOutputCollector collector) {
	        String input = tuple.getString(1);
	        collector.emit(new Values(tuple.getValue(0), input + "1"));
	    }
	 
	    public void declareOutputFields(OutputFieldsDeclarer declarer) {
	        declarer.declare(new Fields("id", "result"));
	    }
	}
	
	 /**
	  * Distributed RPC是由一个”DPRC Server”协调的(storm自带了一个实现)。
	  * DRPC服务调用过程：
	  *  	接收一个RPC请求。
	  *  	发送请求到storm topology 
	  *  	从storm topology接收结果
	  *  	把结果发回给等待的客户端。从客户端的角度来看一个DRPC调用跟一个普通的RPC调用没有任何区别
	  * @param args
	  * @throws Exception
	  */
	public static void main(String[] args) throws Exception {
		LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder("exclamation");
	    builder.addBolt(new ExclaimBolt(), 3);	    
	    Config conf = new Config();
	    conf.setDebug(true);
	    conf.setNumWorkers(2);
		try {
//			LocalDRPC drpc = new LocalDRPC();
//			LocalCluster cluster = new LocalCluster();
//			cluster.submitTopology("drpc-demo", conf, builder.createLocalTopology(drpc)); 
//		    System.out.println("DRPC测试  'hello':" + drpc.execute("exclamation", "hello"));
//		    
//		    cluster.shutdown();
//		    drpc.shutdown();
		 //集群模式
		 conf.setNumWorkers(3);
		 StormSubmitter.submitTopology("exclamation", conf,builder.createRemoteTopology());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
