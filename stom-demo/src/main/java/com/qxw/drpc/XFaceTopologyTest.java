package com.qxw.drpc;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.LocalDRPC;
import org.apache.storm.StormSubmitter;
import org.apache.storm.drpc.LinearDRPCTopologyBuilder;
import org.apache.storm.utils.Utils;
public class XFaceTopologyTest {

	
	public static String spout_name = "raw-spout";
	public static void main(String[] args) throws Exception {
	        run(args);
	    }
    protected static int run(String[] args) throws Exception {
        LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder("lookup");
        builder.addBolt(new ExclamationBolt(),3);
        Config conf = new Config();
        conf.setDebug(false);
        conf.setNumWorkers(1);
        if (args != null && args.length > 0) {
              StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createRemoteTopology());
        } else {
            LocalDRPC drpc = new LocalDRPC();
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("drpc-XFace", conf, builder.createLocalTopology(drpc));
            System.out.println("Results for ‘hello‘:" + drpc.execute("lookup", "hello"));
            System.out.println("Results for ‘hello‘:" + drpc.execute("lookup", "hello12"));

            Utils.sleep(1000000000);
            cluster.killTopology("drpc-XFace");
            cluster.shutdown();
        }
        return 0;
    }
}
