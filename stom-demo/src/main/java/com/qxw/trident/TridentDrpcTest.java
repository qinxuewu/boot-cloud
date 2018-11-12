package com.qxw.trident;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.LocalDRPC;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

public class TridentDrpcTest {
	private  static class MyFunction extends BaseFunction{
        public void execute(TridentTuple tridentTuple, TridentCollector tridentCollector) {
            String sentence = tridentTuple.getString(0);
            for (String word : sentence.split(" ")) {
                tridentCollector.emit(new Values(word));
            }
        }
    }
    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        TridentTopology topology=new TridentTopology();
        Config conf = new Config();
        conf.setMaxSpoutPending(20);
        //本地模式
        if (args.length==0){
            LocalCluster cluster = new LocalCluster();
            LocalDRPC drpc = new LocalDRPC();

            Stream input=topology.newDRPCStream("data",drpc);
            input.each(new Fields("args"),new MyFunction(),new Fields("result")).project(new Fields("result"));
            cluster.submitTopology("wordCount", conf, topology.build());
            //调用
            System.err.println("DRPC RESULT: " + drpc.execute("data", "cat the dog jumped"));
            drpc.shutdown();
            cluster.shutdown();
        }else{
            //集群模式
            conf.setNumWorkers(2);
            StormSubmitter.submitTopology(args[0], conf, topology.build());
        }
    }

}
